package com.agroknow.linkchecker.service;

import com.agroknow.domain.SimpleMetadata;
import com.agroknow.domain.parser.ParserException;
import com.agroknow.domain.parser.factory.SimpleMetadataParserFactory;
import com.agroknow.linkchecker.domain.FileMetadata;
import com.agroknow.linkchecker.domain.LinkCheckerOptions;
import com.agroknow.linkchecker.domain.URLMetadata;
import com.agroknow.linkchecker.exceptions.LinkCheckingException;
import com.agroknow.linkchecker.exceptions.NotSupportedProtocolException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response.Status.Family;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileMetadataService {
    private static final Logger LOG = LoggerFactory.getLogger(FileMetadataService.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private LinkCheckerOptions options;

    public FileMetadataService() {}

    public FileMetadataService(LinkCheckerOptions options) {
        this.options = options;
    }

    public FileMetadata readFile(String filePath) throws ParserException {
        SimpleMetadata tmpMetadata = SimpleMetadataParserFactory.load(options.getFileFormat(), filePath);
        
        if (CollectionUtils.isEmpty(tmpMetadata.getIdentifiers()) || tmpMetadata.getIdentifiers().size() > 1) {
            LOG.error("Unsupported identifiers size {} for file {}. skipping", tmpMetadata.getIdentifiers().size(), filePath);
            return null;
        }

        FileMetadata dto = new FileMetadata(filePath, CollectionUtils.get(tmpMetadata.getIdentifiers(), 0).toString(), options.getFileFormat());
        for (String url : tmpMetadata.getLocations()) {
            try {
                if (!url.startsWith("http")) {
                    throw new NotSupportedProtocolException("Protocol not supported : " + url);
                }

                URL u = new URL(url);
                if (!u.getProtocol().equals("http")) {
                    throw new NotSupportedProtocolException("Protocol not supported : " + url);
                }
                dto.addLocation(new URLMetadata(u.getHost(), url));
            } catch (MalformedURLException e) {
                LOG.debug("Malformed URL {} in file with id : {}", url, dto.getIdentifier());
                dto.addLocation(new URLMetadata(null, url, Family.OTHER, -1));
                dto.setFailed(true);
            }
        }
        return dto;
    }

    public void updateOrCopyFile(FileMetadata fileMeta) throws IOException {
        if (options.isSupportMode()) {
            this.updateFile(fileMeta);
        } else {
            this.copyFile(fileMeta.getFilePath(), !fileMeta.isFailed());
        }
    }
    
    public void copyFile(String filePath, boolean success) {
        LOG.trace("Ready to move file {} to {} folder.", filePath, success ? "success" : "error");

        File rootFolder = new File(options.getRootFolderPath());
        File successFolder = new File(options.getSuccessFolderPath());
        File errorFolder = new File(options.getErrorFolderPath());

        String targetFilePath = filePath.replace(rootFolder.getPath(), success ? successFolder.getPath() : errorFolder.getPath());
        try {
            FileUtils.copyFile(new File(filePath), new File(targetFilePath));
        } catch (Exception e) {
            LOG.error("Error while moving {} to {}", new Object[] { filePath, targetFilePath }, e);
        }
    }

    @SuppressWarnings("unchecked")
    public void updateFile(FileMetadata fileMeta) throws IOException {
        File file = new File(fileMeta.getFilePath());
        String akifString = FileUtils.readFileToString(file);
        JSONObject akifObject = (JSONObject) JSONValue.parse(akifString);
        if (!fileMeta.getIdentifier().equals(akifObject.get("identifier").toString())) {
            throw new LinkCheckingException("Identifiers mismatch!: " + fileMeta.getIdentifier() + " differs from " + akifObject.get("identifier"));
        }
        JSONArray expressions0 = (JSONArray) akifObject.get("expressions");
        JSONArray expressions1 = new JSONArray();
        boolean fileChanged = false;
        for (Object expression : expressions0) {
            JSONObject expression0 = (JSONObject) expression;
            JSONArray manifestations0 = (JSONArray) expression0.get("manifestations");
            JSONArray manifestations1 = new JSONArray();
            for (Object manifestation : manifestations0) {
                JSONObject manifestation0 = (JSONObject) manifestation;
                JSONArray items0 = (JSONArray) manifestation0.get("items");
                JSONArray items1 = new JSONArray();
                for (Object item : items0) {
                    JSONObject item0 = (JSONObject) item;
                    String url = (String) item0.get("url");
                    Boolean broken = (Boolean) item0.get("broken");
                    if(broken != fileMeta.isUrlBroken(url)) {
                        item0.put("broken", Boolean.valueOf(fileMeta.isUrlBroken(url)));
                        fileChanged = true;
                        items1.add(item0);
                    }
                }
                manifestation0.put("items", items1);
                manifestations1.add(manifestation0);
            }
            expression0.put("manifestations", manifestations1);
            expressions1.add(expression0);
        }
        akifObject.put("expressions", expressions1);
        if(fileChanged) {
            akifObject.put("lastUpdateDate", sdf.format(new Date(System.currentTimeMillis())));
            FileUtils.writeStringToFile(file, akifObject.toJSONString());
        }
    }

    public void setOptions(LinkCheckerOptions options) {
        this.options = options;
    }
}

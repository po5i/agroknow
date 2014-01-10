package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public abstract class AbstractParser implements Parser {

    private ObjectMapper mapper;

    public AbstractParser() {}

    public AbstractParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected abstract SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException;

    public SimpleMetadata load(String filePath) throws ParserException {
        FileInputStream fileReader = null;
        try {
            fileReader = FileUtils.openInputStream(new File(filePath));
            return loadInternal(fileReader);
        } catch (IOException e) {
            throw new ParserException("Error loading file " + filePath, e);
        } finally {
            IOUtils.closeQuietly(fileReader);
        }
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
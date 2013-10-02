package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public abstract class AbstractParser {
    public abstract SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException;

    private final ObjectMapper mapper;

    public AbstractParser() {
        mapper = new ObjectMapper();
        // to enable standard indentation ("pretty-printing"):
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        // to write java.util.Date, Calendar as number (timestamp):
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // DeserializationFeature for changing how JSON is read as POJOs:
        // to prevent exception when encountering unknown property:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
    }

    public SimpleMetadata load(String filePath) throws ParserException {
        FileInputStream fileReader = null;
        try {
            fileReader = FileUtils.openInputStream(new File(filePath));
            return loadInternal(fileReader);
        } catch (IOException e) {
            throw new ParserException("Error loading file " + filePath + " - " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(fileReader);
        }
    }

    /**
     * @return the mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
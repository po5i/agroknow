package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;
import com.agroknow.domain.agrif.Agrif;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;

import javax.validation.Validator;

public class SimpleParserAGRIF extends AbstractParser {

    /**
     * @param mapper
     * @param validator
     */
    public SimpleParserAGRIF(ObjectMapper mapper, Validator validator) {
        super(mapper, validator);
    }

    @Override
    public SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException, JsonProcessingException {
        return getMapper().reader(Agrif.class).readValue(fileReader);
    }
}

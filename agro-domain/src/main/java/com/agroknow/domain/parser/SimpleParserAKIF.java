package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;
import com.agroknow.domain.akif.Akif;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;

import javax.validation.Validator;

public class SimpleParserAKIF extends AbstractParser {

    public SimpleParserAKIF(ObjectMapper mapper, Validator validator) {
        super(mapper, validator);
    }

    @Override
    protected SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException {
        return getMapper().reader(Akif.class).readValue(fileReader);
    }
}
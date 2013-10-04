package com.agroknow.domain.parser;

import com.agroknow.domain.Akif;
import com.agroknow.domain.SimpleMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleParserAKIF extends AbstractParser {

    public SimpleParserAKIF() {
    }

    public SimpleParserAKIF(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    protected SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException {
        return getMapper().reader(Akif.class).readValue(fileReader);
    }
}
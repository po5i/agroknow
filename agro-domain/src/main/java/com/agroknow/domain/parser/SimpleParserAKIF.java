package com.agroknow.domain.parser;

import com.agroknow.domain.Akif;
import com.agroknow.domain.SimpleMetadata;

import java.io.FileInputStream;
import java.io.IOException;

public class SimpleParserAKIF extends AbstractParser {

    @Override
    public SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException {
        return getMapper().reader(Akif.class).readValue(fileReader);
    }
}
package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;

/**
 * @author aggelos
 */
public interface Parser {

    SimpleMetadata load(String filePath) throws ParserException;
}

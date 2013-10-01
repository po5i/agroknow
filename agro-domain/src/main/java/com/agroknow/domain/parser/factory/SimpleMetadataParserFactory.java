package com.agroknow.domain.parser.factory;

import com.agroknow.domain.SimpleMetadata;
import com.agroknow.domain.parser.AbstractParser;
import com.agroknow.domain.parser.ParserException;
import com.agroknow.domain.parser.SimpleParserAGRIF;
import com.agroknow.domain.parser.SimpleParserAKIF;
import com.agroknow.domain.parser.SimpleParserDC;
import com.agroknow.domain.parser.SimpleParserLOM;
import com.agroknow.domain.parser.SimpleParserMODS;
import com.agroknow.domain.parser.SimpleParserNSDL;

import java.util.HashMap;
import java.util.Map;

public class SimpleMetadataParserFactory {
    public static final String MODS = "mods";
    public static final String AKIF = "akif";
    public static final String LOM = "lom";
    public static final String NSDL = "nsdl";
    public static final String DC = "dc";
    public static final String AGRIF = "agrif";
    private static final Map<String, AbstractParser> parsersMap = new HashMap<String, AbstractParser>();

    static {
        parsersMap.put(AGRIF, new SimpleParserAGRIF());
        parsersMap.put(MODS, new SimpleParserMODS());
        parsersMap.put(LOM, new SimpleParserLOM());
        parsersMap.put(NSDL, new SimpleParserNSDL());
        parsersMap.put(DC, new SimpleParserDC());
        parsersMap.put(AKIF, new SimpleParserAKIF());
    }

    public static SimpleMetadata load(String metadataType, String filePath) throws ParserException {
        if (!parsersMap.containsKey(metadataType)) {
            throw new ParserException("Unsupported Metadata Format : \"" + metadataType + "\" !");
        }
        return parsersMap.get(metadataType).load(filePath);
    }

    public static boolean isValidMetadataType(String metadataType) {
        return parsersMap.containsKey(metadataType);
    }
}

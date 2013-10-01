package com.agroknow.domain.parser;

import com.agroknow.domain.Akif;
import com.agroknow.domain.SimpleMetadata;

import java.io.FileInputStream;
import java.io.IOException;

public class SimpleParserAKIF extends AbstractParser {

    @Override
    public SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException {
        return getMapper().reader(Akif.class).readValue(fileReader);
        
//        JSONObject akifObject = (JSONObject) JSONValue.parse(IOUtils.toString(fileReader));
//        String identifier = ((Long) akifObject.get("identifier")).toString();
//        Set<String> identifiers = new HashSet<String>();
//        identifiers.add(identifier);
//        Set<String> locations = new HashSet<String>();
//        JSONArray expressions = (JSONArray) akifObject.get("expressions");
//        for (Object expression : expressions) {
//            JSONArray manifestations = (JSONArray) ((JSONObject) expression).get("manifestations");
//            for (Object manifestation : manifestations) {
//                JSONArray items = (JSONArray) ((JSONObject) manifestation).get("items");
//                for (Object item : items) {
//                    locations.add((String) ((JSONObject) item).get("url"));
//                }
//            }
//        }
//
//        return new SimpleMetadata(locations, identifiers);
    }
}

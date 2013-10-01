package com.agroknow.domain.parser;

import com.agroknow.domain.SimpleMetadata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class SimpleParserAGRIF extends AbstractParser {

	@Override
	public SimpleMetadata loadInternal(FileInputStream fileReader) throws IOException 
	{
		JSONObject agrifObject = (JSONObject)JSONValue.parse( IOUtils.toString(fileReader) ) ;
		Set<String> identifiers = new HashSet<String>() ;
		if ( agrifObject.containsKey( "agrifIdentifier" ) )
		{
			String identifier = ((Long)agrifObject.get( "agrifIdentifier" )).toString() ;
			identifiers.add( identifier ) ;			
		}
		Set<String> locations = new HashSet<String>() ;
		JSONArray expressions = (JSONArray)agrifObject.get( "expressions" ) ;
		for ( Object expression: expressions )
		{
			JSONArray manifestations = (JSONArray)((JSONObject)expression).get( "manifestations" ) ;
			for ( Object manifestation: manifestations )
			{
				JSONArray items = (JSONArray)((JSONObject)manifestation).get( "items" ) ;
				for ( Object item: items )
				{
					locations.add( (String)((JSONObject)item).get( "url" ) ) ;
				}
			}
		}
		
		return new SimpleMetadata(locations, identifiers);
	}
}

package com.agroknow.domain.parser;
public class ParserException extends java.lang.Exception 
{
    private static final long serialVersionUID = 1L;
    private String message ;
	
	public ParserException( String message ) 
	{
		super() ;
		this.message = message ;
	}

	public String getMessage()
	{
		return message ;
	}

}

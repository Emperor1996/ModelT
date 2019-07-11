/*****************************************************************************
 Class:        HttpODataException.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

public class HttpODataException extends Exception
{
	private final int httpResponseCode;

	public HttpODataException()
	{
		super();
		httpResponseCode = 0;
	}

	public HttpODataException(final String message, final Throwable cause)
	{
		this(message, cause, 0);
	}

	public HttpODataException(final String message)
	{
		this(message, 0);
	}

	public HttpODataException(final Throwable cause)
	{
		this(cause, 0);
	}

	public HttpODataException(final String message, final Throwable cause, final int httpResponseCode)
	{
		super(message, cause);
		this.httpResponseCode = httpResponseCode;
	}

	public HttpODataException(final String message, final int httpResponseCode)
	{
		super(message);
		this.httpResponseCode = httpResponseCode;
	}

	public HttpODataException(final Throwable cause, final int httpResponseCode)
	{
		super(cause);
		this.httpResponseCode = httpResponseCode;
	}

	public int getHttpResponseCode()
	{
		return httpResponseCode;
	}

}

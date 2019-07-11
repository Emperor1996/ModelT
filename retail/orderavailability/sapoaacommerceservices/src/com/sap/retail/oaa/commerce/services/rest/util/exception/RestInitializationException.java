/*****************************************************************************
    Class:        RestInitializationException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.exception;

/**
 * Exception Class regarding the RestInitalization functionality
 *
 * @author SAP
 */
public class RestInitializationException extends RuntimeException
{
	public RestInitializationException()
	{
		super();
	}

	public RestInitializationException(final String message)
	{
		super(message);
	}

	public RestInitializationException(final Throwable e)
	{
		super(e);
	}
}
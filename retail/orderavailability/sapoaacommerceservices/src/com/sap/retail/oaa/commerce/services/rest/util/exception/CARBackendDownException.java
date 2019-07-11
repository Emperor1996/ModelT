/*****************************************************************************
    Class:        CARBackendDownException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.exception;

/**
 * Exception class regarding the availability of the CAR back end
 */
public class CARBackendDownException extends RuntimeException
{
	public CARBackendDownException()
	{
		super();
	}

	public CARBackendDownException(final String message)
	{
		super(message);
	}

	public CARBackendDownException(final Throwable e)
	{
		super(e);
	}

	public CARBackendDownException(final String message, final Throwable e)
	{
		super(message, e);
	}


}

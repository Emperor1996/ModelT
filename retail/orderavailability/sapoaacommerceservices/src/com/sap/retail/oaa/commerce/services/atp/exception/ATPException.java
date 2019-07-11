/*****************************************************************************
    Class:        ATPException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.atp.exception;

/**
 * Exception Class regarding the aggregated availability functionality
 */
public class ATPException extends RuntimeException
{
	public ATPException()
	{
		super();
	}

	public ATPException(final String message)
	{
		super(message);
	}

	public ATPException(final Throwable e)
	{
		super(e);
	}
}

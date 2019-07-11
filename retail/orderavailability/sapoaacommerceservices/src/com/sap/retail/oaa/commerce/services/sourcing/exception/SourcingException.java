/*****************************************************************************
    Class:        SourcingException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.exception;

/**
 * Exception Class regarding the Sourcing functionality
 */
public class SourcingException extends RuntimeException
{
	public SourcingException()
	{
		super();
	}

	public SourcingException(final String message)
	{
		super(message);
	}

	public SourcingException(final Throwable e)
	{
		super(e);
	}
}

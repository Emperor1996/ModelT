/*****************************************************************************
    Class:        AuthenticationServiceException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.exception;

/**
 * Exception class regarding the authentication service functionality
 *
 * @author SAP
 */
public class AuthenticationServiceException extends RuntimeException
{
	public AuthenticationServiceException()
	{
		super();
	}

	public AuthenticationServiceException(final String message)
	{
		super(message);
	}

	public AuthenticationServiceException(final Throwable e)
	{
		super(e);
	}
}
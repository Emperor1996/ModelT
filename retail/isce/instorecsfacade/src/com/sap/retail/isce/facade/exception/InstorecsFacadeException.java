/*****************************************************************************
 Class:        InstorecsFacadeException
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.exception;

/**
 * Class manages the facade exceptions
 *
 */
public class InstorecsFacadeException extends Exception
{
	public InstorecsFacadeException(final String message)
	{
		super(message);
	}

	public InstorecsFacadeException(final String message, final Throwable t)
	{
		super(message, t);
	}


	/**
	 * @return Message code
	 */
	public String getMessageCode()
	{
		return getMessage();
	}
}

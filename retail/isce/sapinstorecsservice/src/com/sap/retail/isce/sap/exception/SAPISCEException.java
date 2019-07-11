/*****************************************************************************
 Class:        SAPISCEException.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.sap.exception;

/**
 * ISCE checked exception.
 */
public class SAPISCEException extends Exception
{

	/**
	 * Constructs a new exception.
	 */
	public SAPISCEException()
	{
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 *
	 * @param message
	 *           the message containing detail information
	 * @param cause
	 *           the cause of the exception
	 */
	public SAPISCEException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           the message containing detail information
	 */
	public SAPISCEException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           the cause of the exception
	 */
	public SAPISCEException(final Throwable cause)
	{
		super(cause);
	}
}

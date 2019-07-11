/*****************************************************************************
 Class:        SAPISCERuntimeException.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.sap.exception;

/**
 * ISCE Runtime exception.
 */
public class SAPISCERuntimeException extends RuntimeException
{

	/**
	 * Constructs a new exception.
	 */
	public SAPISCERuntimeException()
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
	public SAPISCERuntimeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           the message containing detail information
	 */
	public SAPISCERuntimeException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           the cause of the exception
	 */
	public SAPISCERuntimeException(final Throwable cause)
	{
		super(cause);
	}
}

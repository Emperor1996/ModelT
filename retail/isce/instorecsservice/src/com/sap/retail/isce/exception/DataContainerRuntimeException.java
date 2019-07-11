/*****************************************************************************
 Class:        DataContainerRuntimeException
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;

/**
 * Runtime exception for data container.
 *
 */
public class DataContainerRuntimeException extends RuntimeException
{

	/**
	 * Constructs a new exception.
	 */
	public DataContainerRuntimeException()
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
	public DataContainerRuntimeException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           the message containing detail information
	 */
	public DataContainerRuntimeException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           the cause of the exception
	 */
	public DataContainerRuntimeException(final Throwable cause)
	{
		super(cause);
	}

}

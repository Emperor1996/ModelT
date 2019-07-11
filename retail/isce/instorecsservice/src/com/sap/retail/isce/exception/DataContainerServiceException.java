/*****************************************************************************
 Class:        DataContainerServiceException
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;



/**
 * Exception for service data container.
 *
 */
public class DataContainerServiceException extends DataContainerBaseException
{

	/**
	 * Constructs a new exception.
	 */
	public DataContainerServiceException()
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
	public DataContainerServiceException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 *
	 * @param message
	 *           the message containing detail information
	 */
	public DataContainerServiceException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause.
	 *
	 * @param cause
	 *           the cause of the exception
	 */
	public DataContainerServiceException(final Throwable cause)
	{
		super(cause);
	}
}

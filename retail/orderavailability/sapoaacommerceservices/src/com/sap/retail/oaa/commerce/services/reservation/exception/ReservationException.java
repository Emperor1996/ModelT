/*****************************************************************************
    Class:        ReservationException
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.reservation.exception;


/**
 * Exception class regarding the REST service for updating the temporary reservation.
 */
public class ReservationException extends RuntimeException
{
	public ReservationException()
	{
		super();
	}

	public ReservationException(final String message)
	{
		super(message);
	}

	public ReservationException(final Throwable e)
	{
		super(e);
	}
}

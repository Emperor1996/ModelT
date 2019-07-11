/*****************************************************************************
 Class:        StoreContextNotDefinedException
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.exception;

/**
 * Exception, when the store context was not found
 *
 */
public class StoreContextNotDefinedException extends InstorecsFacadeException
{
	private static final String STORE_CONTEXT_NOT_DEFINED = "instorecs.facade.error.store.context.not.defined";

	public StoreContextNotDefinedException(final String message)
	{
		super(message);
	}

	@Override
	public String getMessageCode()
	{
		return STORE_CONTEXT_NOT_DEFINED;
	}
}

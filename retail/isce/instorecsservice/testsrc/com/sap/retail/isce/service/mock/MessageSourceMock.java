/*****************************************************************************
 Class:        MessageSourceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.mock;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;


public class MessageSourceMock implements MessageSource
{
	@Override
	public String getMessage(final String arg0, final Object[] arg1, final String arg2, final Locale arg3)
	{
		return null;
	}

	@Override
	public String getMessage(final String arg0, final Object[] arg1, final Locale arg2) throws NoSuchMessageException
	{
		return arg0;
	}

	@Override
	public String getMessage(final MessageSourceResolvable arg0, final Locale arg1) throws NoSuchMessageException
	{
		return null;
	}
}

/*****************************************************************************
 Class:        InstorecsFacadeExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;

import com.sap.retail.isce.facade.exception.InstorecsFacadeException;


/**
 * Unit test class for InstorecsFacadeException
 */
@UnitTest
public class InstorecsFacadeExceptionUnitTest
{
	private InstorecsFacadeException classUnderTest;
	private static final String EXCEPTION_MESSAGE = "The message";
	private static final String EXCEPTION_THROWABLE_MESSAGE = "Throwable message";


	@Test
	public void testConstructorMessage()
	{
		classUnderTest = new InstorecsFacadeException(EXCEPTION_MESSAGE);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessageCode not identical", EXCEPTION_MESSAGE, classUnderTest.getMessageCode());
	}

	@Test
	public void testConstructorMessageCause()
	{
		classUnderTest = new InstorecsFacadeException(EXCEPTION_MESSAGE, new Throwable(EXCEPTION_THROWABLE_MESSAGE));
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessageCode not identical", EXCEPTION_MESSAGE, classUnderTest.getMessageCode());
		assertNotNull("getCause is null", classUnderTest.getCause());
		assertEquals("getMessage not identical", EXCEPTION_THROWABLE_MESSAGE, classUnderTest.getCause().getMessage());
	}


}

/*****************************************************************************
 Class:        HttpODataExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


/**
 * Unit test class for HttpODataException
 */
@UnitTest
public class HttpODataExceptionUnitTest
{
	private HttpODataException classUnderTest;
	private static final String EXCEPTION_MESSAGE = "The message";
	private static final String EXCEPTION_THROWABLE_MESSAGE = "Throwable message";

	@Test
	public void testDefaultConstructor()
	{
		classUnderTest = new HttpODataException();
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getHttpResponseCode not identical", 0, classUnderTest.getHttpResponseCode());
	}

	@Test
	public void testConstructorMessageCause()
	{
		classUnderTest = new HttpODataException(EXCEPTION_MESSAGE, new Throwable(EXCEPTION_THROWABLE_MESSAGE));
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getHttpResponseCode not identical", 0, classUnderTest.getHttpResponseCode());
		assertEquals("getMessage not identical", EXCEPTION_MESSAGE, classUnderTest.getMessage());
		assertNotNull("getCause is null", classUnderTest.getCause());
		assertEquals("getMessage not identical", EXCEPTION_THROWABLE_MESSAGE, classUnderTest.getCause().getMessage());
	}

	@Test
	public void testConstructorMessage()
	{
		classUnderTest = new HttpODataException(EXCEPTION_MESSAGE);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessage not identical", EXCEPTION_MESSAGE, classUnderTest.getMessage());
	}

	@Test
	public void testConstructorCause()
	{
		classUnderTest = new HttpODataException(new Throwable(EXCEPTION_THROWABLE_MESSAGE));
		assertNotNull("classUnderTest is null", classUnderTest);
		assertNotNull("getCause is null", classUnderTest.getCause());
		assertEquals("getMessage not identical", EXCEPTION_THROWABLE_MESSAGE, classUnderTest.getCause().getMessage());
	}

	@Test
	public void testConstructorMessageCauseHttpRespondeCode()
	{
		classUnderTest = new HttpODataException(EXCEPTION_MESSAGE, new Throwable(EXCEPTION_THROWABLE_MESSAGE), 1);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessage not identical", EXCEPTION_MESSAGE, classUnderTest.getMessage());
		assertNotNull("getCause is null", classUnderTest.getCause());
		assertEquals("getMessage not identical", EXCEPTION_THROWABLE_MESSAGE, classUnderTest.getCause().getMessage());
		assertEquals("getHttpResponseCode not identical", 1, classUnderTest.getHttpResponseCode());
	}

	@Test
	public void testConstructorMessageHttpRespondeCode()
	{
		classUnderTest = new HttpODataException(EXCEPTION_MESSAGE, 1);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessage not identical", EXCEPTION_MESSAGE, classUnderTest.getMessage());
		assertEquals("getHttpResponseCode not identical", 1, classUnderTest.getHttpResponseCode());
	}

	@Test
	public void testConstructorCauseHttpRespondeCode()
	{
		classUnderTest = new HttpODataException(new Throwable(EXCEPTION_THROWABLE_MESSAGE), 1);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertNotNull("getCause is null", classUnderTest.getCause());
		assertEquals("getMessage not identical", EXCEPTION_THROWABLE_MESSAGE, classUnderTest.getCause().getMessage());
		assertEquals("getHttpResponseCode not identical", 1, classUnderTest.getHttpResponseCode());
	}

}

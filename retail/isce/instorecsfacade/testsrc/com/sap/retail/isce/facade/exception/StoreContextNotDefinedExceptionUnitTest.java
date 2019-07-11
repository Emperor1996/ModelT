/*****************************************************************************
 Class:        StoreContextNotDefinedExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;


/**
 * Unit test class for StoreContextNotDefinedException
 */
@UnitTest
public class StoreContextNotDefinedExceptionUnitTest
{
	private StoreContextNotDefinedException classUnderTest;
	private static final String EXCEPTION_MESSAGE = "The message";


	@Test
	public void testConstructorMessage()
	{
		classUnderTest = new StoreContextNotDefinedException(EXCEPTION_MESSAGE);
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getMessageCode not identical", "instorecs.facade.error.store.context.not.defined",
				classUnderTest.getMessageCode());
		assertEquals("getMessageCode not identical", EXCEPTION_MESSAGE, classUnderTest.getMessage());
	}

}

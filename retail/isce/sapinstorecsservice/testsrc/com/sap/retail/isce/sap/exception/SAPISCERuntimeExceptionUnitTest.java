/*****************************************************************************
 Class:        SAPISCERuntimeExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.sap.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for SAPISCERuntimeException.
 *
 */
@UnitTest
public class SAPISCERuntimeExceptionUnitTest
{

	/**
	 * Tests SAPISCERuntimeException constructors.
	 */
	@Test
	public void testDataContainerRuntimeExceptionConstructors()
	{
		Assert.assertNotNull("Exception object should not be null", new SAPISCERuntimeException());
		Assert.assertNotNull("Exception object should not be null", new SAPISCERuntimeException("Message"));
		Assert.assertNotNull("Exception object should not be null", new SAPISCERuntimeException("Message",
				new SAPISCERuntimeException()));
		final SAPISCERuntimeException ex = new SAPISCERuntimeException(new SAPISCERuntimeException());
		Assert.assertNotNull("Exception object should not be null", ex);
		Assert.assertNotNull("Exception cause object should not be null", ex.getCause());
	}
}

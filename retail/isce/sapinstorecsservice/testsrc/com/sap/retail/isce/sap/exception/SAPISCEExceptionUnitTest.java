/*****************************************************************************
 Class:        SAPISCEExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.sap.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for SAPISCEException.
 *
 */
@UnitTest
public class SAPISCEExceptionUnitTest
{

	/**
	 * Tests SAPISCEException constructors.
	 */
	@Test
	public void testDataContainerExceptionConstructors()
	{
		Assert.assertNotNull("Exception object should not be null", new SAPISCEException());
		Assert.assertNotNull("Exception object should not be null", new SAPISCEException("Message"));
		Assert.assertNotNull("Exception object should not be null", new SAPISCEException("Message", new SAPISCEException()));
		final SAPISCEException ex = new SAPISCEException(new SAPISCEException());
		Assert.assertNotNull("Exception object should not be null", ex);
		Assert.assertNotNull("Exception cause object should not be null", ex.getCause());
	}
}

/*****************************************************************************
 Class:        DataContainerRuntimeExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for DataContainerRuntimeException.
 *
 */
@UnitTest
public class DataContainerRuntimeExceptionUnitTest
{

	/**
	 * Tests DataContainerRuntimeException constructors.
	 */
	@Test
	public void testDataContainerRuntimeExceptionConstructors()
	{
		Assert.assertNotNull("Test context map should not be null", new DataContainerRuntimeException());
		Assert.assertNotNull("Test context map should not be null", new DataContainerRuntimeException("Message"));
		Assert.assertNotNull("Test context map should not be null", new DataContainerRuntimeException("Message",
				new DataContainerRuntimeException()));
		Assert.assertNotNull("Test context map should not be null", new DataContainerRuntimeException(
				new DataContainerRuntimeException()));
	}

}

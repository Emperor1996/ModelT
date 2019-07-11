/*****************************************************************************
 Class:        DataContainerBaseExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for DataContainerBaseException.
 *
 */
@UnitTest
public class DataContainerBaseExceptionUnitTest
{

	/**
	 * Tests DataContainerBaseException constructors.
	 */
	@Test
	public void testDataContainerBaseExceptionConstructors()
	{
		Assert.assertNotNull("Test context map should not be null", new DataContainerBaseException());
		Assert.assertNotNull("Test context map should not be null", new DataContainerBaseException("Message"));
		Assert.assertNotNull("Test context map should not be null", new DataContainerBaseException("Message",
				new DataContainerBaseException()));
		Assert.assertNotNull("Test context map should not be null",
				new DataContainerBaseException(new DataContainerBaseException()));
	}

}

/*****************************************************************************
 Class:        DataContainerServiceExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for DataContainerServiceException.
 *
 */
@UnitTest
public class DataContainerServiceExceptionUnitTest
{

	/**
	 * Tests DataContainerServiceException constructors.
	 */
	@Test
	public void testDataContainerServiceExceptionConstructors()
	{
		Assert.assertNotNull("Test context map should not be null", new DataContainerServiceException());
		Assert.assertNotNull("Test context map should not be null", new DataContainerServiceException("Message"));
		Assert.assertNotNull("Test context map should not be null", new DataContainerServiceException("Message",
				new DataContainerServiceException()));
		Assert.assertNotNull("Test context map should not be null", new DataContainerServiceException(
				new DataContainerServiceException()));
	}

}

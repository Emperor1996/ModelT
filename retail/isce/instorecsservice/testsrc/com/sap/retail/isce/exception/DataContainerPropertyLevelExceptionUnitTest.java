/*****************************************************************************
 Class:        DataContainerPropertyLevelExceptionUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.exception;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit Test class for DataContainerPropertyLevelException.
 *
 */
@UnitTest
public class DataContainerPropertyLevelExceptionUnitTest
{

	/**
	 * Tests DataContainerPropertyLevelException constructors.
	 */
	@Test
	public void testDataContainerPropertyLevelExceptionConstructors()
	{
		Assert.assertNotNull("Test context map should not be null", new DataContainerPropertyLevelException());
		Assert.assertNotNull("Test context map should not be null", new DataContainerPropertyLevelException("Message"));
		Assert.assertNotNull("Test context map should not be null", new DataContainerPropertyLevelException("Message",
				new DataContainerPropertyLevelException()));
		Assert.assertNotNull("Test context map should not be null", new DataContainerPropertyLevelException(
				new DataContainerPropertyLevelException()));
	}

}

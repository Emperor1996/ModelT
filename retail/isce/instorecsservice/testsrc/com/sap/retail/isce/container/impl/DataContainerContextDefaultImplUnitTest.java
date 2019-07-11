/*****************************************************************************
 Class:        DataContainerContextDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;


/**
 * Unit Test class for DataContainerContextDefaultImpl.
 *
 */
@UnitTest
public class DataContainerContextDefaultImplUnitTest extends UnitTestBase
{
	private DataContainerContextDefaultImpl classUnderTest = null;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerContextDefaultImpl();
	}

	/**
	 * Tests readDataContainers.
	 */
	@Test
	public void testReadDataContainers()
	{
		final Map contextMap = classUnderTest.getContextMap();
		Assert.assertNotNull("Test context map should not be null", contextMap);
		assertEquals("Test context map should be empty", Boolean.TRUE, Boolean.valueOf(contextMap.isEmpty()));
	}
}

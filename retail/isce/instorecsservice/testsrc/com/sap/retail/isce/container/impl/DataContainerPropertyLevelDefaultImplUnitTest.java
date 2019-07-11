/*****************************************************************************
 Class:        DataContainerPropertyLevelDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;


/**
 * Unit Test class for DataContainerPropertyLevelDefaultImpl.
 *
 */
@UnitTest
public class DataContainerPropertyLevelDefaultImplUnitTest extends UnitTestBase
{
	private DataContainerPropertyLevelDefaultImpl classUnderTest = null;
	private final static String LEVEL_DESC1 = "LevelDesc1";
	private final static String LEVEL_DESC2 = "LevelDesc2";

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerPropertyLevelDefaultImpl("1", "1", "2", "unit1", LEVEL_DESC1);
	}

	@Test
	public void testGetSetDescription()
	{
		assertEquals(" Description should be: " + LEVEL_DESC1, LEVEL_DESC1, classUnderTest.getDescription());
		classUnderTest.setDescription(LEVEL_DESC2);
		assertEquals(" Description should be: " + LEVEL_DESC2, LEVEL_DESC2, classUnderTest.getDescription());
	}

}

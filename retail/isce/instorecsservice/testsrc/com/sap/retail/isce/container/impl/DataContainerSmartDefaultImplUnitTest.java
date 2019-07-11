/*****************************************************************************
 Class:        DataContainerSmartDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.exception.DataContainerServiceException;


/**
 * Unit Test class for DataContainerSmartDefaultImpl.
 *
 */
public class DataContainerSmartDefaultImplUnitTest
{
	private class DataContainerSmartDefaultImplTest extends DataContainerSmartDefaultImpl
	{
		@Override
		public void readData() throws DataContainerServiceException
		{
			// do nothing
		}

		@Override
		public void setDataInErrorState()
		{
			// no own data to be set
		}

		@Override
		public void encodeHTML()
		{
			//
		}

		@Override
		public String getLocalizedContainerName()
		{
			return null;
		}

		@Override
		public String getContainerContextParamName()
		{
			return "DataContainerSmartDefaultImplTest";
		}
	}

	private DataContainerSmartDefaultImpl classUnderTest = null;


	@Before
	public void setUp()
	{
		// As the class to be tested is abstract, we test a derived class
		classUnderTest = new DataContainerSmartDefaultImplTest();
	}

	/**
	 * Tests readData.
	 */
	@Test
	public void testReadData()
	{
		try
		{
			classUnderTest.readData();
			assertTrue("Test method execution went well", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}
}

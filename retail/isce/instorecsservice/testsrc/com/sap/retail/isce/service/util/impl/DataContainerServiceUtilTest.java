/*****************************************************************************
 Class:        DataContainerServiceUtilTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.impl.DataContainerCombinedDefaultImpl;
import com.sap.retail.isce.container.impl.DataContainerODataDefaultImpl;
import com.sap.retail.isce.container.impl.DataContainerSmartDefaultImpl;
import com.sap.retail.isce.exception.DataContainerServiceException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;


/**
 * Unit Test class for the DataContainerServiceUtilTest class
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class DataContainerServiceUtilTest
{
	private DataContainerServiceUtil classUnderTest = null;

	private TestDataContainerOData testDataContainerOData = null;
	private TestDataContainerSmart testDataContainerSmart = null;

	private class TestDataContainerOData extends DataContainerODataDefaultImpl
	{
		public TestDataContainerOData()
		{
			this.containerName = TestDataContainerOData.class.getName();
		}

		@Override
		public String getServiceURI()
		{
			return null;
		}

		@Override
		public String getServiceEndpointName()
		{
			return null;
		}

		@Override
		public String getHttpDestinationName()
		{
			return null;
		}

		@Override
		public String getFilter()
		{
			return null;
		}

		@Override
		public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
		{
			//
		}

		@Override
		public void setDataInErrorState()
		{
			//
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
			return "TestDataContainerOData";
		}
	}

	private class TestDataContainerSmart extends DataContainerSmartDefaultImpl
	{
		public TestDataContainerSmart()
		{
			this.containerName = TestDataContainerSmart.class.getName();
		}

		@Override
		public void readData() throws DataContainerServiceException
		{
			//
		}

		@Override
		public void setDataInErrorState()
		{
			//
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
			return "TestDataContainerSmart";
		}
	}

	private class TestDataContainerCombined extends DataContainerCombinedDefaultImpl
	{

		@Override
		public void combineData(final Map<String, DataContainer> dataContainerMap) throws DataContainerServiceException
		{
			//
		}

		@Override
		public List<String> getDataContainerNamesForCombining()
		{
			return null;
		}

		@Override
		public String getLocalizedContainerName()
		{
			return null;
		}

		@Override
		public String getContainerContextParamName()
		{
			return null;
		}

		@Override
		public void setDataInErrorState()
		{
			//
		}

		@Override
		public void encodeHTML()
		{
			//
		}

	}

	@Before
	public void setUp()
	{
		this.testDataContainerOData = new TestDataContainerOData();
		this.testDataContainerSmart = new TestDataContainerSmart();
		final TestDataContainerCombined testDataContainerCombined = new TestDataContainerCombined();

		final List<DataContainer> dcList = Arrays.asList(this.testDataContainerOData, this.testDataContainerSmart,
				testDataContainerCombined);
		this.classUnderTest = new DataContainerServiceUtil();
		this.classUnderTest.setDataContainers(dcList);
	}

	@Test
	public void testGetDataContainerForName()
	{
		Map<String, DataContainer> dataContainerInstances = this.classUnderTest.getDataContainersForNames(null);
		assertEquals(dataContainerInstances.size(), 0);

		List<String> dataContainerNamesForCombining = Arrays.asList();

		dataContainerInstances = this.classUnderTest.getDataContainersForNames(dataContainerNamesForCombining);
		assertEquals(dataContainerInstances.size(), 0);

		dataContainerNamesForCombining = Arrays.asList("TestDataContainerOData", "TestDataContainerSmart");

		dataContainerInstances = this.classUnderTest.getDataContainersForNames(dataContainerNamesForCombining);
		assertEquals(dataContainerInstances.size(), 2);
		assertEquals(dataContainerInstances.get("TestDataContainerOData").getContainerContextParamName(), "TestDataContainerOData");
		assertEquals(dataContainerInstances.get("TestDataContainerSmart").getContainerContextParamName(), "TestDataContainerSmart");
		assertEquals(dataContainerInstances.get("TestDataContainerOData"), this.testDataContainerOData);
		assertEquals(dataContainerInstances.get("TestDataContainerSmart"), this.testDataContainerSmart);
	}



}

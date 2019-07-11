/*****************************************************************************
 Class:        DataContainerServiceDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerCombined;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.DataContainerOData;
import com.sap.retail.isce.container.impl.DataContainerCombinedDefaultImpl;
import com.sap.retail.isce.container.impl.DataContainerODataDefaultImpl;
import com.sap.retail.isce.container.impl.DataContainerSmartDefaultImpl;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.exception.DataContainerServiceException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.service.sap.DataContainerSapServiceOData;
import com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImpl;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;
import com.sap.retail.isce.service.util.impl.SpringUtilDefaultImpl;


/**
 * Unit Test class for implementation class DataContainerServiceDefaultImpl.
 *
 */
@UnitTest
public class DataContainerServiceDefaultImplUnitTest extends UnitTestBase
{
	static final String READ_DATA_CALLED = "readDataHasBeenCalled";
	static final String SET_DATA_IN_ERROR_STATE_CALLED = "setDataInErrorStateHasBeenCalled";

	private DataContainerServiceDefaultImpl classUnderTest = null;
	private List<DataContainer> dataContainers = null;
	private final DataContainerSapServiceOData dataContainerSapServiceOData = new DataContainerSapServiceODataDefaultImpl();
	private final DataContainerServiceUtil dataContainerServiceUtil = new DataContainerServiceUtil();
	private DataContainerSapServiceOData dataContainerSapServiceODataSpy = null;
	private TestDataContainerOData testDataContainerOData = null;
	private String setDataInErrorStateCalled = null;
	private boolean throwDataContainerServiceException = false;

	private static String readDataCalled = null;
	private final SpringUtilTest springUtilMock = new SpringUtilTest();

	private class TestDataContainerOData extends DataContainerODataDefaultImpl
	{
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
			setDataInErrorStateCalled = SET_DATA_IN_ERROR_STATE_CALLED;
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

	private class TestDataContainerCombined extends DataContainerCombinedDefaultImpl
	{

		@Override
		public String getContainerName()
		{
			return "TestDataContainerCombinedName";
		}

		@Override
		public String getLocalizedContainerName()
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

		@Override
		public List<String> getDataContainerNamesForCombining()
		{
			return null;
			//			return new List<String>();
		}

		@Override
		public void combineData(final Map<String, DataContainer> dataContainerMap) throws DataContainerServiceException
		{
			if (throwDataContainerServiceException)
			{
				throw new DataContainerServiceException();
			}
		}

		@Override
		public String getContainerContextParamName()
		{
			return "TestDataContainerCombined";
		}
	}

	private class TestDataContainerSmart extends DataContainerSmartDefaultImpl
	{
		@Override
		public String getContainerName()
		{
			return "TestDataContainerSmartException";
		}

		@Override
		public void readData() throws DataContainerServiceException
		{
			readDataCalled = READ_DATA_CALLED;
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

	private class TestDataContainerSmartException extends DataContainerSmartDefaultImpl
	{

		public TestDataContainerSmartException()
		{
			containerName = "containerName1";

		}

		@Override
		public void readData() throws DataContainerServiceException
		{
			throw new DataContainerServiceException();
		}

		@Override
		public void setDataInErrorState()
		{
			setDataInErrorStateCalled = SET_DATA_IN_ERROR_STATE_CALLED;
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
			return "TestDataContainerSmartException";
		}
	}

	class SpringUtilTest extends SpringUtilDefaultImpl
	{
		private DataContainerServiceUtil dataContainerServiceUtil = null;

		public void setDataContainerServiceUtil(final DataContainerServiceUtil dataContainerServiceUtil)
		{
			this.dataContainerServiceUtil = dataContainerServiceUtil;
		}

		@Override
		public Object getBean(final String beanName)
		{
			switch (beanName)
			{
				case "defaultDataContainerServiceUtil":
					return this.dataContainerServiceUtil;
				default:
					return null;
			}
		}
	}


	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerServiceDefaultImpl();

		final SessionService sessionService = mock(SessionService.class);
		classUnderTest.setSessionService(sessionService);

		dataContainerSapServiceODataSpy = Mockito.spy(dataContainerSapServiceOData);
		classUnderTest.setDataContainerSapServiceOData(dataContainerSapServiceODataSpy);
		final DataContainerServiceUtil dataContainerServiceUtilSpy = Mockito.spy(dataContainerServiceUtil);

		springUtilMock.setDataContainerServiceUtil(dataContainerServiceUtilSpy);
		classUnderTest.setSpringUtil(springUtilMock);
	}

	/**
	 * Tests readDataContainers OData.
	 */
	@Test
	public void testReadDataContainersOData()
	{
		testDataContainerOData = new TestDataContainerOData();
		dataContainers = new ArrayList<DataContainer>();
		dataContainers.add(testDataContainerOData);

		Mockito.doNothing().when(dataContainerSapServiceODataSpy)
				.readDataContainers(Arrays.asList((DataContainerOData) testDataContainerOData));
		classUnderTest.readDataContainers(dataContainers, null);
		Mockito.verify(dataContainerSapServiceODataSpy, Mockito.times(1)).readDataContainers(
				Arrays.asList((DataContainerOData) testDataContainerOData));
	}

	/**
	 * Tests testReadCombinedDataContainersFirst Combined as first entry in list.
	 */
	@Test
	public void testReadCombinedDataContainersFirst()
	{
		final DataContainerServiceUtil dataContainerServiceUtil = new DataContainerServiceUtil();
		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();

		dataContainers = new ArrayList<DataContainer>();
		final List<DataContainerCombined> combinedDataContainers = new ArrayList<>();

		final DataContainerCombined dataContainerCombined = Mockito.mock(DataContainerCombined.class);
		dataContainers.add(dataContainerCombined);
		combinedDataContainers.add(dataContainerCombined);

		final DataContainerSmartDefaultImpl dataContainerSmart = Mockito.mock(DataContainerSmartDefaultImpl.class);
		dataContainers.add(dataContainerSmart);
		try
		{
			dataContainerServiceUtil.setDataContainers(dataContainers);

			classUnderTest.readCombinedDataContainers(dataContainerServiceUtil, combinedDataContainers);
			Mockito.verify(dataContainerCombined, Mockito.times(1)).combineData(dataContainerMap);
			final InOrder inOrder = Mockito.inOrder(dataContainerCombined, dataContainerSmart);
			inOrder.verify(dataContainerCombined).combineData(dataContainerMap);
			assertTrue("Test method execution went well", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}


	/**
	 * Tests readDataContainers Combined as last entry in list.
	 */
	@Test
	public void testReadCombinedDataContainersLast()
	{

		final DataContainerServiceUtil dataContainerServiceUtil = new DataContainerServiceUtil();
		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();

		dataContainers = new ArrayList<DataContainer>();
		final List<DataContainerCombined> combinedDataContainers = new ArrayList<>();

		final DataContainerSmartDefaultImpl dataContainerSmart = Mockito.mock(DataContainerSmartDefaultImpl.class);
		dataContainers.add(dataContainerSmart);

		final DataContainerCombined dataContainerCombined = Mockito.mock(DataContainerCombined.class);
		dataContainers.add(dataContainerCombined);
		combinedDataContainers.add(dataContainerCombined);

		try
		{
			classUnderTest.readCombinedDataContainers(dataContainerServiceUtil, combinedDataContainers);
			Mockito.verify(dataContainerCombined, Mockito.times(1)).combineData(dataContainerMap);
			final InOrder inOrder = Mockito.inOrder(dataContainerCombined, dataContainerSmart);
			inOrder.verify(dataContainerCombined).combineData(dataContainerMap);
			assertTrue("Test method execution went well", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}

	/**
	 * Tests testReadCombinedDataContainersException Combined as last entry in list.
	 */
	@Test
	public void testReadCombinedDataContainersException()
	{
		this.suppressLogging(); // Currently unclear why logging must be suppressed here (actually the super.setup() takes care on that) !!
		final DataContainerServiceUtil dataContainerServiceUtil = new DataContainerServiceUtil();

		dataContainers = new ArrayList<DataContainer>();
		final List<DataContainerCombined> dataContainersCombined = new ArrayList<>();

		final DataContainerCombined dataContainerCombined = new TestDataContainerCombined();
		dataContainers.add(dataContainerCombined);
		classUnderTest.readCombinedDataContainers(dataContainerServiceUtil, dataContainersCombined);

		throwDataContainerServiceException = true;
		classUnderTest.readDataContainers(dataContainers, null);

		assertEquals("", Boolean.valueOf(true), dataContainerCombined.getErrorState());
	}

	/**
	 * Tests readDataContainers Combined as last entry in list.
	 */
	@Test
	public void testReadDataContainersCombinedException()
	{
		dataContainers = new ArrayList<DataContainer>();

		//		final DataContainerSmartDefaultImpl dataContainerSmart = new TestDataContainerSmart();
		//		dataContainers.add(dataContainerSmart);
		final DataContainerCombined dataContainersCombined = new TestDataContainerCombined();
		dataContainers.add(dataContainersCombined);

		throwDataContainerServiceException = true;
		classUnderTest.readDataContainers(dataContainers, null);
		assertEquals("", Boolean.valueOf(true), dataContainersCombined.getErrorState());
	}

	/**
	 * Tests readDataContainers Smart.
	 */
	@Test
	public void testReadDataContainersSmart()
	{
		final TestDataContainerSmart testDataContainerSmart = new TestDataContainerSmart();
		dataContainers = new ArrayList<DataContainer>();
		dataContainers.add(testDataContainerSmart);
		classUnderTest.readDataContainers(dataContainers, null);
		assertEquals("Method readData has been properly called", readDataCalled, READ_DATA_CALLED);
	}

	/**
	 * Tests readDataContainers Smart Exception.
	 */
	@Test
	public void testReadDataContainersSmartException()
	{
		setDataInErrorStateCalled = null;
		final TestDataContainerSmartException testDataContainerSmartException = new TestDataContainerSmartException();
		dataContainers = new ArrayList<DataContainer>();
		dataContainers.add(testDataContainerSmartException);
		classUnderTest.readDataContainers(dataContainers, null);
		assertEquals("Error State should be TRUE", Boolean.TRUE, testDataContainerSmartException.getErrorState());
		assertEquals("Data in Error state have been initialized", SET_DATA_IN_ERROR_STATE_CALLED, setDataInErrorStateCalled);
	}

	/**
	 * Tests readDataContainers Exception.
	 */
	@Test
	public void testReadDataContainersOtherException()
	{
		try
		{
			final List<DataContainer> dataContainers = new ArrayList<DataContainer>();
			dataContainers.add(new DataContainer()
			{

				@Override
				public String getContainerName()
				{
					return null;
				}

				@Override
				public void setDataContainerContext(final DataContainerContext dataContainerContext)
				{
					//
				}

				@Override
				public void setErrorState(final Boolean errorState)
				{
					//
				}

				@Override
				public Boolean getErrorState()
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

				@Override
				public String getLocalizedContainerName()
				{
					return null;
				}

				@Override
				public String getContainerContextParamName()
				{
					return "";
				}

				@Override
				public void determineDataForCMSComponent()
				{
					//
				}

				@Override
				public void setCMSComponentModel(final AbstractCMSComponentModel cmsComponentModel)
				{
					//
				}

				@Override
				public void traceInformation()
				{
					//
				}
			});

			classUnderTest.readDataContainers(dataContainers, null);
			assertFalse("Test method execution should not be called", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Test method execution went well", true);
		}
	}

	/**
	 * Tests setDataContainerSapServiceOData.
	 */
	@Test
	public void testSetDataContainerSapServiceOData()
	{
		final DataContainerSapServiceOData dataContainerSapServiceOData = new DataContainerSapServiceODataDefaultImpl();
		classUnderTest.setDataContainerSapServiceOData(dataContainerSapServiceOData);
		assertEquals("DataContainerSapServiceOData shall be exact ", dataContainerSapServiceOData,
				classUnderTest.dataContainerSapServiceOData);
	}

	/**
	 * Tests updateDataContainersForComponent
	 */
	@Test
	public void testUpdateDataContainersForComponent()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = new CMSISCECustomer360ComponentModel();
		dataContainers = new ArrayList<DataContainer>();
		final DataContainer dataContainer = Mockito.mock(DataContainer.class);
		dataContainers.add(dataContainer);

		classUnderTest.updateDataContainersForComponent(dataContainers, cmsComponentModel);

		Mockito.verify(dataContainer, Mockito.times(1)).setCMSComponentModel(cmsComponentModel);
		Mockito.verify(dataContainer, Mockito.times(1)).determineDataForCMSComponent();

		assertTrue("Test method execution went well", true);
	}
}

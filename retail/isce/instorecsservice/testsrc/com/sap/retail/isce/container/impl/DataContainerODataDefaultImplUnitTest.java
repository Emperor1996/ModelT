/*****************************************************************************
 Class:        DataContainerODataDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Unit Test class for implementation class DataContainerODataDefaultImpl.
 *
 */
@UnitTest
public class DataContainerODataDefaultImplUnitTest extends UnitTestBase
{
	private class DataContainerODataDefaultImplTest extends DataContainerODataDefaultImpl
	{
		DataContainerODataDefaultImplTest()
		{
			this.containerName = "containerNameXY";
		}

		@Override
		public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
		{
			throw new DataContainerRuntimeException("This method must be redifined in derived class");
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
			return "DataContainerODataDefaultImplTest";
		}
	}

	private class DataContainerODataDefaultImplInitializedTest extends DataContainerODataDefaultImplTest
	{
		DataContainerODataDefaultImplInitializedTest()
		{
			this.containerName = "containerNameXY";
			this.httpDestinationName = "HttpDestinationNameXY";
			this.serviceURI = "/sap/opu/odata/sap/ServiceNameXY";
			this.serviceEndpointName = "ServiceEndpointNameXY";
			this.resultName = "ResultNameXY";
		}
	}

	// As the class to be tested is abstract, we test a derived class
	private DataContainerODataDefaultImplTest classUnderTest = null;
	private DataContainerODataDefaultImplInitializedTest classUnderTestInitialized = null;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerODataDefaultImplTest();
		classUnderTestInitialized = new DataContainerODataDefaultImplInitializedTest();
	}

	/**
	 * Tests getHttpDestinationName.
	 */
	@Test
	public void testGetHttpDestinationName()
	{
		assertEquals("DestinationName should be initialized", "HttpDestinationNameXY",
				classUnderTestInitialized.getHttpDestinationName());
	}

	/**
	 * Tests testGetHttpDestinationNameException.
	 */
	@Test
	public void testGetHttpDestinationNameException()
	{
		try
		{
			classUnderTest.getHttpDestinationName();
			assertTrue("DataContainerRuntimeException should have been thrown", false);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing HttpDestinationName was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests getServiceURI.
	 */
	@Test
	public void testGetServiceURI()
	{
		assertEquals("ServiceURI should be initialized.", "/sap/opu/odata/sap/ServiceNameXY",
				classUnderTestInitialized.getServiceURI());
	}

	/**
	 * Tests getServiceURI with Exception.
	 */
	@Test
	public void testGetServiceURIException()
	{
		try
		{
			classUnderTest.getServiceURI();
			assertTrue("DataContainerRuntimeException should have been thrown", false);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing ServiceURI was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests getServiceEndpointName.
	 */
	@Test
	public void testGetServiceEndpointName()
	{
		assertEquals("ServiceEndpointName should be initialized", "ServiceEndpointNameXY",
				classUnderTestInitialized.getServiceEndpointName());
	}

	/**
	 * Tests getServiceEndpointName.
	 */
	@Test
	public void testGetServiceEndpointNameException()
	{
		try
		{
			classUnderTest.getServiceEndpointName();
			assertTrue("DataContainerRuntimeException should have been thrown", false);

		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing ServiceEndpointName was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests getKeyPredicate.
	 */
	@Test
	public void testGetKeyPredicate()
	{
		assertEquals("KeyPredicate should be null", null, classUnderTest.getKeyPredicate());
	}

	/**
	 * Tests getNavigationProperties.
	 */
	@Test
	public void testGetNavigationProperties()
	{
		assertEquals("NavigationProperties should be null", null, classUnderTest.getNavigationProperties());
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetFilter()
	{
		assertEquals("Filter should be null", null, classUnderTest.getFilter());
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceabel Filter should not be null", "<implement proper getTraceableFilter method for specific container>",
				classUnderTest.getTraceableFilter());
	}

	/**
	 * Tests getSelect.
	 */
	@Test
	public void testGetSelect()
	{
		assertEquals("Select should be null", null, classUnderTest.getSelect());
	}

	/**
	 * Tests getOrderBy.
	 */
	@Test
	public void testGetOrderBy()
	{
		assertEquals("OrderBy should be null", null, classUnderTest.getOrderBy());
	}

	/**
	 * Tests getTop.
	 */
	@Test
	public void testGetTop()
	{
		assertEquals("Top should be null", null, classUnderTest.getTop());
	}

	/**
	 * Tests getSkip.
	 */
	@Test
	public void testGetSkip()
	{
		assertEquals("Skip should be null", null, classUnderTest.getSkip());
	}

	/**
	 * Tests getInlineCount.
	 */
	@Test
	public void testGetInlineCount()
	{
		assertEquals("InlineCount should be null", null, classUnderTest.getInlineCount());
	}

	/**
	 * Tests getFreeQueryMap.
	 */
	@Test
	public void testGetFreeQueryMap()
	{
		assertEquals("FreeQueryMap should be null", null, classUnderTest.getFreeQueryMap());
	}

	/**
	 * Tests getResultName.
	 */
	@Test
	public void testGetResultName()
	{
		assertEquals("ResultName should be initialized", "ResultNameXY", classUnderTestInitialized.getResultName());
	}

	/**
	 * Tests getResultNameException.
	 */
	@Test
	public void getResultNameException()
	{
		try
		{
			classUnderTest.getResultName();
			assertTrue("DataContainerRuntimeException should have been thrown", false);

		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing ResultName was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests extractOwnDatafromHttpOdataResult.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		try
		{
			classUnderTest.extractOwnDataFromResult(null);
			assertFalse("Method should have thrown an exception", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Method has been called properly", true);
		}
	}
}

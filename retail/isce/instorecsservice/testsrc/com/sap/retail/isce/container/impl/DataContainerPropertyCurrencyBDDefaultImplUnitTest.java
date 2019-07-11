/*****************************************************************************
 Class:        DataContainerPropertyCurrencyBDDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.service.mock.BaseStoreServiceMock;
import com.sap.retail.isce.service.mock.CommonI18NServiceMock;


/**
 * Unit Test class for DataContainerPropertyCurrencyBDDefaultImpl.
 *
 */
@UnitTest
public class DataContainerPropertyCurrencyBDDefaultImplUnitTest extends UnitTestBase
{
	private DataContainerPropertyCurrencyBDDefaultImpl classUnderTest = null;
	private final BaseStoreService baseStoreService = new BaseStoreServiceMock();
	private final CommonI18NService commonI18NService = new CommonI18NServiceMock();

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		initBaseStoreServiceMock();
		initCommonI18NServiceMock();

		classUnderTest = new DataContainerPropertyCurrencyBDDefaultImpl(null, "", "");
		classUnderTest.setBaseStoreService(baseStoreService);
		classUnderTest.setCommonI18NService(commonI18NService);
	}

	@Test
	public void testSetBaseStoreService()
	{
		classUnderTest.setBaseStoreService(null);
		assertNull("baseStoreService should be null.", classUnderTest.baseStoreService);
		classUnderTest.setBaseStoreService(baseStoreService);
		assertEquals("baseStoreService has been set.", baseStoreService, classUnderTest.baseStoreService);
	}

	@Test
	public void testSetCommonI18NService()
	{
		classUnderTest.commonI18NService = null;
		classUnderTest.setCommonI18NService(null);
		assertNull("commonI18NService should be null.", classUnderTest.commonI18NService);
		classUnderTest.setCommonI18NService(commonI18NService);
		assertEquals("commonI18NService has been set.", commonI18NService, classUnderTest.commonI18NService);
	}

	@Test
	public void testGetBaseStoreCurrencyModel()
	{
		try
		{
			initBaseStoreServiceMock();
			classUnderTest.baseStoreService = null;
			classUnderTest.setBaseStoreService(baseStoreService);
			assertNotNull("Currency model should have been found.", classUnderTest.getBaseStoreCurrencyModel());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getBaseStoreCurrencyModel - Exception should not have been thrown.", true);
		}

		//
		// test exceptions
		//

		try
		{
			initBaseStoreServiceMock();
			classUnderTest.baseStoreService = null;
			classUnderTest.getBaseStoreCurrencyModel();
			assertFalse("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}

		try
		{
			initBaseStoreServiceMock();
			classUnderTest.baseStoreService = null;
			classUnderTest.setBaseStoreService(baseStoreService);

			((BaseStoreServiceMock) baseStoreService).returnNoCurrentBaseStore = true;
			classUnderTest.getBaseStoreCurrencyModel();
			assertFalse("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}

		try
		{
			initBaseStoreServiceMock();
			classUnderTest.baseStoreService = null;
			classUnderTest.setBaseStoreService(baseStoreService);

			((BaseStoreServiceMock) baseStoreService).returnNoDefaultCurrencyFromBaseStore = true;
			classUnderTest.getBaseStoreCurrencyModel();
			assertFalse("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getBaseStoreCurrencyModel - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testGetCurrentCurrencyModel()
	{
		try
		{
			initCommonI18NServiceMock();
			classUnderTest.commonI18NService = null;
			classUnderTest.setCommonI18NService(commonI18NService);
			assertNotNull("Currency model should have been found.", classUnderTest.getCurrentCurrencyModel());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getCurrentCurrencyModel - Exception should not have been thrown.", true);
		}

		//
		// test exceptions
		//

		try
		{
			initCommonI18NServiceMock();
			classUnderTest.commonI18NService = null;
			classUnderTest.getCurrentCurrencyModel();
			assertFalse("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}

		try
		{
			initCommonI18NServiceMock();
			classUnderTest.commonI18NService = null;
			classUnderTest.setCommonI18NService(commonI18NService);

			((CommonI18NServiceMock) commonI18NService).returnNoCurrentCurrency = true;
			classUnderTest.getCurrentCurrencyModel();
			assertFalse("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testCurrencyConvertValue()
	{
		final BigDecimal value = BigDecimal.valueOf(100);
		try
		{
			assertEquals("Converted value should be: ", null, classUnderTest.currencyConvertValue(null));
			assertEquals("Converted value should be: ", BigDecimal.valueOf(50), classUnderTest.currencyConvertValue(value));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("currencyConvertValue - Exception should not have been thrown.", true);
		}

		//
		// test exceptions
		//

		try
		{
			initCommonI18NServiceMock();
			classUnderTest.commonI18NService = null;
			classUnderTest.currencyConvertValue(value);
			assertFalse("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}

		try
		{
			initCommonI18NServiceMock();
			classUnderTest.setCommonI18NService(commonI18NService);
			((CommonI18NServiceMock) commonI18NService).causeConvertAndRoundCurrencyInfinity = true;
			classUnderTest.currencyConvertValue(value);
			assertFalse("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("getCurrentCurrencyModel - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testCalculateLevelBoundaryPairs()
	{
		List<Comparable> boundaries = Arrays.asList(BigDecimal.valueOf(1), BigDecimal.valueOf(10));
		List<Comparable> resBoundaries;
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculatelevelBoundaryPairs - 2 levels (1,10) - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 1", BigDecimal.valueOf(1), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 4", BigDecimal.valueOf(4), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 5", BigDecimal.valueOf(5), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - null", null, resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculatelevelBoundaryPairs - 2 levels (1, 10) - no exception should have been thrown", true);
		}

		boundaries = Arrays.asList("", "");
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertFalse("calculatelevelBoundaryPairs - Exception should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e6)
		{
			assertTrue("calculatelevelBoundaryPairs - Exception should have been thrown", true);
		}
	}

	private void initBaseStoreServiceMock()
	{
		((BaseStoreServiceMock) baseStoreService).returnNoCurrentBaseStore = false;
		((BaseStoreServiceMock) baseStoreService).returnNoDefaultCurrencyFromBaseStore = false;
	}

	private void initCommonI18NServiceMock()
	{
		((CommonI18NServiceMock) commonI18NService).returnNoCurrentCurrency = false;
		((CommonI18NServiceMock) commonI18NService).causeConvertAndRoundCurrencyInfinity = false;
	}
}

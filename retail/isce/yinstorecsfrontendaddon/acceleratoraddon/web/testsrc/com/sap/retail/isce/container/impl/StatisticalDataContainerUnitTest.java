/*****************************************************************************
 Class:        StatisticalDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.container.impl.mock.DataContainerPropertyCurrencyBDMock;
import com.sap.retail.isce.container.impl.mock.DataContainerPropertyIntegerMock;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.container.impl.mock.UserServiceMock;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerServiceException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.model.CMSISCEPurchaseHistoryComponentModel;
import com.sap.retail.isce.service.mock.CommonI18NServiceMock;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;
import com.sap.retail.isce.service.util.StatisticalDataSalesVolumeUtil;


/**
 * Unit Test class for StatisticalDataContainer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class StatisticalDataContainerUnitTest extends UnitTestBase
{
	@Resource(name = "statisticalContainerUnderTest")
	private StatisticalDataContainer classUnderTest;
	private final static String TEST_STRING_HTML_ENCODED = "NiceText&#x21;&#x20;&#xa7;&#x24;&#x25;&amp;&#x2f;&#x28;&#x29;&#x3d;&#x2b;&#x2a;&#x23;&#x27;&lt;&gt;&#x7c;&#x2f;&#x2a;-&#x2b;&#x3b;&#x3a;_,.-&#xb5;&#x5e;&#xb0;1234567890&#xdf;&#xdf;&#xb4;&#x60;&#x7e;&#x2b;&#x2a;";

	@Resource(name = "userService")
	private UserServiceMock userServiceMock;

	@Resource(name = "commonI18NService")
	private CommonI18NServiceMock commonI18NServiceMock;

	@Mock
	private CARStatisticalDataContainerOverallVolume carStatisticalContainerOverallVolumeMock;

	@Mock
	private CARStatisticalDataContainerSixMonthVolume carStatisticalContainerSixMonthVolumeMock;

	@Mock
	private CARStatisticalDataContainerOverallCount carStatisticalContainerOverallCountMock;

	@Mock
	private CARStatisticalDataContainerLastPurchase carStatisticalContainerLastPurchaseMock;

	@Mock
	private CARStatisticalDataContainerOverallItemsCount carStatisticalContainerOverallItemsCountMock;

	@Mock
	private StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtilMock;

	@Resource(name = "isceConfigurationService")
	private ISCEConfigurationService isceConfigurationServiceMock;

	LoggerMock loggerMock = new LoggerMock("");

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		MockitoAnnotations.initMocks(this);
		when(statisticalDataSalesVolumeUtilMock.getCurrentCurrency()).thenReturn(CommonI18NServiceMock.usdCurrencyModel);
		loggerMock.clearAll();
		StatisticalDataContainer.log = loggerMock;
	}

	@Test
	public void testSetDataInErrorState()
	{
		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();
		dataContainerMap.put("CARStatisticalDataContainerOverallVolume", carStatisticalContainerOverallVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerSixMonthVolume", carStatisticalContainerSixMonthVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerOverallCount", carStatisticalContainerOverallCountMock);
		dataContainerMap.put("CARStatisticalDataContainerLastPurchase", carStatisticalContainerLastPurchaseMock);
		dataContainerMap.put("CARStatisticalDataContainerOverallItemsCount", carStatisticalContainerOverallItemsCountMock);

		this.classUnderTest.dataContainerMap = dataContainerMap;

		this.classUnderTest.setDataInErrorState();

		verify(carStatisticalContainerOverallVolumeMock, times(1)).setDataInErrorState();
		verify(carStatisticalContainerSixMonthVolumeMock, times(1)).setDataInErrorState();
		verify(carStatisticalContainerOverallCountMock, times(1)).setDataInErrorState();
		verify(carStatisticalContainerLastPurchaseMock, times(1)).setDataInErrorState();
		verify(carStatisticalContainerOverallItemsCountMock, times(1)).setDataInErrorState();
	}

	@Test
	public void testGetLocalizedContainerName()
	{
		final String name = this.classUnderTest.getLocalizedContainerName();

		assertEquals("GetLocalizedContainerName - instorecs.customer360.statistical must have been returned as conatiner name",
				"instorecs.customer360.statistical", name);
	}

	@Test
	public void testGetContainerName()
	{
		final String name = this.classUnderTest.getContainerName();

		assertEquals(
				"GetContainerName - com.sap.retail.isce.container.impl.StatisticalDataContainer must have been returned as conatiner name",
				"com.sap.retail.isce.container.impl.StatisticalDataContainer", name);
	}

	/**
	 * Creates and sets mocks for the required data containers.
	 */
	protected void prepareMocks()
	{
		carStatisticalContainerOverallVolumeMock = new CARStatisticalDataContainerOverallVolume(isceConfigurationServiceMock);
		carStatisticalContainerOverallCountMock = new CARStatisticalDataContainerOverallCount(isceConfigurationServiceMock);
		carStatisticalContainerSixMonthVolumeMock = new CARStatisticalDataContainerSixMonthVolume(isceConfigurationServiceMock);
		carStatisticalContainerLastPurchaseMock = new CARStatisticalDataContainerLastPurchase(isceConfigurationServiceMock);
		carStatisticalContainerOverallItemsCountMock = new CARStatisticalDataContainerOverallItemsCount(
				isceConfigurationServiceMock);

		carStatisticalContainerOverallVolumeMock.setUserService(userServiceMock);
		carStatisticalContainerSixMonthVolumeMock.setUserService(userServiceMock);
		carStatisticalContainerOverallCountMock.setUserService(userServiceMock);
		carStatisticalContainerLastPurchaseMock.setUserService(userServiceMock);
		carStatisticalContainerOverallItemsCountMock.setUserService(userServiceMock);

		carStatisticalContainerOverallVolumeMock.setStatisticalDataSalesVolumeUtil(statisticalDataSalesVolumeUtilMock);
		carStatisticalContainerSixMonthVolumeMock.setStatisticalDataSalesVolumeUtil(statisticalDataSalesVolumeUtilMock);

		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();
		dataContainerMap.put("CARStatisticalDataContainerOverallVolume", carStatisticalContainerOverallVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerSixMonthVolume", carStatisticalContainerSixMonthVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerOverallCount", carStatisticalContainerOverallCountMock);
		dataContainerMap.put("CARStatisticalDataContainerLastPurchase", carStatisticalContainerLastPurchaseMock);
		dataContainerMap.put("CARStatisticalDataContainerOverallItemsCount", carStatisticalContainerOverallItemsCountMock);

		this.classUnderTest.dataContainerMap = dataContainerMap;

	}

	@Test
	public void testGetStorePurchaseRatio()
	{
		prepareMocks();

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(200), 4, ""));
		final String storePurchaseRatio = this.classUnderTest.getStorePurchaseRatio();

		assertEquals("getStorePurchaseRatio - store purchase ratio should be", "0%", storePurchaseRatio);

	}

	@Test
	public void testGetSalesVolumePrice()
	{
		prepareMocks();

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(200), 4, ""));

		final PriceData salesVolumePrice = this.classUnderTest.getOverallSalesVolumePrice();

		assertEquals("getSalesVolumePrice - sales volume price must be 200", 200, salesVolumePrice.getValue().doubleValue(), 0);
	}

	@Test
	public void testGetSalesVolumePriceLastSixMonths()
	{
		prepareMocks();

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(100), 3, ""));
		carStatisticalContainerSixMonthVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(50), 1, ""));

		final PriceData salesVolumePrice = this.classUnderTest.getSalesVolumePriceLastSixMonths();

		assertEquals("getSalesVolumePriceLastSixMonths - last six month sales volume price must be 50", 50, salesVolumePrice
				.getValue().doubleValue(), 0);
	}

	@Test
	public void testCombineData()
	{
		prepareMocks();

		// container missing
		try
		{
			this.classUnderTest.combineData(null);
			assertFalse("CombineData - exception must be thrown due to missing required container", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertTrue("CombineData - exception was thrown due to missing required container", true);
		}

		// wrong number of entries in container missing

		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();
		dataContainerMap.put("CARStatisticalDataContainerOverallVolume", carStatisticalContainerOverallVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerSixMonthVolume", carStatisticalContainerSixMonthVolumeMock);
		dataContainerMap.put("CARStatisticalDataContainerOverallCount", carStatisticalContainerOverallCountMock);


		try
		{
			this.classUnderTest.combineData(dataContainerMap);
			assertFalse("CombineData - exception must be thrown due to wrong number of entries in container", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertTrue("CombineData - exception was thrown due to missing required container", true);
		}

		// session currency != container currency
		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.ZERO, 0, "USD"));
		carStatisticalContainerSixMonthVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.ZERO, 0, "XYZ"));

		try
		{
			dataContainerMap.put("CARStatisticalDataContainerLastPurchase", carStatisticalContainerLastPurchaseMock);
			dataContainerMap.put("CARStatisticalDataContainerOverallItemsCount", carStatisticalContainerOverallItemsCountMock);

			this.classUnderTest.combineData(dataContainerMap);
			assertFalse("CombineData - exception must be thrown due to differing currency", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertTrue("CombineData - exception was thrown due to differing currency", true);
		}

		// everything o.k.
		carStatisticalContainerSixMonthVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.ZERO, 0, "USD"));

		try
		{
			this.classUnderTest.combineData(dataContainerMap);
			assertTrue("CombineData - everything o.k.", true);
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("CombineData - exception must not be thrown", true);
		}

		assertNotNull("salesVolumeProperty should have been created.", classUnderTest.salesVolumeProperty);
		assertNotNull("averageSalesVolumeProperty should have been created.", classUnderTest.averageSalesVolumeProperty);
		assertNotNull("lastPurchaseDateProperty should have been created.", classUnderTest.lastPurchaseDateProperty);
		assertNotNull("storePurchaseRatioProperty should have been created.", classUnderTest.storePurchaseRatioProperty);
	}

	@Test
	public void testGetAverageSalesVolumePrice()
	{
		prepareMocks();

		carStatisticalContainerSixMonthVolumeMock
				.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(77), 888, "USD"));

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.ZERO, 0, "USD"));
		PriceData salesVolumePrice = this.classUnderTest.getAverageOverallSalesVolumePrice();

		assertEquals("getAverageSalesVolumePrice - average sales volume price must be 0", BigDecimal.ZERO,
				salesVolumePrice.getValue());

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(100), 0, ""));
		salesVolumePrice = this.classUnderTest.getAverageOverallSalesVolumePrice();
		assertEquals("getAverageSalesVolumePrice - average sales volume price must be null", null, salesVolumePrice);

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.ZERO, 0, ""));
		salesVolumePrice = this.classUnderTest.getAverageOverallSalesVolumePrice();
		carStatisticalContainerOverallCountMock.setNumberOfTransactions(2);
		assertEquals("getAverageSalesVolumePrice - average sales volume price must be 0", BigDecimal.ZERO,
				salesVolumePrice.getValue());

		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(180), 0, ""));
		carStatisticalContainerOverallCountMock.setNumberOfTransactions(3);
		salesVolumePrice = this.classUnderTest.getAverageOverallSalesVolumePrice();
		assertEquals("getAverageSalesVolumePrice - average sales volume price must be 60", 60, salesVolumePrice.getValue()
				.doubleValue(), 0);
	}

	@Test
	public void testGetDataContainerNamesForCombining()
	{
		final List<String> returnedList = this.classUnderTest.getDataContainerNamesForCombining();
		assertEquals(returnedList.size(), 5);
		assertTrue(returnedList.contains("CARStatisticalDataContainerOverallVolume"));
		assertTrue(returnedList.contains("CARStatisticalDataContainerSixMonthVolume"));
		assertTrue(returnedList.contains("CARStatisticalDataContainerOverallCount"));
		assertTrue(returnedList.contains("CARStatisticalDataContainerLastPurchase"));
		assertTrue(returnedList.contains("CARStatisticalDataContainerOverallItemsCount"));
	}

	@Test
	public void testGetLastPurchaseDate()
	{
		prepareMocks();

		carStatisticalContainerLastPurchaseMock.setLastPurchaseDate("20160502");

		assertEquals("testGetLastPurchaseDate - last Purchase date year should be 2016", "2016",
				(new SimpleDateFormat("yyyy")).format(this.classUnderTest.getLastPurchaseDate()));
		assertEquals("testGetLastPurchaseDate - last Purchase date mont should be 05", "05",
				(new SimpleDateFormat("MM")).format(this.classUnderTest.getLastPurchaseDate()));
		assertEquals("testGetLastPurchaseDate - last Purchase date day should be 02", "02",
				(new SimpleDateFormat("dd")).format(this.classUnderTest.getLastPurchaseDate()));

		carStatisticalContainerLastPurchaseMock.setLastPurchaseDate("");
		assertEquals("testGetLastPurchaseDate - last Purchase date should be null", null, this.classUnderTest.getLastPurchaseDate());
	}

	@Test
	public void testGetAverageNoOfItems()
	{
		prepareMocks();

		carStatisticalContainerOverallItemsCountMock.setNumberOfTransactions(15);
		carStatisticalContainerOverallCountMock.setNumberOfTransactions(0);
		assertEquals("getAverageNoOfItems - average number of items must be 0", 0, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(5);
		assertEquals("getAverageNoOfItems - average number of items must be 3", 3, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(7);
		assertEquals("getAverageNoOfItems - average number of items must be 2", 2, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(12);
		assertEquals("getAverageNoOfItems - average number of items must be 1", 1, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(15);
		assertEquals("getAverageNoOfItems - average number of items must be 1", 1, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(16);
		assertEquals("getAverageNoOfItems - average number of items must be 1", 1, this.classUnderTest.getAverageNoOfItems());

		carStatisticalContainerOverallCountMock.setNumberOfTransactions(200);
		assertEquals("getAverageNoOfItems - average number of items must be 0", 0, this.classUnderTest.getAverageNoOfItems());
	}

	@Test
	public void testGetPrice()
	{
		final String expected = "10USD";

		final PriceData price = this.classUnderTest.getPrice(BigDecimal.TEN);
		assertEquals("getEncodedPrice price.formattedValue for 10 USD should be " + expected, expected, price.getFormattedValue());
	}

	@Test
	public void testGetContainerContextParamName()
	{
		final String containerContextParamName = this.classUnderTest.getContainerContextParamName();
		assertEquals("getContainerContextParamName should be statisticalDataContainer", "statisticalDataContainer",
				containerContextParamName);
	}

	@Test
	public void testGetSalesVolumeProperty()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		classUnderTest.createProperties();
		assertNotNull("salesVolumeProperty should have been created.", classUnderTest.getSalesVolumeProperty());
	}

	@Test
	public void testGetAverageSalesVolumeProperty()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		classUnderTest.createProperties();
		assertNotNull("averageSalesVolumeProperty should have been created.", classUnderTest.getAverageSalesVolumeProperty());
	}

	@Test
	public void testGetLastPurchaseDateProperty()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		classUnderTest.createProperties();
		assertNotNull("lastPurchaseDateProperty should have been created.", classUnderTest.getLastPurchaseDateProperty());
	}

	@Test
	public void testGetStorePurchaseRatioProperty()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		classUnderTest.createProperties();
		assertNotNull("storePurchaseRatioProperty should have been created.", classUnderTest.getStorePurchaseRatioProperty());
	}

	@Test
	public void testCreateProperties()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		assertNull("salesVolumeProperty should not have been created.", classUnderTest.salesVolumeProperty);
		assertNull("averageSalesVolumeProperty should not have been created.", classUnderTest.averageSalesVolumeProperty);
		assertNull("lastPurchaseDateProperty should not have been created.", classUnderTest.lastPurchaseDateProperty);
		assertNull("storePurchaseRatioProperty should not have been created.", classUnderTest.storePurchaseRatioProperty);

		classUnderTest.createProperties();
		assertNotNull("salesVolumeProperty should have been created.", classUnderTest.salesVolumeProperty);
		assertNotNull("averageSalesVolumeProperty should have been created.", classUnderTest.averageSalesVolumeProperty);
		assertNotNull("lastPurchaseDateProperty should have been created.", classUnderTest.lastPurchaseDateProperty);
		assertNotNull("storePurchaseRatioProperty should have been created.", classUnderTest.storePurchaseRatioProperty);
	}

	@Test
	public void testHandlePropertiesInErrorState()
	{
		prepareAndInitMocks();
		setPropertiesToNull();

		assertNull("salesVolumeProperty should not have been created.", classUnderTest.salesVolumeProperty);
		assertNull("averageSalesVolumeProperty should not have been created.", classUnderTest.averageSalesVolumeProperty);
		assertNull("lastPurchaseDateProperty should not have been created.", classUnderTest.lastPurchaseDateProperty);
		assertNull("storePurchaseRatioProperty should not have been created.", classUnderTest.storePurchaseRatioProperty);

		classUnderTest.handlePropertiesInErrorState();
		assertNotNull("salesVolumeProperty should have been created.", classUnderTest.salesVolumeProperty);
		assertNull("Value should be null.", classUnderTest.salesVolumeProperty.getValue());
		try
		{
			assertEquals("Unit should be empty.", "",
					((DataContainerPropertyDefaultImpl) classUnderTest.salesVolumeProperty).determineUnit(BigDecimal.valueOf(1)));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		assertNotNull("averageSalesVolumeProperty should have been created.", classUnderTest.averageSalesVolumeProperty);
		assertNull("Value should be null.", classUnderTest.averageSalesVolumeProperty.getValue());

		assertNotNull("lastPurchaseDateProperty should have been created.", classUnderTest.lastPurchaseDateProperty);
		assertNull("Value should be null.", classUnderTest.lastPurchaseDateProperty.getValue());

		assertNotNull("storePurchaseRatioProperty should have been created.", classUnderTest.storePurchaseRatioProperty);
		assertNull("Value should be null.", classUnderTest.storePurchaseRatioProperty.getValue());
	}

	@Test
	public void testDetermineDataForCMSComponent()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.setCMSComponentModel(cmsComponentModel);
		classUnderTest.createProperties();

		classUnderTest.determineDataForCMSComponent();

		assertEquals("salesVolumeProperty should have 4 levels", 4, classUnderTest.salesVolumeProperty.getLevels().size());
		assertEquals("averageSalesVolumeProperty should have 4 levels", 4, classUnderTest.averageSalesVolumeProperty.getLevels()
				.size());
		assertEquals("lastPurchaseDateProperty should have 4 levels", 4, classUnderTest.lastPurchaseDateProperty.getLevels().size());
		assertEquals("storePurchaseRatioProperty should have 4 levels", 4, classUnderTest.storePurchaseRatioProperty.getLevels()
				.size());
	}

	@Test
	public void testDetermineAverageSalesVolumePropertyLevels()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		prepareAndInitMocks();
		classUnderTest.createProperties();

		classUnderTest.determineAverageSalesVolumePropertyLevels(cmsComponentModel);
		assertEquals("AverageSalesVolume score should have 4 levels", 4, classUnderTest.getAverageSalesVolumeProperty().getLevels()
				.size());
	}

	@Test
	public void testDetermineAverageSalesVolumePropertyLevelsException()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		prepareAndInitMocks();
		classUnderTest.createProperties();

		((DataContainerPropertyCurrencyBDMock) classUnderTest.getAverageSalesVolumeProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTest.determineAverageSalesVolumePropertyLevels(cmsComponentModel);

		assertEquals("There should be one message in the container.", 1, classUnderTest.getMessageList().size());

		((DataContainerPropertyCurrencyBDMock) classUnderTest.getAverageSalesVolumeProperty()).throwDataContainerPropertyLevelException = false;
	}

	@Test
	public void testGetAverageSalesVolumePropertylevelBoundaryPairs()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = getInitializedCMS360ComponentModel();
		//		BigDecimal.valueOf(1), BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(300);
		prepareAndInitMocks();
		classUnderTest.createProperties();
		List<Comparable> levelBoundaryPairs = null;
		try
		{
			levelBoundaryPairs = classUnderTest.getAverageSalesVolumePropertylevelBoundaryPairs(cms360ComponentModel);

			assertEquals("Level 1 low should be: 1", 1, ((BigDecimal) levelBoundaryPairs.get(0)).intValue());
			assertEquals("Level 1 high should be: 50", 50, ((BigDecimal) levelBoundaryPairs.get(1)).intValue());
			assertEquals("Level 2 low should be: 51", 51, ((BigDecimal) levelBoundaryPairs.get(2)).intValue());
			assertEquals("Level 2 high should be: 100", 100, ((BigDecimal) levelBoundaryPairs.get(3)).intValue());
			assertEquals("Level 3 low should be: 101", 101, ((BigDecimal) levelBoundaryPairs.get(4)).intValue());
			assertEquals("Level 3 high should be: 150", 150, ((BigDecimal) levelBoundaryPairs.get(5)).intValue());
			assertEquals("Level 4 low should be: 151", 151, ((BigDecimal) levelBoundaryPairs.get(6)).intValue());
			assertEquals("Level 4 high should null", null, levelBoundaryPairs.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		final AbstractCMSComponentModel cms360ComponentModelEmpty = new CMSISCEPurchaseHistoryComponentModel();

		try
		{
			levelBoundaryPairs = classUnderTest.getAverageSalesVolumePropertylevelBoundaryPairs(cms360ComponentModelEmpty);
			assertEquals("No borders should be contained: ", 0, levelBoundaryPairs.size());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}
	}

	@Test
	public void testDetermineSalesVolumePropertyLevels()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		classUnderTest.determineSalesVolumePropertyLevels(cmsComponentModel);
		assertEquals("SalesVolume should have 4 levels", 4, classUnderTest.getSalesVolumeProperty().getLevels().size());
	}

	@Test
	public void testDetermineSalesVolumePropertyLevelsException()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		((DataContainerPropertyCurrencyBDMock) classUnderTest.getSalesVolumeProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTest.determineSalesVolumePropertyLevels(cmsComponentModel);

		assertEquals("There should be one message in the container.", 1, classUnderTest.getMessageList().size());

		((DataContainerPropertyCurrencyBDMock) classUnderTest.getSalesVolumeProperty()).throwDataContainerPropertyLevelException = false;
	}

	@Test
	public void testGetSalesVolumePropertylevelBoundaryPairs()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = getInitializedCMS360ComponentModel();
		//		BigDecimal.valueOf(1), BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(3000);
		prepareAndInitMocks();
		classUnderTest.createProperties();

		List<Comparable> levelBoundaryPairs = null;
		try
		{
			levelBoundaryPairs = classUnderTest.getSalesVolumePropertylevelBoundaryPairs(cms360ComponentModel);

			assertEquals("Level 1 low should be: 1", 1, ((BigDecimal) levelBoundaryPairs.get(0)).intValue());
			assertEquals("Level 1 high should be: 500", 500, ((BigDecimal) levelBoundaryPairs.get(1)).intValue());
			assertEquals("Level 2 low should be: 501", 501, ((BigDecimal) levelBoundaryPairs.get(2)).intValue());
			assertEquals("Level 2 high should be: 1000", 1000, ((BigDecimal) levelBoundaryPairs.get(3)).intValue());
			assertEquals("Level 3 low should be: 1001", 1001, ((BigDecimal) levelBoundaryPairs.get(4)).intValue());
			assertEquals("Level 3 high should be: 1500", 1500, ((BigDecimal) levelBoundaryPairs.get(5)).intValue());
			assertEquals("Level 4 low should be: 1501", 1501, ((BigDecimal) levelBoundaryPairs.get(6)).intValue());
			assertEquals("Level 4 high should be null ", null, levelBoundaryPairs.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		final AbstractCMSComponentModel cms360ComponentModelEmpty = new CMSISCEPurchaseHistoryComponentModel();
		try
		{
			levelBoundaryPairs = classUnderTest.getSalesVolumePropertylevelBoundaryPairs(cms360ComponentModelEmpty);
			assertEquals("No borders should be contained: ", 0, levelBoundaryPairs.size());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}
	}

	@Test
	public void testDetermineLastPurchaseDatePropertyLevels()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		classUnderTest.determineLastPurchaseDatePropertyLevels(cmsComponentModel);
		assertEquals("LastPurchaseDate score should have 4 levels", 4, classUnderTest.getLastPurchaseDateProperty().getLevels()
				.size());
	}

	@Test
	public void testDetermineLastPurchaseDatePropertyLevelsException()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		((DataContainerPropertyIntegerMock) classUnderTest.getLastPurchaseDateProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTest.determineLastPurchaseDatePropertyLevels(cmsComponentModel);

		assertEquals("There should be one message in the container.", 1, classUnderTest.getMessageList().size());

		((DataContainerPropertyIntegerMock) classUnderTest.getLastPurchaseDateProperty()).throwDataContainerPropertyLevelException = false;
	}

	@Test
	public void testGetLastPurchaseDatePropertylevelBoundaryPairs()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = getInitializedCMS360ComponentModel();
		//		Integer.valueOf(180), Integer.valueOf(30), null, Integer.valueOf(7);
		prepareAndInitMocks();
		classUnderTest.createProperties();

		List<Comparable> levelBoundaryPairs = null;
		try
		{
			levelBoundaryPairs = classUnderTest.getLastPurchaseDatePropertylevelBoundaryPairs(cms360ComponentModel);

			assertEquals("Level 1 low should be: 181", 181, ((Integer) levelBoundaryPairs.get(0)).intValue());
			assertEquals("Level 1 high should be: " + Integer.MAX_VALUE, Integer.valueOf(Integer.MAX_VALUE).intValue(),
					((Integer) levelBoundaryPairs.get(1)).intValue());
			assertEquals("Level 2 low should be: 31", 31, ((Integer) levelBoundaryPairs.get(2)).intValue());
			assertEquals("Level 2 high should be: 180", 180, ((Integer) levelBoundaryPairs.get(3)).intValue());
			assertEquals("Level 3 low should be: 8", 8, ((Integer) levelBoundaryPairs.get(4)).intValue());
			assertEquals("Level 3 high should be: 30", 30, ((Integer) levelBoundaryPairs.get(5)).intValue());
			assertEquals("Level 4 low should be: null", null, levelBoundaryPairs.get(6));
			assertEquals("Level 4 high should be: ", 7, ((Integer) levelBoundaryPairs.get(7)).intValue());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		final AbstractCMSComponentModel cms360ComponentModelEmpty = new CMSISCEPurchaseHistoryComponentModel();
		try
		{
			levelBoundaryPairs = classUnderTest.getLastPurchaseDatePropertylevelBoundaryPairs(cms360ComponentModelEmpty);
			assertEquals("No borders should be contained: ", 0, levelBoundaryPairs.size());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}
	}

	@Test
	public void testDetermineStorePurchaseRatioPropertyLevels()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		classUnderTest.determineStorePurchaseRatioPropertyLevels(cmsComponentModel);
		assertEquals("StorePurchaseRatio score should have 4 levels", 4, classUnderTest.getStorePurchaseRatioProperty().getLevels()
				.size());
	}

	@Test
	public void testDetermineStorePurchaseRatioPropertyLevelsException()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.createProperties();

		((DataContainerPropertyIntegerMock) classUnderTest.getStorePurchaseRatioProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTest.determineStorePurchaseRatioPropertyLevels(cmsComponentModel);

		assertEquals("There should be one message in the container.", 1, classUnderTest.getMessageList().size());

		((DataContainerPropertyIntegerMock) classUnderTest.getStorePurchaseRatioProperty()).throwDataContainerPropertyLevelException = false;
	}

	@Test
	public void testGetStorePurchaseRatioPropertylevelBoundaryPairs()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = getInitializedCMS360ComponentModel();
		//		Integer.valueOf(1), Integer.valueOf(25), Integer.valueOf(50), Integer.valueOf(75);
		prepareAndInitMocks();
		classUnderTest.createProperties();

		List<Comparable> levelBoundaryPairs = null;
		try
		{
			levelBoundaryPairs = classUnderTest.getStorePurchaseRatioPropertylevelBoundaryPairs(cms360ComponentModel);

			assertEquals("Level 1 low should be: 1", 1, ((Integer) levelBoundaryPairs.get(0)).intValue());
			assertEquals("Level 1 high should be: 25", 25, ((Integer) levelBoundaryPairs.get(1)).intValue());
			assertEquals("Level 2 low should be: 26", 26, ((Integer) levelBoundaryPairs.get(2)).intValue());
			assertEquals("Level 2 high should be: 50", 50, ((Integer) levelBoundaryPairs.get(3)).intValue());
			assertEquals("Level 3 low should be: 51", 51, ((Integer) levelBoundaryPairs.get(4)).intValue());
			assertEquals("Level 3 high should be: 75", 75, ((Integer) levelBoundaryPairs.get(5)).intValue());
			assertEquals("Level 4 low should be: 76", 76, ((Integer) levelBoundaryPairs.get(6)).intValue());
			assertEquals("Level 4 high should be: null", null, levelBoundaryPairs.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		final AbstractCMSComponentModel cms360ComponentModelEmpty = new CMSISCEPurchaseHistoryComponentModel();
		try
		{
			levelBoundaryPairs = classUnderTest.getStorePurchaseRatioPropertylevelBoundaryPairs(cms360ComponentModelEmpty);
			assertEquals("No borders should be contained: ", 0, levelBoundaryPairs.size());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}
	}

	/**
	 * Tests encodeHTML.
	 */
	@Test
	public void testEncodeHTMLWithLevels()
	{
		prepareAndInitMocks();
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTest.setCMSComponentModel(cmsComponentModel);
		classUnderTest.createProperties();
		classUnderTest.determineDataForCMSComponent();

		classUnderTest.encodeHTML();
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED, classUnderTest.salesVolumeProperty
				.getLevels().get(0).getUnit());
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED,
				classUnderTest.averageSalesVolumeProperty.getLevels().get(0).getUnit());
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED,
				classUnderTest.lastPurchaseDateProperty.getLevels().get(0).getUnit());
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED,
				classUnderTest.storePurchaseRatioProperty.getLevels().get(0).getUnit());
	}

	@Test
	public void testGetLastPurchaseDateDistance()
	{
		final class StatisticalDataContainerMock extends StatisticalDataContainer
		{
			@Override
			protected Date getToday()
			{
				final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				try
				{
					return dateFormat.parse("20160503");
				}
				catch (final ParseException e)
				{
					return null;
				}
			}
		}
		final StatisticalDataContainerMock statisticalDataContainerMock = new StatisticalDataContainerMock();
		prepareMocks();

		final HashMap<String, DataContainer> dataContainerMap = new HashMap<String, DataContainer>();
		statisticalDataContainerMock.dataContainerMap = dataContainerMap;
		statisticalDataContainerMock.dataContainerMap.put("CARStatisticalDataContainerLastPurchase",
				carStatisticalContainerLastPurchaseMock);
		assertNull("a distance of NULL should be returned", statisticalDataContainerMock.getLastPurchaseDateDistance());

		carStatisticalContainerLastPurchaseMock.setLastPurchaseDate("20160502");
		assertNotNull("a distance  greater 0 should be returned", statisticalDataContainerMock.getLastPurchaseDateDistance());

		assertEquals("Distance should be 1 day.", Integer.valueOf(1), statisticalDataContainerMock.getLastPurchaseDateDistance());
	}

	@Test
	public void testAdaptBigDecimalLevelUIValues()
	{
		final BigDecimal level1Low = BigDecimal.valueOf(1);
		final BigDecimal level1High = BigDecimal.valueOf(1000);
		final BigDecimal level2Low = BigDecimal.valueOf(1001);
		final BigDecimal level2High = null;
		DataContainerPropertyLevel level;

		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		classUnderTest.adaptBigDecimalLevelUIValues(null);
		assertTrue("Method was successfully called", true);
		classUnderTest.adaptBigDecimalLevelUIValues(levels);
		assertTrue("Method was successfully called", true);


		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);

		classUnderTest.adaptBigDecimalLevelUIValues(levels);

		assertThat(levels.get(0).getAdaptedUILowValue()).isInstanceOf(PriceData.class);
		assertEquals("Level value should be 1USD.", "1USD", ((PriceData) levels.get(0).getAdaptedUILowValue()).getFormattedValue());

		assertThat(levels.get(0).getAdaptedUIHighValue()).isInstanceOf(PriceData.class);
		assertEquals("Level value should be 1000USD.", "1000USD",

		((PriceData) levels.get(0).getAdaptedUIHighValue()).getFormattedValue());
		assertEquals("Level value should be 1000USD.", "1000USD",
				((PriceData) levels.get(1).getAdaptedUILowValue()).getFormattedValue());
		assertEquals("Level value should be null.", null, levels.get(1).getAdaptedUIHighValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);

		classUnderTest.adaptBigDecimalLevelUIValues(levels);

		assertEquals("Level value should be 0USD.", "0USD", ((PriceData) levels.get(0).getAdaptedUILowValue()).getFormattedValue());
		assertEquals("Level value should be 1000USD.", "1000USD",
				((PriceData) levels.get(0).getAdaptedUIHighValue()).getFormattedValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", "ClassCastEx", "ClassCastEx", "", "");
		levels.add(level);

		classUnderTest.adaptBigDecimalLevelUIValues(levels);
		assertEquals("Level low value should be null.", null, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be null.", null, levels.get(0).getAdaptedUIHighValue());

	}

	@Test
	public void testAdaptIntegerLevelUIValues()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		classUnderTest.adaptIntegerLevelUIValues(null, true);
		assertTrue("Method was successfully called", true);
		classUnderTest.adaptIntegerLevelUIValues(levels, true);
		assertTrue("Method was successfully called", true);

		// ascending
		Integer level1Low = Integer.valueOf(1);
		Integer level1High = Integer.valueOf(1000);
		Integer level2Low = Integer.valueOf(1001);
		Integer level2High = Integer.valueOf(2000);
		Integer level3Low;
		Integer level3High;
		Integer level4Low;
		Integer level4High;

		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels, true);

		assertEquals("Level low value should be 1.", level1Low, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be 1000.", level1High, levels.get(0).getAdaptedUIHighValue());
		assertEquals("Level low value should be 1000.", Integer.valueOf(level2Low.intValue() - 1), levels.get(1)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 2000.", level2High, levels.get(1).getAdaptedUIHighValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels, true);

		assertEquals("Level low value should be 1.", Integer.valueOf(level1Low.intValue() - 1), levels.get(0)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 1000.", level1High, levels.get(0).getAdaptedUIHighValue());

		// desending
		// MAX_VALUE 180 30 7
		level1Low = Integer.valueOf(181);
		level1High = Integer.valueOf(Integer.MAX_VALUE);
		level2Low = Integer.valueOf(31);
		level2High = Integer.valueOf(180);
		level3Low = Integer.valueOf(8);
		level3High = Integer.valueOf(30);
		level4Low = null;
		level4High = Integer.valueOf(7);

		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("3", level3Low, level3High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("4", level4Low, level4High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels, false);

		assertEquals("Level low value should be 180.", Integer.valueOf(180), levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be MAX_VALUE.", level1High, levels.get(0).getAdaptedUIHighValue());

		assertEquals("Level low value should be 31", level2Low, levels.get(1).getAdaptedUILowValue());
		assertEquals("Level high value should be 180.", level2High, levels.get(1).getAdaptedUIHighValue());

		assertEquals("Level low value should be 8.", level3Low, levels.get(2).getAdaptedUILowValue());
		assertEquals("Level high value should be 30.", level3High, levels.get(2).getAdaptedUIHighValue());

		assertEquals("Level low value should be null", null, levels.get(3).getAdaptedUILowValue());
		assertEquals("Level high value should be 8.", Integer.valueOf(8), levels.get(3).getAdaptedUIHighValue());



		level1Low = Integer.valueOf(31);
		level1High = Integer.valueOf(180);
		level2Low = Integer.valueOf(Integer.MIN_VALUE);
		level2High = Integer.valueOf(30);

		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels, false);

		assertEquals("Level low value should be 30.", Integer.valueOf(level1Low.intValue() - 1), levels.get(0)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 180.", level1High, levels.get(0).getAdaptedUIHighValue());
		assertEquals("Level low value should be " + Integer.MIN_VALUE, Integer.valueOf(Integer.MIN_VALUE), levels.get(1)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 31.", Integer.valueOf(level2High.intValue() + 1), levels.get(1)
				.getAdaptedUIHighValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels, false);

		assertEquals("Level low value should be 31.", level1Low, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be 181.", Integer.valueOf(level1High.intValue() + 1), levels.get(0)
				.getAdaptedUIHighValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", "ClassCastEx", "ClassCastEx", "", "");
		levels.add(level);
		assertEquals("Level low value should be null.", null, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be null.", null, levels.get(0).getAdaptedUIHighValue());
	}

	private void setPropertiesToNull()
	{
		classUnderTest.salesVolumeProperty = null;
		classUnderTest.averageSalesVolumeProperty = null;
		classUnderTest.lastPurchaseDateProperty = null;
		classUnderTest.storePurchaseRatioProperty = null;
	}

	private void prepareAndInitMocks()
	{
		prepareMocks();
		carStatisticalContainerOverallVolumeMock.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(180), 0, ""));
		carStatisticalContainerOverallCountMock.setNumberOfTransactions(3);
	}

	/**
	 * Provides an initialized cms model component for 360 component
	 *
	 * @return
	 */
	private CMSISCECustomer360ComponentModel getInitializedCMS360ComponentModel()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = new CMSISCECustomer360ComponentModel();

		cms360ComponentModel.setCMSISCECustomer360AveragePurchaseVolumeLevel01(Integer.valueOf(1));
		cms360ComponentModel.setCMSISCECustomer360AveragePurchaseVolumeLevel02(Integer.valueOf(101));
		cms360ComponentModel.setCMSISCECustomer360AveragePurchaseVolumeLevel03(Integer.valueOf(201));
		cms360ComponentModel.setCMSISCECustomer360AveragePurchaseVolumeLevel04(Integer.valueOf(301));

		cms360ComponentModel.setCMSISCECustomer360LastPurchaseDateLevel01(Integer.valueOf(Integer.MAX_VALUE));
		cms360ComponentModel.setCMSISCECustomer360LastPurchaseDateLevel02(Integer.valueOf(180));
		cms360ComponentModel.setCMSISCECustomer360LastPurchaseDateLevel03(Integer.valueOf(30));
		cms360ComponentModel.setCMSISCECustomer360LastPurchaseDateLevel04(Integer.valueOf(7));

		cms360ComponentModel.setCMSISCECustomer360SalesVolumeLevel01(Integer.valueOf(1));
		cms360ComponentModel.setCMSISCECustomer360SalesVolumeLevel02(Integer.valueOf(1001));
		cms360ComponentModel.setCMSISCECustomer360SalesVolumeLevel03(Integer.valueOf(2001));
		cms360ComponentModel.setCMSISCECustomer360SalesVolumeLevel04(Integer.valueOf(3001));

		cms360ComponentModel.setCMSISCECustomer360StoreOnlineRatioLevel01(Integer.valueOf(1));
		cms360ComponentModel.setCMSISCECustomer360StoreOnlineRatioLevel02(Integer.valueOf(26));
		cms360ComponentModel.setCMSISCECustomer360StoreOnlineRatioLevel03(Integer.valueOf(51));
		cms360ComponentModel.setCMSISCECustomer360StoreOnlineRatioLevel04(Integer.valueOf(76));

		return cms360ComponentModel;
	}

	@Test
	public void testGetPriceDataFactory()
	{
		assertNotNull("getPriceDataFactory is null", classUnderTest.getPriceDataFactory());
	}

	@Test
	public void testGetToday()
	{
		assertNotNull("getToday is null", classUnderTest.getToday());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.StatisticalDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getDataContainerNamesForCombining()=CARStatisticalDataContainerOverallVolume, CARStatisticalDataContainerOverallCount, CARStatisticalDataContainerSixMonthVolume, CARStatisticalDataContainerLastPurchase, CARStatisticalDataContainerOverallItemsCount";
		assertEquals("TraceInformation (1) - must be" + expected, expected, loggerMock.getDebug().get(1));
	}

}

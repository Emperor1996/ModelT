/*****************************************************************************
 Class:        CARPurchaseHistoryDataContainerCustomerOrdersUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.container.impl.mock.PriceDataFactoryMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult.Channel;
import com.sap.retail.isce.service.util.StatisticalDataSalesVolumeUtil;


/**
 * Unit Test class for CARPurchaseHistoryDataContainerCustomerOrders
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARPurchaseHistoryDataContainerCustomerOrdersUnitTest
{
	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "CARPurchaseHistoryDataContainerCustomerOrdersUnderTest")
	private CARPurchaseHistoryDataContainerCustomerOrders classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dataContainerContext.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);
		loggerMock.clearAll();
		CARPurchaseHistoryDataContainerCustomerOrders.log = loggerMock;
	}

	@Test
	public void testGetSelect()
	{
		final String expected = "OrderChannel,CreationDate,CreationTime,TransactionNumber,TransactionCurrency,TotalNetAmount,TaxAmount";

		assertEquals("getSelect - select must be " + expected, expected, this.classUnderTest.getSelect());
	}

	@Test
	public void testGetOrderBy()
	{
		final String expected = "CreationDate desc";
		assertEquals("getOrderBy - orderBy must be " + expected, expected, this.classUnderTest.getOrderBy());
	}

	@Test
	public void testGetTop()
	{
		final Integer expected = Integer.valueOf(100);
		assertEquals("getTop - top must be " + expected, expected, this.classUnderTest.getTop());
	}

	@Test
	public void testSetGetPurchaseHistoryData()
	{
		final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData = new SearchPageData<>();
		this.classUnderTest.setPurchaseHistoryData(purchaseHistoryData);
		assertEquals("purchaseHistoryData is not equal", purchaseHistoryData, this.classUnderTest.getPurchaseHistoryData());
	}

	@Test
	public void testSetGetMaxEntriesDisplayedPerPage()
	{
		this.classUnderTest.setMaxEntriesDisplayedPerPage(5);
		assertEquals("maxEntriesDisplayedPerPage is not equal", 5, this.classUnderTest.getMaxEntriesDisplayedPerPage());
	}


	@Test
	public void testSetGetPageNumberDisplayed()
	{
		this.classUnderTest.setPageNumberDisplayed(5);
		assertEquals("pageNumberDisplayed is not equal", 5, this.classUnderTest.getPageNumberDisplayed());
	}

	@Test
	public void testSetGetStatisticalDataSalesVolumeUtil()
	{
		final StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtil = new StatisticalDataSalesVolumeUtil()
		{

			@Override
			public CurrencyModel getCurrentCurrency()
			{
				return null;
			}

			@Override
			public ISCESalesVolumeResult calculateSalesVolumeForTargetCurrency(final CurrencyModel targetCurrency,
					final List<ISCESalesVolumeResult> salesVolumePerCurrency, final DataContainer dataContainer, final Channel channel)
			{
				return null;
			}
		};
		this.classUnderTest.setStatisticalDataSalesVolumeUtil(statisticalDataSalesVolumeUtil);
		assertEquals("statisticalDataSalesVolumeUtil is not equal", statisticalDataSalesVolumeUtil,
				this.classUnderTest.getStatisticalDataSalesVolumeUtil());
	}

	@Test
	public void testSetGetPriceDataFactory()
	{
		final PriceDataFactory priceDataFactoryMock = new PriceDataFactoryMock();
		this.classUnderTest.setPriceDataFactory(priceDataFactoryMock);
		assertEquals("priceDataFactory is not equal", priceDataFactoryMock, this.classUnderTest.getPriceDataFactory());
	}

	@Test
	public void testGetLocalizedStorePurchaseNamePurchaseHistory()
	{
		assertEquals("getLocalizedStorePurchaseNamePurchaseHistory is not equal",
				"instorecs.customer360.purchaseHistory.storePurchase",
				this.classUnderTest.getLocalizedStorePurchaseNamePurchaseHistory());
	}

	@Test
	public void testGetLocalizedOnlineOrderNamePurchaseHistory()
	{
		assertEquals("getLocalizedOnlineOrderNamePurchaseHistory is not equal",
				"instorecs.customer360.purchaseHistory.onlineOrder", this.classUnderTest.getLocalizedOnlineOrderNamePurchaseHistory());
	}

	@Test
	public void testGetLocalizedContainerName()
	{
		assertEquals("getLocalizedContainerName is not equal", "instorecs.customer360.purchaseHistory",
				this.classUnderTest.getLocalizedContainerName());
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("getContainerContextParamName is not equal", "CARPurchaseHistoryDataContainerCustomerOrders",
				this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testSetDataInErrorState()
	{
		final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData = new SearchPageData<>();
		final CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrders = new CARPurchaseHistoryCustomerOrders(null, null,
				"purchaseType", "orderNumber");
		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataList = new ArrayList<>();
		purchaseHistoryDataList.add(carPurchaseHistoryCustomerOrders);
		purchaseHistoryData.setResults(purchaseHistoryDataList);
		this.classUnderTest.setPurchaseHistoryData(purchaseHistoryData);

		this.classUnderTest.setDataInErrorState();
		assertEquals("setDataInErrorState - purchaseHistoryData must be an empty list",
				Collections.<CARPurchaseHistoryCustomerOrders> emptyList(), this.classUnderTest.getPurchaseHistoryData().getResults());
	}

	@Test
	public void testGetPurchaseDate()
	{
		final String creationDate = "20160603072730";
		@SuppressWarnings("deprecation")
		Date date = null;
		try
		{
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse(creationDate);
		}
		catch (final ParseException e)
		{
			assertTrue("Test method execution went bad", false);
		}
		assertEquals("getPurchaseDate - date is not equal", date, this.classUnderTest.getPurchaseDate(creationDate));
	}

	@Test
	public void testGetGrossSalesVolumePrice()
	{
		final PriceDataFactory priceDataFactoryMock = new PriceDataFactoryMock();
		this.classUnderTest.setPriceDataFactory(priceDataFactoryMock);
		@SuppressWarnings("deprecation")
		final CurrencyModel currency = new CurrencyModel("EUR", "€");
		final PriceData priceData = this.classUnderTest.getGrossSalesVolumePrice(BigDecimal.valueOf(10), currency);
		assertEquals("getGrossSalesVolumePrice - currencyIso is not equal", "EUR", priceData.getCurrencyIso());
		assertEquals("getGrossSalesVolumePrice - getValue is not equal", BigDecimal.valueOf(10), priceData.getValue());
		assertEquals("getGrossSalesVolumePrice - getPriceType is not equal", PriceDataType.BUY, priceData.getPriceType());
	}

	@Test
	public void testCheckProperties()
	{
		Map<String, Object> properties = null;
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties = new HashMap<>();
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TAX_AMOUNT_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TOTAL_NET_AMOUNT_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_DATE_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_TIME_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_NUMBER_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.ORDER_CHANNEL_PROPERTY, null);
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_CURRENCY_PROPERTY, null);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		final Object newObj = new Object();
		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_DATE_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_TIME_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TAX_AMOUNT_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TOTAL_NET_AMOUNT_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_NUMBER_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_CURRENCY_PROPERTY, newObj);
		assertTrue("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));

		properties.put(CARPurchaseHistoryDataContainerCustomerOrders.ORDER_CHANNEL_PROPERTY, newObj);
		assertFalse("testCheckProperties - checkProperties is not equal", this.classUnderTest.checkProperties(properties));
	}


	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		HttpODataResultImpl httpODataResult = null;

		try
		{
			// Null response
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			List<CARPurchaseHistoryCustomerOrders> results = this.classUnderTest.getPurchaseHistoryData().getResults();
			assertTrue("extractOwnDatafromHttpOdataResult with emtpy response - ", results.isEmpty());

			// Empty response
			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			results = this.classUnderTest.getPurchaseHistoryData().getResults();
			assertTrue("extractOwnDatafromHttpOdataResult with emtpy response - ", results.isEmpty());

			//single entry missing property
			final Map<String, Object> data = new HashMap();
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.TOTAL_NET_AMOUNT_PROPERTY, new BigDecimal(100));
			final List<ODataEntry> oDataEntries = new ArrayList<ODataEntry>();
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));
			httpODataResult.setEntities(oDataEntries);
			try
			{
				classUnderTest.extractOwnDataFromResult(httpODataResult);
				assertFalse("extractOwnDataFromResult missing property - An exception must have been thrown", true);
			}
			catch (final DataContainerRuntimeException e)
			{
				assertTrue("extractOwnDataFromResult missing property - An exception was thrown", true);
			}

			//single entry all properties, online order
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.ORDER_CHANNEL_PROPERTY, "03");
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.TAX_AMOUNT_PROPERTY, new BigDecimal(20));
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_DATE_PROPERTY, "20160603");
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.CREATION_TIME_PROPERTY, "081330");
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_NUMBER_PROPERTY, "123456789");
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.TRANSACTION_CURRENCY_PROPERTY, "USD");

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			results = this.classUnderTest.getPurchaseHistoryData().getResults();
			assertEquals("extractOwnDataFromResult - number of results should be 1", 1, results.size());
			CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrders = results.get(0);
			assertNotNull("extractOwnDataFromResult - carPurchaseHistoryCustomerOrders is null", carPurchaseHistoryCustomerOrders);
			assertNotNull("extractOwnDataFromResult - Date null", carPurchaseHistoryCustomerOrders.getDate().toString());
			PriceData grossSalesVolumePrice = carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice();
			assertNotNull("extractOwnDataFromResult - carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice() is null",
					grossSalesVolumePrice);
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice Currency not equals", "USD",
					grossSalesVolumePrice.getCurrencyIso());
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice Value not equals", new BigDecimal(120),
					grossSalesVolumePrice.getValue());
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice PriceType not equals", PriceDataType.BUY,
					grossSalesVolumePrice.getPriceType());
			assertEquals("extractOwnDataFromResult - transaction number not equals", "123456789",
					carPurchaseHistoryCustomerOrders.getOrderNumber());
			assertEquals("extractOwnDataFromResult - purchase type not equals", "instorecs.customer360.purchaseHistory.onlineOrder",
					carPurchaseHistoryCustomerOrders.getPurchaseType());


			//single entry all properties, store purchase
			data.put(CARPurchaseHistoryDataContainerCustomerOrders.ORDER_CHANNEL_PROPERTY, "07");

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			results = this.classUnderTest.getPurchaseHistoryData().getResults();
			assertEquals("extractOwnDataFromResult - number of results should be 1", 1, results.size());
			carPurchaseHistoryCustomerOrders = results.get(0);
			assertNotNull("extractOwnDataFromResult - carPurchaseHistoryCustomerOrders is null", carPurchaseHistoryCustomerOrders);
			assertNotNull("extractOwnDataFromResult - Date null", carPurchaseHistoryCustomerOrders.getDate().toString());
			grossSalesVolumePrice = carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice();
			assertNotNull("extractOwnDataFromResult - carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice() is null",
					grossSalesVolumePrice);
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice Currency not equals", "USD",
					grossSalesVolumePrice.getCurrencyIso());
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice Value not equals", new BigDecimal(120),
					grossSalesVolumePrice.getValue());
			assertEquals("extractOwnDataFromResult - grossSalesVolumePrice PriceType not equals", PriceDataType.BUY,
					grossSalesVolumePrice.getPriceType());
			assertEquals("extractOwnDataFromResult - transaction number not equals", "",
					carPurchaseHistoryCustomerOrders.getOrderNumber());
			assertEquals("extractOwnDataFromResult - purchase type not equals",
					"instorecs.customer360.purchaseHistory.storePurchase", carPurchaseHistoryCustomerOrders.getPurchaseType());


		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	@Test
	public void testEncodeHTML()
	{
		final String attackVector = "<script>alert('XSS');</script>";
		final PriceDataFactoryMock priceDataFactory = new PriceDataFactoryMock();
		final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(10), "USD");

		final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData = new SearchPageData<>();

		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataList = new ArrayList<>();
		purchaseHistoryData.setResults(null);
		this.classUnderTest.setPurchaseHistoryData(purchaseHistoryData);
		this.classUnderTest.encodeHTML();

		final CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrders = new CARPurchaseHistoryCustomerOrders(null,
				priceData, "purchaseType" + attackVector, "orderNumber" + attackVector);
		purchaseHistoryDataList.add(carPurchaseHistoryCustomerOrders);
		purchaseHistoryData.setResults(purchaseHistoryDataList);
		this.classUnderTest.setPurchaseHistoryData(purchaseHistoryData);

		this.classUnderTest.encodeHTML();

		final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataAfterEncoding = this.classUnderTest
				.getPurchaseHistoryData();
		final List<CARPurchaseHistoryCustomerOrders> resultsAfterEncoding = purchaseHistoryDataAfterEncoding.getResults();

		assertNotNull("Purchase History is null", resultsAfterEncoding);

		for (int i = 0; i < resultsAfterEncoding.size(); i++)
		{
			final CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrdersAfterEncoding = resultsAfterEncoding.get(i);
			assertNotNull("Purchase History After Encoding is null", carPurchaseHistoryCustomerOrdersAfterEncoding);
			assertEquals("encodeHTML - purchaseHistory Type is not encoded properly",
					"purchaseType&lt;script&gt;alert&#x28;&#x27;XSS&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;",
					carPurchaseHistoryCustomerOrdersAfterEncoding.getPurchaseType());
			assertEquals("encodeHTML - orderNumber Type is not encoded properly",
					"orderNumber&lt;script&gt;alert&#x28;&#x27;XSS&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;",
					carPurchaseHistoryCustomerOrdersAfterEncoding.getOrderNumber());
			final PriceData grossSalesVolumePriceAfterEncoding = carPurchaseHistoryCustomerOrdersAfterEncoding
					.getGrossSalesVolumePrice();
			// Currency Encoding is now done in UI (Tag format:price makes the encoding
			assertEquals("encodeHTML - currencyIso Type is not encoded properly", "USD",
					grossSalesVolumePriceAfterEncoding.getCurrencyIso());
			assertEquals("encodeHTML - formattedValue Type is not encoded properly", "10USD",
					grossSalesVolumePriceAfterEncoding.getFormattedValue());
		}
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.CARPurchaseHistoryDataContainerCustomerOrders";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

}

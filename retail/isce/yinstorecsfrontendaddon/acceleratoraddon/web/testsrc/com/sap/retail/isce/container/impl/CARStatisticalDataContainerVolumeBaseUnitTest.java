/*****************************************************************************
 Class:        CARStatisticalDataContainerVolumeBaseUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;


/**
 * Unit Test class for CARStatisticalDataContainerVolumeBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerVolumeBaseUnitTest
{
	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "CARStatisticalDataContainerOverallVolumeUnderTest")
	private CARStatisticalDataContainerVolumeBase classUnderTest;

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dataContainerContext.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);
		CARStatisticalDataContainerVolumeBase.log = new LoggerMock("");
	}

	@Test
	public void testGetStatisticalDataSalesVolumeUtil()
	{
		assertNotNull("getStatisticalDataSalesVolumeUtil is null", classUnderTest.getStatisticalDataSalesVolumeUtil());
	}

	@Test
	public void testSetDataInErrorState()
	{
		setStartData();

		this.classUnderTest.setDataInErrorState();
		assertEquals("setDataInErrorState - Sales volume must be 0", BigDecimal.ZERO, this.classUnderTest.getSalesVolume());
		assertEquals("setDataInErrorState - Store Purchase Ratio must be 0", new Double(0),
				this.classUnderTest.getStorePurchaseRatio());
	}

	@Test
	public void testGetInlineCount()
	{
		assertEquals("getInlineCount - InlineCount must be empty null", null, this.classUnderTest.getInlineCount());
	}

	@Test
	public void testGetSelect()
	{
		final String expected = "TotalNetAmount,TransactionCurrency,OrderChannel,TaxAmount";

		assertEquals("getSelect - select must be " + expected, expected, this.classUnderTest.getSelect());
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceable Filter should be null", null, classUnderTest.getTraceableFilter());
	}

	/**
	 * Tests extractOwnDatafromHttpOdataResult.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		HttpODataResultImpl httpODataResult = null;

		try
		{
			// Null response
			setStartData();

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with null response - sales volume must be 0", BigDecimal.ZERO,
					this.classUnderTest.getSalesVolume());

			assertEquals("extractOwnDatafromHttpOdataResult with null response - store Purchase Ratio must be 0", new Double(0),
					this.classUnderTest.getStorePurchaseRatio());

			// Empty response
			setStartData();

			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());

			classUnderTest.extractOwnDataFromResult(httpODataResult);

			assertEquals("extractOwnDatafromHttpOdataResult with with empty response - sales volume must be 0", BigDecimal.ZERO,
					this.classUnderTest.getSalesVolume());
			assertEquals("extractOwnDatafromHttpOdataResult with empty response - store Purchase Ratio must be 0", new Double(0),
					this.classUnderTest.getStorePurchaseRatio());

			//single entry missing property
			setStartData();

			Map<String, Object> data = new HashMap();
			data.put(CARStatisticalDataContainerVolumeBase.TOTAL_NET_AMOUNT_PROPERTY, new BigDecimal(100));
			data.put(CARStatisticalDataContainerVolumeBase.TAX_AMOUNT_PROPERTY, new BigDecimal(10));
			data.put(CARStatisticalDataContainerVolumeBase.ORDER_CHANNEL_PROPERTY, "06");

			final List<ODataEntry> oDataEntries = new ArrayList<ODataEntry>();
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			httpODataResult.setEntities(oDataEntries);

			try
			{
				classUnderTest.extractOwnDataFromResult(httpODataResult);
				assertFalse("extractOwnDatafromHttpOdataResult missing property - An exception must have been thrown", true);
			}
			catch (final DataContainerRuntimeException e)
			{
				assertTrue("extractOwnDatafromHttpOdataResult missing property -An exception was thrown", true);
			}

			//single entry all properties, not matching channel
			setStartData();

			data.put(CARStatisticalDataContainerVolumeBase.TRANSACTION_CURRENCY_PROPERTY, "USD");

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals(
					"extractOwnDatafromHttpOdataResult with valid entry target currency with wrong channel - Currency ISO code must be USD",
					"USD", this.classUnderTest.getSalesVolumeCurrencyISOCode());
			assertEquals(
					"extractOwnDatafromHttpOdataResult with single entry target currency with wrong channel - sales volume must be 0",
					BigDecimal.ZERO, this.classUnderTest.getSalesVolume());
			assertEquals(
					"extractOwnDatafromHttpOdataResult with single entry target currency with wrong channel - store purchase ratio must be 0",
					new Double(0), this.classUnderTest.getStorePurchaseRatio());

			//single entry all properties
			setStartData();

			data.put(CARStatisticalDataContainerVolumeBase.ORDER_CHANNEL_PROPERTY, "07");

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals(
					"extractOwnDatafromHttpOdataResult with valid single entry target currency - Currency ISO code must be USD",
					"USD", this.classUnderTest.getSalesVolumeCurrencyISOCode());
			assertEquals("extractOwnDatafromHttpOdataResult with valid single entry target currency - sales volume must be 110",
					BigDecimal.valueOf(110), this.classUnderTest.getSalesVolume());

			assertEquals(
					"extractOwnDatafromHttpOdataResult with valid single entry target currency - store purchase ratio must be 100.0",
					new Double(100.0), this.classUnderTest.getStorePurchaseRatio());

			//valid double entry
			setStartData();

			data = new HashMap();
			data.put(CARStatisticalDataContainerVolumeBase.TRANSACTION_CURRENCY_PROPERTY, "JPY");
			data.put(CARStatisticalDataContainerVolumeBase.TOTAL_NET_AMOUNT_PROPERTY, new BigDecimal(400));
			data.put(CARStatisticalDataContainerVolumeBase.TAX_AMOUNT_PROPERTY, new BigDecimal(20));
			data.put(CARStatisticalDataContainerVolumeBase.ORDER_CHANNEL_PROPERTY, "03");

			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with valid double entry result - sales volume must be 320"+this.classUnderTest.getSalesVolume(), BigDecimal
					.valueOf(320).compareTo(this.classUnderTest.getSalesVolume()), 0);

			assertEquals("extractOwnDatafromHttpOdataResult with valid double entry result - store purchase ratio must be 34.375",
					new Double(34.375), this.classUnderTest.getStorePurchaseRatio());

			//valid entry with only POS orders
			this.classUnderTest.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(0), 0, "USD"));

			data = new HashMap();
			data.put(CARStatisticalDataContainerVolumeBase.TRANSACTION_CURRENCY_PROPERTY, "USD");
			data.put(CARStatisticalDataContainerVolumeBase.TOTAL_NET_AMOUNT_PROPERTY, new BigDecimal(100));
			data.put(CARStatisticalDataContainerVolumeBase.TAX_AMOUNT_PROPERTY, new BigDecimal(10));
			data.put(CARStatisticalDataContainerVolumeBase.ORDER_CHANNEL_PROPERTY, "07");

			oDataEntries.removeAll(oDataEntries);
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with valid double entry result - store purchase ratio must be 100",
					new Double(100), this.classUnderTest.getStorePurchaseRatio());

			//valid entry with no Online orders
			this.classUnderTest.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(0), 0, "USD"));

			data = new HashMap();
			data.put(CARStatisticalDataContainerVolumeBase.TRANSACTION_CURRENCY_PROPERTY, "USD");
			data.put(CARStatisticalDataContainerVolumeBase.TOTAL_NET_AMOUNT_PROPERTY, new BigDecimal(100));
			data.put(CARStatisticalDataContainerVolumeBase.TAX_AMOUNT_PROPERTY, new BigDecimal(10));
			data.put(CARStatisticalDataContainerVolumeBase.ORDER_CHANNEL_PROPERTY, "03");

			oDataEntries.removeAll(oDataEntries);
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			this.classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with valid double entry result - store purchase ratio must be 0",
					new Double(0), this.classUnderTest.getStorePurchaseRatio());

		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	/**
	 * Sets some start data for the class under test.
	 */
	protected void setStartData()
	{
		this.classUnderTest.setSalesVolumeResult(new ISCESalesVolumeResult(BigDecimal.valueOf(1000), 99, "USD"));
	}

}

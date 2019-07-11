/*****************************************************************************
 Class:        ItemsOfInterestDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;
import com.sap.retail.isce.service.sap.result.ISCEItemOfInterestResult;


/**
 * Unit Test class for ItemsOfInterestDataContainer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class ItemsOfInterestDataContainerUnitTest
{

	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "itemsOfInterestDataContainerUnderTest")
	private ItemsOfInterestDataContainer classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@Before
	public void setUp()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dataContainerContext.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);
		loggerMock.clearAll();
		ItemsOfInterestDataContainer.log = loggerMock;
	}

	@Test
	public void testSetDataInErrorState()
	{
		this.classUnderTest.setDataInErrorState();
		assertNull("setDataInErrorState - Items of Interest List must be null", this.classUnderTest.getItemsOfInterestList());
	}

	/**
	 * Tests extractOwnDataFromHttpOdataResult.
	 */
	@Test
	public void testExtractOwnDataFromHttpOdataResult()
	{
		try
		{
			// Null response
			final HttpODataResultImpl httpODataResult = null;
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertNull("extractOwnDatafromHttpOdataResult with null response - Interest Items must be null",
					this.classUnderTest.getItemsOfInterestList());


			// filled result set - no oDataEntries
			HttpODataResult mockHttpODataResult = mock(HttpODataResultImpl.class);
			List<ODataEntry> oDataEntries = null;

			when(mockHttpODataResult.getEntities()).thenReturn(oDataEntries);
			classUnderTest.extractOwnDataFromResult(mockHttpODataResult);
			assertNull("extractOwnDatafromHttpOdataResult with null response - Interest Items must be null",
					this.classUnderTest.getItemsOfInterestList());

			// filled result set
			mockHttpODataResult = mock(HttpODataResultImpl.class);
			oDataEntries = new ArrayList<ODataEntry>();

			final ODataEntry mockODataEntry = mock(ODataEntry.class);
			final Map<String, Object> oDataEntryProperties = new HashMap<String, Object>();
			oDataEntryProperties.put("InterestDescription", "Fishing");
			oDataEntryProperties.put("InterestCode", "FSH");
			final BigDecimal valuationAverage = new BigDecimal(2);
			oDataEntryProperties.put("ValuationAverage", valuationAverage);
			when(mockODataEntry.getProperties()).thenReturn(oDataEntryProperties);

			oDataEntries.add(mockODataEntry);
			when(mockHttpODataResult.getEntities()).thenReturn(oDataEntries);

			classUnderTest.itemsOfInterestList = new ArrayList<ISCEItemOfInterestResult>();
			classUnderTest.extractOwnDataFromResult(mockHttpODataResult);

			assertNotNull("", classUnderTest.getItemsOfInterestList());

			// test for correct data
			List itemsOfInterestList = new ArrayList<ISCEItemOfInterestResult>();
			itemsOfInterestList = classUnderTest.getItemsOfInterestList();
			for (int i = 0; i < itemsOfInterestList.size(); ++i)
			{
				final ISCEItemOfInterestResult interest = (ISCEItemOfInterestResult) itemsOfInterestList.get(i);
				assertEquals("Interest Code was not as expected ", "FSH", interest.getInterestCode());
				assertEquals("Interest Description was not as expected ", "Fishing", interest.getInterestDescription());
				assertEquals("Valuation Average was not as expected ", valuationAverage.toBigInteger(),
						interest.getValuationAverage());
			}

		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	@Test
	public void testGetContainerName()
	{
		assertEquals("getContainerName - container name must be ItemsOfInterestDataContainerDataContainer", classUnderTest
				.getClass().getName(), classUnderTest.getContainerName());
	}

	/**
	 * Tests encodeHTML.
	 */
	@Test
	public void testEncodeHTML()
	{
		final String interestCode = "<Male>";
		final String interestDescription = "<script>alert('XSS Attack Fake');</script> Status Description";
		final BigInteger valuationAverage = new BigInteger("100");
		final ISCEItemOfInterestResult interest = new ISCEItemOfInterestResult(interestCode, interestDescription, valuationAverage);

		classUnderTest.itemsOfInterestList.add(interest);

		classUnderTest.encodeHTML();
		List encodedList = classUnderTest.getItemsOfInterestList();
		final ISCEItemOfInterestResult interestEncoded = (ISCEItemOfInterestResult) encodedList.get(0);

		assertEquals("data container context should be equals", "&lt;Male&gt;", interestEncoded.getInterestCode());
		assertEquals(
				"data container context should be equals",
				"&lt;script&gt;alert&#x28;&#x27;XSS&#x20;Attack&#x20;Fake&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;&#x20;Status&#x20;Description",
				interestEncoded.getInterestDescription());

		final List tempList = classUnderTest.itemsOfInterestList;
		classUnderTest.itemsOfInterestList = null;
		classUnderTest.encodeHTML();
		encodedList = classUnderTest.getItemsOfInterestList();
		assertNull("List is not null", encodedList);
		classUnderTest.itemsOfInterestList = tempList;
	}

	@Test
	public void testGetFilter()
	{
		assertEquals("Filter should be " + "EMailAddress eq '" + CUSTOMER_UID_VALUE + "'", "EMailAddress eq '" + CUSTOMER_UID_VALUE
				+ "'" + " and " + classUnderTest.getDateFilter(), classUnderTest.getFilter());
	}

	@Test
	public void testGetSelect()
	{
		assertEquals("InterestCode,InterestDescription,InterestCount,ValuationAverage",
				"InterestCode,InterestDescription,InterestCount,ValuationAverage", classUnderTest.getSelect());
	}

	@Test
	public void testGetDateFilter()
	{
		final String formattedMinDate = getFormattedMinDate();
		final String formattedMaxDate = getFormattedMaxDate();
		assertEquals("Filter should be InteractionTimestamp ge datetime'" + formattedMinDate
				+ "' and InteractionTimestamp le datetime'" + formattedMaxDate + "'", "InteractionTimestamp ge datetime'"
				+ formattedMinDate + "' and InteractionTimestamp le datetime'" + formattedMaxDate + "'",
				classUnderTest.getDateFilter());
	}

	@Test
	public void testGetServiceEndpointName()
	{
		assertEquals("getServiceEndpointName - ServiceEndpointName name must be ContactAnalyses", "ContactAnalyses",
				classUnderTest.getServiceEndpointName());
	}

	@Test
	public void testGetLocalizedContainerName()
	{
		classUnderTest.i18nService.setCurrentLocale(Locale.ENGLISH);
		assertEquals("getLocalizedContainerName - localized container name must be instorecs.customer360.interests",
				"instorecs.customer360.interests", classUnderTest.getLocalizedContainerName());
	}

	/**
	 * @return formatted maximum date
	 */
	private String getFormattedMaxDate()
	{
		final Calendar today = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");

		return sdf.format(today.getTime()) + "T23:59:59.0000000";
	}

	/**
	 * @return formatted minimum date
	 */
	private String getFormattedMinDate()
	{
		final Calendar todayMinusOneYear = Calendar.getInstance();
		todayMinusOneYear.add(Calendar.YEAR, -1);

		final SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");

		return sdf.format(todayMinusOneYear.getTime()) + "T00:00:00.0000000";
	}

	@Test
	public void testGetCustomerData()
	{

		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		classUnderTest.setDataContainerContext(dataContainerContext);

		try
		{
			classUnderTest.getCustomerData();
			assertFalse("Exception was not thrown despite customerData being null", true);
		}

		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Exception is expected behavior!", true);
		}

	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("ContainerContextParamName should be itemsOfInterestDataContainer", "itemsOfInterestDataContainer",
				classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.ItemsOfInterestDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=EMailAddress eq '<value not traced>' and InteractionTimestamp ge datetime'" + getFormattedMinDate()
				+ "' and InteractionTimestamp le datetime'" + getFormattedMaxDate() + "'";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

	@Test
	public void testGetTraceableFilter()
	{
		final String expected = "EMailAddress eq '<value not traced>' and InteractionTimestamp ge datetime'"
				+ getFormattedMinDate() + "' and InteractionTimestamp le datetime'" + getFormattedMaxDate() + "'";
		assertEquals("Traceable Filter should not show email", expected, classUnderTest.getTraceableFilter());
	}
}

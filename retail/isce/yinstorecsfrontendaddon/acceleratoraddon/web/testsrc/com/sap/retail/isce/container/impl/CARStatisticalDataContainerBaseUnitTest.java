/*****************************************************************************
 Class:        CARStatisticalDataContainerBaseUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;


/**
 * Unit Test class for CARStatisticalDataContainerBaseBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerBaseUnitTest
{

	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "CARStatisticalDataContainerOverallVolumeUnderTest")
	private CARStatisticalDataContainerBase classUnderTest;

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
	}

	@Test
	public void testGetContainerName()
	{
		assertEquals("getContainerName - container name must be CARStatisticalDataContainerBase", classUnderTest.getClass()
				.getName(), classUnderTest.getContainerName());
	}

	@Test
	public void testEncodeHTML()
	{
		classUnderTest.encodeHTML();
		assertTrue("encodeHTML not called", true);
	}

	@Test
	public void testGetServiceEndpointName()
	{
		final String expected = "MultiChannelSalesOrdersQuery";

		assertEquals("getServiceEndpointName - ServiceEndpointName name must be " + expected, expected,
				classUnderTest.getServiceEndpointName());
	}

	@Test
	public void testGetHttpDestinationName()
	{
		assertEquals("constructor - HTTPDestinationName must be CARDestination", "CARDestination",
				this.classUnderTest.getHttpDestinationName());
	}

	@Test
	public void testGetServiceURI()
	{
		assertEquals("constructor - ServiceURI must be CARService.xsodata", "CARService.xsodata",
				this.classUnderTest.getServiceURI());
	}

	@Test
	public void testGetLocalizedContainerName()
	{
		assertEquals("getLocalizedContainerName - localized container name must be instorecs.customer360.statistical",
				"instorecs.customer360.statistical", classUnderTest.getLocalizedContainerName());
	}

	@Test
	public void testGetFilter()
	{
		final String expected = "CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07')";
		assertEquals("getFilter - filter must be" + expected, expected, classUnderTest.getFilter());
	}

	@Test
	public void testGetLastSixMonthFilter()
	{
		final String expected = "CreationDate ge '";
		assertTrue("getLastSixMonthFilter - last six monthfilter must start with " + expected, classUnderTest
				.getLastSixMonthFilter().indexOf(expected) == 0);
	}

	@Test
	public void testGetOnlineOrderChannels()
	{
		final ArrayList<String> expected = new ArrayList();
		expected.add("01");
		expected.add("02");
		expected.add("03");
		assertTrue("getOnlineOrderChannels - online order channels must be  " + expected, classUnderTest.getOnlineOrderChannels()
				.containsAll(expected) && expected.containsAll(classUnderTest.getOnlineOrderChannels()));
	}

	@Test
	public void testGetPOSOrderChannels()
	{
		final ArrayList<String> expected = new ArrayList();
		expected.add("07");
		assertTrue("getPOSOrderChannels - POS order Channels must be " + expected, classUnderTest.getPosOrderChannels()
				.containsAll(expected) && expected.containsAll(classUnderTest.getPosOrderChannels()));
	}

	@Test
	public void testGetKeyPredicate()
	{
		final String expected = "P_SAPClient='999'";
		assertEquals("getKeyPredicate - key predicate must be " + expected, expected, classUnderTest.getKeyPredicate());
	}

	@Test
	public void testGetResultName()
	{
		final String expected = "MultiChannelSalesOrdersQueryResults";
		assertEquals("getResultName - result name must be " + expected, expected, classUnderTest.getResultName());
	}

	@Test
	public void testGetNavigationProperties()
	{
		final String expected = "/Results";
		assertEquals("getNavigationProperties - navigation properties must be " + expected, expected,
				classUnderTest.getNavigationProperties());
	}

	@Test
	public void testCalculateDateMinusSixMonths()
	{
		Date testDate = new GregorianCalendar(2016, 8, 1).getTime();
		Date expectedDate = new GregorianCalendar(2016, 2, 1).getTime();
		Date changedDate = classUnderTest.calculateDateMinusSixMonths(testDate);
		assertEquals("3 Month before " + testDate + "must be " + expectedDate, expectedDate, changedDate);

		testDate = new GregorianCalendar(2016, 7, 30).getTime();
		expectedDate = new GregorianCalendar(2016, 1, 29).getTime();
		changedDate = classUnderTest.calculateDateMinusSixMonths(testDate);
		assertEquals("3 Month before " + testDate + "must be " + testDate, expectedDate, changedDate);

		testDate = new GregorianCalendar(2015, 7, 30).getTime();
		expectedDate = new GregorianCalendar(2015, 1, 28).getTime();
		changedDate = classUnderTest.calculateDateMinusSixMonths(testDate);
		assertEquals("3 Month before " + testDate + "must be " + expectedDate, expectedDate, changedDate);

		testDate = new GregorianCalendar(2016, 3, 30).getTime();
		expectedDate = new GregorianCalendar(2015, 9, 30).getTime();
		changedDate = classUnderTest.calculateDateMinusSixMonths(testDate);
		assertEquals("3 Month before " + testDate + "must be " + expectedDate, expectedDate, changedDate);
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceable Filter should be null", null, classUnderTest.getTraceableFilter());
	}

}

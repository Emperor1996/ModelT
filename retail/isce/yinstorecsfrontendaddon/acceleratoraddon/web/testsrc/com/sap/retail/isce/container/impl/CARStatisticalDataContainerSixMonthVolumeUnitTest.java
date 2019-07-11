/*****************************************************************************
 Class:        CARStatisticalDataContainerSixMonthVolumeUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.impl.mock.LoggerMock;


/**
 * Unit Test class for CARStatisticalDataContainerSixMonthlVolume
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerSixMonthVolumeUnitTest
{
	@Resource(name = "CARStatisticalDataContainerSixMonthVolumeUnderTest")
	private CARStatisticalDataContainerSixMonthVolume classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@Before
	public void setUp()
	{
		loggerMock.clearAll();
		CARStatisticalDataContainerSixMonthVolume.log = loggerMock;
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.CARStatisticalDataContainerSixMonthVolume";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07') and CreationDate ge '";
		assertTrue("TraceInformation (6) - must start with" + expected,
				((String) loggerMock.getDebug().get(6)).indexOf(expected) == 0);
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals(
				"getContainerContextParamName - container context param name must be CARStatisticalDataContainerSixMonthVolume",
				"CARStatisticalDataContainerSixMonthVolume", this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testGetFilter()
	{
		final String expected = "CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07') and CreationDate ge '";

		assertTrue("getFilter -  " + expected, this.classUnderTest.getFilter().indexOf(expected) == 0);
	}
}

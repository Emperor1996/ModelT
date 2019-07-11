/*****************************************************************************
 Class:        CARStatisticalDataContainerOverallCountUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.impl.mock.LoggerMock;


/**
 * Unit Test class for CARStatisticalDataContainerOverallCount
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerOverallCountUnitTest
{

	@Resource(name = "CARStatisticalDataContainerOverallCountUnderTest")
	private CARStatisticalDataContainerOverallCount classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@Before
	public void setUp()
	{
		loggerMock.clearAll();
		CARStatisticalDataContainerOverallCount.log = loggerMock;
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("getContainerContextParamName - container context param name must be CARStatisticalDataContainerOverallCount",
				"CARStatisticalDataContainerOverallCount", this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.CARStatisticalDataContainerOverallCount";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

}

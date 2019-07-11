/*****************************************************************************
 Class:        CARStatisticalDataContainerOverallItemsCountUnitTest
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
 * Unit Test class for CARStatisticalDataContainerOverallItemsCount
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerOverallItemsCountUnitTest
{

	@Resource(name = "CARStatisticalDataContainerOverallItemsCountUnderTest")
	private CARStatisticalDataContainerOverallItemsCount classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@Before
	public void setUp()
	{
		loggerMock.clearAll();
		CARStatisticalDataContainerOverallItemsCount.log = loggerMock;
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals(
				"getContainerContextParamName - container context param name must be CARStatisticalDataContainerOverallItemsCount",
				"CARStatisticalDataContainerOverallItemsCount", this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testSelect()
	{
		assertEquals("getSelect - select must be CTransactionNumber,TransactionIndex", "TransactionNumber,TransactionIndex",
				this.classUnderTest.getSelect());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.CARStatisticalDataContainerOverallItemsCount";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

}
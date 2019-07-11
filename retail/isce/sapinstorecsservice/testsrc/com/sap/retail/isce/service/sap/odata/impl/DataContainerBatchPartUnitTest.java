/*****************************************************************************
 Class:        DataContainerBatchPartUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.core.batch.BatchQueryPartImpl;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DataContainerBatchPartUnitTest
{
	private DataContainerBatchPart classUnderTest;

	@Before
	public void setUp()
	{
		classUnderTest = new DataContainerBatchPart();
	}

	@Test
	public void testResultName()
	{
		final String resultName = "resultName";
		classUnderTest.setResultName(resultName);
		assertEquals("ResultName should be resultName", resultName, classUnderTest.getResultName());
	}

	@Test
	public void testDataContainerName()
	{
		final String dataContainerName = "dataContainerName";
		classUnderTest.setDataContainerName(dataContainerName);
		assertEquals("DataContainerName should be dataContainerName", dataContainerName, classUnderTest.getDataContainerName());
	}

	@Test
	public void testBatchPart()
	{
		final BatchPart batchPart = new BatchQueryPartImpl();
		classUnderTest.setBatchPart(batchPart);
		assertEquals("BatchPart should be returned", batchPart, classUnderTest.getBatchPart());
	}
}

/*****************************************************************************
 Class:        CARStatisticalDataContainerCountBaseUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.commons.InlineCount;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test class for CARStatisticalDataContainerCountBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerCountBaseUnitTest
{

	@Resource(name = "CARStatisticalDataContainerOverallCountUnderTest")
	private CARStatisticalDataContainerCountBase classUnderTest;

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		CARStatisticalDataContainerCountBase.log = new LoggerMock("");
	}

	@Test
	public void testGetInlineCount()
	{
		assertEquals("getInlineCount - InlineCount must be allpages", InlineCount.ALLPAGES, this.classUnderTest.getInlineCount());
	}

	@Test
	public void testGetTop()
	{
		assertEquals("getTop - top must be 0", 0, this.classUnderTest.getTop().intValue());
	}

	@Test
	public void testGetSelect()
	{
		assertEquals("getSelect - select must be TransactionNumber", "TransactionNumber", this.classUnderTest.getSelect());
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
			this.classUnderTest.setNumberOfTransactions(99);

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with null response - number of transactions must be 0", 0,
					this.classUnderTest.getNumberOfTransactions());

			// Empty response
			this.classUnderTest.setNumberOfTransactions(99);

			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with empty response - number of transactions must be 0", 0,
					this.classUnderTest.getNumberOfTransactions());

			//valid response
			this.classUnderTest.setNumberOfTransactions(99);
			httpODataResult.setCount(Integer.valueOf(12));
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with valid response - number of transactions must be 12", 12,
					this.classUnderTest.getNumberOfTransactions());
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	@Test
	public void testSetDataInErrorState()
	{
		this.classUnderTest.setNumberOfTransactions(99);
		this.classUnderTest.setDataInErrorState();
		assertEquals("setDataInErrorState - number of transactions must be 0", 0, this.classUnderTest.getNumberOfTransactions());
	}
}

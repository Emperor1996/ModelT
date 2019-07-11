/*****************************************************************************
 Class:        CARStatisticalDataContainerLastPurchaseUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.UnitTest;

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

import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test class for CARStatisticalDataContainerLastPurchaseUnitTest
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class CARStatisticalDataContainerLastPurchaseUnitTest
{
	@Resource(name = "CARStatisticalDataContainerLastPurchaseUnderTest")
	private CARStatisticalDataContainerLastPurchase classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		loggerMock.clearAll();
		CARStatisticalDataContainerLastPurchase.log = loggerMock;
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("getContainerContextParamName - container context param name must be CARStatisticalDataContainerLastPurchase",
				"CARStatisticalDataContainerLastPurchase", this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testGetSelect()
	{
		assertEquals("testGetSelect - select should contains CreationDate", "CreationDate", this.classUnderTest.getSelect());
	}

	@Test
	public void testOrderBy()
	{
		assertEquals("testOrderBy - orderby should be creationDate descending", "CreationDate desc",
				this.classUnderTest.getOrderBy());
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
			this.classUnderTest.setLastPurchaseDate("20160524");

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with null response - last purchase date must be empty string", "",
					this.classUnderTest.getLastPurchaseDate());

			// Empty response
			this.classUnderTest.setLastPurchaseDate("20160524");

			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with null response - last purchase date must be empty string", "",
					this.classUnderTest.getLastPurchaseDate());

			//valid response
			this.classUnderTest.setLastPurchaseDate("");
			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());
			final Map<String, Object> data = new HashMap();
			data.put(CARStatisticalDataContainerLastPurchase.SELECTED_FIELDS, "20160524");

			final List<ODataEntry> oDataEntries = new ArrayList<ODataEntry>();
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			httpODataResult.setEntities(oDataEntries);

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			assertEquals("extractOwnDatafromHttpOdataResult with valid response - number of transactions must be 20160524",
					"20160524", this.classUnderTest.getLastPurchaseDate());
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.CARStatisticalDataContainerLastPurchase";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=CustomerNumber eq '0000001221' and (OrderChannel eq '01' or OrderChannel eq '02' or OrderChannel eq '03' or OrderChannel eq '07')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

}

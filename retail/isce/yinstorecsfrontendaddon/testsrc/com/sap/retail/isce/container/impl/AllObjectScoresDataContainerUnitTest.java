/*****************************************************************************
 Class:        AllObjectScoresDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;


/**
 * Unit Test class for AllObjectScoresDataContainer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCE_CMS_Test-spring.xml" })
@UnitTest
public class AllObjectScoresDataContainerUnitTest
{
	@Resource(name = "AllObjectScoresDataContainerUnderTest")
	private AllObjectScoresDataContainer classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	@Before
	public void setUp()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		classUnderTest.setDataContainerContext(dataContainerContext);
		loggerMock.clearAll();
		AllObjectScoresDataContainer.log = loggerMock;
	}

	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		HttpODataResultImpl httpODataResult = null;

		try
		{
			// Null response
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			List<ISCEObjectScoreResult> results = this.classUnderTest.getObjectScoresList();
			assertTrue("extractOwnDatafromHttpOdataResult with emtpy response - ", results.isEmpty());

			// Empty response
			httpODataResult = new HttpODataResultImpl();
			httpODataResult.setEntities(new ArrayList<ODataEntry>());
			classUnderTest.extractOwnDataFromResult(httpODataResult);
			results = this.classUnderTest.getObjectScoresList();
			assertTrue("extractOwnDatafromHttpOdataResult with emtpy response - ", results.isEmpty());

			//non empty response
			final List<ODataEntry> oDataEntries = new ArrayList<ODataEntry>();
			Map<String, Object> data = new HashMap();
			data.put(AllObjectScoresDataContainer.PROPERTY_SCORE_ID, "Id1");
			data.put(AllObjectScoresDataContainer.PROPERTY_SCORE_DESC, "Desc1");
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			data = new HashMap();
			data.put(AllObjectScoresDataContainer.PROPERTY_SCORE_ID, "Id2");
			data.put(AllObjectScoresDataContainer.PROPERTY_SCORE_DESC, "Desc2");
			oDataEntries.add(new ODataEntryImpl(data, null, null, null));

			httpODataResult.setEntities(oDataEntries);

			classUnderTest.extractOwnDataFromResult(httpODataResult);
			results = this.classUnderTest.getObjectScoresList();
			assertEquals("extractOwnDatafromHttpOdataResult with non empty response - size must be 2", 2, results.size());

			assertEquals("extractOwnDatafromHttpOdataResult with non empty response - first entry scoreId must be 'Id1'", "Id1",
					results.get(0).getScoreId());
			assertEquals(
					"extractOwnDatafromHttpOdataResult with non empty response - second entry ScoreDescription must be 'Desc2'",
					"Desc2", results.get(1).getScoreDescription());

		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("extractOwnDatafromHttpOdataResult - No exception should have been thrown", true);
		}
	}

	@Test
	public void testGetContainerName()
	{
		assertEquals("getContainerName - container name must be " + classUnderTest.getClass(), classUnderTest.getClass().getName(),
				classUnderTest.getContainerName());
	}

	@Test
	public void testVetDataInErrorState()
	{
		// Empty response
		final HttpODataResultImpl httpODataResult = new HttpODataResultImpl();
		httpODataResult.setEntities(new ArrayList<ODataEntry>());
		classUnderTest.extractOwnDataFromResult(httpODataResult);

		classUnderTest.setDataInErrorState();
		assertEquals("setDataInErrorState - objectScoresList must be null", null, classUnderTest.getObjectScoresList());
	}

	@Test
	public void testGetFilter()
	{
		final String expected = "(ObjectType eq 'CUAN_CONSUMER')";
		assertEquals("getFilter - filter must be" + expected, expected, classUnderTest.getFilter());
	}

	@Test
	public void testGetSelect()
	{
		final String expected = "ScoreId,ScoreDesc";
		assertEquals("getFilter - select must be" + expected, expected, classUnderTest.getSelect());
	}

	@Test
	public void testGetOrderBy()
	{
		final String expected = "ScoreDesc";
		assertEquals("getOrderBy - orderBy must be" + expected, expected, classUnderTest.getOrderBy());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.AllObjectScoresDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=(ObjectType eq 'CUAN_CONSUMER')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceable Filter should be null", null, classUnderTest.getTraceableFilter());
	}

}

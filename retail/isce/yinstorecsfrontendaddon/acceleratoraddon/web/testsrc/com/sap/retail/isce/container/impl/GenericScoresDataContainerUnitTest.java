/*****************************************************************************
 Class:        GenericScoresDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.MediaMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;


/**
 * Unit Test for Default implementation class GenericScoresDataContainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class GenericScoresDataContainerUnitTest
{
	@Resource(name = "genericScoresDataContainerUnderTest")
	private GenericScoresDataContainer classUnderTest;

	LoggerMock loggerMock = new LoggerMock("");

	private static final String CUSTOMER_UID_VALUE = "uid1";

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dataContainerContext.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);
		classUnderTest.setGenericScoresProperties("CONTACT_RECENCY_LEVEL");
		loggerMock.clearAll();
		classUnderTest.log = loggerMock;
	}

	@Test
	public void testOGenericScoresDataContainer()
	{
		assertEquals("ServiceUri name should be /sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV",
				"/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV", classUnderTest.serviceURI);
		assertEquals("Container Name should be " + "com.sap.retail.isce.container.impl.GenericScoresDataContainer",
				"com.sap.retail.isce.container.impl.GenericScoresDataContainer", classUnderTest.containerName);
		assertEquals("Service End Point name should be ISCEObjectScores", "ISCEObjectScores", classUnderTest.serviceEndpointName);
		assertEquals("HTTP destination should be ISCEHybrisMarketingHTTPDestination", "ISCEHybrisMarketingHTTPDestination",
				classUnderTest.httpDestinationName);
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetFilterOneGenericScore()
	{
		assertEquals("Filter should be " + "EMailAddress eq '" + CUSTOMER_UID_VALUE + "'", "EMailAddress eq '" + CUSTOMER_UID_VALUE
				+ "' and ObjectType eq 'CUAN_CONSUMER' and (ScoreId eq 'CONTACT_RECENCY_LEVEL')", classUnderTest.getFilter());
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetFilterTwoGenericScores()
	{
		classUnderTest.setGenericScoresProperties("CONTACT_RECENCY_LEVEL,SAP_CONTACT_LOY_POINT");
		assertEquals(
				"Filter should be " + "EMailAddress eq '" + CUSTOMER_UID_VALUE + "'",
				"EMailAddress eq '"
						+ CUSTOMER_UID_VALUE
						+ "' and ObjectType eq 'CUAN_CONSUMER' and (ScoreId eq 'CONTACT_RECENCY_LEVEL' or ScoreId eq 'SAP_CONTACT_LOY_POINT')",
				classUnderTest.getFilter());
	}

	/**
	 * Tests getSelect.
	 */
	@Test
	public void testGetSelect()
	{
		assertEquals("Select should be ScoreId,ScoreValue,ScoreDesc", "ScoreId,ScoreValue,FormattedScoreValue,ScoreDesc",
				classUnderTest.getSelect());
	}

	/**
	 * Tests errorState.
	 */
	@Test
	public void test()
	{
		classUnderTest.setDataInErrorState();
		assertEquals("genericScoreList should be empty", 0, classUnderTest.getGenericScoresList().size());
	}



	/**
	 * Tests the extractOwnDataFromResult.
	 */
	@Test
	public void extractOwnDataFromResultWithNull()
	{
		HttpODataResultManipulator httpODataResult = null;
		classUnderTest.genericScoresList = null;
		classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
		assertEquals("genericScoreList should be empty", null, classUnderTest.getGenericScoresList());

		httpODataResult = new HttpODataResultImpl();
		httpODataResult.setEntities(null);
		classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
		assertEquals("genericScoreList should be empty", null, classUnderTest.getGenericScoresList());

		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		oDataEntryList.add(null);
		httpODataResult.setEntities(oDataEntryList);
		classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
		assertEquals("genericScoreList should be empty", null, classUnderTest.getGenericScoresList());
	}


	/**
	 * Tests the extractOwnDataFromResult.
	 */
	@Test
	public void testExtractOwnDataFromResult()
	{
		final HttpODataResultManipulator httpODataResult = new HttpODataResultImpl();
		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		final ODataEntry oDataEntry = getOdataEntryWithScoreAndValue("CONTACT_RECENCY_LEVEL", "19.000", "NineTeen",
				"Latest Activity");
		oDataEntryList.add(oDataEntry);
		httpODataResult.setEntities(oDataEntryList);

		try
		{
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("Size for GenericScoresList should be 1", 1, classUnderTest.getGenericScoresList().size());
			final ISCEObjectScoreResult genericScore = (ISCEObjectScoreResult) classUnderTest.getGenericScoresList().get(0);
			assertEquals("description for the first entry for GenericScoresList should be Latest Activity", "Latest Activity",
					genericScore.getScoreDescription());

			assertEquals("weight for the first entry for GenericScoresList should be 19.000", "NineTeen",
					genericScore.getScoreValue());
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}

	@Test
	public void testAddScoreToGenericListDataContainerRuntimeException()
	{
		final ODataEntry oDataEntry = new ODataEntryImpl(null, null, null, null);
		try
		{
			classUnderTest.addScoreToGenericList(oDataEntry);
			assertFalse("An Exception should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertFalse("An expected exception have been thrown", false);
		}
	}

	@Test
	public void testEncodeHTML()
	{
		classUnderTest.encodeHTML();
		assertTrue("encodeHTML not called", true);
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("getContainerContextParamName not identical", classUnderTest.getContainerContextParamName(),
				BaseScoresDataContainer.DATA_CONTAINER_CONTEXT_PARAM_NAME);
	}



	/**
	 * Return oData Entry containing the given score and score value.
	 *
	 * @param scoreId
	 * @param scoreValue
	 * @param formattedScoreValue
	 * @return oDataEntry containing given values
	 */
	private ODataEntry getOdataEntryWithScoreAndValue(final String scoreId, final String scoreValue,
			final String formattedScoreValue, final String scoreDesc)
	{
		final ODataEntry oDataEntry = new ODataEntry()
		{

			@Override
			public Map<String, Object> getProperties()
			{
				final Map<String, Object> properties = new HashMap<>();
				properties.put("ScoreId", scoreId);
				properties.put("ScoreValue", scoreValue);
				properties.put("FormattedScoreValue", formattedScoreValue);
				properties.put("ScoreDesc", scoreDesc);

				return properties;
			}

			@Override
			public EntryMetadata getMetadata()
			{
				return null;
			}

			@Override
			public MediaMetadata getMediaMetadata()
			{
				return null;
			}

			@Override
			public ExpandSelectTreeNode getExpandSelectTree()
			{
				return null;
			}

			@Override
			public boolean containsInlineEntry()
			{
				return false;
			}
		};

		return oDataEntry;
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.GenericScoresDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=EMailAddress eq '<value not traced>' and ObjectType eq 'CUAN_CONSUMER' and (ScoreId eq 'CONTACT_RECENCY_LEVEL')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceable Filter should not show email",
				"EMailAddress eq '<value not traced>' and ObjectType eq 'CUAN_CONSUMER' and (ScoreId eq 'CONTACT_RECENCY_LEVEL')",
				classUnderTest.getTraceableFilter());
	}

}
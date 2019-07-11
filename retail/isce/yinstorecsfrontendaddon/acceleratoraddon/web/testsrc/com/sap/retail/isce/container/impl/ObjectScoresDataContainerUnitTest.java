/*****************************************************************************
 Class:        ObjectScoresDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.MediaMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.container.impl.mock.DataContainerPropertyIntegerMock;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.model.CMSISCEPurchaseHistoryComponentModel;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test for Default implementation class ObjectScoresDataContainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class ObjectScoresDataContainerUnitTest extends UnitTestBase
{
	@Resource(name = "objectScoresDataContainerUnderTest")
	private ObjectScoresDataContainer classUnderTest;
	@Resource(name = "objectScoresDataContainerActivityUnderTest")
	private ObjectScoresDataContainer classUnderTestActivity;
	@Resource(name = "objectScoresDataContainerSentimentUnderTest")
	private ObjectScoresDataContainer classUnderTestSentiment;

	LoggerMock loggerMock = new LoggerMock("");

	private HttpODataResultManipulator httpODataResult = null;

	private static final String PROPERTY_SCORE_VALUE = "ScoreValue";
	private static final String PROPERTY_FORMATTED_SCORE_VALUE = "FormattedScoreValue";
	private static final String PROPERTY_SCORE_ID = "ScoreId";
	private static final BigDecimal SCORE_ID_AGE_BIG_DECIMAL_VALUE = BigDecimal.valueOf(10);
	private static final String SCORE_ID_AGE_VALUE = "10";
	private static final BigDecimal SCORE_ID_ACTIVITY_BIG_DECIMAL_VALUE = BigDecimal.valueOf(5);
	private static final String SCORE_ID_ACTIVITY_VALUE = "5";
	private static final BigDecimal SCORE_ID_SENTIMENT_BIG_DECIMAL_VALUE = BigDecimal.valueOf(3);
	private static final String SCORE_ID_SENTIMENT_VALUE = "A";
	private static final String SCORE_ID_AGE_VALUE_FORMATTED = "10";
	private static final String SCORE_ID_ACTIVITY_VALUE_FORMATTED = "Very Busy";
	private static final String SCORE_ID_SENTIMENT_VALUE_FORMATTED = "Neutral";
	private static final String SCORE_ID_AGE = "CONTACT_AGE_SCORE";
	private static final String SCORE_ID_ACTIVITY = "CONTACT_ACTIVITY_SCORE";
	private static final String SCORE_ID_SENTIMENT = "CONTACT_SENTIMENT_SCORE";
	private static final String CUSTOMER_UID_VALUE = "uid1";
	private final static String TEST_STRING_HTML_ENCODED = "NiceText&#x21;&#x20;&#xa7;&#x24;&#x25;&amp;&#x2f;&#x28;&#x29;&#x3d;&#x2b;&#x2a;&#x23;&#x27;&lt;&gt;&#x7c;&#x2f;&#x2a;-&#x2b;&#x3b;&#x3a;_,.-&#xb5;&#x5e;&#xb0;1234567890&#xdf;&#xdf;&#xb4;&#x60;&#x7e;&#x2b;&#x2a;";

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		loggerMock.clearAll();
		ObjectScoresDataContainer.log = loggerMock;
	}

	/**
	 * Tests ObjectScoresDataContainer.
	 */
	@Test
	public void testObjectScoresDataContainer()
	{
		assertEquals("ServiceUri name should be /sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV",
				"/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV", classUnderTest.serviceURI);
		assertEquals("Container Name should be " + "com.sap.retail.isce.container.impl.ObjectScoresDataContainer",
				"com.sap.retail.isce.container.impl.ObjectScoresDataContainerAge", classUnderTest.containerName);
		assertEquals("Service End Point name should be ISCEObjectScores", "ISCEObjectScores", classUnderTest.serviceEndpointName);
		assertEquals("HTTP destination should be ISCEHybrisMarketingHTTPDestination", "ISCEHybrisMarketingHTTPDestination",
				classUnderTest.httpDestinationName);
		final ObjectScoresDataContainer anotherClassUnderTest = new ObjectScoresDataContainer(null);
		assertEquals("scoreId is not empty", anotherClassUnderTest.scoreId, "");
		assertEquals("containerId is not empty", anotherClassUnderTest.containerId, "");
		assertEquals("containerName is not com.sap.retail.isce.container.impl.ObjectScoresDataContainer",
				anotherClassUnderTest.containerName, "com.sap.retail.isce.container.impl.ObjectScoresDataContainer");
	}

	/**
	 * Tests getAgeInYears.
	 */
	@Test
	public void testGetAgeInYears()
	{
		classUnderTest.ageInYears = SCORE_ID_AGE_VALUE;
		assertEquals("ageInYears should be " + SCORE_ID_AGE_VALUE, SCORE_ID_AGE_VALUE, classUnderTest.getAgeInYears().toString());
	}

	/**
	 * Tests getSentimentScore.
	 */
	@Test
	public void testGetSentimentScore()
	{
		classUnderTest.sentimentScore = SCORE_ID_SENTIMENT_VALUE;
		assertEquals("sentimentScore should be " + SCORE_ID_SENTIMENT_VALUE, SCORE_ID_SENTIMENT_VALUE,
				classUnderTest.getSentimentScore());
	}

	/**
	 * Tests getActivityScore.
	 */
	@Test
	public void testGetActivityScore()
	{
		classUnderTest.activityScore = SCORE_ID_ACTIVITY_VALUE;
		assertEquals("activityScore should be " + SCORE_ID_ACTIVITY_VALUE, SCORE_ID_ACTIVITY_VALUE,
				classUnderTest.getActivityScore());
	}

	/**
	 * Tests getGetAgeInYearsDescription.
	 */
	@Test
	public void testGetAgeInYearsDescription()
	{
		classUnderTest.ageInYearsDescription = SCORE_ID_AGE_VALUE_FORMATTED;
		assertEquals("ageInYearsDescription should be " + SCORE_ID_AGE_VALUE_FORMATTED, SCORE_ID_AGE_VALUE_FORMATTED,
				classUnderTest.getAgeInYearsDescription().toString());
	}

	/**
	 * Tests getSentimentScoreDescription.
	 */
	@Test
	public void testGetSentimentScoreDescription()
	{
		classUnderTest.sentimentScoreDescription = SCORE_ID_SENTIMENT_VALUE_FORMATTED;
		assertEquals("sentimentScoreDescription should be " + SCORE_ID_SENTIMENT_VALUE_FORMATTED,
				SCORE_ID_SENTIMENT_VALUE_FORMATTED, classUnderTest.getSentimentScoreDescription());
	}

	/**
	 * Tests getActivityScoreDescription.
	 */
	@Test
	public void testGetActivityScoreDescription()
	{
		classUnderTest.activityScoreDescription = SCORE_ID_ACTIVITY_VALUE_FORMATTED;
		assertEquals("activityScoreDescription should be " + SCORE_ID_ACTIVITY_VALUE_FORMATTED, SCORE_ID_ACTIVITY_VALUE_FORMATTED,
				classUnderTest.getActivityScoreDescription());
	}

	/**
	 * Tests testExtractOwnDatafromHttpOdataResultForAgeScore.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResultForAgeScore()
	{
		prepareTestDataForExtraction();

		httpODataResult = new HttpODataResultImpl();
		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		//final ODataEntry oDataEntry = getOdataEntryWithScoreAndValue(SCORE_ID_AGE, SCORE_ID_AGE_VALUE, SCORE_ID_AGE_VALUE_FORMATTED);
		final ODataEntry oDataEntry = getOdataEntryWithScoreAndValue(SCORE_ID_AGE, SCORE_ID_AGE_BIG_DECIMAL_VALUE,
				SCORE_ID_AGE_VALUE_FORMATTED);



		oDataEntryList.add(oDataEntry);
		httpODataResult.setEntities(oDataEntryList);

		try
		{
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("AgeInYears should be" + SCORE_ID_AGE_BIG_DECIMAL_VALUE.toString(),
					SCORE_ID_AGE_BIG_DECIMAL_VALUE.toString(), classUnderTest.getAgeInYears());
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}

	/**
	 * Tests testExtractOwnDatafromHttpOdataResultForActivityScore.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResultForActivityScore()
	{
		prepareTestDataForExtraction();

		httpODataResult = new HttpODataResultImpl();
		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		final ODataEntry oDataEntry = getOdataEntryWithScoreAndValue(SCORE_ID_ACTIVITY, SCORE_ID_ACTIVITY_BIG_DECIMAL_VALUE,
				SCORE_ID_ACTIVITY_VALUE_FORMATTED);
		oDataEntryList.add(oDataEntry);
		httpODataResult.setEntities(oDataEntryList);

		try
		{
			//classUnderTest.containerId = "Activity";
			classUnderTestActivity.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("ActivityScore should be" + SCORE_ID_ACTIVITY_BIG_DECIMAL_VALUE.toString(),
					SCORE_ID_ACTIVITY_BIG_DECIMAL_VALUE.toString(), classUnderTestActivity.getActivityScore());

		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Test method execution went bad", true);
		}

	}

	/**
	 * Tests testExtractOwnDatafromHttpOdataResultForSentimentScore.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResultForSentimentScore()
	{
		prepareTestDataForExtraction();

		httpODataResult = new HttpODataResultImpl();
		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		final ODataEntry oDataEntry = getOdataEntryWithScoreAndValue(SCORE_ID_SENTIMENT, SCORE_ID_SENTIMENT_BIG_DECIMAL_VALUE,
				SCORE_ID_SENTIMENT_VALUE_FORMATTED);
		oDataEntryList.add(oDataEntry);
		httpODataResult.setEntities(oDataEntryList);

		try
		{
			//classUnderTestSentiment.containerId = "Sentiment";
			classUnderTestSentiment.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("SentimentScore should be" + SCORE_ID_SENTIMENT_BIG_DECIMAL_VALUE.toString(),
					SCORE_ID_SENTIMENT_BIG_DECIMAL_VALUE.toString(), classUnderTestSentiment.getSentimentScore());

		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Test method execution went bad", true);
		}

	}

	/**
	 * Tests extractOwnDatafromHttpOdataResult - Not Happy Path.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResultNoHappyPath()
	{
		prepareTestDataForExtraction();

		httpODataResult = new HttpODataResultImpl();

		try
		{
			classUnderTest.extractOwnDataFromResult(null);
			assertEquals("AgeInYears should be " + null, null, classUnderTest.getAgeInYears());

			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("AgeInYears should be null.", null, classUnderTest.getAgeInYears());

			final List<ODataEntry> oDataEntryListEmpty = new ArrayList<>();
			oDataEntryListEmpty.add(null);
			httpODataResult.setEntities(oDataEntryListEmpty);
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertEquals("AgeInYears should be null.", null, classUnderTest.getAgeInYears());
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}


	/**
	 * Tests extractOwnDatafromHttpOdataResult - Not Happy Path - Exception.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResultNoHappyPathException()
	{
		prepareTestDataForExtraction();

		httpODataResult = new HttpODataResultImpl();

		try
		{

			final List<ODataEntry> oDataEntryList3 = new ArrayList<>();
			final ODataEntry oDataEntryNull = getOdataEntryWithScoreAndValue(null, null, null);
			oDataEntryList3.add(oDataEntryNull);
			httpODataResult.setEntities(oDataEntryList3);
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResult);
			assertFalse("Test method execution should have thrown an exception", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Test method execution went well", true);
		}
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetFilter()
	{
		assertEquals("Filter should be " + "EMailAddress eq '" + CUSTOMER_UID_VALUE + "'", "EMailAddress eq '" + CUSTOMER_UID_VALUE
				+ "' and ObjectType eq 'CUAN_CONSUMER' and ScoreId eq 'CONTACT_AGE_SCORE'", classUnderTest.getFilter());
	}

	/**
	 * Tests getSelect.
	 */
	@Test
	public void testGetSelect()
	{
		assertEquals("Select should be ScoreId,ScoreValue,FormattedScoreValue", "ScoreId,ScoreValue,FormattedScoreValue",
				classUnderTest.getSelect());
	}

	/**
	 * Tests getHttpDestinationName.
	 */
	@Test
	public void testGetHttpDestinationName()
	{
		assertEquals("HTTP destination should be ISCEHybrisMarketingHTTPDestination", "ISCEHybrisMarketingHTTPDestination",
				classUnderTest.getHttpDestinationName());
	}

	/**
	 * Tests getServiceEndpointName.
	 */
	@Test
	public void testGetServiceEndpointName()
	{
		assertEquals("Service End Point name should be ISCEObjectScores", "ISCEObjectScores",
				classUnderTest.getServiceEndpointName());
	}



	/**
	 * Tests errorState.
	 */
	@Test
	public void testErrorState()
	{
		classUnderTest.setErrorState(Boolean.TRUE);
		assertEquals("ErrorState should be Boolean.True", classUnderTest.errorState, classUnderTest.getErrorState());
	}

	/**
	 * Tests setDataInErrorState.
	 */
	@Test
	public void testSetDataInErrorState()
	{
		classUnderTest.ageInYears = "38";
		classUnderTest.setDataInErrorState();
		assertEquals("ageInYears should be null", classUnderTest.getAgeInYears(), null);
	}

	/**
	 * Tests setDataToNull.
	 */
	@Test
	public void testSetDataToNull()
	{
		classUnderTest.ageInYears = "38";
		classUnderTest.setDataToNull();
		assertEquals("ageInYears should be null", classUnderTest.getAgeInYears(), null);
	}

	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerNameAgeContainer()
	{
		//classUnderTest.containerId = "Age";
		assertEquals("Localized Container Name should be ", "instorecs.customer360.profile",
				classUnderTest.getLocalizedContainerName());
	}


	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerNameSentimentScoreContainer()
	{
		//classUnderTest.containerId = "Sentiment";
		assertEquals("Localized Container Name should be ", "instorecs.customer360.statistical",
				classUnderTestSentiment.getLocalizedContainerName());
	}

	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerNameActivityScoreContainer()
	{
		//classUnderTest.containerId = "Activity";
		assertEquals("Localized Container Name should be ", "instorecs.customer360.statistical",
				classUnderTestActivity.getLocalizedContainerName());
	}

	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerNameDefaultContainer()
	{
		classUnderTest.containerId = "";
		assertEquals("Localized Container Name should be ", "instorecs.customer360.statistical",
				classUnderTest.getLocalizedContainerName());
		classUnderTest.containerId = "Age";
	}

	/**
	 * Tests getContainerContextParamName.
	 */
	@Test
	public void getContainerContextParamNameAgeScore()
	{
		//classUnderTestActivity.containerId = "Age";
		assertEquals("Container Context Param Name should be 'objectScoresDataContainerAge'", "objectScoresDataContainerAge",
				classUnderTest.getContainerContextParamName());
	}

	/**
	 * Tests getContainerContextParamName.
	 */
	@Test
	public void getContainerContextParamNameActivityScore()
	{
		//classUnderTest.containerId = "Activity";
		assertEquals("Container Context Param Name should be 'objectScoresDataContainerActivity'",
				"objectScoresDataContainerActivity", classUnderTestActivity.getContainerContextParamName());
	}

	/**
	 * Tests getContainerContextParamName.
	 */
	@Test
	public void getContainerContextParamNameSentimentScore()
	{
		//classUnderTestSentiment.containerId = "Sentiment";
		assertEquals("Container Context Param Name should be 'objectScoresDataContainerSentiment'",
				"objectScoresDataContainerSentiment", classUnderTestSentiment.getContainerContextParamName());
	}

	/**
	 * Tests encodeHTML.
	 */
	@Test
	public void testEncodeHTML()
	{
		classUnderTest.ageInYearsDescription = "<Male>";
		classUnderTest.sentimentScoreDescription = "<script>alert('XSS Attack Fake');</script> Status Description";
		classUnderTest.activityScoreDescription = "<script>alert('XSRF Attack Fake');</script> Status Description";
		classUnderTest.encodeHTML();
		assertEquals("data container context should be equals", "&lt;Male&gt;", classUnderTest.ageInYearsDescription);
		assertEquals(
				"data container context should be equals",
				"&lt;script&gt;alert&#x28;&#x27;XSS&#x20;Attack&#x20;Fake&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;&#x20;Status&#x20;Description",
				classUnderTest.sentimentScoreDescription);
		assertEquals(
				"data container context should be equals",
				"&lt;script&gt;alert&#x28;&#x27;XSRF&#x20;Attack&#x20;Fake&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;&#x20;Status&#x20;Description",
				classUnderTest.activityScoreDescription);
	}

	/**
	 * Tests encodeHTML considering properties.
	 */
	@Test
	public void testEncodeHTMLWithLevels()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTestActivity.setCMSComponentModel(cmsComponentModel);
		classUnderTestActivity.createProperties();
		classUnderTestActivity.determineDataForCMSComponent();
		classUnderTestActivity.encodeHTML();
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED,
				classUnderTestActivity.activityScoreProperty.getLevels().get(0).getUnit());

		classUnderTestSentiment.createProperties();
		classUnderTestSentiment.determineDataForCMSComponent();
		classUnderTestSentiment.encodeHTML();
		assertEquals("Description of first level should be encoded", TEST_STRING_HTML_ENCODED,
				classUnderTestSentiment.sentimentScoreProperty.getLevels().get(0).getUnit());
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
	public void testGetScoreProperties()
	{
		//classUnderTest.containerId = "Age";
		classUnderTest.createProperties();
		assertNull("activityScoreProperty should not have been created.", classUnderTest.getActivityScoreProperty());
		assertNull("sentiemntScoreProperty should not have been created.", classUnderTestSentiment.getSentimentScoreProperty());
	}

	@Test
	public void testGetActivityScoreProperty()
	{
		classUnderTestActivity.createProperties();
		assertNotNull("activityScoreProperty should have been created.", classUnderTestActivity.getActivityScoreProperty());
	}

	@Test
	public void testGetSentimentScoreProperty()
	{
		classUnderTestSentiment.createProperties();
		assertNotNull("activityScoreProperty should have been created.", classUnderTestSentiment.getSentimentScoreProperty());
	}

	@Test
	public void testCreateProperties()
	{
		classUnderTest.createProperties();
		assertNull("activityScoreProperty should not have been created.", classUnderTestActivity.activityScoreProperty);
		assertNull("classUnderTestSentiment should not have been created.", classUnderTestSentiment.sentimentScoreProperty);

		classUnderTestActivity.createProperties();
		assertNotNull("activityScoreProperty should have been created.", classUnderTestActivity.activityScoreProperty);

		classUnderTestSentiment.createProperties();
		assertNotNull("classUnderTestSentiment should have been created.", classUnderTestSentiment.sentimentScoreProperty);
	}

	@Test
	public void testDetermineDataForCMSComponent()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		classUnderTestActivity.setCMSComponentModel(cmsComponentModel);
		classUnderTestActivity.createProperties();

		classUnderTestActivity.determineDataForCMSComponent();
		assertEquals("Activity score should have 4 levels", 4, classUnderTestActivity.activityScoreProperty.getLevels().size());

		classUnderTestSentiment.createProperties();
		classUnderTestSentiment.determineDataForCMSComponent();
		assertEquals("sentiment score should have 5 levels", 5, classUnderTestSentiment.sentimentScoreProperty.getLevels().size());
	}

	@Test
	public void testDetermineActivityPropertyLevels()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		//		Integer.valueOf(1), Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(150);
		classUnderTestActivity.createProperties();
		classUnderTestActivity.determineActivityPropertyLevels(cmsComponentModel);
		assertEquals("Activity score should have 4 levels", 4, classUnderTestActivity.activityScoreProperty.getLevels().size());
	}

	@Test
	public void testDetermineActivityPropertyLevelsException()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = getInitializedCMS360ComponentModel();
		//		Integer.valueOf(1), Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(150);
		classUnderTestActivity.createProperties();
		((DataContainerPropertyIntegerMock) classUnderTestActivity.getActivityScoreProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTestActivity.determineActivityPropertyLevels(cmsComponentModel);

		assertEquals("There should be one message in the container.", 1, classUnderTestActivity.getMessageList().size());

		((DataContainerPropertyIntegerMock) classUnderTestActivity.getActivityScoreProperty()).throwDataContainerPropertyLevelException = false;
	}

	@Test
	public void testGetActivityPropertylevelBoundaryPairs()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = getInitializedCMS360ComponentModel();
		//		Integer.valueOf(1), Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(150);
		classUnderTestActivity.createProperties();

		List<Comparable> levelBoundaryPairs = null;
		try
		{
			levelBoundaryPairs = classUnderTestActivity.getActivityPropertylevelBoundaryPairs(cms360ComponentModel);

			assertEquals("Level 1 low should be: 1", 1, ((Integer) levelBoundaryPairs.get(0)).intValue());
			assertEquals("Level 1 high should be: 50", 50, ((Integer) levelBoundaryPairs.get(1)).intValue());
			assertEquals("Level 2 low should be: 51", 51, ((Integer) levelBoundaryPairs.get(2)).intValue());
			assertEquals("Level 2 high should be: 100", 100, ((Integer) levelBoundaryPairs.get(3)).intValue());
			assertEquals("Level 3 low should be: 101", 101, ((Integer) levelBoundaryPairs.get(4)).intValue());
			assertEquals("Level 3 high should be: 150", 150, ((Integer) levelBoundaryPairs.get(5)).intValue());
			assertEquals("Level 4 low should be: 151", 151, ((Integer) levelBoundaryPairs.get(6)).intValue());
			assertEquals("Level 4 high should be: null", null, levelBoundaryPairs.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}

		final AbstractCMSComponentModel cms360ComponentModelEmpty = new CMSISCEPurchaseHistoryComponentModel();
		try
		{
			levelBoundaryPairs = classUnderTestActivity.getActivityPropertylevelBoundaryPairs(cms360ComponentModelEmpty);
			assertEquals("No borders should be contained: ", 0, levelBoundaryPairs.size());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown ", true);
		}
	}

	@Test
	public void testDetermineSentimentPropertyLevels()
	{
		classUnderTestSentiment.createProperties();
		classUnderTestSentiment.determineSentimentPropertyLevels();
		assertEquals("Sentiment score should have 5 levels", 5, classUnderTestSentiment.sentimentScoreProperty.getLevels().size());
	}

	@Test
	public void testDetermineSentimentPropertyLevelsException()
	{
		classUnderTestSentiment.createProperties();
		((DataContainerPropertyIntegerMock) classUnderTestSentiment.getSentimentScoreProperty()).throwDataContainerPropertyLevelException = true;
		classUnderTest.clearMessages();

		classUnderTestSentiment.determineSentimentPropertyLevels();

		assertEquals("There should be one message in the container.", 1, classUnderTestSentiment.getMessageList().size());
		((DataContainerPropertyIntegerMock) classUnderTestSentiment.getSentimentScoreProperty()).throwDataContainerPropertyLevelException = false;

	}

	@Test
	public void testGetSentimentPropertylevelBoundaryPairs()
	{
		classUnderTestSentiment.createProperties();

		final List<Comparable> levelBoundaryPairs = classUnderTestSentiment.getSentimentPropertylevelBoundaryPairs();

		assertEquals("Level 1 low should be: ", 1, ((Integer) levelBoundaryPairs.get(0)).intValue());
		assertEquals("Level 1 high should be: ", 1, ((Integer) levelBoundaryPairs.get(1)).intValue());
		assertEquals("Level 2 low should be: ", 2, ((Integer) levelBoundaryPairs.get(2)).intValue());
		assertEquals("Level 2 high should be: ", 2, ((Integer) levelBoundaryPairs.get(3)).intValue());
		assertEquals("Level 3 low should be: ", 3, ((Integer) levelBoundaryPairs.get(4)).intValue());
		assertEquals("Level 3 high should be: ", 3, ((Integer) levelBoundaryPairs.get(5)).intValue());
		assertEquals("Level 4 low should be: ", 4, ((Integer) levelBoundaryPairs.get(6)).intValue());
		assertEquals("Level 4 high should be: ", 4, ((Integer) levelBoundaryPairs.get(7)).intValue());
		assertEquals("Level 5 low should be: ", 5, ((Integer) levelBoundaryPairs.get(8)).intValue());
		assertEquals("Level 5 high should be: ", 5, ((Integer) levelBoundaryPairs.get(9)).intValue());
	}

	@Test
	public void testAdaptIntegerLevelUIValues()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		classUnderTest.adaptIntegerLevelUIValues(null);
		assertTrue("Method was successfully called", true);
		classUnderTest.adaptIntegerLevelUIValues(levels);
		assertTrue("Method was successfully called", true);

		// ascending
		final Integer level1Low = Integer.valueOf(1);
		final Integer level1High = Integer.valueOf(1000);
		final Integer level2Low = Integer.valueOf(1001);
		final Integer level2High = Integer.valueOf(2000);

		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels);

		assertEquals("Level low value should be 1.", level1Low, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be 1000.", level1High, levels.get(0).getAdaptedUIHighValue());
		assertEquals("Level low value should be 1000.", Integer.valueOf(level2Low.intValue() - 1), levels.get(1)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 2000.", level2High, levels.get(1).getAdaptedUIHighValue());


		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels);

		assertEquals("Level low value should be 1.", Integer.valueOf(level1Low.intValue() - 1), levels.get(0)
				.getAdaptedUILowValue());
		assertEquals("Level high value should be 1000.", level1High, levels.get(0).getAdaptedUIHighValue());

		levels.clear();
		level = new DataContainerPropertyLevelDefaultImpl("1", "ClassCastEx", "ClassCastEx", "", "");
		levels.add(level);

		classUnderTest.adaptIntegerLevelUIValues(levels);
		assertEquals("Level low value should be null.", null, levels.get(0).getAdaptedUILowValue());
		assertEquals("Level high value should be null.", null, levels.get(0).getAdaptedUIHighValue());
	}

	/**
	 * Return oData Entry containing the given score and score value.
	 *
	 * @param scoreId
	 * @param scoreValue
	 * @return oDataEntry containing given values
	 */
	private ODataEntry getOdataEntryWithScoreAndValue(final String scoreId, final Object scoreValue,
			final String scoreValueFormatted)
	{
		final ODataEntry oDataEntry = new ODataEntry()
		{

			@Override
			public Map<String, Object> getProperties()
			{
				final Map<String, Object> properties = new HashMap<>();
				properties.put(PROPERTY_SCORE_ID, scoreId);
				properties.put(PROPERTY_SCORE_VALUE, scoreValue);
				properties.put(PROPERTY_FORMATTED_SCORE_VALUE, scoreValueFormatted);

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

	/**
	 * Set data as required by extractOwnDataFromResulte-method.
	 */
	private void prepareTestDataForExtraction()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dataContainerContext.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);
	}

	/**
	 * Provides an initialized cms model component for 360 component
	 *
	 * @return
	 */
	private CMSISCECustomer360ComponentModel getInitializedCMS360ComponentModel()
	{
		final CMSISCECustomer360ComponentModel cms360ComponentModel = new CMSISCECustomer360ComponentModel();

		cms360ComponentModel.setCMSISCECustomer360ActivityScoreLevel01(Integer.valueOf(1));
		cms360ComponentModel.setCMSISCECustomer360ActivityScoreLevel02(Integer.valueOf(51));
		cms360ComponentModel.setCMSISCECustomer360ActivityScoreLevel03(Integer.valueOf(101));
		cms360ComponentModel.setCMSISCECustomer360ActivityScoreLevel04(Integer.valueOf(151));

		return cms360ComponentModel;
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.ObjectScoresDataContainerAge";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=EMailAddress eq '<value not traced>' and ObjectType eq 'CUAN_CONSUMER' and ScoreId eq 'CONTACT_AGE_SCORE'";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals("Traceable Filter should not show email",
				"EMailAddress eq '<value not traced>' and ObjectType eq 'CUAN_CONSUMER' and ScoreId eq 'CONTACT_AGE_SCORE'",
				classUnderTest.getTraceableFilter());
	}
}

/*****************************************************************************
 Class:        MarketingAttributesDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test for Default implementation class MarketingAttributesDataContainer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class MarketingAttributesDataContainerUnitTest
{
	@Resource(name = "marketingAttributesDataContainerUnderTest")
	private MarketingAttributesDataContainer classUnderTest;

	private HttpODataResultManipulator httpODataResultManipulator = null;
	private final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
	private static final LoggerMock loggerMock = new LoggerMock("");
	private CustomerData customerData;
	private static final String AGE_IN_YEARS = "AgeInYears";
	private static final Integer AGE_IN_YEARS_VALUE = Integer.valueOf(10);
	private static final String GENDER_DESCRIPTION = "GenderDescription";
	private static final String GENDER_DESCRIPTION_VALUE = "Male";
	private static final String MARITAL_STATUS_DESCRIPTION = "MaritalStatusDescription";
	private static final String MARITAL_STATUS_DESCRIPTION_VALUE = "Married";
	private static final String CUSTOMER_UID_VALUE = "uid1";

	private Map dcContextMap = null;

	@Before
	public void setUp()
	{
		loggerMock.clearAll();
		MarketingAttributesDataContainer.log = loggerMock;
		dcContextMap = dataContainerContext.getContextMap();
		customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dcContextMap.put(DataContainerContext.CUSTOMER_DATA, customerData);
		classUnderTest.setDataContainerContext(dataContainerContext);

		// Prepare input data
		httpODataResultManipulator = new HttpODataResultImpl();

		final List<ODataEntry> oDataEntryList = new ArrayList<>();
		final ODataEntry oDataEntry = new ODataEntry()
		{

			@Override
			public Map<String, Object> getProperties()
			{
				final Map<String, Object> properties = new HashMap<>();
				properties.put(AGE_IN_YEARS, AGE_IN_YEARS_VALUE);
				properties.put(GENDER_DESCRIPTION, GENDER_DESCRIPTION_VALUE);
				properties.put(MARITAL_STATUS_DESCRIPTION, MARITAL_STATUS_DESCRIPTION_VALUE);
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
		oDataEntryList.add(oDataEntry);
		httpODataResultManipulator.setEntities(oDataEntryList);
	}

	/**
	 * Tests extractOwnDatafromHttpOdataResult.
	 */
	@Test
	public void testMarketingAttributesDataContainer()
	{
		assertEquals("Service URI should be /sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV", "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV",
				classUnderTest.serviceURI);
		assertEquals("Result name prefix should be ContactPersons", "ContactPersons", classUnderTest.resultName);
		assertEquals("Container Name should be " + "com.sap.retail.isce.container.impl.MarketingAttributesDataContainer",
				"com.sap.retail.isce.container.impl.MarketingAttributesDataContainer", classUnderTest.containerName);
		assertEquals("Service End Point name should be ContactPersons", "ContactPersons", classUnderTest.serviceEndpointName);
		assertEquals("HTTP destination should be ISCEHybrisMarketingHTTPDestination", "ISCEHybrisMarketingHTTPDestination",
				classUnderTest.httpDestinationName);
	}

	/**
	 * Tests extractOwnDatafromHttpOdataResult.
	 */
	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		try
		{
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResultManipulator);
			assertEquals("GenderDescription should be" + GENDER_DESCRIPTION_VALUE, GENDER_DESCRIPTION_VALUE,
					classUnderTest.getGenderDescription());
			assertEquals("MaritalStatusDescription should be" + MARITAL_STATUS_DESCRIPTION_VALUE, MARITAL_STATUS_DESCRIPTION_VALUE,
					classUnderTest.getMaritalStatusDescription());
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
		try
		{
			classUnderTest.extractOwnDataFromResult(null);
			assertEquals("GenderDescription should be" + null, null, classUnderTest.getGenderDescription());
			assertEquals("MaritalStatusDescription should be" + null, null, classUnderTest.getMaritalStatusDescription());

			final List<ODataEntry> oDataEntryList2 = new ArrayList<>();
			oDataEntryList2.add(null);
			httpODataResultManipulator.setEntities(oDataEntryList2);
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResultManipulator);
			assertEquals("GenderDescription should be" + null, null, classUnderTest.getGenderDescription());
			assertEquals("MaritalStatusDescription should be" + null, null, classUnderTest.getMaritalStatusDescription());
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
		try
		{

			final List<ODataEntry> oDataEntryList3 = new ArrayList<>();
			final ODataEntry oDataEntryNull = new ODataEntry()
			{

				@Override
				public Map<String, Object> getProperties()
				{
					return null;
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
			oDataEntryList3.add(oDataEntryNull);
			httpODataResultManipulator.setEntities(oDataEntryList3);
			classUnderTest.extractOwnDataFromResult((HttpODataResult) httpODataResultManipulator);
			assertFalse("Test method execution should have thrown an exception", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Test method execution went well", true);
		}
	}

	/**
	 * Tests getContainerName.
	 */
	@Test
	public void testGetContainerName()
	{
		assertEquals("Container Name should be " + "com.sap.retail.isce.container.impl.MarketingAttributesDataContainer",
				"com.sap.retail.isce.container.impl.MarketingAttributesDataContainer", classUnderTest.getContainerName());
	}

	/**
	 * Tests testGetContainerContextParamName.
	 */
	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("Context Param Name should be found.", MarketingAttributesDataContainer.DATA_CONTAINER_CONTEXT_PARAM_NAME,
				classUnderTest.getContainerContextParamName());
	}

	/**
	 * Tests getFilter.
	 */
	@Test
	public void testGetFilter()
	{
		assertEquals("Filter should be " + "EMailAddress eq '" + CUSTOMER_UID_VALUE + "'", "EMailAddress eq '" + CUSTOMER_UID_VALUE
				+ "'", classUnderTest.getFilter());
	}

	/**
	 * Tests testGetCustomerData.
	 */
	@Test
	public void testGetCustomerData()
	{
		assertEquals("CustomerData should have been found.", this.customerData, classUnderTest.getCustomerData());
		try
		{
			final DataContainerContext dataContainerContextLocal = new DataContainerContextDefaultImpl();
			classUnderTest.setDataContainerContext(dataContainerContextLocal);
			classUnderTest.getCustomerData();
			assertFalse("exception should have been thrown", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("exception was thrown", true);
		}
	}

	/**
	 * Tests testGetSelect.
	 */
	@Test
	public void testGetSelect()
	{
		assertEquals("Select string be found.", "GenderDescription,MaritalStatusDescription,DateofBirth",
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
		assertEquals("Service End Point name should be ContactPersons", "ContactPersons", classUnderTest.getServiceEndpointName());
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
		classUnderTest.genderDescription = "Male";
		classUnderTest.maritalStatusDescription = "Married";
		classUnderTest.setDataInErrorState();
		assertEquals("genderDescription should be null", classUnderTest.getGenderDescription(), null);
		assertEquals("maritalStatusDescription should be null", classUnderTest.getMaritalStatusDescription(), null);
	}

	/**
	 * Tests setDataToNull.
	 */
	@Test
	public void testSetDataToNull()
	{
		classUnderTest.genderDescription = "Male";
		classUnderTest.maritalStatusDescription = "Married";
		classUnderTest.setDataToNull();
		assertEquals("genderDescription should be null", classUnderTest.getGenderDescription(), null);
		assertEquals("maritalStatusDescription should be null", classUnderTest.getMaritalStatusDescription(), null);
	}



	/**
	 * Tests setDataContainerContext.
	 */
	@Test
	public void testSetDataContainerContext()
	{
		// Setter done in unit test class Constructor
		assertEquals("data container context should be equals", dcContextMap, classUnderTest.dataContainerContext.getContextMap());
	}

	/**
	 * Tests encodeHTML.
	 */
	@Test
	public void testEncodeHTML()
	{
		classUnderTest.genderDescription = "<Male>";
		classUnderTest.maritalStatusDescription = "<script>alert('XSS Attack Fake');</script> Status Description";
		classUnderTest.encodeHTML();
		assertEquals("data container context should be equals", "&lt;Male&gt;", classUnderTest.genderDescription);
		assertEquals(
				"data container context should be equals",
				"&lt;script&gt;alert&#x28;&#x27;XSS&#x20;Attack&#x20;Fake&#x27;&#x29;&#x3b;&lt;&#x2f;script&gt;&#x20;Status&#x20;Description",
				classUnderTest.maritalStatusDescription);
	}

	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerName()
	{
		assertEquals("getLocalizedContainerName - localized container name must be instorecs.customer360.profile",
				"instorecs.customer360.profile", classUnderTest.getLocalizedContainerName());

	}

	/**
	 * Tests isCurrentDateEqualToBirthDate.
	 */
	@Test
	public void testIsCurrentDateEqualToBirthDateNullDate()
	{
		assertFalse("True was returned though given date was NULL", classUnderTest.isCurrentDateEqualToBirthDate(null)
				.booleanValue());
	}

	/**
	 * Tests isCurrentDateEqualToBirthDate.
	 */
	@Test
	public void testIsCurrentDateEqualToBirthDateEqualDates()
	{
		final GregorianCalendar today = (GregorianCalendar) Calendar.getInstance();

		assertTrue("False was returned though given date is current date", classUnderTest.isCurrentDateEqualToBirthDate(today)
				.booleanValue());
	}

	/**
	 * Tests isCurrentDateEqualToBirthDate.
	 */
	@Test
	public void testIsCurrentDateEqualToBirthDateUnequalDates()
	{
		final GregorianCalendar todayMinusOneMonth = (GregorianCalendar) Calendar.getInstance();

		todayMinusOneMonth.add(Calendar.MONTH, -1);

		assertFalse("True was returned though given date is not current date",
				classUnderTest.isCurrentDateEqualToBirthDate(todayMinusOneMonth).booleanValue());
	}

	/**
	 * Test getIsBirthDay
	 */
	@Test
	public void testGetIsBirthDay()
	{
		assertFalse(classUnderTest.getIsBirthDay().booleanValue());
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.MarketingAttributesDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=EMailAddress eq '<value not traced>'";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

}

/*****************************************************************************
 Class:        NewsletterSubscriptionDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

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
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit test class for NewsletterSubscriptionDataContainer
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class NewsletterSubscriptionDataContainerUnitTest
{

	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "NewsletterSubscriptionDataContainerUnderTest")
	private NewsletterSubscriptionDataContainer classUnderTest;

	private final DataContainerContext dcContextMap = new DataContainerContextDefaultImpl();

	LoggerMock loggerMock = new LoggerMock("");

	@SuppressWarnings("static-access")
	@Before
	public void setUp()
	{
		this.classUnderTest.setDataContainerContext(dcContextMap);
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dcContextMap.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
		loggerMock.clearAll();
		classUnderTest.log = loggerMock;
	}

	@Test
	public void testGetContainerContextParamName()
	{
		final String expected = "NewsletterSubscriptionDataContainer";

		assertEquals("getContainerContextParamName - container context param name must be " + expected, expected,
				this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testExtractOwnDatafromHttpOdataResult()
	{
		HttpODataResultImpl httpODataResult = null;

		// Null response
		classUnderTest.extractOwnDataFromResult(httpODataResult);
		assertEquals("extractOwnDatafromHttpOdataResult with null response - NewsletterSubscripted must be null", null,
				this.classUnderTest.getNewsletterSubscripted());

		// Empty response status not ok
		httpODataResult = new HttpODataResultImpl();
		httpODataResult.setStatusCode(HttpODataResult.StatusCode.ERROR);
		httpODataResult.setEntities(new ArrayList<ODataEntry>());

		classUnderTest.extractOwnDataFromResult(httpODataResult);

		assertEquals(
				"extractOwnDatafromHttpOdataResult with empty response but status not ok - NewsletterSubscripted must be null", null,
				this.classUnderTest.getNewsletterSubscripted());

		// Empty response status  ok
		httpODataResult = new HttpODataResultImpl();
		httpODataResult.setStatusCode(HttpODataResult.StatusCode.OK);
		httpODataResult.setEntities(new ArrayList<ODataEntry>());

		classUnderTest.extractOwnDataFromResult(httpODataResult);

		assertEquals(
				"extractOwnDatafromHttpOdataResult with with empty response and status ok - NewsletterSubscripted must be Boolean.FALSE",
				Boolean.FALSE, this.classUnderTest.getNewsletterSubscripted());

		//single entry unknown interaction type

		Map<String, Object> data = new HashMap();
		data.put(InteractionsDataContainerBase.PROPERTY_INTERACTION_TYPE_CODE, "IA_UNKNOWN");

		List<ODataEntry> oDataEntries = new ArrayList<ODataEntry>();
		oDataEntries.add(new ODataEntryImpl(data, null, null, null));

		httpODataResult.setEntities(oDataEntries);

		classUnderTest.extractOwnDataFromResult(httpODataResult);

		assertEquals(
				"extractOwnDatafromHttpOdataResult with with unknown interaction type response - NewsletterSubscripted must be null",
				null, this.classUnderTest.getNewsletterSubscripted());

		//single entry un-subscripted

		data = new HashMap();
		data.put(InteractionsDataContainerBase.PROPERTY_INTERACTION_TYPE_CODE,
				NewsletterSubscriptionDataContainer.INTERATIONTYPE_NESLETTER_UN_SUBSCRIPTED);

		oDataEntries = new ArrayList<ODataEntry>();
		oDataEntries.add(new ODataEntryImpl(data, null, null, null));

		httpODataResult.setEntities(oDataEntries);

		classUnderTest.extractOwnDataFromResult(httpODataResult);

		assertEquals(
				"extractOwnDatafromHttpOdataResult with with un-subscription response - NewsletterSubscripted must be Boolean.FALSE",
				Boolean.FALSE, this.classUnderTest.getNewsletterSubscripted());

		//single entry subscripted

		data = new HashMap();
		data.put(InteractionsDataContainerBase.PROPERTY_INTERACTION_TYPE_CODE,
				NewsletterSubscriptionDataContainer.INTERATIONTYPE_NESLETTER_SUBSCRIPTED);

		oDataEntries = new ArrayList<ODataEntry>();
		oDataEntries.add(new ODataEntryImpl(data, null, null, null));

		httpODataResult.setEntities(oDataEntries);

		classUnderTest.extractOwnDataFromResult(httpODataResult);

		assertEquals(
				"extractOwnDatafromHttpOdataResult with with subscription response - NewsletterSubscripted must be Boolean.TRUE",
				Boolean.TRUE, this.classUnderTest.getNewsletterSubscripted());
	}

	@Test
	public void testSetDataInErrorState()
	{
		this.classUnderTest.setNewsletterSubscripted(Boolean.FALSE);

		this.classUnderTest.setDataInErrorState();

		assertEquals("setDataInErrorState - NewsletterSubscripted must be null", null,
				this.classUnderTest.getNewsletterSubscripted());
	}

	@Test
	public void testGetFilter()
	{
		final String expected = "ContactId eq 'uid1' and ContactIdOrigin eq 'EMAIL' and (InteractionTypeCode eq 'NEWSLETTER_SUBSCR' or InteractionTypeCode eq 'NEWSLETTER_UNSUBSCR')";

		assertEquals("getFilter - must be " + expected, expected, this.classUnderTest.getFilter());
	}

	@Test
	public void testGetTop()
	{
		assertEquals("getTop - Top must be 1", Integer.valueOf(1), this.classUnderTest.getTop());
	}

	@Test
	public void testGetOrderBy()
	{
		final String expected = "Timestamp desc";

		assertEquals("getOrderBy - OrderBy must be " + expected, expected, this.classUnderTest.getOrderBy());
	}

	@Test
	public void testGetSelect()
	{
		final String expected = "InteractionTypeCode";

		assertEquals("getSelect - select must be " + expected, expected, this.classUnderTest.getSelect());
	}

	@Test
	public void testEncodeHTML()
	{
		classUnderTest.encodeHTML();
		assertTrue("encodeHTML not called", true);
	}

	@Test
	public void testTraceInformation()
	{
		loggerMock.setDebug(true);
		String expected = "getContainerName()=com.sap.retail.isce.container.impl.NewsletterSubscriptionDataContainer";
		classUnderTest.traceInformation();
		assertEquals("TraceInformation (0) - must be" + expected, expected, loggerMock.getDebug().get(0));
		expected = "getFilter()=ContactId eq '<value not traced>' and ContactIdOrigin eq 'EMAIL' and (InteractionTypeCode eq 'NEWSLETTER_SUBSCR' or InteractionTypeCode eq 'NEWSLETTER_UNSUBSCR')";
		assertEquals("TraceInformation (6) - must be" + expected, expected, loggerMock.getDebug().get(6));
	}

	@Test
	public void testGetTraceableFilter()
	{
		assertEquals(
				"Traceable Filter should not show email",
				"ContactId eq '<value not traced>' and ContactIdOrigin eq 'EMAIL' and (InteractionTypeCode eq 'NEWSLETTER_SUBSCR' or InteractionTypeCode eq 'NEWSLETTER_UNSUBSCR')",
				classUnderTest.getTraceableFilter());
	}
}

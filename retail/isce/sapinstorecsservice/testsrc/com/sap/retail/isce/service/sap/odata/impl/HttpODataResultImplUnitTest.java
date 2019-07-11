/*****************************************************************************
 Class:        HttpODataResultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.edm.provider.EdmImplProv;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


@UnitTest
public class HttpODataResultImplUnitTest
{
	HttpODataResultManipulator classUnderTestManipulator;
	HttpODataResult classUnderTestGet;
	HttpODataResultImpl classUnderTest;

	@Before
	public void setUp()
	{
		classUnderTest = new HttpODataResultImpl();
		classUnderTestGet = classUnderTest;
		classUnderTestManipulator = (HttpODataResultManipulator) classUnderTestGet;
	}

	@Test
	public void testCount()
	{
		final Integer count = Integer.valueOf(10);
		classUnderTestManipulator.setCount(count);
		assertEquals("count should be 10", count, classUnderTestGet.getCount());
	}

	@Test
	public void testResponseBody()
	{
		final String body = "Body { : .}";
		classUnderTestManipulator.setResponseBody(body);
		assertEquals("body should be Body { : .}", body, classUnderTestGet.getResponseBody());
	}

	@Test
	public void testStatusCode()
	{
		StatusCode statusCode = StatusCode.OK;
		classUnderTestManipulator.setStatusCode(statusCode);
		assertEquals("StatusCode should be OK", statusCode, classUnderTestGet.getStatusCode());

		statusCode = StatusCode.ERROR;
		classUnderTestManipulator.setStatusCode(statusCode);
		assertEquals("StatusCode should be ERROR", statusCode, classUnderTestGet.getStatusCode());
	}

	@Test
	public void testHttpStatusCode()
	{
		final HttpStatusCodes httpStatusCodes = HttpStatusCodes.FORBIDDEN;
		classUnderTestManipulator.setHttpStatusCode(httpStatusCodes);
		assertEquals("HttpStatusCode should be FORBIDDEN", httpStatusCodes, classUnderTestGet.getHttpStatusCode());
	}

	@Test
	public void testEntities()
	{
		assertEquals("Should get empty entities list", 0, classUnderTestGet.getEntities().size());

		classUnderTest.setEntities(null);
		assertEquals("Should get empty entities list", 0, classUnderTestGet.getEntities().size());

		final ODataEntry entity = new ODataEntryImpl(null, null, null, null);
		classUnderTest.setEntity(entity);
		assertEquals("Should get entities", 1, classUnderTestGet.getEntities().size());

		final List<ODataEntry> entities = new ArrayList<>();
		classUnderTestManipulator.setEntities(entities);
		assertEquals("Should get empty entities list", 0, classUnderTestGet.getEntities().size());

		entities.add(entity);
		classUnderTestManipulator.setEntities(entities);
		assertEquals("Should get entities list with one entry", 1, classUnderTestGet.getEntities().size());
	}

	@Test
	public void testEntity()
	{
		assertEquals("Should get no entity", null, classUnderTestGet.getEntity());

		final List<ODataEntry> entities = new ArrayList<>();
		classUnderTestManipulator.setEntities(entities);
		assertEquals("Should get no entity", null, classUnderTestGet.getEntity());

		classUnderTestManipulator.setEntity(null);
		assertEquals("Should get no entity", null, classUnderTestGet.getEntity());

		final ODataEntry entity = new ODataEntryImpl(null, null, null, null);

		classUnderTestManipulator.setEntity(entity);
		assertEquals("Should get entity", entity, classUnderTestGet.getEntity());

		entities.add(entity);
		classUnderTestManipulator.setEntities(entities);
		assertEquals("Should get entity", entity, classUnderTestGet.getEntity());
	}

	@Test
	public void testHttpHeaders()
	{
		final Map<String, List<String>> httpHeaders = new HashMap<>();
		classUnderTestManipulator.setHttpHeaders(httpHeaders);
		assertEquals("Should get httpHeaders", httpHeaders, classUnderTestGet.getHttpHeaders());
	}

	@Test
	public void testMessages()
	{
		final List<BackendMessage> messages = new ArrayList<>();
		classUnderTestManipulator.setMessages(messages);
		assertEquals("Should get messages", messages, classUnderTestGet.getMessages());
	}

	@Test
	public void testMetaData()
	{
		final Edm edm = new EdmImplProv(null);
		classUnderTestManipulator.setMetaData(edm);
		assertEquals("Should get metaData", edm, classUnderTestGet.getMetaData());
	}

	@Test
	public void testMediaContent()
	{
		final byte[] content = "content".getBytes();
		classUnderTestManipulator.setMediaContent(content);
		assertEquals("Should get mediaContent", content, classUnderTestGet.getMediaContent());
	}
}

/*****************************************************************************
 Class:        HttpODataCommonStorageImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class HttpODataCommonStorageImplUnitTest
{

	private HttpODataCommonStorageImpl classUnderTest;
	private static final String DESTINATION_NAME = "DESTINATION_NAME";
	private static final String DESTINATION_ENTRY_KEY = "DestEntry";
	private Map<String, Object> destination;
	private Object entry;

	@Before
	public void setUp()
	{
		classUnderTest = new HttpODataCommonStorageImpl();
		destination = new HashMap<String, Object>();
	}

	@Test
	public void testPutGet()
	{
		classUnderTest.putDestination(DESTINATION_NAME, destination);
		Map<String, Object> cachedDestination = classUnderTest.getDestination(DESTINATION_NAME);
		assertNotNull("cachedDestination object shall not be null", cachedDestination);
		assertEquals("cachedDestination size should be 0", 0, cachedDestination.size());

		entry = new Object();
		destination.put(DESTINATION_ENTRY_KEY, entry);
		classUnderTest.putDestination(DESTINATION_NAME, destination);
		cachedDestination = classUnderTest.getDestination(DESTINATION_NAME);
		assertNotNull("cachedDestination object shall not be null", cachedDestination);
		assertEquals("cachedDestination size should be 1", 1, cachedDestination.size());
		assertTrue("cachedDestination key not found", cachedDestination.containsKey(DESTINATION_ENTRY_KEY));
		assertEquals("cachedDestination value not equal", entry, cachedDestination.get(DESTINATION_ENTRY_KEY));
	}

	@Test
	public void testPutRemove()
	{
		classUnderTest.putDestination(DESTINATION_NAME, destination);
		Map<String, Object> cachedDestination = classUnderTest.getDestination(DESTINATION_NAME);
		assertNotNull("cachedDestination object shall not be null", cachedDestination);
		assertEquals("cachedDestination size should be 0", 0, cachedDestination.size());

		entry = new Object();
		destination.put(DESTINATION_ENTRY_KEY, entry);
		classUnderTest.putDestination(DESTINATION_NAME, destination);
		cachedDestination = classUnderTest.getDestination(DESTINATION_NAME);
		assertNotNull("cachedDestination object shall not be null", cachedDestination);
		assertEquals("cachedDestination size should be 1", 1, cachedDestination.size());
		assertTrue("cachedDestination key not found", cachedDestination.containsKey(DESTINATION_ENTRY_KEY));
		assertEquals("cachedDestination value not equal", entry, cachedDestination.get(DESTINATION_ENTRY_KEY));

		classUnderTest.removeDestination(DESTINATION_NAME);
		cachedDestination = classUnderTest.getDestination(DESTINATION_NAME);
		assertNull("cachedDestination object shall be null", cachedDestination);
	}


}

/*****************************************************************************
 Class:        BackendMessageUtilImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;


/**
 * Unit test class for BackendMessageUtilImpl
 */
@UnitTest
public class BackendMessageUtilImplUnitTest
{

	private BackendMessageUtilImpl classUnderTest;

	@Before
	public void setUp()
	{
		classUnderTest = new BackendMessageUtilImpl();
	}

	@Test
	public void testHasErrorMessages()
	{
		// null
		boolean hasMessages = classUnderTest.hasErrorMessage(null);
		assertFalse("testHasErrorMessages - messagelist is null hasErrorMessages should be false", hasMessages);

		final List<BackendMessage> messages = new ArrayList();

		// empty list
		hasMessages = classUnderTest.hasErrorMessage(messages);
		assertFalse("testHasErrorMessages - empty messagelist hasErrorMessages should be false", hasMessages);

		// non empty list, but no error messages
		messages.add(new BackendMessageImpl());

		hasMessages = classUnderTest.hasErrorMessage(messages);
		assertFalse("testHasErrorMessages - non empty messagelist, but no error message hasErrorMessages should be false",
				hasMessages);

		// non empty list, with error messages
		messages.add(new BackendMessageImpl(Severity.ERROR, null, 0, null));

		hasMessages = classUnderTest.hasErrorMessage(messages);
		assertTrue("testHasErrorMessages - non empty messagelist, with error message hasErrorMessages should be true", hasMessages);
	}

	@Test
	public void testMergeMessageLists()
	{

		final List<BackendMessage> messageListOne = new ArrayList();
		final List<BackendMessage> messageListTwo = new ArrayList();

		// Both list empty
		List<BackendMessage> mergedMessages = classUnderTest.mergeMessageLists(messageListOne, messageListTwo);
		assertEquals("testMergeMessageLists - both messagelist are empty mergedMessages must be Collections.EMPTY_LIST",
				Collections.EMPTY_LIST, mergedMessages);

		// First list not empty empty
		messageListOne.add(new BackendMessageImpl());
		mergedMessages = classUnderTest.mergeMessageLists(messageListOne, messageListTwo);
		assertEquals("testMergeMessageLists - messageListOne not empty mergedMessages must be messageListOne", messageListOne,
				mergedMessages);

		// Second list not empty empty
		messageListOne.clear();
		messageListTwo.add(new BackendMessageImpl(Severity.WARNING, null, 0, null));
		mergedMessages = classUnderTest.mergeMessageLists(messageListOne, messageListTwo);
		assertEquals("testMergeMessageLists - messageListTwo not empty mergedMessages must be messageListTwo", messageListTwo,
				mergedMessages);

		// Both lists not empty empty
		messageListOne.add(new BackendMessageImpl(Severity.ERROR, null, 0, null));
		messageListTwo.add(new BackendMessageImpl(Severity.INFO, null, 0, null));
		mergedMessages = classUnderTest.mergeMessageLists(messageListOne, messageListTwo);
		assertEquals("testMergeMessageLists - both message lists are not empty mergedMessages size must be 3", 3,
				mergedMessages.size());

		assertEquals(
				"testMergeMessageLists - both message lists are not empty first message must be identical to first message of messageListOne",
				messageListOne.get(0), mergedMessages.get(0));
		assertEquals(
				"testMergeMessageLists - both message lists are not empty second message must be identical to first message of messageListTwo",
				messageListTwo.get(0), mergedMessages.get(1));
		assertEquals(
				"testMergeMessageLists - both message lists are not empty third message must be identical to second message of messageListTwo",
				messageListTwo.get(1), mergedMessages.get(2));

	}
}

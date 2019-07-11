/*****************************************************************************
 Class:        BackendMessageUtilImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Test;

import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.impl.BackendMessageImpl;


/**
 * Unit test class for BackendMessageImpl
 */
@UnitTest
public class BackendMessageImplUnitTest
{

	final static String theMessageClassOne = "MessageClassOne";
	final static String theMessageTextOne = "Message Text One";

	final static String theMessageClassTwo = "MessageClassTwo";
	final static String theMessageTextTwo = "Message Text Two";

	private BackendMessageImpl classUnderTest;

	@Test
	public void testDefaultConstructor()
	{
		classUnderTest = new BackendMessageImpl();

		assertEquals("testDefaultConstructor - severity must be null", null, classUnderTest.getSeverity());
		assertEquals("testDefaultConstructor - messageClass must be null", null, classUnderTest.getMessageClass());
		assertEquals("testDefaultConstructor - text must be null", null, classUnderTest.getText());
		assertEquals("testDefaultConstructor - number must be 0", 0, classUnderTest.getNumber());
	}

	@Test
	public void testParametrizedConstructor()
	{
		classUnderTest = new BackendMessageImpl(Severity.ERROR, theMessageClassOne, 123, theMessageTextOne);

		assertEquals("testParametrizedConstructor - severity must be Severity.ERROR", Severity.ERROR, classUnderTest.getSeverity());
		assertEquals("testParametrizedConstructor - messageClass must be MessageClassOne", theMessageClassOne,
				classUnderTest.getMessageClass());
		assertEquals("testParametrizedConstructor - text must be 'Message Text One'", theMessageTextOne, classUnderTest.getText());
		assertEquals("testParametrizedConstructor - number must be 123", 123, classUnderTest.getNumber());
	}


	@Test
	public void testToString()
	{
		classUnderTest = new BackendMessageImpl();

		String expected = "null (null/0) null";

		assertEquals("testToString - message to string must be " + expected, expected, classUnderTest.toString());

		classUnderTest = new BackendMessageImpl(Severity.ERROR, null, 456, theMessageTextOne);

		expected = "ERROR (null/456) Message Text One";

		assertEquals("testToString - message to string must be " + expected, expected, classUnderTest.toString());

		classUnderTest = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);

		expected = "INFO (MessageClassOne/456) Message Text One";

		assertEquals("testToString - message to string must be " + expected, expected, classUnderTest.toString());
	}

	@Test
	public void testEquals()
	{
		classUnderTest = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);
		BackendMessageImpl compareMessage = null;

		// compare to null
		assertFalse("testEquals - message compared to null must be false", classUnderTest.equals(compareMessage));

		// compare to different object
		assertFalse("testEquals - message compared to different object must be false", classUnderTest.equals("Test"));

		// compare to itself
		assertTrue("testEquals - message compared to itself must be true", classUnderTest.equals(classUnderTest));

		// compare differing severity
		compareMessage = new BackendMessageImpl(Severity.ERROR, theMessageClassOne, 456, theMessageTextOne);
		assertFalse("testEquals - message compared to message with differeing severity must be false",
				classUnderTest.equals(compareMessage));

		// compare differing MessageClass
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassTwo, 456, theMessageTextOne);
		assertFalse("testEquals - message compared to message with differeing MessageClass must be false",
				classUnderTest.equals(compareMessage));

		// compare differing number
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 789, theMessageTextOne);
		assertFalse("testEquals - message compared to message with differeing number must be false",
				classUnderTest.equals(compareMessage));

		// compare differing text
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextTwo);
		assertTrue("testEquals - message compared to message with differeing text must be true",
				classUnderTest.equals(compareMessage));

		// compare to identical message
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);
		assertTrue("testEquals - message compared to identical message must be true", classUnderTest.equals(compareMessage));

		// compare with empty message class
		classUnderTest = new BackendMessageImpl(Severity.INFO, null, 456, theMessageTextOne);
		assertFalse("testEquals - message withe empty message class compared to message must be false",
				classUnderTest.equals(compareMessage));
	}

	@Test
	public void testHashCode()
	{
		classUnderTest = new BackendMessageImpl();
		BackendMessageImpl compareMessage = new BackendMessageImpl();

		// Code for empty message
		assertEquals("testHashCode - hashCode for empty message must be 29791", 29791, classUnderTest.hashCode());

		// two empty messages
		assertEquals("testHashCode - hashCode for two empty messages must be identical", compareMessage.hashCode(),
				classUnderTest.hashCode());

		// two identical messages
		classUnderTest = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);
		assertEquals("testHashCode - hashCode for identical messages must be identical", compareMessage.hashCode(),
				classUnderTest.hashCode());

		// two non identical messages
		classUnderTest = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 455, theMessageTextOne);
		compareMessage = new BackendMessageImpl(Severity.INFO, theMessageClassOne, 456, theMessageTextOne);
		assertTrue("testHashCode - hashCode for non identical messages must be different",
				compareMessage.hashCode() != classUnderTest.hashCode());
	}

	@Test
	public void testSeverityFromBapi()
	{
		assertEquals("", BackendMessage.Severity.ERROR, BackendMessage.Severity.fromBAPI('A'));
		assertEquals("", BackendMessage.Severity.ERROR, BackendMessage.Severity.fromBAPI('E'));
		assertEquals("", BackendMessage.Severity.WARNING, BackendMessage.Severity.fromBAPI('W'));
		assertEquals("", BackendMessage.Severity.INFO, BackendMessage.Severity.fromBAPI('S'));
		assertEquals("", BackendMessage.Severity.INFO, BackendMessage.Severity.fromBAPI('I'));
		try
		{
			BackendMessage.Severity.fromBAPI('X');
			assertFalse("Exception should have been thrown", true);
		}
		catch (final IllegalArgumentException e)
		{
			assertTrue("Expected exception was thrown", true);
		}
	}
}

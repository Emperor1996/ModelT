/*****************************************************************************
 Class:        DataContainerPropertyDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;


/**
 * Unit Test class for DataContainerPropertyDefaultImpl.
 *
 */
@UnitTest
public class DataContainerPropertyDefaultImplUnitTest extends UnitTestBase
{
	private boolean ThrowUnsupportedEncodingException;
	private boolean valueIsInLevelRange;
	private final static String TEST_STRING_4_ENCODING = "NiceText to be HTML encoded with some strange characters ! §$%&/()=+*#'<>|/*-+;:_,.-µ^°1234567890´`~+*";
	private final static String TEST_STRING_HTML_ENCODED = "NiceText&#x20;to&#x20;be&#x20;HTML&#x20;encoded&#x20;with&#x20;some&#x20;strange&#x20;characters&#x20;&#x21;&#x20;&#xa7;&#x24;&#x25;&amp;&#x2f;&#x28;&#x29;&#x3d;&#x2b;&#x2a;&#x23;&#x27;&lt;&gt;&#x7c;&#x2f;&#x2a;-&#x2b;&#x3b;&#x3a;_,.-&#xb5;&#x5e;&#xb0;1234567890&#xb4;&#x60;&#x7e;&#x2b;&#x2a;";


	private class DataContainerPropertyDefaultImplTest extends DataContainerPropertyDefaultImpl
	{
		@Override
		protected String encodeHTMLInner(final String input) throws UnsupportedEncodingException
		{
			if (ThrowUnsupportedEncodingException)
			{
				throw new UnsupportedEncodingException();
			}
			else
			{
				return super.encodeHTMLInner(input);
			}
		}

		@Override
		protected boolean isValueInLevelRange(final DataContainerPropertyLevel level, final Comparable value)
		{
			return valueIsInLevelRange;
		}

		@Override
		public void determineCorrespondingLevel() throws DataContainerPropertyLevelException
		{
			//
		}

		@Override
		public String determineUnit(final Object object)
		{
			return null;
		}

		@Override
		public List<Comparable> calculateLevelBoundaryPairs(final List<Comparable> levelBoundaries, final boolean ascending)
				throws DataContainerPropertyLevelException
		{
			return null;
		}
	}


	//
	// We test a derived class
	//

	private DataContainerPropertyDefaultImpl classUnderTest = null;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		// As the class to be tested is abstract, we test a derived class
		classUnderTest = new DataContainerPropertyDefaultImplTest();
		classUnderTest.correspondingLevel = null;
		classUnderTest.levels = null;
	}

	@Test
	public void testGetEmptyLevelList()
	{
		assertNotNull("Should get empty level list.", classUnderTest.getEmptyLevelList());
		assertEquals("Should get empty level list.", 0, classUnderTest.getEmptyLevelList().size());
	}

	@Test
	public void testGetLevels()
	{
		classUnderTest.levels = createTestLevels();
		classUnderTest.getLevels();
		assertEquals("Property slhould have 4 levels", 4, classUnderTest.getLevels().size());
	}

	@Test
	public void testGetCorrespondingLevel()
	{
		final DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", "1", "1000", "", "");

		classUnderTest.correspondingLevel = null;
		assertEquals("Corresponding level should be null.", null, classUnderTest.getCorrespondingLevel());

		classUnderTest.correspondingLevel = level;
		assertEquals("Corresponding level should be found.", level, classUnderTest.getCorrespondingLevel());
	}

	//	@Test
	//	public void testEncodeHTML()
	//	{
	//		final String encodedInput = TEST_STRING_HTML_ENCODED;
	//
	//		classUnderTest.levels = createLevels4SEncoding();
	//		classUnderTest.unitPlural = TEST_STRING_4_ENCODING;
	//		classUnderTest.unitSingular = TEST_STRING_4_ENCODING;
	//		classUnderTest.encodeHTML();
	//
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.levels.get(0).getLevelFlag());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.levels.get(0).getUnit());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.levels.get(0).getDescription());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput,
	//				classUnderTest.levels.get(0).getLowValue().toString());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput,
	//				classUnderTest.levels.get(0).getHighValue().toString());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput,
	//				classUnderTest.levels.get(0).getAdaptedUILowValue().toString());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput,
	//				classUnderTest.levels.get(0).getAdaptedUIHighValue().toString());
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.unitPlural);
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.unitSingular);
	//	}

	@Test
	public void testEncodeHTMLWithArgument()
	{
		//		assertEquals("The encoded input should be " + TEST_STRING_HTML_ENCODED, TEST_STRING_HTML_ENCODED,
		//				classUnderTest.encodeHTML(TEST_STRING_4_ENCODING));

		ThrowUnsupportedEncodingException = true;
		try
		{
			classUnderTest.encodeHTML("");
			assertFalse("Exception should have been thrown", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Expected DataContainerRuntimeException was thrown", true);
		}
		finally
		{
			ThrowUnsupportedEncodingException = false;
		}
		// XSSEncoder is a final class, therefore we cannot mock it to test the catch block
	}

	@Test
	public void testDetermineCorrespondingLevel()
	{
		try
		{
			classUnderTest.levels = null;
			assertNull("No level should be found as level list is null.",
					classUnderTest.determineCorrespondingLevel(Integer.valueOf(1)));

			final List<DataContainerPropertyLevel> levelList = new ArrayList<>();
			classUnderTest.levels = levelList;
			assertNull("No level should be found as level list is empty.",
					classUnderTest.determineCorrespondingLevel(Integer.valueOf(1)));

			classUnderTest.levels = null;
			assertNull("No level should be found as the value is null.", classUnderTest.determineCorrespondingLevel(null));

			classUnderTest.levels = createTestLevels();
			valueIsInLevelRange = true;
			assertEquals("First level should be found.", classUnderTest.levels.get(0),
					classUnderTest.determineCorrespondingLevel(Integer.valueOf(1)));

			valueIsInLevelRange = false;
			assertEquals("No level should be found.", null, classUnderTest.determineCorrespondingLevel(Integer.valueOf(-100)));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown", true);
		}
	}

	@Test
	public void testIsCorrespondingLevel()
	{
		try
		{
			Integer levelLow = null;
			Integer levelHigh = null;
			DataContainerPropertyLevel level = null;
			Integer value = null;

			assertFalse("There should be no corresponding level", classUnderTest.isCorrespondingLevel(null, null));
			assertFalse("There should be no corresponding level", classUnderTest.isCorrespondingLevel(null, Integer.valueOf(1)));

			levelLow = Integer.valueOf(3001);
			level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
			assertFalse("There should be no corresponding level", classUnderTest.isCorrespondingLevel(level, null));

			levelLow = null;
			value = Integer.valueOf(3001);
			level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
			assertTrue("There should be a corresponding level", classUnderTest.isCorrespondingLevel(level, value));

			levelHigh = Integer.valueOf(4001);
			level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
			assertFalse("There should be no corresponding level", classUnderTest.isCorrespondingLevel(level, value));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown", true);
		}
	}

	@Test
	public void testCheckDataTypes()
	{
		final DataContainerPropertyLevel level;
		final Class clazz = Integer.class;
		final Integer levelHigh = Integer.valueOf(3001);
		final Integer levelLow = Integer.valueOf(3001);
		final Integer value = Integer.valueOf(3001);

		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");

		try
		{
			classUnderTest.checkDataTypes(level, value, clazz);
			assertTrue("checkDataTypes run succesful.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("DataContainerPropertyLevelException should not have been thrown", true);
		}

		try
		{
			classUnderTest.checkDataTypes(level, null, clazz);
			assertFalse("DataContainerPropertyLevelException should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("DataContainerPropertyLevelException should have been thrown", true);
		}

		try
		{
			classUnderTest.checkDataTypes(null, value, clazz);
			assertFalse("DataContainerPropertyLevelException should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("DataContainerPropertyLevelException should have been thrown", true);
		}

		try
		{
			classUnderTest.checkDataTypes(level, value, BigDecimal.class);
			assertFalse("DataContainerPropertyLevelException should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("DataContainerPropertyLevelException should have been thrown", true);
		}
	}

	@Test
	public void testCreateLevels()
	{
		List<DataContainerPropertyLevel> levels;
		final Integer level1Low = Integer.valueOf(1);
		final Integer level1High = Integer.valueOf(10);
		List<Comparable> levelBoundaryPairs = null;

		try
		{
			levels = classUnderTest.createLevels(levelBoundaryPairs);
			assertEquals("Number of levels to be created: 0", 0, levels.size());

			levelBoundaryPairs = new ArrayList();
			levels = classUnderTest.createLevels(levelBoundaryPairs);
			assertEquals("Number of levels to be created: 0", 0, levels.size());

			levelBoundaryPairs = Arrays.asList(level1Low, level1High);
			levels = classUnderTest.createLevels(levelBoundaryPairs);
			assertEquals("Number of levels to be created: 1", 1, levels.size());
			assertEquals("Level low value should be: 1", level1Low, levels.get(0).getLowValue());
			assertEquals("Level low value should be: 1", level1High, levels.get(0).getHighValue());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("createLevels - Exception should not have been thrown.", false);
		}
	}

	private List<DataContainerPropertyLevel> createTestLevels()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		final Integer level1Low = Integer.valueOf(1);
		final Integer level1High = Integer.valueOf(1000);
		final Integer level2Low = Integer.valueOf(1001);
		final Integer level2High = Integer.valueOf(2000);
		final Integer level3Low = Integer.valueOf(2001);
		final Integer level3High = Integer.valueOf(3000);
		final Integer level4Low = Integer.valueOf(3001);
		final Integer level4High = null;

		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("3", level3Low, level3High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("4", level4Low, level4High, "", "");
		levels.add(level);

		return levels;
	}

	private List<DataContainerPropertyLevel> createLevels4SEncoding()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levelList = new ArrayList<>();
		level = new DataContainerPropertyLevelDefaultImpl(TEST_STRING_4_ENCODING, TEST_STRING_4_ENCODING, TEST_STRING_4_ENCODING,
				TEST_STRING_4_ENCODING, TEST_STRING_4_ENCODING);
		level.setAdaptedUIHighValue(TEST_STRING_4_ENCODING);
		level.setAdaptedUILowValue(TEST_STRING_4_ENCODING);
		levelList.add(level);
		return levelList;
	}
}

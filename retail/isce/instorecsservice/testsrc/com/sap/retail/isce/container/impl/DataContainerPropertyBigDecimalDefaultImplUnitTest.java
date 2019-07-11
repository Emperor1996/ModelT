/*****************************************************************************
 Class:        DataContainerPropertyBigDecimalDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


/**
 * Unit Test class for DataContainerPropertyBigDecimalDefaultImpl.
 *
 */
public class DataContainerPropertyBigDecimalDefaultImplUnitTest extends UnitTestBase
{

	private final static String UNIT_SINGULAR = "unitSingular";
	private final static String UNIT_PLURAL = "unitPlural";
	private final static BigDecimal VALUE1 = BigDecimal.ONE;

	private DataContainerPropertyDefaultImpl classUnderTest = null;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerPropertyBigDecimalDefaultImpl(VALUE1, UNIT_SINGULAR, UNIT_PLURAL);
	}

	@Test
	public void testSetGetValue()
	{
		final BigDecimal value = BigDecimal.valueOf(1);

		classUnderTest.value = null;
		assertNull(classUnderTest.getValue());
		classUnderTest.setValue(value);
		assertEquals(value, classUnderTest.getValue());
	}

	@Test
	public void testDetermineUnit()
	{
		try
		{
			assertEquals("Singular unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(BigDecimal.valueOf(-1)));
			assertEquals("Singular unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(BigDecimal.valueOf(0)));
			assertEquals("Plural unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(BigDecimal.valueOf(1)));
			assertEquals("Plural unit should be returned", UNIT_PLURAL, classUnderTest.determineUnit(null));
			assertEquals("Plural unit should be returned", UNIT_PLURAL, classUnderTest.determineUnit(BigDecimal.valueOf(2)));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("determineUnit - Exception should not have been thrown.", false);
		}

		try
		{
			classUnderTest.determineUnit("ClassCastException");
			assertFalse("determineUnit - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("determineUnit - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testCalculatelevelBoundaryPairs()
	{
		// levels 1, 10 - ASC
		List<Comparable> boundaries = Arrays.asList(BigDecimal.valueOf(1), BigDecimal.valueOf(10));
		List<Comparable> resBoundaries;
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculatelevelBoundaryPairs - 2 levels (1,10) - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 1", BigDecimal.valueOf(1), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 9", BigDecimal.valueOf(9), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 10", BigDecimal.valueOf(10), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - null", null, resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e6)
		{
			assertFalse("calculatelevelBoundaryPairs - 2 levels (1, 10) - no exception should have been thrown", true);
		}

		// levels 10, 1 - ASC - Not properly ascending
		boundaries = Arrays.asList(BigDecimal.valueOf(10), BigDecimal.valueOf(1));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertFalse("calculatelevelBoundaryPairs - 2 levels (10, 1) - an exception should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e4)
		{
			assertTrue("calculatelevelBoundaryPairs - 2 levels (10, 1) - an exception was thrown", true);
		}

		// levels 2, 15, 28, 33 - ASC
		boundaries = Arrays.asList(BigDecimal.valueOf(2), BigDecimal.valueOf(15), BigDecimal.valueOf(28), BigDecimal.valueOf(33));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculatelevelBoundaryPairs - 4 levels (2, 15, 28, 33) - result list must contain 8 entries", 8,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 2", BigDecimal.valueOf(2), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 14", BigDecimal.valueOf(14), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 15", BigDecimal.valueOf(15), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - 27", BigDecimal.valueOf(27), resBoundaries.get(3));
			assertEquals("calculatelevelBoundaryPairs - 5th entry - 28", BigDecimal.valueOf(28), resBoundaries.get(4));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - 32", BigDecimal.valueOf(32), resBoundaries.get(5));
			assertEquals("calculatelevelBoundaryPairs - 5th entry - 33", BigDecimal.valueOf(33), resBoundaries.get(6));
			assertEquals("calculatelevelBoundaryPairs - 8th entry - null", null, resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e3)
		{
			assertFalse("calculatelevelBoundaryPairs - 4 levels (2, 15, 28, 33) - no exception should have been thrown", true);
		}

		// levels 1, 10 - ASC
		boundaries = Arrays.asList(null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculatelevelBoundaryPairs - 2 levels (null, null) - result list must contain 0 entries", 0,
					resBoundaries.size());
		}
		catch (final DataContainerPropertyLevelException e6)
		{
			assertFalse("calculatelevelBoundaryPairs - 2 levels (null, null) - no exception should have been thrown", true);
		}

		// levels 2, 15, null, 33 - ASC
		boundaries = Arrays.asList(BigDecimal.valueOf(2), BigDecimal.valueOf(15), null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculatelevelBoundaryPairs - 4 levels (2, 15, null, null) - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 2", BigDecimal.valueOf(2), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 14", BigDecimal.valueOf(14), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 15", BigDecimal.valueOf(15), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - null", null, resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e3)
		{
			assertFalse("calculatelevelBoundaryPairs - 4 levels (2, 15, 28, 33) - no exception should have been thrown", true);
		}

		//////////////
		// Descending
		//////////////

		// levels 7, null, null - Desc
		boundaries = Arrays.asList(BigDecimal.valueOf(7), null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculatelevelBoundaryPairs - 3 levels (7, null, null) - Desc - result list must contain 2 entries", 2,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - null", null, resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 7", BigDecimal.valueOf(7), resBoundaries.get(1));

		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculatelevelBoundaryPairs - 2 levels (7, null, null) - Desc - no exception should have been thrown", true);
		}

		// levels 10, 1, null - Desc
		boundaries = Arrays.asList(BigDecimal.valueOf(10), BigDecimal.valueOf(1), null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculatelevelBoundaryPairs - 3 levels (10, 1, null) - Desc - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 2", BigDecimal.valueOf(2), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 10", BigDecimal.valueOf(10), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3nd entry - null", null, resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - 1", BigDecimal.valueOf(1), resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculatelevelBoundaryPairs - 2 levels (10, 1, null) - Desc - no exception should have been thrown", true);
		}

		// levels 1, 10, null - Not properly Desc
		boundaries = Arrays.asList(BigDecimal.valueOf(1), BigDecimal.valueOf(10), null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertFalse(
					"calculatelevelBoundaryPairs - 2 levels (1, 10, null) - Desc - Not properly descending exception should have been thrown",
					true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("calculatelevelBoundaryPairs - 2 levels (1, 10, null) - Desc - Not properly descending exception was thrown",
					true);
		}

		// levels MAX_INT, 180, 90, 7, null
		boundaries = Arrays.asList(BigDecimal.valueOf(Integer.MAX_VALUE), BigDecimal.valueOf(180), BigDecimal.valueOf(30),
				BigDecimal.valueOf(7));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals(
					"calculatelevelBoundaryPairs - 4 levels (Integer.MAX_VALUE, 180, 90, 7) - Desc - result list must contain 8 entries",
					8, resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 181", BigDecimal.valueOf(181), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - Integer.MAX_VALUE", BigDecimal.valueOf(Integer.MAX_VALUE),
					resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 31", BigDecimal.valueOf(31), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - 180", BigDecimal.valueOf(180), resBoundaries.get(3));
			assertEquals("calculatelevelBoundaryPairs - 5rd entry - 8", BigDecimal.valueOf(8), resBoundaries.get(4));
			assertEquals("calculatelevelBoundaryPairs - 6th entry - 30", BigDecimal.valueOf(30), resBoundaries.get(5));
			assertEquals("calculatelevelBoundaryPairs - 7rd entry - null", null, resBoundaries.get(6));
			assertEquals("calculatelevelBoundaryPairs - 8th entry - 7", BigDecimal.valueOf(7), resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse(
					"calculatelevelBoundaryPairs - 4 levels (Integer.MAX_VALUE, 180, 90, 7) - Desc - no exception should have been thrown",
					true);
		}

		// levels 180, 90, 30, 7
		boundaries = Arrays.asList(BigDecimal.valueOf(180), BigDecimal.valueOf(90), BigDecimal.valueOf(30), BigDecimal.valueOf(7));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculatelevelBoundaryPairs - 4 levels (180, 90, 30, 7) - Desc - result list must contain 8 entries", 8,
					resBoundaries.size());
			assertEquals("calculatelevelBoundaryPairs - 1st entry - 91", BigDecimal.valueOf(91), resBoundaries.get(0));
			assertEquals("calculatelevelBoundaryPairs - 2nd entry - 180", BigDecimal.valueOf(180), resBoundaries.get(1));
			assertEquals("calculatelevelBoundaryPairs - 3rd entry - 31", BigDecimal.valueOf(31), resBoundaries.get(2));
			assertEquals("calculatelevelBoundaryPairs - 4th entry - 90", BigDecimal.valueOf(90), resBoundaries.get(3));
			assertEquals("calculatelevelBoundaryPairs - 5rd entry - 8", BigDecimal.valueOf(8), resBoundaries.get(4));
			assertEquals("calculatelevelBoundaryPairs - 6th entry - 30", BigDecimal.valueOf(30), resBoundaries.get(5));
			assertEquals("calculatelevelBoundaryPairs - 7rd entry - null", null, resBoundaries.get(6));
			assertEquals("calculatelevelBoundaryPairs - 8th entry - 7", BigDecimal.valueOf(7), resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculatelevelBoundaryPairs - 4 levels (1180, 90, 30, 7) - Desc - no exception should have been thrown",
					true);
		}

		//
		// cast exception
		//
		try
		{
			boundaries = Arrays.asList("ClassCastEx");
			classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertFalse("calculatelevelBoundaryPairs - Exception should not have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("calculatelevelBoundaryPairs - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testCreateLevels()
	{
		List<DataContainerPropertyLevel> levels;
		final BigDecimal level1Low = BigDecimal.valueOf(1);
		final BigDecimal level1High = BigDecimal.valueOf(10);
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

			levelBoundaryPairs = Arrays.asList(null, null);
			levels = classUnderTest.createLevels(levelBoundaryPairs);
			assertEquals("Number of levels to be created: 1", 1, levels.size());
			assertEquals("Level low value should be: 1", null, levels.get(0).getLowValue());
			assertEquals("Level low value should be: 1", null, levels.get(0).getHighValue());
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("createLevels - Exception should not have been thrown.", false);
		}

		try
		{
			// not an even number of level borders
			levelBoundaryPairs = Arrays.asList(level1Low, level1High, level1Low);
			levels = classUnderTest.createLevels(levelBoundaryPairs);
			assertFalse("createLevels - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("createLevels - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testIsCorrespondingLevel()
	{
		try
		{
			BigDecimal value;

			BigDecimal levelLow = BigDecimal.valueOf(1000);
			BigDecimal levelHigh = BigDecimal.valueOf(2000);
			DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
			value = BigDecimal.valueOf(1000);
			assertFalse("No corresponding level should be found", classUnderTest.isCorrespondingLevel(null, null));
			assertFalse("No corresponding level should be found", classUnderTest.isCorrespondingLevel(level, null));
			assertFalse("No corresponding level should be found", classUnderTest.isCorrespondingLevel(null, value));
			assertTrue("A corresponding level should have been found", classUnderTest.isCorrespondingLevel(level, value));

			levelLow = null;
			levelHigh = null;
			level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
			value = BigDecimal.valueOf(1000);
			assertFalse("No corresponding level should be found", classUnderTest.isCorrespondingLevel(null, value));
			// rest of permutations are implicitly tested by testDetermineCorrespondingLevel
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("isCorrespondingLevel - Exception should not have been thrown.", false);
		}
	}

	@Test
	public void testIsValueInLevelRange()
	{
		BigDecimal value;

		BigDecimal levelLow = BigDecimal.valueOf(1000);
		BigDecimal levelHigh = BigDecimal.valueOf(2000);
		DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(1000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = BigDecimal.valueOf(0);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = BigDecimal.valueOf(3000);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));

		levelLow = BigDecimal.valueOf(1000);
		levelHigh = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(1000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = BigDecimal.valueOf(0);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));

		levelLow = null;
		levelHigh = BigDecimal.valueOf(2000);
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(2000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = BigDecimal.valueOf(3000);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
	}

	@Test
	public void testIsValueInClosedLevelRange()
	{
		BigDecimal value;
		BigDecimal levelLow = BigDecimal.valueOf(1000);
		BigDecimal levelHigh = BigDecimal.valueOf(2000);

		DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");

		value = BigDecimal.valueOf(1000);
		assertTrue("Value must be inside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = BigDecimal.valueOf(2000);
		assertTrue("Value must be inside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = BigDecimal.valueOf(999);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = BigDecimal.valueOf(2001);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = null;
		levelHigh = BigDecimal.valueOf(2000);
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(1000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = BigDecimal.valueOf(1000);
		levelHigh = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(2000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = BigDecimal.valueOf(2000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

	}

	@Test
	public void testDetermineCorrespondingLevel()
	{
		try
		{
			classUnderTest.correspondingLevel = null;
			classUnderTest.value = null;
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			final List<DataContainerPropertyLevel> levels = new ArrayList<>();
			classUnderTest.correspondingLevel = null;
			classUnderTest.levels = levels;
			classUnderTest.value = null;
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			// closed levels will be set
			classUnderTest.levels = this.createClosedLevels();

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = null;
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(0);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);


			// open levels will be set
			classUnderTest.levels = this.createOpenLevels(true, true, true);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 3", "3", classUnderTest.correspondingLevel.getLevelFlag());

			//
			classUnderTest.levels = this.createOpenLevels(true, false, false);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			//
			classUnderTest.levels = this.createOpenLevels(false, false, true);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			// full open level
			classUnderTest.levels = this.createFullOpenLevel();

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = BigDecimal.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("determineCorrespondingLevel - Exception should not have been thrown.", false);
		}

		try
		{
			classUnderTest.correspondingLevel = null;
			classUnderTest.value = "CastEx";
			classUnderTest.determineCorrespondingLevel();
			assertFalse("determineCorrespondingLevel - Exception should have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("determineCorrespondingLevel - Exception should have been thrown.", true);
		}
	}

	private List<DataContainerPropertyLevel> createClosedLevels()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		final BigDecimal level1Low = BigDecimal.valueOf(1);
		final BigDecimal level1High = BigDecimal.valueOf(1000);
		final BigDecimal level2Low = BigDecimal.valueOf(1001);
		final BigDecimal level2High = BigDecimal.valueOf(2000);

		level = new DataContainerPropertyLevelDefaultImpl("1", level1Low, level1High, "", "");
		levels.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", level2Low, level2High, "", "");
		levels.add(level);

		return levels;
	}

	private List<DataContainerPropertyLevel> createOpenLevels(final boolean left, final boolean closed, final boolean right)
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		final BigDecimal level1Low = null;
		final BigDecimal level1High = BigDecimal.valueOf(1000);
		final BigDecimal level2Low = BigDecimal.valueOf(1001);
		final BigDecimal level2High = BigDecimal.valueOf(2000);
		final BigDecimal level3Low = BigDecimal.valueOf(2001);
		final BigDecimal level3High = null;
		int i = 1;

		if (left)
		{
			level = new DataContainerPropertyLevelDefaultImpl("" + i, level1Low, level1High, "", "");
			levels.add(level);
			i++;
		}
		if (closed)
		{
			level = new DataContainerPropertyLevelDefaultImpl("" + i, level2Low, level2High, "", "");
			levels.add(level);
			i++;
		}
		if (right)
		{
			level = new DataContainerPropertyLevelDefaultImpl("" + i, level3Low, level3High, "", "");
			levels.add(level);
		}
		return levels;
	}

	private List<DataContainerPropertyLevel> createFullOpenLevel()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		level = new DataContainerPropertyLevelDefaultImpl("1", null, null, "", "");
		levels.add(level);
		return levels;
	}

}

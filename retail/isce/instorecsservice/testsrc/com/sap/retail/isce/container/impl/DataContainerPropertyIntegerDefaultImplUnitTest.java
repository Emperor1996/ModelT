/*****************************************************************************
 Class:        DataContainerPropertyIntegerDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


/**
 * Unit Test class for DataContainerPropertyIntegerDefaultImpl.
 *
 */
@UnitTest
public class DataContainerPropertyIntegerDefaultImplUnitTest extends UnitTestBase
{
	private final static String UNIT_SINGULAR = "unitSingular";
	private final static String UNIT_PLURAL = "unitPlural";
	private final static Integer VALUE1 = Integer.valueOf(1);
	private final static Integer VALUE2 = Integer.valueOf(20);

	private DataContainerPropertyIntegerDefaultImpl classUnderTest = null;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new DataContainerPropertyIntegerDefaultImpl(VALUE1, UNIT_SINGULAR, UNIT_PLURAL);
	}

	@Test
	public void testGetSetValue()
	{
		assertEquals(" Value should be: " + VALUE1.toString(), VALUE1, classUnderTest.getValue());
		classUnderTest.setValue(VALUE2);
		assertEquals(" Value should be: " + VALUE2.toString(), VALUE2, classUnderTest.getValue());
	}

	@Test
	public void testDetermineUnit()
	{
		try
		{
			assertEquals("Plural unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(Integer.valueOf(-1)));
			assertEquals("Singular unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(Integer.valueOf(0)));
			assertEquals("Singular unit should be returned", UNIT_SINGULAR, classUnderTest.determineUnit(Integer.valueOf(1)));
			assertEquals("Plural unit should be returned", UNIT_PLURAL, classUnderTest.determineUnit(Integer.valueOf(2)));
			assertEquals("Plural unit should be returned", UNIT_PLURAL, classUnderTest.determineUnit(null));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("determineUnit - Exception should not have been thrown.", false);
		}

		try
		{
			classUnderTest.determineUnit("ClassCastEx");
			assertFalse("determineUnit - Exception should not have been thrown.", true);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertTrue("determineUnit - Exception should have been thrown.", true);
		}
	}

	@Test
	public void testIsCorrespondingLevel()
	{
		DataContainerPropertyLevel level;
		classUnderTest.levels = this.createClosedLevels();
		final Integer value = Integer.valueOf(1);
		level = classUnderTest.levels.get(0);
		try
		{
			assertEquals("First level should be returned as corresponding level.", Boolean.TRUE,
					Boolean.valueOf(classUnderTest.isCorrespondingLevel(level, value)));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("Exception should not have been thrown", true);
		}
	}

	@Test
	public void testCalculateLevelBoundaryPairs()
	{
		// levels 1, 10 - ASC
		List<Comparable> boundaries = Arrays.asList(Integer.valueOf(1), Integer.valueOf(10));
		List<Comparable> resBoundaries;
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculateLevelBoundaryPairs - 2 levels (1,10) - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 1", Integer.valueOf(1), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 9", Integer.valueOf(9), resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3rd entry - 10", Integer.valueOf(10), resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - null", null, resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e6)
		{
			assertFalse("calculateLevelBoundaryPairs - 2 levels (1, 10) - no exception should have been thrown", true);
		}

		// levels 10, 1 - ASC - Not properly ascending
		boundaries = Arrays.asList(Integer.valueOf(10), Integer.valueOf(1));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertFalse("calculateLevelBoundaryPairs - 2 levels (10, 1) - an exception should have been thrown", true);
		}
		catch (final DataContainerPropertyLevelException e4)
		{
			assertTrue("calculateLevelBoundaryPairs - 2 levels (10, 1) - an exception was thrown", true);
		}

		// levels 2, 15, 28, 33 - ASC
		boundaries = Arrays.asList(Integer.valueOf(2), Integer.valueOf(15), Integer.valueOf(28), Integer.valueOf(33));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculateLevelBoundaryPairs - 4 levels (2, 15, 28, 33) - result list must contain 8 entries", 8,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 2", Integer.valueOf(2), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 14", Integer.valueOf(14), resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3rd entry - 15", Integer.valueOf(15), resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - 27", Integer.valueOf(27), resBoundaries.get(3));
			assertEquals("calculateLevelBoundaryPairs - 5th entry - 28", Integer.valueOf(28), resBoundaries.get(4));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - 32", Integer.valueOf(32), resBoundaries.get(5));
			assertEquals("calculateLevelBoundaryPairs - 5th entry - 33", Integer.valueOf(33), resBoundaries.get(6));
			assertEquals("calculateLevelBoundaryPairs - 8th entry - null", null, resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e3)
		{
			assertFalse("calculateLevelBoundaryPairs - 4 levels (2, 15, 28, 33) - no exception should have been thrown", true);
		}

		// levels 1, 10 - ASC
		boundaries = Arrays.asList(null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculateLevelBoundaryPairs - 2 levels (null, null) - result list must contain 0 entries", 0,
					resBoundaries.size());
		}
		catch (final DataContainerPropertyLevelException e6)
		{
			assertFalse("calculateLevelBoundaryPairs - 2 levels (null, null) - no exception should have been thrown", true);
		}

		// levels 2, 15, null, 33 - ASC
		boundaries = Arrays.asList(Integer.valueOf(2), Integer.valueOf(15), null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, true);
			assertEquals("calculateLevelBoundaryPairs - 4 levels (2, 15, null, null) - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 2", Integer.valueOf(2), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 14", Integer.valueOf(14), resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3rd entry - 15", Integer.valueOf(15), resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - null", null, resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e3)
		{
			assertFalse("calculateLevelBoundaryPairs - 4 levels (2, 15, 28, 33) - no exception should have been thrown", true);
		}

		//////////////
		// Descending
		//////////////

		// levels 7, null, null - Desc
		boundaries = Arrays.asList(Integer.valueOf(7), null, null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculateLevelBoundaryPairs - 3 levels (7, null, null) - Desc - result list must contain 2 entries", 2,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - null", null, resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 7", Integer.valueOf(7), resBoundaries.get(1));

		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculateLevelBoundaryPairs - 2 levels (7, null, null) - Desc - no exception should have been thrown", true);
		}

		// levels 10, 1, null - Desc
		boundaries = Arrays.asList(Integer.valueOf(10), Integer.valueOf(1), null);
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculateLevelBoundaryPairs - 3 levels (10, 1, null) - Desc - result list must contain 4 entries", 4,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 2", Integer.valueOf(2), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 10", Integer.valueOf(10), resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3nd entry - null", null, resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - 1", Integer.valueOf(1), resBoundaries.get(3));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculateLevelBoundaryPairs - 2 levels (10, 1, null) - Desc - no exception should have been thrown", true);
		}

		// levels MAX_INT, 180, 90, 7, MIN_INT
		boundaries = Arrays.asList(Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(180), Integer.valueOf(30),
				Integer.valueOf(7));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculateLevelBoundaryPairs - 4 levels (MAX_INT, 180, 90, 7) - Desc - result list must contain 8 entries",
					8, resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 181", Integer.valueOf(181), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - MAX Integer", Integer.valueOf(Integer.MAX_VALUE),
					resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3rd entry - 31", Integer.valueOf(31), resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - 180", Integer.valueOf(180), resBoundaries.get(3));
			assertEquals("calculateLevelBoundaryPairs - 5rd entry - 8", Integer.valueOf(8), resBoundaries.get(4));
			assertEquals("calculateLevelBoundaryPairs - 6th entry - 30", Integer.valueOf(30), resBoundaries.get(5));
			assertEquals("calculateLevelBoundaryPairs - 7rd entry - null", null, resBoundaries.get(6));
			assertEquals("calculateLevelBoundaryPairs - 8th entry - 7", Integer.valueOf(7), resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse(
					"calculateLevelBoundaryPairs - 4 levels (MAX_INT, 180, 90, 7) - Desc - no exception should have been thrown", true);
		}

		// levels 180, 90, 30, 7
		boundaries = Arrays.asList(Integer.valueOf(180), Integer.valueOf(90), Integer.valueOf(30), Integer.valueOf(7));
		try
		{
			resBoundaries = classUnderTest.calculateLevelBoundaryPairs(boundaries, false);
			assertEquals("calculateLevelBoundaryPairs - 4 levels (180, 90, 30, 7) - Desc - result list must contain 8 entries", 8,
					resBoundaries.size());
			assertEquals("calculateLevelBoundaryPairs - 1st entry - 91", Integer.valueOf(91), resBoundaries.get(0));
			assertEquals("calculateLevelBoundaryPairs - 2nd entry - 180", Integer.valueOf(180), resBoundaries.get(1));
			assertEquals("calculateLevelBoundaryPairs - 3rd entry - 31", Integer.valueOf(31), resBoundaries.get(2));
			assertEquals("calculateLevelBoundaryPairs - 4th entry - 90", Integer.valueOf(90), resBoundaries.get(3));
			assertEquals("calculateLevelBoundaryPairs - 5rd entry - 8", Integer.valueOf(8), resBoundaries.get(4));
			assertEquals("calculateLevelBoundaryPairs - 6th entry - 30", Integer.valueOf(30), resBoundaries.get(5));
			assertEquals("calculateLevelBoundaryPairs - 7rd entry - null", null, resBoundaries.get(6));
			assertEquals("calculateLevelBoundaryPairs - 8th entry - 7", Integer.valueOf(7), resBoundaries.get(7));
		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("calculateLevelBoundaryPairs - 4 levels (1180, 90, 30, 7) - Desc - no exception should have been thrown",
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
	public void testCheckBoundaryValidity()
	{
		final Integer boundaryMax = Integer.valueOf(Integer.MAX_VALUE);
		final Integer boundaryMaxMinusOne = Integer.valueOf(Integer.MAX_VALUE - 1);

		final Integer boundaryMin = Integer.valueOf(Integer.MIN_VALUE);
		final Integer boundaryMinPlusOne = Integer.valueOf(Integer.MIN_VALUE + 1);

		final Integer comparisonBoundaryMax = Integer.valueOf(Integer.MAX_VALUE);
		final Integer comparisonBoundaryMin = Integer.valueOf(Integer.MIN_VALUE);

		classUnderTest.checkBoundaryValidity(true, boundaryMax, comparisonBoundaryMax);
		assertFalse("Validation should fail.", classUnderTest.checkBoundaryValidity(true, boundaryMax, comparisonBoundaryMax));
		assertTrue("Validation should succeed.",
				classUnderTest.checkBoundaryValidity(true, boundaryMaxMinusOne, comparisonBoundaryMax));

		assertFalse("Validation should fail.", classUnderTest.checkBoundaryValidity(false, boundaryMin, comparisonBoundaryMin));
		assertTrue("Validation should succeed.",
				classUnderTest.checkBoundaryValidity(false, boundaryMinPlusOne, comparisonBoundaryMin));
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
			classUnderTest.value = Integer.valueOf(2);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(0);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);


			// open levels will be set
			classUnderTest.levels = this.createOpenLevels(true, true, true);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 2", "2", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 3", "3", classUnderTest.correspondingLevel.getLevelFlag());

			//
			classUnderTest.levels = this.createOpenLevels(true, false, false);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1000);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(1001);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			//
			classUnderTest.levels = this.createOpenLevels(false, false, true);

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2000);
			classUnderTest.determineCorrespondingLevel();
			assertNull("No corresponding level should be found", classUnderTest.correspondingLevel);

			// full open level
			classUnderTest.levels = this.createFullOpenLevel();

			classUnderTest.correspondingLevel = null;
			classUnderTest.value = Integer.valueOf(2001);
			classUnderTest.determineCorrespondingLevel();
			assertEquals("Corresponding level should be level 1", "1", classUnderTest.correspondingLevel.getLevelFlag());

		}
		catch (final DataContainerPropertyLevelException e)
		{
			assertFalse("determineCorrespondingLevel - Exception should not have been thrown.", true);
		}
	}

	private List<DataContainerPropertyLevel> createFullOpenLevel()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		level = new DataContainerPropertyLevelDefaultImpl("1", null, null, "", "");
		levels.add(level);
		return levels;
	}

	@Test
	public void testIsValueInLevelRange()
	{
		Integer value;

		Integer levelLow = Integer.valueOf(1000);
		Integer levelHigh = Integer.valueOf(2000);
		DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(1000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = Integer.valueOf(0);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = Integer.valueOf(3000);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));

		levelLow = Integer.valueOf(1000);
		levelHigh = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(1000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = Integer.valueOf(0);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));

		levelLow = null;
		levelHigh = Integer.valueOf(2000);
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(2000);
		assertTrue("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
		value = Integer.valueOf(3000);
		assertFalse("Value must be inside a level", classUnderTest.isValueInLevelRange(level, value));
	}

	@Test
	public void testIsValueInClosedLevelRange()
	{
		Integer value;
		Integer levelLow = Integer.valueOf(1000);
		Integer levelHigh = Integer.valueOf(2000);

		DataContainerPropertyLevel level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");

		value = Integer.valueOf(1000);
		assertTrue("Value must be inside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = Integer.valueOf(2000);
		assertTrue("Value must be inside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = Integer.valueOf(999);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		value = Integer.valueOf(2001);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = null;
		levelHigh = Integer.valueOf(2000);
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(1000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = Integer.valueOf(1000);
		levelHigh = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(2000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

		levelLow = null;
		level = new DataContainerPropertyLevelDefaultImpl("1", levelLow, levelHigh, "", "");
		value = Integer.valueOf(2000);
		assertFalse("Value must be outside a closed level", classUnderTest.isValueInClosedLevelRange(level, value));

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

	private List<DataContainerPropertyLevel> createClosedLevels()
	{
		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levels = new ArrayList<>();

		final Integer level1Low = Integer.valueOf(1);
		final Integer level1High = Integer.valueOf(1000);
		final Integer level2Low = Integer.valueOf(1001);
		final Integer level2High = Integer.valueOf(2000);

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

		final Integer level1Low = null;
		final Integer level1High = Integer.valueOf(1000);
		final Integer level2Low = Integer.valueOf(1001);
		final Integer level2High = Integer.valueOf(2000);
		final Integer level3Low = Integer.valueOf(2001);
		final Integer level3High = null;
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
}

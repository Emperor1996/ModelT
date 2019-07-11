/*****************************************************************************
 Class:        ISCEObjectScoreResultUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit Test class for ISCEObjectScoreResult.
 *
 */
@UnitTest
public class ISCEObjectScoreResultUnitTest
{
	private ISCEObjectScoreResult classUnderTest;
	private final String scoreValueFormatted = "10";
	private final String scoreValue2Formatted = "20";
	private static final String SCORE_ID = "scoreId";
	private static final String SCORE_ID2 = "scoreId2";
	private static final String SCORE_DESCRIPTION = "scoreDescription";
	private static final String SCORE_DESCRIPTION2 = "scoreDescription2";

	@Before
	public void setUp()
	{
		classUnderTest = new ISCEObjectScoreResult(SCORE_ID, SCORE_DESCRIPTION, scoreValueFormatted);
	}

	/**
	 * Tests null and empty inputs.
	 */
	@Test
	public void testSetterGetter()
	{
		assertEquals("getScoreId not identical", classUnderTest.getScoreId(), SCORE_ID);
		assertEquals("getScoreDescription not identical", classUnderTest.getScoreDescription(), SCORE_DESCRIPTION);
		assertEquals("getScoreValue not identical", classUnderTest.getScoreValue(), scoreValueFormatted);

		classUnderTest.setScoreId(SCORE_ID2);
		classUnderTest.setScoreDescription(SCORE_DESCRIPTION2);
		classUnderTest.setScoreValue(scoreValue2Formatted);

		assertEquals("getScoreId not identical", classUnderTest.getScoreId(), SCORE_ID2);
		assertEquals("getScoreDescription not identical", classUnderTest.getScoreDescription(), SCORE_DESCRIPTION2);
		assertEquals("getScoreValue not identical", classUnderTest.getScoreValue(), scoreValue2Formatted);
	}
}
/*****************************************************************************
 Class:        ISCEItemOfInterestResultUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit Test class for ISCEItemOfInterestResult.
 *
 */
@UnitTest
public class ISCEItemOfInterestResultUnitTest
{
	private ISCEItemOfInterestResult classUnderTest;

	private static final String INTEREST_DESCRIPTION = "interestDescription";
	private static final String INTEREST_CODE = "interestCode";
	private final BigInteger valuationAverage = new BigInteger("10");

	@Before
	public void setUp()
	{
		classUnderTest = new ISCEItemOfInterestResult(INTEREST_CODE, INTEREST_DESCRIPTION, valuationAverage);
	}

	/**
	 * Tests null and empty inputs.
	 */
	@Test
	public void testSetterGetter()
	{
		assertEquals("getInterestCode not identical", classUnderTest.getInterestCode(), INTEREST_CODE);
		assertEquals("getInterestDescription not identical", classUnderTest.getInterestDescription(), INTEREST_DESCRIPTION);
		assertEquals("getValuationAverage not identical", classUnderTest.getValuationAverage(), valuationAverage);

		classUnderTest.setInterestCode(INTEREST_CODE);
		classUnderTest.setInterestDescription(INTEREST_DESCRIPTION);
		classUnderTest.setValuationAverage(valuationAverage);

		assertEquals("getInterestCode not identical", classUnderTest.getInterestCode(), INTEREST_CODE);
		assertEquals("getInterestDescription not identical", classUnderTest.getInterestDescription(), INTEREST_DESCRIPTION);
		assertEquals("getValuationAverage not identical", classUnderTest.getValuationAverage(), valuationAverage);
	}
}

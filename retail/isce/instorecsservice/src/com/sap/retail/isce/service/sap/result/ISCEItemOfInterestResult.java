/*****************************************************************************
 Class:        ISCEItemOfInterestResult
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;

import java.math.BigInteger;



/**
 * Class to hold the result of a ISCE Item of Interest request
 */
public class ISCEItemOfInterestResult
{
	protected String interestCode;
	protected String interestDescription;
	protected BigInteger valuationAverage;

	/**
	 * Create a new ISCESalesVolumeResult with the given attributes.
	 *
	 * @param interestCode
	 *           the interest code
	 * @param interestDescription
	 *           the interest description
	 * @param valuationAverage
	 *           the average valuation of the interest
	 */
	public ISCEItemOfInterestResult(final String interestCode, final String interestDescription, final BigInteger valuationAverage)
	{
		this.interestCode = interestCode;
		this.interestDescription = interestDescription;
		this.valuationAverage = valuationAverage;
	}

	/**
	 * @return the interestCode
	 */
	public String getInterestCode()
	{
		return interestCode;
	}

	/**
	 * @param interestCode
	 *           the interestCode to set
	 */
	public void setInterestCode(final String interestCode)
	{
		this.interestCode = interestCode;
	}

	/**
	 * @return the interestDescription
	 */
	public String getInterestDescription()
	{
		return interestDescription;
	}

	/**
	 * @param interestDescription
	 *           the interestDescription to set
	 */
	public void setInterestDescription(final String interestDescription)
	{
		this.interestDescription = interestDescription;
	}

	/**
	 * @return the valuationAverage
	 */
	public BigInteger getValuationAverage()
	{
		return valuationAverage;
	}

	/**
	 * @param valuationAverage
	 *           the valuationAverage to set
	 */
	public void setValuationAverage(final BigInteger valuationAverage)
	{
		this.valuationAverage = valuationAverage;
	}



}
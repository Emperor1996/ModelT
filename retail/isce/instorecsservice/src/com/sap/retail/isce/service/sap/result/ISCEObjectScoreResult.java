/*****************************************************************************
 Class:        ISCEObjectScoreResult
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;




/**
 * Class to hold the result of an ISCE object score request
 */
public class ISCEObjectScoreResult
{
	protected String scoreId;
	protected String scoreDescription;
	protected String scoreValueFormatted;

	/**
	 * Create a new ISCEObjectScoreResult with the given attributes.
	 *
	 * @param scoreId
	 *           the score id
	 * @param scoreDescription
	 *           the score description
	 * @param scoreValueFormatted
	 *           the score value formatted
	 */
	public ISCEObjectScoreResult(final String scoreId, final String scoreDescription, final String scoreValueFormatted)
	{
		this.scoreId = scoreId;
		this.scoreDescription = scoreDescription;
		this.scoreValueFormatted = scoreValueFormatted;
	}

	/**
	 * @return the scoreId
	 */
	public String getScoreId()
	{
		return scoreId;
	}

	/**
	 * @param scoreId
	 *           the scoreId to set
	 */
	public void setScoreId(final String scoreId)
	{
		this.scoreId = scoreId;
	}

	/**
	 * @return the interestDescription
	 */
	public String getScoreDescription()
	{
		return scoreDescription;
	}

	/**
	 * @param scoreDescription
	 *           the scoreDescription to set
	 */
	public void setScoreDescription(final String scoreDescription)
	{
		this.scoreDescription = scoreDescription;
	}

	/**
	 * @return the scoreValue
	 */
	public String getScoreValue()
	{
		return scoreValueFormatted;
	}

	/**
	 * @param scoreValueFormatted
	 *           the scoreValue to set
	 */
	public void setScoreValue(final String scoreValueFormatted)
	{
		this.scoreValueFormatted = scoreValueFormatted;
	}

}
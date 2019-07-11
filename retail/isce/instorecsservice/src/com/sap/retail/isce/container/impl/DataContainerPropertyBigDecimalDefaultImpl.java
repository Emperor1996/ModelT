/*****************************************************************************
 Class:        DataContainerPropertyBigDecimalDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

/**
 * 	 This class supports only the number range of Integer
 */


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.retail.isce.container.DataContainerPropertyBigDecimal;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


public class DataContainerPropertyBigDecimalDefaultImpl extends DataContainerPropertyDefaultImpl implements
		DataContainerPropertyBigDecimal
{
	private static final Logger LOG = Logger.getLogger(DataContainerPropertyBigDecimalDefaultImpl.class.getName());

	/**
	 * Constructs the class and sets id and value
	 *
	 * @param value
	 *           value of the property
	 * @param unitSingular
	 *           unit string for a value <=1
	 * @param unitPlural
	 *           unit string for a value >1
	 */
	public DataContainerPropertyBigDecimalDefaultImpl(final BigDecimal value, final String unitSingular, final String unitPlural)
	{
		this.value = value;
		this.unitSingular = unitSingular;
		this.unitPlural = unitPlural;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerProperty#determineCorrespondingLevel()
	 */
	@Override
	public void determineCorrespondingLevel() throws DataContainerPropertyLevelException
	{
		if (value == null)
		{
			return;
		}
		try
		{
			this.correspondingLevel = super.determineCorrespondingLevel(((BigDecimal) this.value).setScale(0, BigDecimal.ROUND_UP));
		}
		catch (final ClassCastException e)
		{
			logAndThrowExForCastEx(LOG, e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.impl.DataContainerPropertyDefaultImpl#determineUnit(java.lang.Object)
	 */
	@Override
	public String determineUnit(final Object objectValue) throws DataContainerPropertyLevelException
	{
		try
		{
			if (objectValue != null && ((BigDecimal) objectValue).compareTo(BigDecimal.valueOf(-1)) >= 0
					&& ((BigDecimal) objectValue).compareTo(BigDecimal.valueOf(1)) <= 0)
			{
				return this.unitSingular;
			}
		}
		catch (final ClassCastException e)
		{
			logAndThrowExForCastEx(LOG, e);
		}
		return this.unitPlural;
	}

	/**
	 * Checks if the current level is valid, this is if it is properly ascending or descending.
	 *
	 * @param ascending
	 *           should the level be ascending or descending
	 * @param currentLevel
	 *           the level to check the validity for
	 * @param comparisonLevel
	 *           the level use as basis for the comparison
	 * @return true if the level is not valid, false else
	 */
	protected boolean checkLevelValidity(final boolean ascending, final BigDecimal currentLevel, final BigDecimal comparisonLevel)
	{
		if (comparisonLevel == null)
		{
			return true;
		}
		return (ascending && currentLevel.add(BigDecimal.ONE).compareTo(comparisonLevel) <= 0)
				|| (!ascending && currentLevel.subtract(BigDecimal.ONE).compareTo(comparisonLevel) >= 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#calculateLevelBoundaryPairs(java.util.Comparable,
	 * boolean)
	 */
	@Override
	public List<Comparable> calculateLevelBoundaryPairs(final List<Comparable> levelBoundaries, final boolean ascending)
			throws DataContainerPropertyLevelException
	{
		final List<Comparable> resBoundaries = new ArrayList();
		try
		{
			Comparable comparisonBoundary;

			for (int i = 0; i < levelBoundaries.size() && levelBoundaries.get(i) != null; i++)
			{
				comparisonBoundary = (i < levelBoundaries.size() - 1) ? levelBoundaries.get(i + 1) : null;
				//check that boundaries are properly ascending or descending and the delta isn't to big
				if (!checkLevelValidity(ascending, (BigDecimal) levelBoundaries.get(i), (BigDecimal) comparisonBoundary))
				{
					throw new DataContainerPropertyLevelException("Wrong sequence in level list");
				}

				addBoundaryPair(levelBoundaries.get(i), ascending, resBoundaries, comparisonBoundary);
			}
		}
		catch (final ClassCastException e)
		{
			logAndThrowExForCastEx(LOG, e);
		}
		return resBoundaries;
	}

	/**
	 * Adds a pair of boundaries
	 *
	 * @param boundary
	 * @param ascending
	 * @param resBoundaries
	 * @param comparisonBoundary
	 */
	protected void addBoundaryPair(final Comparable boundary, final boolean ascending, final List<Comparable> resBoundaries,
			final Comparable comparisonBoundary)
	{
		if (!ascending)
		{
			resBoundaries.add((comparisonBoundary != null) ? ((BigDecimal) comparisonBoundary).add(BigDecimal.ONE) : null);
		}
		resBoundaries.add(boundary);
		if (ascending)
		{
			resBoundaries.add((comparisonBoundary != null) ? ((BigDecimal) comparisonBoundary).subtract(BigDecimal.ONE) : null);
		}
	}
}
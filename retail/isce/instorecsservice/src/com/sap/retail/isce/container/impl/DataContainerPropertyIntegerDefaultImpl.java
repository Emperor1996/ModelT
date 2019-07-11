/*****************************************************************************
 Class:        DataContainerPropertyIntegerDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.retail.isce.container.DataContainerPropertyInteger;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


public class DataContainerPropertyIntegerDefaultImpl extends DataContainerPropertyDefaultImpl implements
		DataContainerPropertyInteger
{
	private static final Logger LOG = Logger.getLogger(DataContainerPropertyIntegerDefaultImpl.class.getName());

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
	public DataContainerPropertyIntegerDefaultImpl(final Integer value, final String unitSingular, final String unitPlural)
	{
		this.unitSingular = unitSingular;
		this.unitPlural = unitPlural;
		this.value = value;
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
			if (objectValue != null && ((Integer) objectValue).compareTo(Integer.valueOf(-1)) >= 0
					&& ((Integer) objectValue).compareTo(Integer.valueOf(1)) <= 0)
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
	 * Checks if the current boundary is valid, this is if it is properly ascending or descending and the additionally
	 * calculated boundaries stay in valid range of type int.
	 *
	 * @param ascending
	 *           should the boundary be ascending or descending
	 * @param currentBoundary
	 *           the boundary to check the validity for
	 * @param comparisonBoundary
	 *           the boundary use as basis for the comparison
	 * @return true if the boundary is not valid, false else
	 */
	protected boolean checkBoundaryValidity(final boolean ascending, final Integer currentBoundary,
			final Integer comparisonBoundary)
	{
		if (comparisonBoundary == null)
		{
			return true;
		}

		// This also prevents from creating boundaries that are outside range MIN_VALUE to MAX_VALUE of int
		return (ascending && currentBoundary.intValue() <= comparisonBoundary.intValue() - 1)
				|| (!ascending && currentBoundary.intValue() >= comparisonBoundary.intValue() + 1);
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
				//check that levels are properly ascending or descending and the delta isn't to big
				if (!checkBoundaryValidity(ascending, (Integer) levelBoundaries.get(i), (Integer) comparisonBoundary))
				{
					throw new DataContainerPropertyLevelException("Wrong sequence in level list or overflow");
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
			resBoundaries.add((comparisonBoundary != null) ? Integer.valueOf(((Integer) comparisonBoundary).intValue() + 1) : null);
		}
		resBoundaries.add(boundary);
		if (ascending)
		{
			resBoundaries.add((comparisonBoundary != null) ? Integer.valueOf(((Integer) comparisonBoundary).intValue() - 1) : null);
		}
	}
}

/*****************************************************************************
 Interface:        DataContainerProperty
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import java.util.List;

import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


public interface DataContainerProperty
{

	/**
	 * @return the value
	 */
	public Object getValue();

	/**
	 * @param value
	 *           the value to set
	 */
	public void setValue(Object value);

	/**
	 * Returns the level that corresponds to the value
	 *
	 * @return the corresponding level if the corresponding level can be determined, otherwise null
	 */
	public DataContainerPropertyLevel getCorrespondingLevel();

	/**
	 * Determines the level that corresponds to the value
	 *
	 * @throws DataContainerPropertyLevelException
	 */
	public void determineCorrespondingLevel() throws DataContainerPropertyLevelException;

	/**
	 * Determines the levels of the property
	 *
	 * @return list of levels
	 */
	public List<DataContainerPropertyLevel> getLevels();

	/**
	 * HTML encodes the string properties of the levels
	 *
	 */
	public void encodeHTML();

	/**
	 * Creates the levels based on the level borders
	 *
	 * @param levelBoundaryPairs
	 *           list of level boundaries.
	 * @return the created levels
	 * @throws DataContainerPropertyLevelException
	 */
	public List<DataContainerPropertyLevel> createLevels(List<Comparable> levelBoundaryPairs)
			throws DataContainerPropertyLevelException;

	/**
	 * Calculates the level boundary pairs (tuple of low and high value of a level) for the given list of level
	 * properties with ascending or descending order. If ascending is specified, the given numbers are assumed to be the
	 * lower boundaries of the level, if descending then they are interpreted as upper boundaries.
	 *
	 * @param levelBoundaries
	 *           list of boundary values for the levels, sorted in ascending or descending order.
	 * @param ascending
	 *           flag to indicate, if an ascending or descending list should be processed
	 * @return the calculated level pair list for the given levels, either in ascending or descending order.
	 * @throws DataContainerPropertyLevelException
	 */
	public List<Comparable> calculateLevelBoundaryPairs(final List<Comparable> levelBoundaries, boolean ascending)
			throws DataContainerPropertyLevelException;
}

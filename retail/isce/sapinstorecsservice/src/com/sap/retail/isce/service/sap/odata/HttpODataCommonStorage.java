/*****************************************************************************
 interface:        HttpODataCommonStorage.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata;

import java.util.Map;


/**
 * Interface HttpODataCommonStorage
 */
public interface HttpODataCommonStorage
{
	/**
	 * Retrieves an entry from the concurrent destinations storage map.
	 *
	 * @param destinationName
	 *           String containing the destination name
	 * @return an Map representing a destination.
	 */

	public Map<String, Object> getDestination(final String destinationName);

	/**
	 * Puts a new entry in the concurrent destinations storage map.
	 *
	 * @param destinationName
	 *           String containing the destination name
	 * @param destination
	 *           Map<String, Object> representing a destination
	 */
	public void putDestination(final String destinationName, final Map<String, Object> destination);

	/**
	 * Removes a given entry in the concurrent destinations storage map.
	 *
	 * @param destinationName
	 *           String containing the destination name
	 */
	public void removeDestination(final String destinationName);
}

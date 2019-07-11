/*****************************************************************************
 Interface:        DataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import java.util.List;
import java.util.Map;

import com.sap.retail.isce.exception.DataContainerServiceException;


/**
 * Container for combining data of other data containers.
 */
public interface DataContainerCombined extends DataContainer
{
	/**
	 * Combine data of the underlying data the containers.
	 *
	 * @param dataContainerMap
	 *           a list of the DataContainer instances that should be used in the function
	 *
	 * @throws DataContainerServiceException
	 *            exception thrown in error case
	 */
	public void combineData(Map<String, DataContainer> dataContainerMap) throws DataContainerServiceException;

	/**
	 * Gets the data container names that should be used for the combine action.
	 *
	 * @return a list of data container names
	 */
	public List<String> getDataContainerNamesForCombining();
}

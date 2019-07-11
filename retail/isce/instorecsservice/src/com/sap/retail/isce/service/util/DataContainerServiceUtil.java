/*****************************************************************************
 Class:        DataContainerServiceUtil
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.retail.isce.container.DataContainer;


/**
 * Util class where all data container are registered. It is possible to get the instance associated to a combined
 * container.
 *
 */
public class DataContainerServiceUtil
{
	private final Map<String, DataContainer> dataContainerMap;

	/**
	 * Constructor. Initializes the internal hash map that stores all dataContainer with their context name.
	 */
	public DataContainerServiceUtil()
	{
		this.dataContainerMap = new HashMap<>();
	}

	/**
	 * Gets the data containers instances for the given names.
	 *
	 * @param dataContainerNames
	 *           : the names of the dataContainers that should be used for finding the instances
	 * @return the data container instances that should be found
	 */
	public Map<String, DataContainer> getDataContainersForNames(final List<String> dataContainerNames)
	{
		final Map<String, DataContainer> dcList = new HashMap<>();

		if (dataContainerNames == null || dataContainerNames.isEmpty())
		{
			return dcList;
		}

		for (final String dcName : dataContainerNames)
		{
			dcList.put(dcName, this.dataContainerMap.get(dcName));
		}
		return dcList;
	}

	/**
	 * Gets the data container instance for the given name.
	 *
	 * @param dataContainerName
	 *           : the name of the dataContainer that should be used for finding the instance
	 * @return the data container instances that should be found
	 */
	public DataContainer getDataContainerForName(final String dataContainerName)
	{
		return this.dataContainerMap.get(dataContainerName);
	}

	/**
	 * Sets the data container
	 *
	 * @param dataContainers
	 *           : a list of all data containers that are relevant for the scenario
	 */
	public void setDataContainers(final List<DataContainer> dataContainers)
	{
		for (final DataContainer dc : dataContainers)
		{
			this.dataContainerMap.put(dc.getContainerContextParamName(), dc);
		}
	}
}

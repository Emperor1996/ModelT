/*****************************************************************************
 Interface:        DataContainerService
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;

import com.sap.retail.isce.container.DataContainer;


/**
 * Service for handling data containers.
 */
public interface DataContainerService
{
	// Constant for the session attribute, that store already loaded ISCE 360 Data Containers
	public static final String ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE = "ISCECustomer360DataContainers";

	/**
	 * Fills the container with data.
	 *
	 * @param dataContainers
	 *           A list of data containers.
	 * @param component
	 *           optional, the CMS component the read is triggered by
	 */
	public void readDataContainers(List<DataContainer> dataContainers, AbstractCMSComponentModel component);

	/**
	 *
	 * @param dataContainers
	 *           A list of data containers.
	 * @param component
	 *           optional, the CMS component for which the data containers should update its data
	 */
	public void updateDataContainersForComponent(List<DataContainer> dataContainers, AbstractCMSComponentModel component);

}

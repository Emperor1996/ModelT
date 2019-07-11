/*****************************************************************************
 Interface:        Customer360Facade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;

import com.sap.retail.isce.container.DataContainer;


/**
 * Interface for Customer360 Facade.
 */
public interface Customer360Facade
{
	/**
	 * Reads the Customer360 Data.
	 *
	 * @param dataContainers
	 *           a list of data container to be filled by the underlying oData call
	 * @param component
	 *           optional, the CMS component the read is triggered by
	 */
	public void readCustomer360Data(List<DataContainer> dataContainers, AbstractCMSComponentModel component);

	/**
	 *
	 * @param dataContainers
	 *           A list of data containers.
	 * @param component
	 *           optional, the CMS component for which the data containers should update its data
	 */
	public void updateDataContainersForComponent(List<DataContainer> dataContainers, AbstractCMSComponentModel component);

}
/*****************************************************************************
 Interface:        DataContainerSapServiceOData
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap;

import java.util.List;

import com.sap.retail.isce.container.DataContainerOData;


/**
 * The interface prepares and executes the oData calls required for the Customer360 page.
 *
 */
public interface DataContainerSapServiceOData
{
	/**
	 * Retrieves the Container Data for a specific OData backend call.
	 *
	 * @param dataContainersOData
	 *           the list of container for oData call results
	 */
	public void readDataContainers(List<DataContainerOData> dataContainersOData);
}

/*****************************************************************************
 Class:        Customer360FacadeDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.facade.Customer360Facade;
import com.sap.retail.isce.service.DataContainerService;


/**
 * Default implementation.
 *
 */
public class Customer360FacadeDefaultImpl implements Customer360Facade
{
	private DataContainerService dataContainerService;

	/**
	 * @param dataContainerService
	 *           the dataContainerService to set, injected from Spring framework according to xml definition, to be used
	 *           to retrieve the data.
	 */
	@Required
	public void setDataContainerService(final DataContainerService dataContainerService)
	{
		this.dataContainerService = dataContainerService;
	}

	@Override
	public void readCustomer360Data(final List<DataContainer> dataContainers, final AbstractCMSComponentModel component)
	{
		dataContainerService.readDataContainers(dataContainers, component);
	}

	@Override
	public void updateDataContainersForComponent(final List<DataContainer> dataContainers,
			final AbstractCMSComponentModel component)
	{
		dataContainerService.updateDataContainersForComponent(dataContainers, component);
	}
}

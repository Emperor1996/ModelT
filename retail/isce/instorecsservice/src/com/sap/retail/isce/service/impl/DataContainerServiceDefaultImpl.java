/*****************************************************************************
 Class:        DataContainerServiceDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerCombined;
import com.sap.retail.isce.container.DataContainerOData;
import com.sap.retail.isce.container.DataContainerSmart;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.exception.DataContainerServiceException;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.sap.DataContainerSapServiceOData;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Default implementation of data container service.
 */
public class DataContainerServiceDefaultImpl implements DataContainerService
{
	private static final Logger LOG = Logger.getLogger(DataContainerServiceDefaultImpl.class.getName());
	protected DataContainerSapServiceOData dataContainerSapServiceOData; // Singleton
	protected SpringUtil springUtil = null;
	protected SessionService sessionService = null;

	/**
	 * @param springUtil
	 *           the springUtil to set
	 */
	@Required
	public void setSpringUtil(final SpringUtil springUtil)
	{
		this.springUtil = springUtil;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.DataContainerService#readDataContainers(java.util.List)
	 */
	@Override
	public void readDataContainers(final List<DataContainer> dataContainers, final AbstractCMSComponentModel component)
	{
		DataContainerServiceUtil dataContainerServiceUtil;

		if (getSessionService().getAttribute(DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE) != null)
		{
			dataContainerServiceUtil = (DataContainerServiceUtil) getSessionService().getAttribute(
					DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE);
		}
		else
		{
			dataContainerServiceUtil = (DataContainerServiceUtil) this.springUtil.getBean("defaultDataContainerServiceUtil");
			getSessionService().setAttribute(DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE,
					dataContainerServiceUtil);
		}

		dataContainerServiceUtil.setDataContainers(dataContainers);
		final List<DataContainerOData> dataContainersOData = new ArrayList<>();
		final List<DataContainerCombined> dataContainersCombined = new ArrayList<>();

		for (final DataContainer dataContainer : dataContainers)
		{
			dataContainer.setCMSComponentModel(component);

			dataContainer.traceInformation();

			if (dataContainer instanceof DataContainerOData)
			{
				// Combine those belonging together
				dataContainersOData.add((DataContainerOData) dataContainer);
			}
			else if (dataContainer instanceof DataContainerSmart)
			{
				try
				{
					((DataContainerSmart) dataContainer).readData();
				}
				catch (final DataContainerServiceException e)
				{
					LOG.error(
							"The DataContainerOData " + dataContainer.getContainerName() + " could not read its smart data"
									+ e.toString(), e);
					dataContainer.setErrorState(Boolean.TRUE);
					dataContainer.setDataInErrorState();
				}
			}
			else if (dataContainer instanceof DataContainerCombined)
			{
				// combined containers must be processed after all others
				dataContainersCombined.add((DataContainerCombined) dataContainer);
			}
			else
			{
				LOG.error("Unsupported DataContainer type : " + dataContainer.getContainerName()
						+ ". DataContainerRuntimeException thrown.");
				throw new DataContainerRuntimeException("Not supported DataContainer Type : " + dataContainer.getContainerName());
			}
		}

		// Read oData
		if (!dataContainersOData.isEmpty())
		{
			this.dataContainerSapServiceOData.readDataContainers(dataContainersOData);
		}

		readCombinedDataContainers(dataContainerServiceUtil, dataContainersCombined);
	}

	/**
	 * Ask DataContainerCombined to combine its data
	 *
	 * @param dataContainerServiceUtil
	 * @param dataContainersCombined
	 */
	protected void readCombinedDataContainers(final DataContainerServiceUtil dataContainerServiceUtil,
			final List<DataContainerCombined> dataContainersCombined)
	{
		for (final DataContainerCombined dataContainerCombined : dataContainersCombined)
		{
			try
			{
				final List<String> dataContainerNamesForCombining = dataContainerCombined.getDataContainerNamesForCombining();
				final Map<String, DataContainer> dataContainerInstancesForCombine = dataContainerServiceUtil
						.getDataContainersForNames(dataContainerNamesForCombining);
				dataContainerCombined.combineData(dataContainerInstancesForCombine);
			}
			catch (final DataContainerServiceException e)
			{
				LOG.error(
						"The DataContainerOData " + dataContainerCombined.getContainerName() + " could not combine data" + e.toString(),
						e);
				dataContainerCombined.setErrorState(Boolean.TRUE);
				dataContainerCombined.setDataInErrorState();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.DataContainerService#updateDataContainersForComponent(java.util.List,
	 * de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel)
	 */
	@Override
	public void updateDataContainersForComponent(final List<DataContainer> dataContainers,
			final AbstractCMSComponentModel component)
	{
		for (final DataContainer dataContainer : dataContainers)
		{
			dataContainer.setCMSComponentModel(component);
			dataContainer.determineDataForCMSComponent();
		}
	}

	/**
	 * @param dataContainerSapServiceOData
	 *           the dataContainerSapServiceOData to set
	 */
	@Required
	public void setDataContainerSapServiceOData(final DataContainerSapServiceOData dataContainerSapServiceOData)
	{
		this.dataContainerSapServiceOData = dataContainerSapServiceOData;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}

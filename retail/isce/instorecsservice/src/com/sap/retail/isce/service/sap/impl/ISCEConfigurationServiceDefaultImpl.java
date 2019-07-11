/*****************************************************************************
 Class:        ISCEConfigurationServiceDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import de.hybris.platform.sap.core.configuration.exception.ConfigurationBaseRuntimeException;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Default implementation of the ISCEConfigurationService Interface.
 */
public class ISCEConfigurationServiceDefaultImpl implements ISCEConfigurationService
{

	private BaseStoreService baseStoreService;
	private static final Logger LOG = Logger.getLogger(ISCEConfigurationServiceDefaultImpl.class);


	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;

	}

	@Override
	public SAPHTTPDestinationModel getCARHttpDestination()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_HTTPDestination();
	}

	@Override
	public SAPHTTPDestinationModel getYMktHttpDestination()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_yMkt_HTTPDestination();
	}

	@Override
	public String getCARSAPClient()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_client();
	}

	@Override
	public String getCARServiceName()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_serviceName();
	}

	@Override
	public String getCARPOSOrderChannels()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_posChannelList();
	}

	@Override
	public String getCAROnlineOrderChannels()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_onlineChannelList();
	}

	/**
	 * Returns the current SAPConfiguration. If not found an exception is thrown.
	 *
	 * @return SAPConfigurationModel the current SAPConfiguration to determione the confifuratuion from.
	 */
	protected SAPConfigurationModel getConfiguration()
	{
		final SAPConfigurationModel configurationModel = getBaseStoreService().getCurrentBaseStore().getSAPConfiguration();
		if (configurationModel == null)
		{
			final String message = "The current BaseStore is not assigned to a SAP Configuration";
			LOG.fatal(message);
			throw new ConfigurationBaseRuntimeException("There can't be more than one SAPGlobalConfigurationModel!");
		}
		return configurationModel;
	}

	@Override
	public Integer getCARMaxNumberTransactionsPurchaseHistory()
	{
		return getConfiguration().getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions();
	}

}

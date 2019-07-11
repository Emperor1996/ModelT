/*****************************************************************************
    Class:        DefaultSourcingStrategy
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import org.apache.log4j.Logger;

import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingService;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;
import com.sap.retail.oaa.commerce.services.sourcing.strategy.SourcingStrategy;


/**
 * Default Implementation for SourcingStrategy
 */
public class DefaultSourcingStrategy implements SourcingStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultSourcingStrategy.class);

	private SourcingService sourcingService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.oaa.commerce.services.sourcing.strategy.OaaSourcingStrategy#doSourcing(de.hybris.platform.core.
	 * model.order.AbstractOrderModel)
	 */
	@Override
	public boolean doSourcing(final AbstractOrderModel abstractOrderModel)
	{
		LOG.info("Source Order: " + abstractOrderModel.getCode());

		try
		{
			sourcingService.callRestServiceAndPersistResult(abstractOrderModel);
		}
		catch (final SourcingException e)
		{
			LOG.error(e);
			return false;
		}
		catch (final CARBackendDownException e)
		{
			LOG.error(e.getMessage(), e);

			//When Cart has Pick Up Items sourcing is not successful in offline scenario
			if (hasPickUpItems(abstractOrderModel))
			{
				return false;
			}

		}
		return true;
	}

	protected boolean hasPickUpItems(final AbstractOrderModel abstractOrderModel)
	{
		for (final AbstractOrderEntryModel entry : abstractOrderModel.getEntries())
		{
			if (entry.getDeliveryPointOfService() != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the sourcingService
	 */
	protected SourcingService getSourcingService()
	{
		return sourcingService;
	}

	/**
	 * @param sourcingService
	 *           the sourcingService to set
	 */
	public void setSourcingService(final SourcingService sourcingService)
	{
		this.sourcingService = sourcingService;
	}

}

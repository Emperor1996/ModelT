/**
 *
 */
package com.sap.retail.oaa.orderexchange.actions;

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.sap.yorderfulfillment.actions.CheckSAPOrderAction;
import de.hybris.platform.site.BaseSiteService;

import org.apache.log4j.Logger;

import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingService;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;


/**
 * This OAA specific action is derived from CheckSAPOrderAction. It calls the sourcing REST service in CAR and
 * creates/updates the temporary reservation with the order id and reservation status 'O' (Order).
 */
public class CheckSAPOaaOrderAction extends CheckSAPOrderAction
{
	private static final Logger LOG = Logger.getLogger(CheckSAPOaaOrderAction.class);


	private SourcingService sourcingService;
	private BaseSiteService baseSiteService;



	/**
	 * @return the sourcingService
	 */
	public SourcingService getSourcingService()
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

	/**
	 * @return the baseSiteService
	 */
	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		// call parent action
		if (super.executeAction(process) == Transition.NOK)
		{
			return Transition.NOK;
		}

		try
		{
			baseSiteService.setCurrentBaseSite(process.getOrder().getSite(), false);
			sourcingService.callRestServiceAndPersistResult(process.getOrder());
		}
		catch (final SourcingException e)
		{
			LOG.error("Error when executing Sourcing", e);
			return Transition.NOK;
		}
		catch (final CARBackendDownException e)
		{
			LOG.error("CAR Backend is not responding", e);
			return Transition.NOK;
		}

		return Transition.OK;
	}

}

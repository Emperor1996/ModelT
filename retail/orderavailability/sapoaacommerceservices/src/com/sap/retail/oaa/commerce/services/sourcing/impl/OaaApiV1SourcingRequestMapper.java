/*****************************************************************************
    Class:        DefaultSourcingRequestMapper
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import com.sap.retail.oaa.commerce.services.constants.SapoaacommerceservicesConstants;
import com.sap.retail.oaa.commerce.services.rest.RestServiceConfiguration;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingRequestMapper;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.CartRequest;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingAbapRequest;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingRequest;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.Values;


/**
 * Default implementation for mapping into sourcing requests
 */
public class OaaApiV1SourcingRequestMapper extends SourcingRequestMapperBase implements SourcingRequestMapper
{
	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.oaa.commerce.services.sourcing.SourcingRequestMapper#mapCartModelToSourcingRequest(de.hybris.
	 * platform .core.model.order.CartModel, boolean, boolean)
	 */
	@Override
	public SourcingAbapRequest mapCartModelToSourcingRequest(final CartModel cartModel, final boolean execAllStrategies,
			final boolean reserve, final RestServiceConfiguration restConfiguration) throws SourcingException
	{
		return mapModelToSourcingRequest(cartModel, execAllStrategies, reserve,
				SapoaacommerceservicesConstants.RESERVATION_STATUS_CART, restConfiguration);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.oaa.commerce.services.sourcing.SourcingRequestMapper#mapOrderModelToSourcingRequest(de.hybris.
	 * platform .core.model.order.OrderModel, boolean, boolean)
	 */
	@Override
	public SourcingAbapRequest mapOrderModelToSourcingRequest(final OrderModel orderModel, final boolean execAllStrategies,
			final boolean reserve, final RestServiceConfiguration restConfiguration) throws SourcingException
	{
		return mapModelToSourcingRequest(orderModel, execAllStrategies, reserve,
				SapoaacommerceservicesConstants.RESERVATION_STATUS_ORDER, restConfiguration);
	}

	protected SourcingAbapRequest mapModelToSourcingRequest(final AbstractOrderModel abstractOrderModel,
			final boolean execAllStrategies, final boolean reserve, final String reservationStatus,
			final RestServiceConfiguration restConfiguration) throws SourcingException
	{
		final SourcingAbapRequest abap = new SourcingAbapRequest();
		final Values values = new Values();
		final SourcingRequest sourcing = new SourcingRequest();
		final CartRequest cart = new CartRequest();

		sourcing.setOaaProfileId(restConfiguration.getOaaProfile());

		// Create/update temporary reservation?
		if (reserve)
		{
			sourcing.setReserve(ABAP_TRUE);

			sourcing.setStatus(reservationStatus);
			sourcing.setConsumerId(restConfiguration.getConsumerId());
		}

		// Execute all sourcing strategies?
		if (execAllStrategies)
		{
			sourcing.setExecAllStrategies(ABAP_TRUE);
		}

		setCart(abstractOrderModel, cart, reservationStatus);
		sourcing.setCart(cart);
		values.setSourcing(sourcing);
		abap.setValues(values);

		return abap;
	}

}

/*****************************************************************************
    Interface:    CartToSourcingRequestMapper
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import com.sap.retail.oaa.commerce.services.rest.RestServiceConfiguration;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingAbapRequest;


/**
 * Maps Hybris cart or order model to sourcing request
 */
public interface SourcingRequestMapper
{
	/**
	 * Maps hybris cart to sourcing request
	 *
	 * @param cartModel
	 *           Hybris Cart
	 * @param execAllStrategies
	 *           execute all strategies
	 * @param reserve
	 *           create or update a temporary reservation
	 * @param restConfiguration
	 *           configuration object
	 * @return ABAP sourcing request
	 * @throws SourcingException
	 */
	SourcingAbapRequest mapCartModelToSourcingRequest(CartModel cartModel, boolean execAllStrategies, boolean reserve,
			RestServiceConfiguration restConfiguration) throws SourcingException;


	/**
	 * Maps hybris order to sourcing request
	 *
	 * @param orderModel
	 *           Hybris Cart
	 * @param execAllStrategies
	 *           execute all strategies
	 * @param reserve
	 *           create or update a temporary reservation
	 * @param restConfiguration
	 *           configuration object
	 * @return ABAP sourcing request
	 * @throws SourcingException
	 */
	SourcingAbapRequest mapOrderModelToSourcingRequest(OrderModel orderModel, boolean execAllStrategies, boolean reserve,
			RestServiceConfiguration restConfiguration) throws SourcingException;
}

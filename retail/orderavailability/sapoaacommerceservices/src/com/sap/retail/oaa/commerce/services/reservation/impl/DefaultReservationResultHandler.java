/*****************************************************************************
    Class:        DefaultReservationResultHandler
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.reservation.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import com.sap.retail.oaa.commerce.services.reservation.ReservationResultHandler;
import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.response.ReservationResponse;


/**
 * Default Result Handler for reservation Service.
 */
public class DefaultReservationResultHandler implements ReservationResultHandler
{
	private ModelService modelService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.oaa.commerce.services.reservation.ReservationResultHandler#updateReservation(de.hybris.platform.
	 * core.model.order.AbstractOrderModel,
	 * com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.response.ReservationResponse)
	 */
	@Override
	public void updateReservation(final AbstractOrderModel order, final ReservationResponse reservationResponse)
	{
		//Do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.oaa.commerce.services.reservation.ReservationResultHandler#deleteReservation(de.hybris.platform.
	 * core.model.order.AbstractOrderModel)
	 */
	@Override
	public void deleteReservation(final AbstractOrderModel order)
	{
		order.setSapCarReservation(Boolean.FALSE);

		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			entry.setSapCarReservation(Boolean.FALSE);
			getModelService().save(entry);
		}

		getModelService().save(order);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.oaa.commerce.services.reservation.ReservationResultHandler#deleteReservationItem(de.hybris.platform
	 * .core.model.order.AbstractOrderEntryModel)
	 */
	@Override
	public void deleteReservationItem(final AbstractOrderEntryModel orderEntry)
	{
		orderEntry.setSapCarReservation(Boolean.FALSE);
		getModelService().save(orderEntry);
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}

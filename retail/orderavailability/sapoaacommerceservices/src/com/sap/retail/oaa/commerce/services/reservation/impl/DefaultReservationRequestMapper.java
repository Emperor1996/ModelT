/*****************************************************************************
    Class:        DefaultReservationRequestMapper
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.reservation.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.sap.retail.oaa.commerce.services.constants.SapoaacommerceservicesConstants;
import com.sap.retail.oaa.commerce.services.reservation.ReservationRequestMapper;
import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationAbapRequest;
import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationRequest;
import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationValuesRequest;
import com.sap.retail.oaa.commerce.services.rest.RestServiceConfiguration;
import com.sap.retail.oaa.model.enums.Sapoaa_mode;



/**
 * Default Request Mapper for reservation Service.
 */
public class DefaultReservationRequestMapper implements ReservationRequestMapper
{

	/*
	 *
	 * @see
	 * com.sap.retail.oaa.commerce.services.reservation.ReservationRequestMapper#mapToReservationRequest(java.lang.String
	 * , java.lang.String , com.sap.retail.oaa.commerce.services.rest.RestServiceConfiguration )
	 */
	@Override
	public ReservationAbapRequest mapOrderModelToReservationRequest(final AbstractOrderModel abstractOderModel,
			final String reservationStatus, final RestServiceConfiguration restConfiguration)
	{
		final ReservationAbapRequest abap = new ReservationAbapRequest();
		final ReservationValuesRequest values = new ReservationValuesRequest();
		final ReservationRequest reservation = new ReservationRequest();

		if (reservationStatus.equals(SapoaacommerceservicesConstants.RESERVATION_STATUS_ORDER))
		{
			reservation.setOrderId(abstractOderModel.getCode());
		}
		reservation.setStatus(reservationStatus);

		this.setMode(reservation, restConfiguration);

		values.setReservation(reservation);
		abap.setValues(values);

		return abap;
	}

	private void setMode(final ReservationRequest reservation, final RestServiceConfiguration restConfiguration)
	{
		if (restConfiguration.getMode() != null && restConfiguration.getMode().equals(Sapoaa_mode.SALESCHANNEL))
		{
			reservation.setSalesChannel(restConfiguration.getSalesChannel());
		}
		else
		{
			reservation.setConsumerId(restConfiguration.getConsumerId());
		}
	}

}

/*****************************************************************************
    Interface:		ReservationRequestMapper
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.reservation;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationAbapRequest;
import com.sap.retail.oaa.commerce.services.rest.RestServiceConfiguration;



/**
 * Request Mapper for reservation REST Service
 */
public interface ReservationRequestMapper
{

	/**
	 * Map to request structure for OAA reservation REST Service
	 *
	 * @param abstractOrderModel
	 * @param reservationStatus
	 * @param restConfiguration
	 * @return ReservationAbapRequest
	 */
	ReservationAbapRequest mapOrderModelToReservationRequest(AbstractOrderModel abstractOrderModel, String reservationStatus,
			RestServiceConfiguration restConfiguration);

}

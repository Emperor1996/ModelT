package com.sap.retail.oaa.commerce.services.reservation;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import com.sap.retail.oaa.commerce.services.reservation.exception.ReservationException;
import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.response.ReservationResponse;
import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;



/**
 * Reservation Service which calls the OAA reservation service to update a reservation to an order, after the order is
 * confirmed.
 */
public interface ReservationService
{

	/**
	 * Calls the REST service to update the temporary reservation. This only updates the Status and the Order ID of the
	 * temporary reservation in the Backend, other changes which where done in the order are not changed in the
	 * reservation - new Item, changed items etc.
	 *
	 * @param abstractOrderModel
	 * @param reservationStatus
	 * @return reservationResponse
	 */
	ReservationResponse updateReservation(final AbstractOrderModel abstractOrderModel, String reservationStatus)
			throws ReservationException, CARBackendDownException;

	/**
	 * Deletes entire reservation in CAR.
	 *
	 * @param abstractOrderModel
	 */
	void deleteReservation(final AbstractOrderModel abstractOrderModel) throws ReservationException, CARBackendDownException;

	/**
	 * Deletes reservation entry in CAR.
	 *
	 * @param abstractOrderModel
	 * @param abstractOrderEntryModel
	 */
	void deleteReservationItem(final AbstractOrderModel abstractOrderModel, final AbstractOrderEntryModel abstractOrderEntryModel)
			throws ReservationException, CARBackendDownException;
}

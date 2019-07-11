/*****************************************************************************
    Class:        DefaultOaaSessionCloseStrategy
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.session.impl;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.web.DefaultSessionCloseStrategy;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sap.retail.oaa.commerce.services.reservation.strategy.ReservationStrategy;


/**
 * Default OAA Session Close Strategy. Deletes Reservation, when session is closed.
 */
public class DefaultOaaSessionCloseStrategy extends DefaultSessionCloseStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultOaaSessionCloseStrategy.class);

	private CartService cartService;
	private ReservationStrategy reservationStrategy;


	@Override
	public void closeJaloSession(final JaloSession session)
	{
		LOG.info("Delete JALO Session");

		this.getReservationStrategy().deleteReservation(this.getCartService().getSessionCart());

		super.closeJaloSession(session);
	}

	@Override
	public void closeSessionInHttpSession(final HttpSession session)
	{
		LOG.info("Delete HTTP Session");

		this.getReservationStrategy().deleteReservation(this.getCartService().getSessionCart());

		super.closeSessionInHttpSession(session);
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the reservationStrategy
	 */
	public ReservationStrategy getReservationStrategy()
	{
		return reservationStrategy;
	}

	/**
	 * @param reservationStrategy
	 *           the reservationStrategy to set
	 */
	public void setReservationStrategy(final ReservationStrategy reservationStrategy)
	{
		this.reservationStrategy = reservationStrategy;
	}
}

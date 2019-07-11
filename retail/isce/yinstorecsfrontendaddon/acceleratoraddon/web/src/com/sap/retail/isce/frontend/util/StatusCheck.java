/*****************************************************************************
 Interface:        StatusCheck
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.util;

import de.hybris.platform.commercefacades.order.data.OrderData;


/**
 *
 * Interface to check status of the order, dependent on the status handover button is shown(order is ready to pick up
 * and agent's store matches the pickup location ) or link to show the invoice document ( order picked up successfully
 * and invoice document is generated)
 *
 */
public interface StatusCheck
{
	/**
	 * @param orderData
	 * @return true when hand over button should be shown
	 */
	public Boolean isHandoverButtonShown(final OrderData orderData);

	/**
	 * @param orderData
	 * @return true when link to the invoice document should be shown
	 */
	public Boolean isInvoiceLinkShown(final OrderData orderData);
}
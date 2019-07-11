/*****************************************************************************
 Class:        StatusCheckFactory
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.util.impl;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.Registry;

import com.sap.retail.isce.frontend.util.StatusCheck;


/**
 * Factory class generate the StatusCheck class. Using the factory class the StatusCheck can be easily enhanced by
 * customer The new status check needs to be introduced as a bean "instorecsStatusCheck"
 *
 */
public class StatusCheckFactory
{
	private static StatusCheck statusCheck;

	private StatusCheckFactory()
	{
		// private constructor to hide the implicit public one
	}

	static void setStatusCheck(final StatusCheck statusCheck)
	{
		StatusCheckFactory.statusCheck = statusCheck;
	}

	private static StatusCheck getStatusCheck()
	{
		if (statusCheck == null)
		{
			statusCheck = (StatusCheck) Registry.getApplicationContext().getBean("instorecsStatusCheck");
		}
		return statusCheck;
	}

	public static Boolean isHandoverButtonShown(final OrderData orderData)
	{
		return getStatusCheck().isHandoverButtonShown(orderData);
	}

	public static Boolean isInvoiceLinkShown(final OrderData orderData)
	{
		return getStatusCheck().isInvoiceLinkShown(orderData);
	}
}

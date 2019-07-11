/*****************************************************************************
    Class:    SapoaacommerceservicesConstants
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.constants;

/**
 * Global class for all Sapoaacommerceservices constants. You can add global constants for your extension into this
 * class.
 */
public final class SapoaacommerceservicesConstants extends GeneratedSapoaacommerceservicesConstants
{
	public static final String EXTENSIONNAME = "sapoaacommerceservices";

	/** Indicates the reservation status in the mode order */
	public static final String RESERVATION_STATUS_ORDER = "O";
	/** Indicates the reservation status in the mode cart */
	public static final String RESERVATION_STATUS_CART = "C";
	/** Error message for offline scenario */
	public static final String CAR_BACKEND_DOWN_MESSAGE = "CAR Backend is not responding";

	private SapoaacommerceservicesConstants()
	{
		//empty to avoid instantiating this constant class
	}
}

/*****************************************************************************
    Interface:    OaaCheckoutFacade
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.facades.checkout;

import de.hybris.platform.commercefacades.order.CheckoutFacade;


/**
 * Checkout Facade for OAA Logic. Adds Sourcing for Session Cart.
 */
public interface OaaCheckoutFacade extends CheckoutFacade
{
	public boolean doSourcingForSessionCart();
}
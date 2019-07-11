/*****************************************************************************
    Interface:    OaaAcceleratorCheckoutFacade
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.acceleratorfacades.order;

import de.hybris.platform.acceleratorfacades.order.AcceleratorCheckoutFacade;


/**
 * Accelerator checkout facade for enabling OAA in express checkout functionality.
 */
public interface OaaAcceleratorCheckoutFacade extends AcceleratorCheckoutFacade
{
	public boolean doSourcingForExpressCheckoutCart();

}

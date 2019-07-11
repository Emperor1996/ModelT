/*****************************************************************************
 Class:        DefaultInstorecsAssistedServiceService
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import de.hybris.platform.assistedserviceservices.impl.DefaultAssistedServiceService;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;


public class DefaultInstorecsAssistedServiceService extends DefaultAssistedServiceService
{
	@Override
	public CustomerAccountService getCustomerAccountService()
	{
		// increase visibility, super method has only scope 'protected'
		return super.getCustomerAccountService();
	}
}

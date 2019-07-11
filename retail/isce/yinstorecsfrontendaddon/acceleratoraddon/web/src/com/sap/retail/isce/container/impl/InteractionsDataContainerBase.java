/*****************************************************************************
 Class:        InteractionsDataContainerBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Retrieve information on customer interactions from Hybris marketing
 */
public abstract class InteractionsDataContainerBase extends DataContainerODataDefaultImpl
{
	/**
	 * Constants
	 */
	protected static final Integer INTEGER_ONE = Integer.valueOf(1);

	protected static final String SERVICE_URI = "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV";
	protected static final String SERVICE_ENDPOINT_NAME = "Interactions";

	protected static final String PROPERTY_INTERACTION_TYPE_CODE = "InteractionTypeCode";
	protected static final String PROPERTY_INTERACTION_TYPE_DESCRIPTION = "InteractionTypeDescription";
	protected static final String PROPERTY_TIMESTAMP = "Timestamp";

	/**
	 * Default constructor.
	 */
	public InteractionsDataContainerBase(final ISCEConfigurationService isceConfigurationService)
	{
		this.serviceURI = SERVICE_URI;
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.resultName = SERVICE_ENDPOINT_NAME;
		if (isceConfigurationService != null && isceConfigurationService.getYMktHttpDestination() != null)
		{
			this.httpDestinationName = isceConfigurationService.getYMktHttpDestination().getHttpDestinationName();
		}
	}

	@Override
	public String getFilter()
	{
		this.filter = new StringBuilder("ContactId eq '").append(getCustomerData().getUid())
				.append("' and ContactIdOrigin eq 'EMAIL'").toString();
		return filter;
	}

	/**
	 * Get customer data from context map and check for being null. If yes, throw exception.
	 *
	 * @return customerData the customerData after checking for null
	 */
	protected CustomerData getCustomerData()
	{
		final CustomerData customerData = (CustomerData) this.dataContainerContext.getContextMap().get(
				DataContainerContext.CUSTOMER_DATA);
		if (customerData == null)
		{
			throw new DataContainerRuntimeException("CustomerData could not be retrieved from ContextMap");
		}
		return customerData;
	}

}

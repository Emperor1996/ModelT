/*****************************************************************************
Class: PPSClient
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing;

import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;

import com.sap.ppengine.client.dto.PriceCalculate;
import com.sap.ppengine.client.dto.PriceCalculateResponse;


/**
 * Client for the Promotion Pricing Service
 *
 */
public interface PPSClient
{
	/**
	 * Call promotion pricing service
	 *
	 * @param priceCalculate
	 *           {@link com.sap.ppengine.client.dto.PriceCalculate}
	 * @param sapConfig
	 *           SAP base store configuration
	 * @return {@link com.sap.ppengine.client.dto.PriceCalculateResponse}
	 */
	PriceCalculateResponse callPPS(PriceCalculate priceCalculate, SAPConfigurationModel sapConfig);

}

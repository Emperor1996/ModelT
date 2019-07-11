/*****************************************************************************
    Interface:    ATPStrategy
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.atp.strategy;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import com.sap.retail.oaa.commerce.services.atp.pojos.ATPAvailability;


/**
 * Strategy for Omni Channel Availability Sourcing
 */
public interface ATPAggregationStrategy
{
	/**
	 * Aggregate the Schedule Lines Quantity returned by the ATP Service
	 *
	 * @param cartGuid
	 * @param productModel
	 * @param pointOfServiceModel
	 */
	public Long aggregateAvailability(final String cartGuid, final ProductModel productModel,
			final PointOfServiceModel pointOfServiceModel, final List<ATPAvailability> availabilities);

}

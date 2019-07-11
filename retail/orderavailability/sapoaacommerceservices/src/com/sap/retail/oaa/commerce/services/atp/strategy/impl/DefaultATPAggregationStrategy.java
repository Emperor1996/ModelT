/*****************************************************************************
    Class:    DefaultATPAggregationStrategy
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.atp.strategy.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import com.sap.retail.oaa.commerce.services.atp.pojos.ATPAvailability;
import com.sap.retail.oaa.commerce.services.atp.strategy.ATPAggregationStrategy;


/**
 * Default Implementation for ATPStrategy
 */
public class DefaultATPAggregationStrategy implements ATPAggregationStrategy
{

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.oaa.commerce.services.atp.strategy.ATPStrategy#getAvailableStockLevel(java.lang.String,
	 * de.hybris.platform.core.model.product.ProductModel, de.hybris.platform.storelocator.model.PointOfServiceModel)
	 */
	@Override
	public Long aggregateAvailability(final String cartGuid, final ProductModel productModel,
			final PointOfServiceModel pointOfServiceModel, final List<ATPAvailability> availabilities)
	{
		final long availableStock = availabilities.stream().mapToLong(a -> a.getQuantity().longValue()).sum();

		return new Long(availableStock);
	}
}
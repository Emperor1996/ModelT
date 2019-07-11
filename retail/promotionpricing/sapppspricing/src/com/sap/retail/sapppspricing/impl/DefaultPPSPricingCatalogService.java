/*****************************************************************************
Class: DefaultPPSPricingCatalogService
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.commerceservices.price.impl.NetPriceService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;

import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.PricingBackend;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;


/**
 * Catalog price calculation via PPS if set to active
 */
public class DefaultPPSPricingCatalogService extends NetPriceService
{
	private PricingBackend pricingBackend;
	private PPSConfigService configService;

	@Override
	public List<PriceInformation> getPriceInformationsForProduct(final ProductModel model)
	{
		try
		{
			return getConfigService().isPpsActive(model) ? getPricingBackend().readPriceInformationForProducts(
					Collections.singletonList(model), getNetGrossStrategy().isNet()) : super.getPriceInformationsForProduct(model);
		}
		catch (final RestClientException e)
		{
			throw new SapPPSPricingRuntimeException(e);
		}
	}

	@SuppressWarnings("javadoc")
	public PricingBackend getPricingBackend()
	{
		return pricingBackend;
	}

	@SuppressWarnings("javadoc")
	public void setPricingBackend(final PricingBackend pricingBackend)
	{
		this.pricingBackend = pricingBackend;
	}

	@SuppressWarnings("javadoc")
	public PPSConfigService getConfigService()
	{
		return configService;
	}

	@SuppressWarnings("javadoc")
	public void setConfigService(final PPSConfigService configService)
	{
		this.configService = configService;
	}

}

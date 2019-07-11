/*****************************************************************************
Class: PricingBackendPPS

@Copyright (c) 2016, SAP SE, Germany, All rights reserved.

 *****************************************************************************/

package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestClientException;

import com.sap.ppengine.client.PPSRuntimeException;
import com.sap.ppengine.client.dto.ExtendedAmountType;
import com.sap.ppengine.client.dto.ItemDomainSpecific;
import com.sap.ppengine.client.dto.PriceCalculate;
import com.sap.ppengine.client.dto.PriceCalculateResponse;
import com.sap.ppengine.client.util.PPSClientBeanAccessor;
import com.sap.retail.sapppspricing.Cache;
import com.sap.retail.sapppspricing.PPSClient;
import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.PPSRequestCreator;
import com.sap.retail.sapppspricing.PriceCalculateToOrderMapper;
import com.sap.retail.sapppspricing.PricingBackend;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;


/**
 * Implementation of {@link PricingBackend} performing the calls for catalog or basket pricing against an embedded or
 * remote PPS. Takes care for request creation as well as mapping the response to the corresponding hybris objects
 */
public class PricingBackendPPS implements PricingBackend
{
	private double highPrice;
	private boolean useHighPrice = true;
	private static final Logger LOG = LoggerFactory.getLogger(PricingBackendPPS.class);
	private PPSClient ppsClient;
	private Cache<PriceInformation, ProductModel> catalogPriceCache;
	private PPSClientBeanAccessor accessor;
	private List<PriceCalculateToOrderMapper> resultToOrderMappers;
	private PPSRequestCreator requestCreator;
	private CommonI18NService commonI18NService;
	private PPSConfigService configService;

	@Override
	public void readPricesForCart(final AbstractOrderModel order) throws RestClientException
	{
		LOG.debug("entering readPricesForCart()");
		if (order.getEntries().isEmpty())
		{
			return;
		}
		final PriceCalculate priceCalculate = getRequestCreator().createRequestForCart(order);

		try
		{
			final PriceCalculateResponse response = getPpsClient().callPPS(priceCalculate, order.getStore().getSAPConfiguration());
			for (final PriceCalculateToOrderMapper mapper : getResultToOrderMappers())
			{
				mapper.map(response, order);
			}
		}
		catch (final RestClientException | PPSRuntimeException e)
		{
			throw new SapPPSPricingRuntimeException("Calculation for AbstractOrder " + order.getCode() + " failed", e);
		}
		LOG.debug("exiting");
	}

	@Override
	public List<PriceInformation> readPriceInformationForProducts(final List<ProductModel> productModels, final boolean isNet)
	{
		LOG.debug("entering readPriceInformationForProducts()");
		final String expectedCurrencyCode = getCommonI18NService().getCurrentCurrency().getIsocode();

		final List<PriceInformation> result = readPriceInformationForProducts(productModels, expectedCurrencyCode, isNet);
		LOG.debug("exiting");
		return result;
	}

	@Deprecated
	protected PriceInformation readPriceInformationForProduct(final ProductModel prod, final String expectedCurrencyCode,
			final boolean isNet)
	{
		final List<PriceInformation> result = readPriceInformationForProducts(Collections.singletonList(prod),
				expectedCurrencyCode, isNet);
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}

	protected List<PriceInformation> readPriceInformationForProducts(final List<ProductModel> prods,
			final String expectedCurrencyCode, final boolean isNet)
	{
		final List<ProductModel> ppsProds = new ArrayList<>();
		final List<PriceInformation> result = new ArrayList<>(prods.size());
		final Map<ProductModel, Integer> prodIndexes = new HashMap<>();
		for (int i = 0; i < prods.size(); i++)
		{
			final ProductModel prod = prods.get(i);
			final PriceInformation pinfo = getCatalogPriceCache().read(prod);
			if (pinfo == null)
			{
				result.add(null); // Placeholder for later result
				ppsProds.add(prod);
				prodIndexes.put(prod, i);
			}
			else
			{
				result.add(pinfo);
			}
		}
		final Map<ProductModel, PriceInformation> ppsPinfos = readPriceInfosFromPps(ppsProds, isNet);
		if (ppsPinfos != null)
		{
			for (final Entry<ProductModel, PriceInformation> ppsPinfo : ppsPinfos.entrySet())
			{
				PriceInformation pinfo = ppsPinfo.getValue();
				final ProductModel prod = ppsPinfo.getKey();
				final String actualCurrencyCode = pinfo.getPriceValue().getCurrencyIso();
				if (!expectedCurrencyCode.equals(actualCurrencyCode))
				{
					LOG.warn("Unexpected currency code {} for item {}, setting to {}", actualCurrencyCode, prod.getCode(),
							expectedCurrencyCode);
					pinfo = new PriceInformation(new PriceValue(expectedCurrencyCode, pinfo.getPriceValue().getValue(), true));
				}
				// Put into cache only if price looks reasonable
				if (pinfo.getPriceValue().getValue() < getHighPrice())
				{
					getCatalogPriceCache().write(ppsPinfo.getKey(), pinfo);
				}
				result.set(prodIndexes.get(prod), pinfo);
			}
		}
		return result;
	}

	@Deprecated
	protected PriceInformation readPriceInfoFromPps(final ProductModel prod, final boolean isNet)
	{
		final Map<ProductModel, PriceInformation> result = readPriceInfosFromPps(Collections.singletonList(prod), isNet);
		return result == null ? null : result.get(prod);
	}


	// This method needs to be adjusted once we support the line item mode - only for Client API version 2 or higher!
	protected Map<ProductModel, PriceInformation> readPriceInfosFromPps(final List<ProductModel> prods, final boolean isNet)
	{
		final Map<ProductModel, PriceInformation> result = new HashMap<>();
		final SAPConfigurationModel sapConfig = CollectionUtils.isEmpty(prods) ? null : getConfigService().getSapConfig(
				prods.get(0));
		for (final ProductModel prod : prods)
		{
			PriceInformation pinfo = null;
			boolean isPinfo = true;
			final PriceCalculate priceCalculate = getRequestCreator().createRequestForCatalog(prod, isNet);
			double calculatedPrice;
			String currencyCode = "";
			try
			{
				final PriceCalculateResponse response = getPpsClient().callPPS(priceCalculate, sapConfig);
				final ItemDomainSpecific lineItemContent = getAccessor().getHelper().getLineItemContent(
						response.getPriceCalculateBody().get(0).getShoppingBasket().getLineItem().get(0));
				if (lineItemContent != null)
				{
					final ExtendedAmountType extendedAmount = lineItemContent.getExtendedAmount();
					calculatedPrice = extendedAmount.getValue().doubleValue();
					currencyCode = extendedAmount.getCurrency();
				}
				else
				{
					throw new SapPPSPricingRuntimeException("Line item missing in response");
				}
			}
			catch (final RuntimeException e)
			{
				calculatedPrice = getHighPrice();
				if (!isUseHighPrice())
				{
					LOG.error("Could not determine catalog price for product " + prod.getCode(), e);
					isPinfo = false;
				}

				else
				{
					LOG.error("Could not determine catalog price for product " + prod.getCode() + ". Defaulting price to " + calculatedPrice, e);
				}
			}
			if (isPinfo)
			{
				// Special handling as long as currency code is not returned by PPS - null is not allowed for a PriceValue
				pinfo = new PriceInformation(new PriceValue(currencyCode == null ? "" : currencyCode, calculatedPrice, true));
			}
			result.put(prod, pinfo);
		}
		return result;
	}

	@SuppressWarnings("javadoc")
	public PPSClient getPpsClient()
	{
		return ppsClient;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setPpsClient(final PPSClient ppsClient)
	{
		this.ppsClient = ppsClient;
	}

	@SuppressWarnings("javadoc")
	public Cache<PriceInformation, ProductModel> getCatalogPriceCache()
	{
		return catalogPriceCache;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setCatalogPriceCache(final Cache<PriceInformation, ProductModel> catalogPriceCache)
	{
		this.catalogPriceCache = catalogPriceCache;
	}

	@SuppressWarnings("javadoc")
	public PPSClientBeanAccessor getAccessor()
	{
		return accessor;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setAccessor(final PPSClientBeanAccessor accessor)
	{
		this.accessor = accessor;
	}

	@SuppressWarnings("javadoc")
	public List<PriceCalculateToOrderMapper> getResultToOrderMappers()
	{
		return resultToOrderMappers;
	}

	@SuppressWarnings("javadoc")
	public void setResultToOrderMappers(final List<PriceCalculateToOrderMapper> resultToOrderMappers)
	{
		this.resultToOrderMappers = resultToOrderMappers;
		Collections.sort(this.resultToOrderMappers, new OrderedComparator());
		if (LOG.isDebugEnabled())
		{
			LOG.debug("List of PriceCalculate result mappers after sorting: " + this.resultToOrderMappers);
		}
	}

	@SuppressWarnings("javadoc")
	public PPSRequestCreator getRequestCreator()
	{
		return requestCreator;
	}

	@SuppressWarnings("javadoc")
	public void setRequestCreator(final PPSRequestCreator creator)
	{
		this.requestCreator = creator;
	}

	private static class OrderedComparator implements Comparator<Ordered>
	{
		@Override
		public int compare(final Ordered arg0, final Ordered arg1)
		{
			return Integer.valueOf(arg0.getOrder()).compareTo(Integer.valueOf(arg1.getOrder()));
		}
	}

	@SuppressWarnings("javadoc")
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@SuppressWarnings("javadoc")
	public double getHighPrice()
	{
		return highPrice;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setHighPrice(final double highPrice)
	{
		this.highPrice = highPrice;
	}

	@SuppressWarnings("javadoc")
	public PPSConfigService getConfigService()
	{
		return configService;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setConfigService(final PPSConfigService configService)
	{
		this.configService = configService;
	}

	@SuppressWarnings("javadoc")
	public boolean isUseHighPrice()
	{
		return useHighPrice;
	}

	@SuppressWarnings("javadoc")
	public void setUseHighPrice(final boolean useHighPrice)
	{
		this.useHighPrice = useHighPrice;
	}

}

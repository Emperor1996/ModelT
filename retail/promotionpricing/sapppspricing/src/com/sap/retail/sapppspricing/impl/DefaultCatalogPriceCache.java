/*****************************************************************************
Class: DefaultCatalogPriceCache
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.AbstractCacheKey;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.region.CacheRegion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.sapppspricing.Cache;
import com.sap.retail.sapppspricing.PPSConfigService;


/**
 * Wrapper for accessing catalog prices of products in the hybris cache
 */
public class DefaultCatalogPriceCache implements Cache<PriceInformation, ProductModel>
{
	/**
	 * Name of cache region for PPS catalog prices
	 */
	public static final String PPS_CATALOGPRICE = "SAP_PPS_CATALOGPRICE";
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCatalogPriceCache.class);
	private CacheRegion cacheRegion;
	private PPSConfigService configService;

	@Override
	public PriceInformation read(final ProductModel productModel)
	{
		PriceInformation result = null;
		LOG.debug("entering readCachedPriceInformationForProduct(...)");
		if (getConfigService().isCacheCatalogPrices(productModel))
		{
			result = (PriceInformation) cacheRegion.get(cacheKeyFor(productModel));
			LOG.debug("product={}, result={}", productModel.getCode(), result);
		}
		LOG.debug("exiting");
		return result;
	}

	@Override
	public void write(final ProductModel productModel, final PriceInformation priceInformation)
	{
		LOG.debug("entering cachePriceInformationForProduct(...)");
		try
		{
			if (getConfigService().isCacheCatalogPrices(productModel))
			{
				LOG.debug("Caching price for product {}", productModel.getCode());
				put(cacheKeyFor(productModel), priceInformation);
			}
		}
		finally
		{
			LOG.debug("exiting");
		}
	}

	protected void put(final CacheKey key, final Object object)
	{
		final DefaultCacheValueLoaderImpl loader = new DefaultCacheValueLoaderImpl();
		loader.setValue(object);
		cacheRegion.remove(key, false);
		LOG.debug("Object with following key(s) put into cache: {}", key);
		cacheRegion.getWithLoader(key, loader);
	}

	// We assume that PK uniquely identifies the item. Currency not part of the
	// key since we expect it actually
	// as part of the REPONSE and do not know it beforehand
	protected CacheKey cacheKeyFor(final ProductModel productModel)
	{
		return new CatalogPriceCacheKey(productModel.getPk(), Registry.getCurrentTenant().getTenantID());
	}

	@SuppressWarnings("javadoc")
	public CacheRegion getCacheRegion()
	{
		return cacheRegion;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setCacheRegion(final CacheRegion cacheRegion)
	{
		this.cacheRegion = cacheRegion;
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

	protected static class DefaultCacheValueLoaderImpl implements CacheValueLoader<Object>
	{
		private Object obj;

		@Override
		public Object load(final CacheKey arg0) throws CacheValueLoadException
		{
			return this.obj;
		}

		public void setValue(final Object obj)
		{
			this.obj = obj;
		}
	}

	protected static class CatalogPriceCacheKey extends AbstractCacheKey
	{
		private final PK productKey;

		public CatalogPriceCacheKey(final PK productKey, final String tenantId)
		{
			super(PPS_CATALOGPRICE, tenantId);
			this.productKey = productKey;
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == null || !super.equals(obj))
			{
				return false;
			}
			final CatalogPriceCacheKey other = (CatalogPriceCacheKey) obj;
			return productKey != null && productKey.equals(other.productKey);
		}

		@Override
		public int hashCode()
		{
			return super.hashCode() * 31 + productKey.hashCode();
		}

	}

}

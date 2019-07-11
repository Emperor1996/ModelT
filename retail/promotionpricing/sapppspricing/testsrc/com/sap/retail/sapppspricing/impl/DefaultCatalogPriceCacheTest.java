/*****************************************************************************
Class: DefaultCatalogPriceCacheTest
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.regioncache.key.CacheUnitValueType;
import de.hybris.platform.regioncache.region.CacheRegion;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.util.PriceValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.impl.DefaultCatalogPriceCache;
import com.sap.retail.sapppspricing.impl.DefaultCatalogPriceCache.CatalogPriceCacheKey;
import com.sap.retail.sapppspricing.impl.DefaultCatalogPriceCache.DefaultCacheValueLoaderImpl;


@SuppressWarnings("javadoc")
@UnitTest
public class DefaultCatalogPriceCacheTest
{

	@Mock
	private CacheRegion cacheRegionMock;
	@Mock
	private PPSConfigService configService;
	private ProductModel productModel;
	private DefaultCatalogPriceCache cut;

	@Before
	public void setUp()
	{
		cut = new DefaultCatalogPriceCache();
		productModel = new ProductModel();
		MockitoAnnotations.initMocks(this);
		Mockito.when(configService.isCacheCatalogPrices(productModel)).thenReturn(Boolean.TRUE);
		cut.setConfigService(configService);
	}

	@Test
	public void testSetGetBaseConfigService()
	{
		cut = new DefaultCatalogPriceCache();
		assertNull(cut.getConfigService());
		cut.setConfigService(configService);
		assertSame(configService, cut.getConfigService());
	}

	@Test
	public void testSetGetCacheRegion()
	{
		assertNull(cut.getCacheRegion());
		cut.setCacheRegion(cacheRegionMock);
		assertSame(cacheRegionMock, cut.getCacheRegion());
	}

	@Test
	public void testCacheKeyForPKEmpty()
	{
		final CacheKey cacheKeyFor = cut.cacheKeyFor(productModel);
		assertEquals("junit", cacheKeyFor.getTenantId());
		assertEquals(CacheUnitValueType.NON_SERIALIZABLE, cacheKeyFor.getCacheValueType());
		assertEquals(DefaultCatalogPriceCache.PPS_CATALOGPRICE, cacheKeyFor.getTypeCode().toString());
	}

	@Test
	public void testWritePKEmpty() throws Exception
	{
		final PriceValue priceValue = new PriceValue("EUR", Double.parseDouble("11"), false);
		final PriceInformation priceInformation = new PriceInformation(priceValue);
		cut.setCacheRegion(this.cacheRegionMock);
		cut.write(productModel, priceInformation);
		Mockito.verify(cacheRegionMock).getWithLoader(Mockito.any(CacheKey.class), Mockito.any(DefaultCacheValueLoaderImpl.class));
	}

	@Test(expected = CacheValueLoadException.class)
	public void testWriteException() throws Exception
	{
		final PriceInformation priceInformation = new PriceInformation(new PriceValue("EUR", Double.parseDouble("11"), false));
		cut.setCacheRegion(this.cacheRegionMock);
		Mockito.doThrow(new CacheValueLoadException("bla")).when(cacheRegionMock)
				.getWithLoader(Mockito.any(CacheKey.class), Mockito.any(CacheValueLoader.class));
		cut.write(productModel, priceInformation);
	}

	@Test
	public void testReadPKIsEmpty()
	{
		final PriceInformation priceInformation = new PriceInformation(new PriceValue("EUR", Double.parseDouble("11"), false));
		cut.setCacheRegion(this.cacheRegionMock);
		cut.read(productModel);
		Mockito.when(cacheRegionMock.get(Mockito.any(CacheKey.class))).thenReturn(priceInformation);
		assertSame(priceInformation, cut.read(productModel));
	}

	@Test
	public void testLoader() throws Exception
	{
		final DefaultCacheValueLoaderImpl loader = new DefaultCacheValueLoaderImpl();
		loader.setValue("bla");
		assertEquals("bla", loader.load(null));
	}

	@Test
	public void testCacheKey() throws Exception
	{
		final CatalogPriceCacheKey key1 = new CatalogPriceCacheKey(PK.fromLong(17), "tenant");
		final CatalogPriceCacheKey key2 = new CatalogPriceCacheKey(PK.fromLong(17), "tenant");
		final CatalogPriceCacheKey key3 = new CatalogPriceCacheKey(PK.fromLong(17), "bla");
		final CatalogPriceCacheKey key4 = new CatalogPriceCacheKey(PK.fromLong(18), "tenant");
		assertEquals(key1, key2);
		assertEquals(key1.hashCode(), key2.hashCode());
		assertFalse(key1.equals(null));
		assertFalse(key1.equals(key3));
		assertFalse(key1.hashCode() == key3.hashCode());
		assertFalse(key1.equals(key4));
		assertFalse(key1.hashCode() == key4.hashCode());
	}
}

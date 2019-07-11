/*****************************************************************************
    Class:        RoughStockIndicatorValueResolver
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.search.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.store.services.BaseStoreService;

import com.sap.retail.oaa.commerce.services.stock.impl.DefaultSapOaaCommerceStockService;


/**
 * SOLR resolver for OAA rough stock indicator.
 */
public class RoughStockIndicatorValueResolver extends AbstractValueResolver<ProductModel, String, Object>
{
	private DefaultSapOaaCommerceStockService oaaStockService;
	private BaseStoreService baseStoreService = null;


	@Override
	protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
			final IndexedProperty indexedProperty, final ProductModel productModel,
			final ValueResolverContext<String, Object> resolverContext) throws FieldValueProviderException
	{
		document.addField(indexedProperty,
				oaaStockService.getStockLevelStatusForProductAndBaseStore(productModel, baseStoreService.getCurrentBaseStore()),
				resolverContext.getFieldQualifier());
	}

	/**
	 * @return the baseStoreService
	 */
	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the oaaStockService
	 */
	protected DefaultSapOaaCommerceStockService getOaaStockService()
	{
		return oaaStockService;
	}

	/**
	 * @param oaaStockService
	 *           the oaaStockService to set
	 */
	public void setOaaStockService(final DefaultSapOaaCommerceStockService oaaStockService)
	{
		this.oaaStockService = oaaStockService;
	}
}

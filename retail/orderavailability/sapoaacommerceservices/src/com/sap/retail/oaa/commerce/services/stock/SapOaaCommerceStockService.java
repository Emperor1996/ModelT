/*****************************************************************************
    Interface:    SapOaaCommerceStockService
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.stock;

import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.List;

import com.sap.retail.oaa.commerce.services.atp.exception.ATPException;
import com.sap.retail.oaa.commerce.services.atp.pojos.ATPAvailability;
import com.sap.retail.oaa.commerce.services.atp.pojos.ATPProductAvailability;
import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;



/**
 * Stock Service used in Omni Channel Commerce.
 */
public interface SapOaaCommerceStockService extends CommerceStockService
{
	/**
	 * Get the availability for a given cart item.
	 *
	 * @param cartGuid
	 * @param product
	 * @return list of availability
	 */
	public List<ATPAvailability> getAvailabilityForProduct(final String cartGuid, String itemId, final ProductModel product)
			throws ATPException, CARBackendDownException;

	/**
	 * Get the availability for a given product and source.
	 *
	 * @param product
	 * @param source
	 * @return list of availability
	 */
	public List<ATPAvailability> getAvailabilityForProductAndSource(final ProductModel product, final String source)
			throws ATPException, CARBackendDownException;

	/**
	 * Get the availability for a given cart item and source.
	 *
	 * @param cartGuid
	 * @param product
	 * @param source
	 * @return list of availability
	 */
	public List<ATPAvailability> getAvailabilityForProductAndSource(final String cartGuid, String itemId,
			final ProductModel product, final String source) throws ATPException, CARBackendDownException;

	/**
	 * Get the availability for a given cart items and corresponding product unit.
	 *
	 * @param cartGuid
	 * @param productUnit
	 * @param productList
	 * @return list of ATP product availability
	 * @throws ATPException
	 */
	public List<ATPProductAvailability> getAvailabilityForProducts(final String cartGuid, String itemId, final String productUnit,
			final List<ProductModel> productList) throws ATPException, CARBackendDownException;

	/**
	 * Get the availability for a given cart item and sources.
	 *
	 * @param cartGuid
	 * @param product
	 * @param sourcesList
	 * @return list of ATP product availability
	 * @throws ATPException
	 */
	public List<ATPProductAvailability> getAvailabilityForProductAndSources(final String cartGuid, String itemId,
			final ProductModel product, final List<String> sourcesList) throws ATPException, CARBackendDownException;


	/**
	 * Get current stock information calling ATP Service (CAR)
	 *
	 * @param cartGuid
	 * @param productModel
	 * @return total aggregated ATP amount
	 */
	public Long getAvailableStockLevelForPos(final String cartGuid, final ProductModel productModel,
			final PointOfServiceModel pointOfService) throws ATPException, CARBackendDownException;

	/**
	 * @param product
	 * @param baseStore
	 * @return StockLevelModel
	 */
	public StockLevelModel getStockLevelForRSI(final ProductModel product, final BaseStoreModel baseStore);

	/**
	 * Get current stock information calling ATP Service (CAR)
	 *
	 * @param cartGuid
	 * @param productModel
	 * @return total aggregated ATP amount
	 */
	public Long getAvailableStockLevel(final String cartGuid, final String itemId, final ProductModel productModel,
			final PointOfServiceModel pointOfServiceModel) throws ATPException, CARBackendDownException;

}

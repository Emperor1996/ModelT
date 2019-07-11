/*****************************************************************************
    Interface:    ATPService
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.atp;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.sap.retail.oaa.commerce.services.atp.exception.ATPException;
import com.sap.retail.oaa.commerce.services.atp.pojos.ATPAvailability;
import com.sap.retail.oaa.commerce.services.atp.pojos.ATPProductAvailability;
import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;


/**
 * Service for aggregated availability
 */
public interface ATPService
{

	/**
	 * Call the REST Service in Customer Activity Repository (CAR) for a given product to get current product
	 * availability
	 *
	 * @param cartGuid
	 * @param product
	 * @return list of current product availability
	 * @throws ATPException
	 */
	List<ATPAvailability> callRestAvailabilityServiceForProduct(final String cartGuid, String itemId, final ProductModel product)
			throws ATPException, CARBackendDownException;

	/**
	 * Call the REST Service in Customer Activity Repository (CAR) for a given product to get current product
	 * availability in the given source
	 *
	 * @param product
	 * @param source
	 * @return list of current product location availability
	 * @throws ATPException
	 */
	List<ATPAvailability> callRestAvailabilityServiceForProductAndSource(final ProductModel product, final String source)
			throws ATPException, CARBackendDownException;

	/**
	 * Call the REST Service in Customer Activity Repository (CAR) for a given product to get current product
	 * availability in the given source
	 *
	 * @param cartGuid
	 * @param product
	 * @param source
	 * @return list of current product location availability
	 * @throws ATPException
	 */
	List<ATPAvailability> callRestAvailabilityServiceForProductAndSource(final String cartGuid, String itemId,
			final ProductModel product, final String source) throws ATPException, CARBackendDownException;

	/**
	 * Call the REST Service in Customer Activity Repository (CAR) for a given product list to get current product
	 * availability lines
	 *
	 * @param cartGuid
	 * @param itemIdList
	 *           Must have the same order as parameter productList
	 * @param productUnit
	 *           Single unit for all products
	 * @param productList
	 *           Must have the same order as parameter itemIdList
	 * @return list of current availability for given products
	 * @throws ATPException
	 */
	List<ATPProductAvailability> callRestAvailabilityServiceForProducts(final String cartGuid, List<String> itemIdList,
			final String productUnit, List<ProductModel> productList) throws ATPException, CARBackendDownException;


	/**
	 * Call the REST Service in Customer Activity Repository (CAR) for a given product and sources list to get current
	 * product availability lines
	 *
	 * @param cartGuid
	 * @param product
	 * @param sourcesList
	 * @return list of current availability for given product and sources
	 * @throws ATPException
	 */
	List<ATPProductAvailability> callRestAvailabilityServiceForProductAndSources(final String cartGuid, final String itemId,
			final ProductModel product, final List<String> sourcesList) throws ATPException, CARBackendDownException;

}

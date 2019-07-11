/*****************************************************************************
Class: PPSConfigService
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
package com.sap.retail.sapppspricing;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;

import com.sap.retail.sapppspricing.enums.InterfaceVersion;


/**
 * Access to the configuration relevant for price calculation via PPS
 */
public interface PPSConfigService
{
	/**
	 * @param prod
	 *           Product
	 * @return true if price calculation via PPS is active for given product
	 */
	boolean isPpsActive(ProductModel prod);

	/**
	 *
	 * @param order
	 *           cart / order
	 * @return true if price calculation via PPS is active for given order
	 */
	boolean isPpsActive(AbstractOrderModel order);

	/**
	 * @param prod
	 *           Product
	 * @return true if caching of catalog prices via the hybris cache is configured
	 */
	boolean isCacheCatalogPrices(ProductModel prod);

	/**
	 * @param prod
	 *           Product
	 * @return the business unit ID for which the price calculation shall take place for given product
	 */
	String getBusinessUnitId(ProductModel prod);

	/**
	 * @return the business unit ID for which the price calculation shall take place for given order
	 */
	String getBusinessUnitId(AbstractOrderModel order);

	/**
	 * @param prod
	 *           Product
	 * @return the relevant SAP base store configuration for this product
	 */
	SAPConfigurationModel getSapConfig(ProductModel prod);
	
	/**
	 * @return the client interface version for the given order
	 */
	InterfaceVersion getClientInterfaceVersion(AbstractOrderModel order);


	/**
	 * @return the client interface version for the given product
	 */
	InterfaceVersion getClientInterfaceVersion(ProductModel prod);
	
}

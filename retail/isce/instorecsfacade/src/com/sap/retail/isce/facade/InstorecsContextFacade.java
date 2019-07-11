/*****************************************************************************
 Interface:        InstorecsContextFacade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.facade;

import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.ProductModel;

import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;


/**
 *
 * The facade provides the store context of the store associate who is currently logged in. The
 * DefaultInstorecsAssistedServiceFacade is the default implementation of the InstorecsContextFacade interface. The
 * facade includes methods for determining the store to which the store associate is assigned.
 *
 */
public interface InstorecsContextFacade
{
	/**
	 * Gets store associate's store from the session context
	 *
	 * @return store information for the particular store associate
	 * @throws AssistedServiceAgentNotLoggedInException
	 * @throws StoreContextNotDefinedException
	 */
	public PointOfServiceData getEmployeeStore() throws AssistedServiceAgentNotLoggedInException, StoreContextNotDefinedException;

	/**
	 * Determines store associate's store using employee data
	 *
	 * @return store information for the particular store associate
	 * @throws AssistedServiceAgentNotLoggedInException
	 * @throws StoreContextNotDefinedException
	 */
	public PointOfServiceData determineStoreContext() throws AssistedServiceAgentNotLoggedInException,
			StoreContextNotDefinedException;

	/**
	 * @param productModel
	 * @return quantity of the product in the current store
	 */
	public Long getProductQtyInCurrentStore(ProductModel productModel);
}

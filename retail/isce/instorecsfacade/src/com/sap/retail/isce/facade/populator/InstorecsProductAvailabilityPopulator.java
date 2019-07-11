/*****************************************************************************
 Class:        InstorecsProductAvailabilityPopulator
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.populator;

import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.facade.InstorecsContextFacade;


/**
 *
 * This populator reads the quantity in current store from the model and adds it to ProductData DTO.
 *
 * @param <S>
 *           Product model
 * @param <T>
 *           Product Data
 */
public class InstorecsProductAvailabilityPopulator<S extends ProductModel, T extends ProductData> extends
		AbstractProductPopulator<S, T>
{
	private InstorecsContextFacade instorecsContextFacade;

	@Override
	public void populate(final S productModel, final T productData) throws ConversionException
	{
		final Long qtyInCurrentStore = instorecsContextFacade.getProductQtyInCurrentStore(productModel);
		productData.setQtyInCurrentStore(qtyInCurrentStore);
	}

	public InstorecsContextFacade getInstorecsContextFacade()
	{
		return instorecsContextFacade;
	}

	@Required
	public void setInstorecsContextFacade(final InstorecsContextFacade instorecsContextFacade)
	{
		this.instorecsContextFacade = instorecsContextFacade;
	}



}

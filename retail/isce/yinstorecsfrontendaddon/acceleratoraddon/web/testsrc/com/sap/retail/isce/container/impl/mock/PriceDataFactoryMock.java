/*****************************************************************************
 Class:        PriceDataFactoryMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;


/**
 * Unit test mock for PriceDataFactory
 */
public class PriceDataFactoryMock implements PriceDataFactory
{

	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final String currencyIso)
	{
		final PriceData price = new PriceData();
		price.setValue(value);
		price.setPriceType(priceType);
		price.setCurrencyIso(currencyIso);
		price.setFormattedValue(value.toString() + currencyIso);
		return price;
	}

	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{
		final PriceData price = new PriceData();
		price.setValue(value);
		price.setPriceType(priceType);
		price.setCurrencyIso(currency.getIsocode());
		price.setFormattedValue(value.toString() + currency.getSymbol());

		return price;
	}

}

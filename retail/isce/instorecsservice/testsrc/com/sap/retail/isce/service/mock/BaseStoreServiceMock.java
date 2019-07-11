/*****************************************************************************
 Class:        BaseStoreServiceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.mock;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.List;


/**
 * Unit test Mock for BaseStoreService
 */
public class BaseStoreServiceMock implements BaseStoreService
{

	private static final String STORE_PK_VALUE = "pk1";
	private final BaseStoreModel storeModel = new BaseStoreModel();

	public static final CurrencyModel usdCurrencyModel = new CurrencyModel();
	public static final CurrencyModel jpyCurrencyModel = new CurrencyModel();

	public boolean returnNoCurrentBaseStore = false; // NOSONAR
	public boolean returnNoDefaultCurrencyFromBaseStore = false; //NOSONAR

	static
	{
		usdCurrencyModel.setConversion(new Double(1));
		usdCurrencyModel.setIsocode("USD");
		usdCurrencyModel.setSymbol("USD");
		usdCurrencyModel.setDigits(Integer.valueOf(2));

		jpyCurrencyModel.setConversion(new Double(2));
		jpyCurrencyModel.setIsocode("JPY");
		jpyCurrencyModel.setSymbol("JPY");
		jpyCurrencyModel.setDigits(Integer.valueOf(0));
	}

	@Override
	public List<BaseStoreModel> getAllBaseStores()
	{
		return null; // NOSONAR
	}

	@Override
	public BaseStoreModel getBaseStoreForUid(final String arg0) throws AmbiguousIdentifierException, UnknownIdentifierException
	{
		return null;
	}

	@Override
	public BaseStoreModel getCurrentBaseStore()
	{
		if (returnNoCurrentBaseStore)
		{
			return null;
		}
		storeModel.setUid(STORE_PK_VALUE);
		if (returnNoDefaultCurrencyFromBaseStore)
		{
			storeModel.setDefaultCurrency(null);
		}
		else
		{
			storeModel.setDefaultCurrency(jpyCurrencyModel);
		}
		return storeModel;
	}
}

/*****************************************************************************
 Class:        CommonI18NServiceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.mock;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.List;
import java.util.Locale;


/**
 * Unit test mock class for CommonI18NService
 */
public class CommonI18NServiceMock implements CommonI18NService
{

	public static final CurrencyModel usdCurrencyModel = new CurrencyModel();
	public static final CurrencyModel jpyCurrencyModel = new CurrencyModel();
	public static final CurrencyModel dangerousCurrencyModel = new CurrencyModel();

	public boolean returnNoCurrentCurrency = false; // NOSONAR
	public boolean causeConvertAndRoundCurrencyInfinity = false; // NOSONAR

	private boolean useDangerousCurrency = false;

	static
	{
		usdCurrencyModel.setConversion(new Double(1));
		usdCurrencyModel.setIsocode("USD");
		usdCurrencyModel.setSymbol("USD");
		usdCurrencyModel.setDigits(Integer.valueOf(1));

		jpyCurrencyModel.setConversion(new Double(2));
		jpyCurrencyModel.setIsocode("JPY");
		jpyCurrencyModel.setSymbol("JPY");
		jpyCurrencyModel.setDigits(Integer.valueOf(0));

		dangerousCurrencyModel.setConversion(new Double(1));
		dangerousCurrencyModel.setIsocode("DGC");
		dangerousCurrencyModel.setSymbol("<alert>hi</alert>");
		dangerousCurrencyModel.setDigits(Integer.valueOf(2));
	}

	@Override
	public double convertAndRoundCurrency(final double arg0, final double arg1, final int arg2, final double arg3)
	{
		if (causeConvertAndRoundCurrencyInfinity)
		{
			return Double.MAX_VALUE * Double.MAX_VALUE;
		}
		return (arg3 * arg1) / arg0;
	}

	@Override
	public double convertCurrency(final double arg0, final double arg1, final double arg2)
	{
		return 0;
	}

	@Override
	public List<CountryModel> getAllCountries()
	{
		return null; // NOSONAR
	}

	@Override
	public List<CurrencyModel> getAllCurrencies()
	{
		return null; // NOSONAR
	}

	@Override
	public List<LanguageModel> getAllLanguages()
	{
		return null; // NOSONAR
	}

	@Override
	public List<RegionModel> getAllRegions()
	{
		return null; // NOSONAR
	}

	@Override
	public CurrencyModel getBaseCurrency()
	{
		return null;
	}

	@Override
	public CountryModel getCountry(final String arg0)
	{
		return null;
	}

	@Override
	public CurrencyModel getCurrency(final String arg0)
	{

		if ("USD".equals(arg0))
		{
			return CommonI18NServiceMock.usdCurrencyModel;
		}
		else if ("JPY".equals(arg0))
		{
			return CommonI18NServiceMock.jpyCurrencyModel;
		}

		throw new UnknownIdentifierException("Currency " + arg0 + " not known.");
	}

	@Override
	public CurrencyModel getCurrentCurrency()
	{
		if (returnNoCurrentCurrency)
		{
			return null;
		}
		if (useDangerousCurrency)
		{
			useDangerousCurrency = false;
			return CommonI18NServiceMock.dangerousCurrencyModel;
		}
		return CommonI18NServiceMock.usdCurrencyModel;
	}

	@Override
	public LanguageModel getCurrentLanguage()
	{
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");

		return languageModel;
	}

	@Override
	public LanguageModel getLanguage(final String arg0)
	{
		final LanguageModel languageModel = new LanguageModel();
		languageModel.setIsocode("en");

		return languageModel;
	}

	@Override
	public Locale getLocaleForLanguage(final LanguageModel arg0)
	{
		return null;
	}

	@Override
	public Locale getLocaleForIsoCode(final String arg0)
	{
		return null;
	}

	@Override
	public RegionModel getRegion(final CountryModel arg0, final String arg1)
	{
		return null;
	}

	@Override
	public double roundCurrency(final double arg0, final int arg1)
	{
		return 0;
	}

	@Override
	public void setCurrentCurrency(final CurrencyModel arg0)
	{
		final CurrencyModel currentCurrency = arg0;
		currentCurrency.getActive();
	}

	@Override
	public void setCurrentLanguage(final LanguageModel arg0)
	{
		final LanguageModel currentLanguage = arg0;
		currentLanguage.getActive();
	}

	/**
	 * Set the flag, to use the dangerous currency as default currency.
	 *
	 * @param useDangerousCurrency
	 *           the useDangerousCurrency to set
	 */
	public void setUseDangerousCurrency(final boolean useDangerousCurrency)
	{
		this.useDangerousCurrency = useDangerousCurrency;
	}

}
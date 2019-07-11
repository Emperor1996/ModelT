/*****************************************************************************
 Class:        I18NServiceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.mock;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;


public class I18NServiceMock implements I18NService
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getAllActiveLanguages()
	 */
	@Override
	public Set<LanguageModel> getAllActiveLanguages()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getAllCountries()
	 */
	@Override
	public Set<CountryModel> getAllCountries()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getAllCurrencies()
	 */
	@Override
	public Set<CurrencyModel> getAllCurrencies()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getAllLanguages()
	 */
	@Override
	public Set<LanguageModel> getAllLanguages()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getAllLocales(java.util.Locale)
	 */
	@Override
	public Locale[] getAllLocales(final Locale arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBaseCurrency()
	 */
	@Override
	public CurrencyModel getBaseCurrency()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBestMatchingJavaCurrency(java.lang.String)
	 */
	@Override
	public Currency getBestMatchingJavaCurrency(final String arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBestMatchingLocale(java.util.Locale)
	 */
	@Override
	public Locale getBestMatchingLocale(final Locale arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBundle(java.lang.String)
	 */
	@Override
	public ResourceBundle getBundle(final String arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBundle(java.lang.String, java.util.Locale[])
	 */
	@Override
	public ResourceBundle getBundle(final String arg0, final Locale[] arg1)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getBundle(java.lang.String, java.util.Locale[],
	 * java.lang.ClassLoader)
	 */
	@Override
	public ResourceBundle getBundle(final String arg0, final Locale[] arg1, final ClassLoader arg2)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCountry(java.lang.String)
	 */
	@Override
	public CountryModel getCountry(final String arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCurrency(java.lang.String)
	 */
	@Override
	public CurrencyModel getCurrency(final String arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCurrentCurrency()
	 */
	@Override
	public CurrencyModel getCurrentCurrency()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCurrentJavaCurrency()
	 */
	@Override
	public Currency getCurrentJavaCurrency()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCurrentLocale()
	 */
	@Override
	public Locale getCurrentLocale()
	{
		// YTODO Auto-generated method stub
		return new Locale("en");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getCurrentTimeZone()
	 */
	@Override
	public TimeZone getCurrentTimeZone()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.i18n.I18NService#getEnumLocalizedName(de.hybris.platform.core.HybrisEnumValue)
	 */
	@Override
	public String getEnumLocalizedName(final HybrisEnumValue arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getFallbackLocales(java.util.Locale)
	 */
	@Override
	public Locale[] getFallbackLocales(final Locale arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getLangPKFromLocale(java.util.Locale)
	 */
	@Override
	public PK getLangPKFromLocale(final Locale arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getLanguage(java.lang.String)
	 */
	@Override
	public LanguageModel getLanguage(final String arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getSupportedDataLocales()
	 */
	@Override
	public Set<Locale> getSupportedDataLocales()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getSupportedJavaCurrencies()
	 */
	@Override
	public Set<Currency> getSupportedJavaCurrencies()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#getSupportedLocales()
	 */
	@Override
	public Set<Locale> getSupportedLocales()
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#isLocalizationFallbackEnabled()
	 */
	@Override
	public boolean isLocalizationFallbackEnabled()
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.i18n.I18NService#setCurrentCurrency(de.hybris.platform.core.model.c2l.CurrencyModel
	 * )
	 */
	@Override
	public void setCurrentCurrency(final CurrencyModel arg0)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#setCurrentJavaCurrency(java.util.Currency)
	 */
	@Override
	public void setCurrentJavaCurrency(final Currency arg0)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#setCurrentLocale(java.util.Locale)
	 */
	@Override
	public void setCurrentLocale(final Locale arg0)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#setCurrentTimeZone(java.util.TimeZone)
	 */
	@Override
	public void setCurrentTimeZone(final TimeZone arg0)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.i18n.I18NService#setEnumLocalizedName(de.hybris.platform.core.HybrisEnumValue,
	 * java.lang.String)
	 */
	@Override
	public void setEnumLocalizedName(final HybrisEnumValue arg0, final String arg1)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.i18n.I18NService#setLocalizationFallbackEnabled(boolean)
	 */
	@Override
	public void setLocalizationFallbackEnabled(final boolean arg0)
	{
		// YTODO Auto-generated method stub

	}


}

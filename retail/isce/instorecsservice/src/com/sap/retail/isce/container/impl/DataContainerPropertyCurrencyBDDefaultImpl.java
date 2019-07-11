/*****************************************************************************
 Class:        DataContainerPropertyCurrencyBDDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainerPropertyCurrencyBD;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


public class DataContainerPropertyCurrencyBDDefaultImpl extends DataContainerPropertyBigDecimalDefaultImpl implements
		DataContainerPropertyCurrencyBD
{
	private static final Logger LOG = Logger.getLogger(DataContainerPropertyCurrencyBDDefaultImpl.class.getName());

	protected CommonI18NService commonI18NService;
	protected BaseStoreService baseStoreService;

	/**
	 * Constructs the class and sets id and value
	 *
	 * @param value
	 *           value of the property
	 * @param unitSingular
	 *           unit string for a value <=1
	 * @param unitPlural
	 *           unit string for a value >1
	 */
	public DataContainerPropertyCurrencyBDDefaultImpl(final BigDecimal value, final String unitSingular, final String unitPlural)
	{
		super(value, unitSingular, unitPlural);
	}

	/**
	 * Sets the base store service
	 *
	 * @param baseStoreService
	 *           the service to be set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * Sets the i18N cmomon service.
	 *
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.retail.isce.container.impl.DataContainerPropertyBigDecimalDefaultImpl#calculateLevelBoundaryPairs(java
	 * .util.List, boolean)
	 */
	@Override
	public List<Comparable> calculateLevelBoundaryPairs(final List<Comparable> levelBoundaries, final boolean ascending)
			throws DataContainerPropertyLevelException
	{
		try
		{
			for (int i = 0; i < levelBoundaries.size(); i++)
			{
				levelBoundaries.set(i, currencyConvertValue((BigDecimal) levelBoundaries.get(i)));
			}
		}
		catch (final ClassCastException e)
		{
			logAndThrowExForCastEx(LOG, e);
		}

		return super.calculateLevelBoundaryPairs(levelBoundaries, ascending);
	}

	/**
	 * Converts a value suitable to the current currency based on the CMS level (which is based on the base store
	 * currency)
	 *
	 * @param value
	 *           the value to be converted
	 * @throws DataContainerPropertyLevelException
	 */
	protected BigDecimal currencyConvertValue(final BigDecimal value) throws DataContainerPropertyLevelException
	{
		double convertedValue;
		if (commonI18NService == null)
		{
			LOG.error("currencyConvertValue - commonI18NService instance is null.");
			throw new DataContainerPropertyLevelException("currencyConvertValue - commonI18NService instance is null.");
		}

		final CurrencyModel sourceCurrency = getBaseStoreCurrencyModel();
		final CurrencyModel targetCurrency = getCurrentCurrencyModel();

		try
		{
			if (value != null)
			{
				convertedValue = commonI18NService.convertAndRoundCurrency(sourceCurrency.getConversion().doubleValue(),
						targetCurrency.getConversion().doubleValue(), targetCurrency.getDigits().intValue(), value.doubleValue());
				if (Double.isInfinite(convertedValue))
				{
					LOG.error("Overflow occured during converting level value to current currency.");
					throw new DataContainerPropertyLevelException(
							"Overflow occured during converting level value to current currency.");
				}
				return BigDecimal.valueOf(Math.round(convertedValue));
			}
			return null;
		}
		catch (final RuntimeException e)
		{
			LOG.error("RuntimeException occured during converting level value to current currency.");
			throw new DataContainerPropertyLevelException(
					"RuntimeException occured during converting level value to current currency.", e);
		}
	}

	/**
	 * Determines the base store currency model
	 *
	 * @return the base store currency model
	 * @throws DataContainerPropertyLevelException
	 */
	protected CurrencyModel getBaseStoreCurrencyModel() throws DataContainerPropertyLevelException
	{
		if (this.baseStoreService == null)
		{
			LOG.error("Got no baseStoreService instance.");
			throw new DataContainerPropertyLevelException("Got no baseStoreService instance from spring.");
		}
		if (this.baseStoreService.getCurrentBaseStore() == null)
		{
			LOG.error("Got no current base store from baseStoreService instance.");
			throw new DataContainerPropertyLevelException("Got no current base store from baseStoreService instance.");
		}

		if (this.baseStoreService.getCurrentBaseStore().getDefaultCurrency() == null)
		{
			LOG.error("Got no default currency from current base store of baseStoreService instance.");
			throw new DataContainerPropertyLevelException(
					"Got no default currency from current base store of baseStoreService instance.");
		}

		return this.baseStoreService.getCurrentBaseStore().getDefaultCurrency();
	}

	/**
	 * Determines the current currency model
	 *
	 * @return the current currency model
	 * @throws DataContainerPropertyLevelException
	 */
	protected CurrencyModel getCurrentCurrencyModel() throws DataContainerPropertyLevelException
	{
		if (this.commonI18NService == null)
		{
			LOG.error("Got no commonI18NService instance.");
			throw new DataContainerPropertyLevelException("Got no commonI18NService instance from spring.");
		}
		if (this.commonI18NService.getCurrentCurrency() == null)
		{
			LOG.error("Got no current currency from commonI18NService instance.");
			throw new DataContainerPropertyLevelException("Got Got no current currency from commonI18NService instance.");
		}

		return this.commonI18NService.getCurrentCurrency();
	}
}
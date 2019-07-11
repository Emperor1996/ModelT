/*****************************************************************************
 Interface:        StatisticalDataSalesVolumeUtil
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util;

import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.util.List;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;


/**
 * Interface for Statistical Data Sales Volume Utilities.
 */
public interface StatisticalDataSalesVolumeUtil
{
	/**
	 * Calculates the sales volume in the given target currency, based on the given list of sales volumes per currency.
	 *
	 * @param targetCurrency
	 *           the currency all sales volumes should be converted to
	 * @param salesVolumePerCurrency
	 *           a list of sales volumes for different currencies.
	 * @param dataContainer
	 *           the currentDataContainer
	 * @param channel
	 *           the channel that should be used for calculating the sales volumes
	 * @return ISCESalesVolumeResult the sum of all sales volumes in the list, converted into the target currency
	 */
	public ISCESalesVolumeResult calculateSalesVolumeForTargetCurrency(final CurrencyModel targetCurrency,
			final List<ISCESalesVolumeResult> salesVolumePerCurrency, final DataContainer dataContainer,
			final ISCESalesVolumeResult.Channel channel);


	/**
	 * Returns the current Currency.
	 *
	 * @return CurrencyModel the current currency
	 */
	public CurrencyModel getCurrentCurrency();
}

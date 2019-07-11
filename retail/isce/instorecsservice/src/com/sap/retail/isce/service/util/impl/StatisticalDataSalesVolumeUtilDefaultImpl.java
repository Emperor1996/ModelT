/*****************************************************************************
Class:        StatisticalDataSalesVolumeUtilDefaultImpl
Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageListHolder;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;
import com.sap.retail.isce.service.util.StatisticalDataSalesVolumeUtil;


/**
 * Default implementation class for the StatisticalDataSalesVolumeUtil.
 */
public class StatisticalDataSalesVolumeUtilDefaultImpl implements StatisticalDataSalesVolumeUtil
{

	private static final Logger LOG = Logger.getLogger(StatisticalDataSalesVolumeUtilDefaultImpl.class.getName());

	private CommonI18NService commonI18NService;

	/**
	 * Returns the i18NService.
	 *
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * Sets the i18NService.
	 *
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public ISCESalesVolumeResult calculateSalesVolumeForTargetCurrency(final CurrencyModel targetCurrency,
			final List<ISCESalesVolumeResult> salesVolumePerCurrency, final DataContainer dataContainer,
			final ISCESalesVolumeResult.Channel channel)
	{
		if (salesVolumePerCurrency == null)
		{
			return new ISCESalesVolumeResult(BigDecimal.ZERO, 0, targetCurrency.getIsocode());
		}

		BigDecimal salesVolume = BigDecimal.ZERO;
		int noOfOrders = 0;
		double convertedSalesVolume;
		CurrencyModel sourceCurrency;
		String failedCurrencies = "";
		String failedCurrenciesSeparator = "";

		for (final ISCESalesVolumeResult salesVolumeResult : salesVolumePerCurrency)
		{
			if (channel != null && !areChannelIdentical(channel, salesVolumeResult.getChannel()))
			{
				continue;
			}

			try
			{
				sourceCurrency = commonI18NService.getCurrency(salesVolumeResult.getCurrencyISOCode());
				noOfOrders += salesVolumeResult.getNumberOfOrders();

				if (sourceCurrency.equals(targetCurrency))
				{
					salesVolume = salesVolume.add(salesVolumeResult.getSalesVolume());
				}
				else
				{
					convertedSalesVolume = commonI18NService.convertAndRoundCurrency(sourceCurrency.getConversion().doubleValue(),
							targetCurrency.getConversion().doubleValue(), targetCurrency.getDigits().intValue(), salesVolumeResult
									.getSalesVolume().doubleValue());
					salesVolume = salesVolume.add(BigDecimal.valueOf(convertedSalesVolume));
				}
			}
			catch (final UnknownIdentifierException exception)
			{
				LOG.error("Calculating sales volume for source currency " + salesVolumeResult.getCurrencyISOCode() + " failed.",
						exception);
				failedCurrencies = failedCurrencies + failedCurrenciesSeparator + salesVolumeResult.getCurrencyISOCode();
				failedCurrenciesSeparator = ", ";
			}
		}

		if (failedCurrencies.length() > 0 && dataContainer instanceof MessageListHolder)
		{
			final Message message = new Message(2, "instorecs.customer360.currencyConversionError", new String[]
			{ failedCurrencies }, null);
			((MessageListHolder) dataContainer).addMessage(message);
		}

		return new ISCESalesVolumeResult(salesVolume, noOfOrders, targetCurrency.getIsocode());
	}

	/**
	 * Checks if the channel for the shop and for the currently order are the same. We can only add orders with the same
	 * channel together.
	 *
	 * @param shopChannel
	 *           the channel from the shop
	 * @param salesVolumeChannel
	 *           the channel from the given salesVolume
	 * @return true if both channels are identical, false otherwise
	 */
	private static boolean areChannelIdentical(final ISCESalesVolumeResult.Channel shopChannel,
			final ISCESalesVolumeResult.Channel salesVolumeChannel)
	{
		if (ISCESalesVolumeResult.Channel.ANY.equals(shopChannel) || shopChannel.equals(salesVolumeChannel))
		{
			return true;
		}
		return false;
	}


	@Override
	public CurrencyModel getCurrentCurrency()
	{
		return this.getCommonI18NService().getCurrentCurrency();
	}

}

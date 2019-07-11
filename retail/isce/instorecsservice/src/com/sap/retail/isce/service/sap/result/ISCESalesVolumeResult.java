/*****************************************************************************
 Class:        ISCESalesVolumeResult
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;

import java.math.BigDecimal;


/**
 * Class to hold the result of a ISCE Customer Account sales volume request
 */
public class ISCESalesVolumeResult
{
	public enum Channel
	{
		ANY, ONLINE, POS;
	}

	private BigDecimal salesVolume;
	private int numberOfOrders;
	private final String currencyISOCode;
	private Channel channel;

	/**
	 * Create a new ISCESalesVolumeResult with the given attributes.
	 *
	 * @param salesVolume
	 *           the sales volume as BigDecimal value
	 * @param numberOfOrders
	 *           the number of orders
	 * @param currencyISOCode
	 *           the ISO code of the currency
	 */
	public ISCESalesVolumeResult(final BigDecimal salesVolume, final int numberOfOrders, final String currencyISOCode)
	{
		this.salesVolume = salesVolume;
		this.numberOfOrders = numberOfOrders;
		this.currencyISOCode = currencyISOCode;
		this.channel = Channel.ANY;
	}

	/**
	 * Create a new ISCESalesVolumeResult with the given attributes.
	 *
	 * @param salesVolume
	 *           the sales volume as BigDecimal value
	 * @param numberOfOrders
	 *           the number of orders
	 * @param currencyISOCode
	 *           the ISO code of the currency
	 * @param channel
	 *           the channel the sales volume originates from
	 */
	public ISCESalesVolumeResult(final BigDecimal salesVolume, final int numberOfOrders, final String currencyISOCode,
			final Channel channel)
	{
		this.salesVolume = salesVolume;
		this.numberOfOrders = numberOfOrders;
		this.currencyISOCode = currencyISOCode;
		this.channel = channel;
	}

	/**
	 * Returns the channel, this volume is related to.
	 *
	 * @return the channel, if not set the volume might be related to several channels
	 */
	public Channel getChannel()
	{
		return channel;
	}

	/**
	 * Set the channel, this volume is related to.
	 *
	 * @param channel
	 *           the channel to set, if set to null or an empty string the volume might be related to several channels
	 */
	public void setChannel(final Channel channel)
	{
		this.channel = channel;
	}

	/**
	 * Returns the sales volume as BigDecimal value.
	 *
	 * @return the salesVolume
	 */
	public BigDecimal getSalesVolume()
	{
		return salesVolume;
	}

	/**
	 * Returns the ISO code of the currency.
	 *
	 * @return the currencyISOCode
	 */
	public String getCurrencyISOCode()
	{
		return currencyISOCode;
	}

	/**
	 * Sets the sales volume as BigDecimal value.
	 *
	 * @param salesVolume
	 *           the salesVolume to set
	 */
	public void setSalesVolume(final BigDecimal salesVolume)
	{
		this.salesVolume = salesVolume;
	}

	/**
	 * Returns the number of orders the sales volume is related to
	 *
	 * @return the numberOfOrders
	 */
	public int getNumberOfOrders()
	{
		return numberOfOrders;
	}

	/**
	 * Sets the number of orders the sales volume is related to
	 *
	 * @param numberOfOrders
	 *           the numberOfOrders to set
	 */
	public void setNumberOfOrders(final int numberOfOrders)
	{
		this.numberOfOrders = numberOfOrders;
	}

}

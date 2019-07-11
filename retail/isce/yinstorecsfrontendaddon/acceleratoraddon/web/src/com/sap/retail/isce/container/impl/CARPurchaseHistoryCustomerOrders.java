/*****************************************************************************
 Class:        CARPurchaseHistoryCustomerOrders
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.Date;


/**
 * POJO containing the formatted results for the Purchase History UI -customer 360° Page.
 *
 */
public class CARPurchaseHistoryCustomerOrders
{
	protected Date date;
	protected PriceData grossSalesVolumePrice;
	protected String purchaseType;
	protected String orderNumber;

	/**
	 * Constructor based on fields.
	 *
	 * @param date
	 *           Date purchased date of the order
	 * @param grossSalesVolumePrice
	 *           PriceData containing the sales volume price (net + tax)
	 * @param purchaseType
	 *           String differentiating between ONLINE SHOP and STORE PURCHASE
	 * @param orderNumber
	 *           String containing the order number
	 */
	public CARPurchaseHistoryCustomerOrders(final Date date, final PriceData grossSalesVolumePrice, final String purchaseType,
			final String orderNumber)
	{
		this.date = date;
		this.grossSalesVolumePrice = grossSalesVolumePrice;
		this.purchaseType = purchaseType;
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *           the date to set
	 */
	protected void setDate(final Date date)
	{
		this.date = date;
	}

	/**
	 * @return the grossSalesVolumePrice
	 */
	public PriceData getGrossSalesVolumePrice()
	{
		return grossSalesVolumePrice;
	}

	/**
	 * @param grossSalesVolumePrice
	 *           the grossSalesVolumePrice to set
	 */
	protected void setGrossSalesVolumePrice(final PriceData grossSalesVolumePrice)
	{
		this.grossSalesVolumePrice = grossSalesVolumePrice;
	}

	/**
	 * @return the purchaseType
	 */
	public String getPurchaseType()
	{
		return purchaseType;
	}

	/**
	 * @param purchaseType
	 *           the purchaseType to set
	 */
	protected void setPurchaseType(final String purchaseType)
	{
		this.purchaseType = purchaseType;
	}

	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber()
	{
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *           the orderNumber to set
	 */
	protected void setOrderNumber(final String orderNumber)
	{
		this.orderNumber = orderNumber;
	}


}

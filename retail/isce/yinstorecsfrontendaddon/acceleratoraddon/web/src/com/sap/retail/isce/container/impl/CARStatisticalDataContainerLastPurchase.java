/*****************************************************************************
 Class:        CARStatisticalDataContainerLastPurchase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import org.apache.log4j.Logger;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Abstract class for retrieving the last purchase date from the online sales or the POS sales
 *
 */
public class CARStatisticalDataContainerLastPurchase extends CARStatisticalDataContainerBase
{
	protected static final String SELECTED_FIELDS = "CreationDate";
	protected static final String ORDERBY_FIELDS = "CreationDate";
	protected static final String ORDERBY_DIRECTION = "desc";
	protected static final Integer INTEGER_ONE = Integer.valueOf(1);
	protected static Logger log = Logger.getLogger(CARStatisticalDataContainerLastPurchase.class.getName());

	protected String lastPurchaseDate = "";

	/**
	 * Default constructor
	 *
	 * @param isceConfigurationService
	 */
	public CARStatisticalDataContainerLastPurchase(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);

		this.orderBy = ORDERBY_FIELDS + " " + ORDERBY_DIRECTION;
		this.top = INTEGER_ONE;
		this.select = SELECTED_FIELDS;
		this.containerName = CARStatisticalDataContainerLastPurchase.class.getName();
	}

	/**
	 * Gets the last purchase date from the online or POS sales
	 *
	 * @return the lastPurchaseDate
	 */
	public String getLastPurchaseDate()
	{
		return lastPurchaseDate;
	}

	/**
	 * Sets the last purchase date from the online or POS sales
	 *
	 * @param lastPurchaseDate
	 *           the lastPurchaseDate to set
	 */
	public void setLastPurchaseDate(final String lastPurchaseDate)
	{
		this.lastPurchaseDate = lastPurchaseDate;
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		this.setDataInErrorState();

		if (httpODataResult == null || httpODataResult.getEntity() == null)
		{
			return;
		}
		this.setLastPurchaseDate(httpODataResult.getEntity().getProperties().get(SELECTED_FIELDS).toString());
	}

	@Override
	public void setDataInErrorState()
	{
		this.lastPurchaseDate = "";
	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARStatisticalDataContainerLastPurchase";
	}

	@Override
	public void traceInformation()
	{
		super.traceInformation(log);
	}

}

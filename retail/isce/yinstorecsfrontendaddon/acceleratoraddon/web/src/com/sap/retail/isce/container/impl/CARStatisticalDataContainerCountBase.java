/*****************************************************************************
 Class:        CARStatisticalDataContainerCountBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.InlineCount;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Abstract class for retrieving the count of transactions
 *
 */
public abstract class CARStatisticalDataContainerCountBase extends CARStatisticalDataContainerBase
{
	protected static Logger log = Logger.getLogger(CARStatisticalDataContainerCountBase.class.getName());

	protected static final String SELECTED_FIELDS = "TransactionNumber";
	protected static final Integer INTEGER_ZERO = Integer.valueOf(0);

	private int numberOfTransactions = 0;

	/**
	 * Default constructor
	 *
	 * @param isceConfigurationService
	 */
	public CARStatisticalDataContainerCountBase(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.inlineCount = InlineCount.ALLPAGES;
		this.top = INTEGER_ZERO;
		this.select = SELECTED_FIELDS;
	}

	/**
	 * Returns the number of transactions.
	 *
	 * @return the numberOfTransactions
	 */
	public int getNumberOfTransactions()
	{
		return numberOfTransactions;
	}

	/**
	 * Sets the number of transactions.
	 *
	 * @param numberOfTransactions
	 *           the numberOfTransactionsto set
	 */
	public void setNumberOfTransactions(final int numberOfTransactions)
	{
		this.numberOfTransactions = numberOfTransactions;
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		this.setDataInErrorState();

		if (httpODataResult == null || httpODataResult.getCount() == null)
		{
			return;
		}

		this.setNumberOfTransactions(httpODataResult.getCount().intValue());
	}

	@Override
	public void setDataInErrorState()
	{
		this.numberOfTransactions = 0;
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, null);
	}

}

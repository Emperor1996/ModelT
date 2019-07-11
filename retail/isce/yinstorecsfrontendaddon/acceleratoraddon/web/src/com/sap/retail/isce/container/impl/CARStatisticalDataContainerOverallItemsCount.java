/*****************************************************************************
 Class:        CARStatisticalDataContainerOverallItemsCount
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Class to determine the overall number of items in transactions from the car system.
 */
public class CARStatisticalDataContainerOverallItemsCount extends CARStatisticalDataContainerCountBase
{

	protected static final String SELECTED_FIELDS = "TransactionNumber,TransactionIndex";

	/**
	 * @param isceConfigurationService
	 */
	public CARStatisticalDataContainerOverallItemsCount(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.select = SELECTED_FIELDS;
		this.containerName = CARStatisticalDataContainerOverallItemsCount.class.getName();
	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARStatisticalDataContainerOverallItemsCount";
	}

}

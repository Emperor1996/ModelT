/*****************************************************************************
 Class:        CARStatisticalDataContainerOverallCount
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Class to determine the overall number of transactions from the car system.
 */
public class CARStatisticalDataContainerOverallCount extends CARStatisticalDataContainerCountBase
{
	/**
	 * Default constructor.
	 *
	 * @param isceConfigurationService
	 *           the ISCE configuration to use
	 */
	public CARStatisticalDataContainerOverallCount(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerName = CARStatisticalDataContainerOverallCount.class.getName();

	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARStatisticalDataContainerOverallCount";
	}

}
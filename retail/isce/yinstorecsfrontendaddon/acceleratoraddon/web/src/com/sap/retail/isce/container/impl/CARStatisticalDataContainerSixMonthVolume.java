/*****************************************************************************
 Class:        CARStatisticalDataContainerSixMonthVolume
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Class to determine the sales volume of the last six month from the car system.
 */
public class CARStatisticalDataContainerSixMonthVolume extends CARStatisticalDataContainerVolumeBase
{
	/**
	 * Default constructor.
	 *
	 * @param isceConfigurationService
	 *           the ISCE configuration to use
	 */
	public CARStatisticalDataContainerSixMonthVolume(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerName = CARStatisticalDataContainerSixMonthVolume.class.getName();
	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARStatisticalDataContainerSixMonthVolume";
	}

	@Override
	public String getFilter()
	{
		return super.getFilter() + " and " + getLastSixMonthFilter();
	}

}

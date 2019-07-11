/*****************************************************************************
 Class:        CARStatisticalDataContainerOverallVolume
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Class to provide overall statistical data from the car system.
 */
public class CARStatisticalDataContainerOverallVolume extends CARStatisticalDataContainerVolumeBase
{
	/**
	 * Default constructor.
	 *
	 * @param isceConfigurationService
	 *           the ISCE configuration to use
	 */
	public CARStatisticalDataContainerOverallVolume(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerName = CARStatisticalDataContainerOverallVolume.class.getName();
	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARStatisticalDataContainerOverallVolume";
	}

}
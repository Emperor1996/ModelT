/*****************************************************************************
 Class:        ISCEConfigurationServiceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


public class ISCEConfigurationServiceMock implements ISCEConfigurationService
{
	private final SAPHTTPDestinationModel httpDestinationMock = mock(SAPHTTPDestinationModel.class);

	@Override
	public SAPHTTPDestinationModel getCARHttpDestination()
	{
		when(httpDestinationMock.getHttpDestinationName()).thenReturn("CARDestination");
		return httpDestinationMock;
	}

	@Override
	public SAPHTTPDestinationModel getYMktHttpDestination()
	{
		when(httpDestinationMock.getHttpDestinationName()).thenReturn("ISCEHybrisMarketingHTTPDestination");
		return httpDestinationMock;
	}

	@Override
	public String getCARSAPClient()
	{
		return "999";
	}

	@Override
	public String getCARServiceName()
	{
		return "CARService.xsodata";
	}

	@Override
	public String getCARPOSOrderChannels()
	{
		return "07";
	}

	@Override
	public String getCAROnlineOrderChannels()
	{
		return "01, 02,03";
	}

	@Override
	public Integer getCARMaxNumberTransactionsPurchaseHistory()
	{
		return new Integer(100);
	}

}

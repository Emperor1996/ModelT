/*****************************************************************************
 Class:        ISCEConfigurationServiceDefaultImplTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.exception.ConfigurationBaseRuntimeException;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.UnitTestBase;


/**
 * Unit Test class for ISCEConfigurationServiceDefaultImpl.
 */
@UnitTest
public class ISCEConfigurationServiceDefaultImplUnitTest extends UnitTestBase
{
	private ISCEConfigurationServiceDefaultImpl classUnderTest;
	private BaseStoreModel baseStoreModelMock;
	private SAPConfigurationModel configurationModelMock;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new ISCEConfigurationServiceDefaultImpl();

		baseStoreModelMock = mock(BaseStoreModel.class);
		final BaseStoreService baseStoreServiceMock = mock(BaseStoreService.class);
		configurationModelMock = mock(SAPConfigurationModel.class);
		when(baseStoreServiceMock.getCurrentBaseStore()).thenReturn(baseStoreModelMock);
		when(baseStoreModelMock.getSAPConfiguration()).thenReturn(configurationModelMock);

		classUnderTest.setBaseStoreService(baseStoreServiceMock);
	}

	@Test
	public void testGetConfiguration()
	{
		SAPConfigurationModel configuration = classUnderTest.getConfiguration();

		assertEquals(
				"ISCEConfigurationServiceDefaultImplTest - testGetConfiguration - configuration must be configurationModelMock",
				configurationModelMock, configuration);

		when(baseStoreModelMock.getSAPConfiguration()).thenReturn(null);

		try
		{
			configuration = classUnderTest.getConfiguration();
		}
		catch (final ConfigurationBaseRuntimeException ex)
		{
			assertTrue(
					"ISCEConfigurationServiceDefaultImplTest - testGetConfiguration - no Basde configuration, exception mus be thrown",
					true);
		}
	}

	@Test
	public void testGetCARSAPClient()
	{
		final String returnedValue = "123";

		when(configurationModelMock.getInstorecsassistedservicestorefront_CAR_client()).thenReturn(returnedValue);

		assertEquals("ISCEConfigurationServiceDefaultImplTest - testGetCARSAPClient -CAR SAP Client must be " + returnedValue,
				returnedValue, classUnderTest.getCARSAPClient());

	}


	@Test
	public void testGetCARPOSOrderChannels()
	{
		final String returnedValue = "P1, P2";

		when(configurationModelMock.getInstorecsassistedservicestorefront_CAR_posChannelList()).thenReturn(returnedValue);

		assertEquals("ISCEConfigurationServiceDefaultImplTest - testGetCARPOSOrderChannels -CAR POS OrderChannels must be "
				+ returnedValue, returnedValue, classUnderTest.getCARPOSOrderChannels());

	}

	@Test
	public void testGetCAROnlineOrderChannels()
	{
		final String returnedValue = "O1, 02";

		when(configurationModelMock.getInstorecsassistedservicestorefront_CAR_onlineChannelList()).thenReturn(returnedValue);

		assertEquals("ISCEConfigurationServiceDefaultImplTest - testGetCAROnlineOrderChannels -CAR Online OrderChannels must be "
				+ returnedValue, returnedValue, classUnderTest.getCAROnlineOrderChannels());

	}

	@Test
	public void testGetCARServiceName()
	{
		final String returnedValue = "CARService";

		when(configurationModelMock.getInstorecsassistedservicestorefront_CAR_serviceName()).thenReturn(returnedValue);

		assertEquals("ISCEConfigurationServiceDefaultImplTest - testGetCAROnlineOrderChannels -CAR Service Name must be "
				+ returnedValue, returnedValue, classUnderTest.getCARServiceName());
	}

	@Test
	public void testGetCARHttpDestination()
	{
		final SAPHTTPDestinationModel returnedValue = mock(SAPHTTPDestinationModel.class);

		when(configurationModelMock.getInstorecsassistedservicestorefront_CAR_HTTPDestination()).thenReturn(returnedValue);

		assertEquals(
				"ISCEConfigurationServiceDefaultImplTest - testGetCAROnlineOrderChannels -CAR HTTP Destination must be mocked SAPHTTPDestinationModel",
				returnedValue, classUnderTest.getCARHttpDestination());
	}

	@Test
	public void testGetYMktHttpDestination()
	{
		final SAPHTTPDestinationModel returnedValue = mock(SAPHTTPDestinationModel.class);

		when(configurationModelMock.getInstorecsassistedservicestorefront_yMkt_HTTPDestination()).thenReturn(returnedValue);

		assertEquals(
				"ISCEConfigurationServiceDefaultImplTest - testGetCAROnlineOrderChannels -YMkt HTTP Destination must be mocked SAPHTTPDestinationModel",
				returnedValue, classUnderTest.getYMktHttpDestination());
	}

	@Test
	public void testGetCARMaxNumberTransactionsPurchaseHistory()
	{
		final Integer returnedValue = Integer.valueOf(100);

		when(
				configurationModelMock
						.getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions()).thenReturn(
				returnedValue);

		assertEquals(
				"ISCEConfigurationServiceDefaultImplTest - testGetCARMaxNumberTransactionsPurchaseHistory -CAR purchaseHistoryCustomerOrders_maxNumberofTransactions must be "
						+ returnedValue, returnedValue, classUnderTest.getCARMaxNumberTransactionsPurchaseHistory());

	}
}

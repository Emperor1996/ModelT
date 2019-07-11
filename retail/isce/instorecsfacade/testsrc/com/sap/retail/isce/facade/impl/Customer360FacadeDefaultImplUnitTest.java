/*****************************************************************************
 Class:        Customer360FacadeDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.impl.DataContainerServiceDefaultImpl;


/**
 * Unit Test for Default implementation class Customer360FacadeDefaultImpl
 *
 */
@UnitTest
public class Customer360FacadeDefaultImplUnitTest
{
	private Customer360FacadeDefaultImpl classUnderTest;
	private final DataContainerService dataContainerService = new DataContainerServiceDefaultImpl();
	private DataContainerService dataContainerServiceSpy = null;

	@Before
	public void setUp()
	{
		classUnderTest = new Customer360FacadeDefaultImpl();

		dataContainerServiceSpy = Mockito.spy(dataContainerService);
		// and inject this mocked Service into the classUnderTest Facade
		classUnderTest.setDataContainerService(dataContainerServiceSpy);

	}

	/**
	 * Tests readCustomer360Data.
	 */
	@Test
	public void testReadCustomer360Data()
	{
		final List<DataContainer> dataContainers = new ArrayList<DataContainer>();
		Mockito.doNothing().when(dataContainerServiceSpy).readDataContainers(dataContainers, null);
		classUnderTest.readCustomer360Data(dataContainers, null);
		Mockito.verify(dataContainerServiceSpy, Mockito.times(1)).readDataContainers(dataContainers, null);
	}

	/**
	 * Tests updateDataContainersForComponent.
	 */
	@Test
	public void testUpdateDataContainersForComponent()
	{
		final List<DataContainer> dataContainers = new ArrayList<DataContainer>();
		Mockito.doNothing().when(dataContainerServiceSpy).updateDataContainersForComponent(dataContainers, null);
		classUnderTest.updateDataContainersForComponent(dataContainers, null);
		Mockito.verify(dataContainerServiceSpy, Mockito.times(1)).updateDataContainersForComponent(dataContainers, null);
	}
}

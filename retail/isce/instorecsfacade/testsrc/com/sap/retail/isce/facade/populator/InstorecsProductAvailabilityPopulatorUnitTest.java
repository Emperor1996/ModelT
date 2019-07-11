/*****************************************************************************
 Class:        InstorecsProductAvailabilityPopulatorUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.populator;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.retail.isce.facade.InstorecsContextFacade;


@UnitTest
public class InstorecsProductAvailabilityPopulatorUnitTest extends TestCase
{

	private InstorecsProductAvailabilityPopulator classUnderTest;

	@Mock
	private InstorecsContextFacade instorecsContextFacadeMock;

	@Override
	@Before
	public void setUp()
	{
		Logger.getLogger(InstorecsProductAvailabilityPopulator.class.getName()).setLevel(Level.DEBUG);
		MockitoAnnotations.initMocks(this);
		classUnderTest = new InstorecsProductAvailabilityPopulator();

		classUnderTest.setInstorecsContextFacade(instorecsContextFacadeMock);
		assertSame("instorecsContextFacadeMock not identical", instorecsContextFacadeMock,
				classUnderTest.getInstorecsContextFacade());

	}

	@Test
	public void testPopulate() throws InterceptorException
	{
		final Long qtyInCurrentStore = new Long(123L);
		final ProductModel productModel = null;
		when(instorecsContextFacadeMock.getProductQtyInCurrentStore(productModel)).thenReturn(qtyInCurrentStore);

		final ProductData productData = new ProductData();
		classUnderTest.populate(productModel, productData);
		assertEquals("getQtyInCurrentStore not identical", productData.getQtyInCurrentStore(), new Long(123L));

	}
}

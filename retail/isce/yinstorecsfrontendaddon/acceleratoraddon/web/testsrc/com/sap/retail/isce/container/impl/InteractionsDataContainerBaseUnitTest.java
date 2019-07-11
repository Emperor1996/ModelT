/*****************************************************************************
 Class:        InteractionsDataContainerBaseUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainerContext;


/**
 * Unit test class for InteractionsDataContainerBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class InteractionsDataContainerBaseUnitTest
{

	private static final String CUSTOMER_UID_VALUE = "uid1";

	@Resource(name = "NewsletterSubscriptionDataContainerUnderTest")
	private InteractionsDataContainerBase classUnderTest;

	private final DataContainerContext dcContextMap = new DataContainerContextDefaultImpl();

	@Before
	public void setUp()
	{
		this.classUnderTest.setDataContainerContext(dcContextMap);
		final CustomerData customerData = new CustomerData();
		customerData.setUid(CUSTOMER_UID_VALUE);
		dcContextMap.getContextMap().put(DataContainerContext.CUSTOMER_DATA, customerData);
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("getContainerContextParamName - container context param name must be NewsletterSubscriptionDataContainer",
				"NewsletterSubscriptionDataContainer", this.classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testGetFilter()
	{
		final String expected = "ContactId eq 'uid1' and ContactIdOrigin eq 'EMAIL'";

		assertEquals("getFilter - must start with " + expected, 0, this.classUnderTest.getFilter().indexOf(expected));
	}

	@Test
	public void testGetInlineCount()
	{
		assertEquals("getInlineCount - InlineCount must be null", null, this.classUnderTest.getInlineCount());
	}

	@Test
	public void testGetResultName()
	{
		assertEquals("getResultName - ResultName must be " + InteractionsDataContainerBase.SERVICE_ENDPOINT_NAME,
				InteractionsDataContainerBase.SERVICE_ENDPOINT_NAME, this.classUnderTest.getResultName());
	}

}

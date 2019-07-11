/*****************************************************************************
 Class:        ISCESalesVolumeResultUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.result;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;

import java.math.BigDecimal;

import org.junit.Test;

import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult.Channel;


/**
 * Unit Test class for ISCESalesVolumeResult.
 *
 */
@UnitTest
public class ISCESalesVolumeResultUnitTest
{
	private ISCESalesVolumeResult classUnderTest;
	private final BigDecimal salesVolume = new BigDecimal("10");
	private final int numberOfOrders = 10;
	private final String currencyISOCode = "EUR";

	@Test
	public void testSetterGetter()
	{
		classUnderTest = new ISCESalesVolumeResult(salesVolume, numberOfOrders, currencyISOCode,
				ISCESalesVolumeResult.Channel.ONLINE);

		assertEquals("getSalesVolume not identical", classUnderTest.getSalesVolume(), salesVolume);
		assertEquals("getNumberOfOrders not identical", classUnderTest.getNumberOfOrders(), numberOfOrders);
		assertEquals("getCurrencyISOCode not identical", classUnderTest.getCurrencyISOCode(), currencyISOCode);
		assertEquals("getChannel not identical", classUnderTest.getChannel(), ISCESalesVolumeResult.Channel.ONLINE);

		classUnderTest = new ISCESalesVolumeResult(salesVolume, numberOfOrders, currencyISOCode);

		assertEquals("getSalesVolume not identical", classUnderTest.getSalesVolume(), salesVolume);
		assertEquals("getNumberOfOrders not identical", classUnderTest.getNumberOfOrders(), numberOfOrders);
		assertEquals("getCurrencyISOCode not identical", classUnderTest.getCurrencyISOCode(), currencyISOCode);
		assertEquals("getChannel not identical", classUnderTest.getChannel(), ISCESalesVolumeResult.Channel.ANY);

		classUnderTest.setChannel(Channel.POS);
		classUnderTest.setNumberOfOrders(11);
		classUnderTest.setSalesVolume(salesVolume);

		assertEquals("getSalesVolume not identical", classUnderTest.getSalesVolume(), salesVolume);
		assertEquals("getNumberOfOrders not identical", classUnderTest.getNumberOfOrders(), 11);
		assertEquals("getChannel not identical", classUnderTest.getChannel(), ISCESalesVolumeResult.Channel.POS);

	}
}

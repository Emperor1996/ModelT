/*****************************************************************************
 Class:        StatisticalDataSalesVolumeUtilDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageListHolder;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.impl.DataContainerDefaultImpl;
import com.sap.retail.isce.service.mock.CommonI18NServiceMock;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;


/**
 * Unit test for StatisticalDataSalesVolumeUtilDefaultImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class StatisticalDataSalesVolumeUtilDefaultImplUnitTest extends UnitTestBase
{
	@Resource(name = "classUnderTest")
	private StatisticalDataSalesVolumeUtilDefaultImpl classUnderTest;

	@Test
	public void testCalculateSalesVolumne()
	{
		final DataContainer dataContainer = mock(DataContainerDefaultImpl.class);

		// undefined list
		ISCESalesVolumeResult calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(
				CommonI18NServiceMock.usdCurrencyModel, null, dataContainer, null);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - result for undefined list must be 0",
				BigDecimal.ZERO, calculatedVolume.getSalesVolume());

		//empty list
		final ArrayList<ISCESalesVolumeResult> salesVolumePerCurrency = new ArrayList<ISCESalesVolumeResult>();

		calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(CommonI18NServiceMock.usdCurrencyModel,
				salesVolumePerCurrency, dataContainer, null);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - sales volume for empty list must be 0",
				BigDecimal.ZERO, calculatedVolume.getSalesVolume());
		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - number of orders empty list must be 0",
				0, calculatedVolume.getNumberOfOrders());

		// Single entry matching target currency
		salesVolumePerCurrency.add(new ISCESalesVolumeResult(BigDecimal.valueOf(100), 2, "USD"));

		calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(CommonI18NServiceMock.usdCurrencyModel,
				salesVolumePerCurrency, dataContainer, null);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - sales volume for single USD entry list must be 100",
				BigDecimal.valueOf(100), calculatedVolume.getSalesVolume());

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - number of orders for single USD entr list must be 2",
				2, calculatedVolume.getNumberOfOrders());

		// single entry not matching target currency
		salesVolumePerCurrency.clear();

		salesVolumePerCurrency.add(new ISCESalesVolumeResult(BigDecimal.valueOf(400), 3, "JPY"));

		calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(CommonI18NServiceMock.usdCurrencyModel,
				salesVolumePerCurrency, dataContainer, null);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - sales volume for single JPY entry list must be 200",
				0, BigDecimal.valueOf(200).compareTo(calculatedVolume.getSalesVolume()));

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - number of orders for single JPY entry list must be 3",
				3, calculatedVolume.getNumberOfOrders());

		// double entry
		salesVolumePerCurrency.add(new ISCESalesVolumeResult(BigDecimal.valueOf(100), 2, "USD"));

		calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(CommonI18NServiceMock.usdCurrencyModel,
				salesVolumePerCurrency, dataContainer, null);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - sales volume for USD and JPY entries list must be 300",
				BigDecimal.valueOf(300).compareTo(calculatedVolume.getSalesVolume()), 0);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - number of orders for USD and JPY entries entries list must be 5",
				5, calculatedVolume.getNumberOfOrders());

		// triple entry with an unknown currency - in this case all know currencies should be added
		salesVolumePerCurrency.add(new ISCESalesVolumeResult(BigDecimal.valueOf(300), 5, "XYT"));

		calculatedVolume = classUnderTest.calculateSalesVolumeForTargetCurrency(CommonI18NServiceMock.usdCurrencyModel,
				salesVolumePerCurrency, dataContainer, null);

		Mockito.verify((MessageListHolder) dataContainer).addMessage(Mockito.any(Message.class));

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - sales volume for USD and JPY entries list must be 300",
				BigDecimal.valueOf(300).compareTo(calculatedVolume.getSalesVolume()), 0);

		assertEquals(
				"StatisticalDataSalesVolumeUtilDefaultImplUnitTest - testcalculateSalesVolumne - number of orders for USD and JPY entries entries list must be 5",
				5, calculatedVolume.getNumberOfOrders());
	}


}

/*****************************************************************************
    Class:        DefaultOaaScheduleLinePopulatorTest
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.facades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;

import java.sql.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.oaa.commerce.facades.schedulelines.ScheduleLineData;
import com.sap.retail.oaa.model.model.ScheduleLineModel;


@UnitTest
@SuppressWarnings("javadoc")
public class DefaultOaaScheduleLinePopulatorTest
{
	private DefaultOaaScheduleLinePopulator cut;


	@Before
	public void setUp()
	{
		cut = new DefaultOaaScheduleLinePopulator();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testPopulate()
	{
		final ScheduleLineModel model = new ScheduleLineModel();
		final ScheduleLineData data = new ScheduleLineData();

		model.setConfirmedQuantity(new Double(4));
		//		model.setQuantityUnit("PC");
		model.setConfirmedDate(new Date(2015, 8, 15));

		cut.populate(model, data);

		Assert.assertEquals(model.getConfirmedDate(), data.getConfirmedDate());
		//		Assert.assertEquals(model.getQuantityUnit(), data.getQuantityUnit());
		Assert.assertEquals(model.getConfirmedQuantity(), data.getConfirmedQuantity());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateWithError()
	{
		final ScheduleLineModel model = new ScheduleLineModel();
		final ScheduleLineData data = new ScheduleLineData();

		cut.populate(model, data);
	}
}

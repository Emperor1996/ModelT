/*****************************************************************************
    Class:        DefaultOaaScheduleLinePopulator
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.facades.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.sap.retail.oaa.commerce.facades.schedulelines.ScheduleLineData;
import com.sap.retail.oaa.model.model.ScheduleLineModel;


/**
 * Populator for ScheduleLineModel to ScheduleLineData conversion
 */
public class DefaultOaaScheduleLinePopulator implements Populator<ScheduleLineModel, ScheduleLineData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final ScheduleLineModel source, final ScheduleLineData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		Assert.notNull(source.getConfirmedDate(), "Parameter confirmedDate cannot be null.");
		Assert.notNull(source.getConfirmedQuantity(), "Parameter confirmedQuantity cannot be null.");


		target.setConfirmedQuantity(source.getConfirmedQuantity());
		target.setConfirmedDate(source.getConfirmedDate());

	}

}

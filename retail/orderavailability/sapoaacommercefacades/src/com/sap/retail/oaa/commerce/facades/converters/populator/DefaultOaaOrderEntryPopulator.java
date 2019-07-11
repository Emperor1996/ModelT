/*****************************************************************************
    Class:        DefaultOaaOrderEntryPopulator
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.facades.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.util.Assert;

import com.sap.retail.oaa.commerce.facades.schedulelines.ScheduleLineData;
import com.sap.retail.oaa.model.model.ScheduleLineModel;


/**
 * Populator for OrderEntryModel to OrderEntryData conversion Add Oaa Specific Fields to OrderEntryContributor
 */
public class DefaultOaaOrderEntryPopulator extends OrderEntryPopulator
{

	private Converter<ScheduleLineModel, ScheduleLineData> scheduleLineConverter;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		super.populate(source, target);

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getScheduleLines() != null)
		{
			target.setScheduleLines(Converters.convertAll(source.getScheduleLines(), getScheduleLineConverter()));
		}

		if (source.getSapSource() != null)
		{
			target.setSapSource(getPointOfServiceConverter().convert(source.getSapSource()));
		}

		if (source.getSapVendor() != null)
		{
			target.setSapVendor(source.getSapVendor());
		}
	}

	public Converter<ScheduleLineModel, ScheduleLineData> getScheduleLineConverter()
	{
		return scheduleLineConverter;
	}

	public void setScheduleLineConverter(final Converter<ScheduleLineModel, ScheduleLineData> scheduleLineConverter)
	{
		this.scheduleLineConverter = scheduleLineConverter;
	}
}

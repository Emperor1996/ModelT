/*****************************************************************************
 Class:        TakeHomeNowAction
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.jalo.actions;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


/**
 * The class implements action for the take home now button of the product detail page, using Hybris actions
 *
 */
public class TakeHomeNowAction extends GeneratedTakeHomeNowAction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(TakeHomeNowAction.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}
}

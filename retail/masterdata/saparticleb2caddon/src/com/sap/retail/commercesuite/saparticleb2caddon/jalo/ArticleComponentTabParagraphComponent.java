/*****************************************************************************
    Class:        ArticleComponentTabParagraphComponent.java
    Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
 *****************************************************************************/
package com.sap.retail.commercesuite.saparticleb2caddon.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


public class ArticleComponentTabParagraphComponent extends GeneratedArticleComponentTabParagraphComponent
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(ArticleComponentTabParagraphComponent.class.getName());

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

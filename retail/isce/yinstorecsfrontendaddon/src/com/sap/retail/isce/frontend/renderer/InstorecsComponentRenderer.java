/*****************************************************************************
 Class:        InstorecsComponentRenderer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.renderer;

import de.hybris.platform.assistedservicestorefront.component.renderer.AssistedServiceComponentRenderer;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;


/**
 * Instore specific renderer class provides information if store associate logged on or not This information is used to
 * bring additional actions to the user, like handover button on the order detail page
 *
 * @param <C>InstorecsHandoverOrderComponentRenderer.java
 */
public class InstorecsComponentRenderer<C extends AbstractCMSComponentModel> extends AssistedServiceComponentRenderer<C>
{
	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final Boolean asmSessionStatus = Boolean.valueOf(getAssistedServiceFacade().isAssistedServiceAgentLoggedIn());

		if (asmSessionStatus != null && asmSessionStatus.booleanValue())
		{
			final String asmModuleView = getView(component);
			pageContext.include(asmModuleView);
		}
	}

	@Override
	protected String getUIExperienceFolder()
	{
		return "responsive";
	}
}

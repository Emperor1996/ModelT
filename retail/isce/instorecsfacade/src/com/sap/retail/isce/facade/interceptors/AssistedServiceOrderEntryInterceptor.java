/*****************************************************************************
 Class:        AssistedServiceOrderEntryInterceptor
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.interceptors;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.facade.constants.InstorecsfacadeConstants;


/**
 * This Interceptor adds the employee ID of the sales agent to each order item that was created during an active ASM
 * session.
 */
public class AssistedServiceOrderEntryInterceptor implements PrepareInterceptor<AbstractOrderEntryModel>
{
	private AssistedServiceFacade assistedServiceFacade;

	private static final Logger LOG = Logger.getLogger(AssistedServiceOrderEntryInterceptor.class.getName());


	@Override
	public void onPrepare(final AbstractOrderEntryModel orderEntryModel, final InterceptorContext ctx) throws InterceptorException
	{
		if (ctx.isNew(orderEntryModel) && orderEntryModel.getCreatedInAsmMode() == null)
		{
			if (assistedServiceFacade.isAssistedServiceAgentLoggedIn())
			{
				final Map<String, Object> assistedSessionAttributes = assistedServiceFacade.getAssistedServiceSessionAttributes();
				final String employeeId = (String) assistedSessionAttributes.get(InstorecsfacadeConstants.AGENTUID);
				orderEntryModel.setEmployeeId(employeeId);
				orderEntryModel.setCreatedInAsmMode(Boolean.TRUE);

				if (LOG.isDebugEnabled())
				{
					LOG.debug("ASM-Mode ON: Creating new item '" + orderEntryModel.getEntryNumber() + "' for order '"
							+ orderEntryModel.getOrder().getCode() + "' assigned to employee '" + employeeId + "'.");
				}
			}
			else
			{
				orderEntryModel.setCreatedInAsmMode(Boolean.FALSE);
				if (LOG.isDebugEnabled())
				{
					LOG.debug("ASM-Mode OFF: Creating new item '" + orderEntryModel.getEntryNumber() + "' for order '"
							+ orderEntryModel.getOrder().getCode() + "' without employee assignment.");
				}
			}
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Skipping Employee asignment, for existing Item '" + orderEntryModel.getEntryNumber() + "' of order '"
						+ orderEntryModel.getOrder().getCode() + "; instead keeping ASM-State: isCreatedInAsmMode="
						+ orderEntryModel.getCreatedInAsmMode() + "; employeeId=" + orderEntryModel.getEmployeeId() + ".");
			}
		}

	}

	public AssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	@Required
	public void setAssistedServiceFacade(final AssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}
}

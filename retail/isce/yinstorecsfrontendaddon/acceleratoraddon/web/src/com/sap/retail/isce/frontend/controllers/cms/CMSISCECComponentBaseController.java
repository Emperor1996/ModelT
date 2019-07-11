/*****************************************************************************
 Class:        CMSISCECComponentBaseController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.addonsupport.controllers.cms.AbstractCMSAddOnComponentController;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageList;
import de.hybris.platform.sap.core.common.message.MessageListHolder;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.Model;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.facade.Customer360Facade;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Abstract super class for ISCE related CMS component controller
 */
public class CMSISCECComponentBaseController<T extends AbstractCMSComponentModel> extends AbstractCMSAddOnComponentController<T>
{
	protected static Logger log = Logger.getLogger(CMSISCECComponentBaseController.class.getName());

	protected static final String DATA_CONTAINER_CONTEXT_BEAN_ALIAS = "dataContainerContext";
	protected static final String MESSAGEKEY_READ_ERROR = "instorecs.customer360.errorDataContainerRead";

	protected SessionService sessionService;
	protected SpringUtil springUtil;
	protected CustomerFacade customerFacade;
	protected Customer360Facade customer360Facade;
	protected String[] dataContainerBeanAliases;

	@Override
	protected String getAddonUiExtensionName(final AbstractCMSComponentModel component)
	{
		return "yinstorecsfrontendaddon";
	}

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final T component)
	{
		final CustomerData customerData = customerFacade.getCurrentCustomer();

		// Fill Context
		final DataContainerContext dataContainerContext = (DataContainerContext) springUtil
				.getBean(DATA_CONTAINER_CONTEXT_BEAN_ALIAS);

		final Map<String, Object> dcContextMap = dataContainerContext.getContextMap();
		dcContextMap.put(DataContainerContext.CUSTOMER_DATA, customerData);

		// Data Container
		final List<DataContainer> missingContainers = new ArrayList<>();
		final List<DataContainer> allContainers = new ArrayList<>();
		DataContainer dataContainerBean;

		final DataContainerServiceUtil dataContainerServiceUtil = (DataContainerServiceUtil) getSessionService().getAttribute(
				DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE);

		for (final String dataContainerBeanAlias : dataContainerBeanAliases)
		{

			if (dataContainerServiceUtil != null && dataContainerServiceUtil.getDataContainerForName(dataContainerBeanAlias) != null)
			{
				model.addAttribute(dataContainerBeanAlias, dataContainerServiceUtil.getDataContainerForName(dataContainerBeanAlias));
				allContainers.add(dataContainerServiceUtil.getDataContainerForName(dataContainerBeanAlias));
				continue;
			}

			dataContainerBean = (DataContainer) springUtil.getBean(dataContainerBeanAlias);

			if (dataContainerBean == null)
			{
				throw new DataContainerRuntimeException("Bean " + dataContainerBeanAlias + " could not be instantiated");
			}

			dataContainerBean.setDataContainerContext(dataContainerContext);
			missingContainers.add(dataContainerBean);
			allContainers.add(dataContainerBean);
			model.addAttribute(dataContainerBeanAlias, dataContainerBean);
		}

		if (!missingContainers.isEmpty())
		{
			customer360Facade.readCustomer360Data(missingContainers, component);
			// Triggers the HTML encoding of the concerned attributes for each Data Container
			doAfterRead(missingContainers);
		}
		// finally provide current component to all containers
		customer360Facade.updateDataContainersForComponent(allContainers, component);

		// all containers as after message processing the message list is cleared -> avoids duplicate messages
		addMessagesToGlobalMessages(model, allContainers);
	}

	/**
	 * Adds messages from containers and adds a further single combining message for erroneous containers.
	 *
	 * @param mvcModel
	 *           the Model View Controller Model
	 * @param dataContainers
	 *           the list of different data containers
	 */
	protected void addMessagesToGlobalMessages(final Model mvcModel, final List<DataContainer> dataContainers)
	{
		String erroneousDataContainerNames = "";
		String separator = "";
		final Object[] attributes = new Object[1];

		for (final DataContainer dataContainer : dataContainers)
		{
			if (dataContainer.getErrorState().booleanValue())
			{
				erroneousDataContainerNames += separator + dataContainer.getLocalizedContainerName();
				separator = "; ";
			}

			addMessagesFromDataContainerToGlobalMessages(mvcModel, dataContainer);
			((MessageListHolder) dataContainer).clearMessages();

		}
		if (!"".equals(erroneousDataContainerNames))
		{
			attributes[0] = erroneousDataContainerNames;
			addMessageToGlobalMessages(mvcModel, GlobalMessages.ERROR_MESSAGES_HOLDER, MESSAGEKEY_READ_ERROR, attributes);
		}
	}

	/**
	 * Processes all steps after read. <br/>
	 * - HTML encodes data containers
	 *
	 * @param dataContainers
	 *           the list of different data containers
	 */
	protected void doAfterRead(final List<DataContainer> dataContainers)
	{
		// add all processing steps after read
		for (final DataContainer dataContainer : dataContainers)
		{
			dataContainer.encodeHTML();
		}
	}

	/**
	 * Add single message to given message holder of global messages list.
	 *
	 * @param mvcModel
	 * @param messageHolder
	 * @param messageKey
	 * @param attributes
	 */
	protected void addMessageToGlobalMessages(final Model mvcModel, final String messageHolder, final String messageKey,
			final Object[] attributes)
	{
		GlobalMessages.addMessage(mvcModel, messageHolder, messageKey, attributes);
	}

	/**
	 * Add dataContainer messages to corresponding message holder of global messages list.
	 *
	 * @param mvcModel
	 * @param dataContainer
	 */
	protected void addMessagesFromDataContainerToGlobalMessages(final Model mvcModel, final DataContainer dataContainer)
	{
		if (dataContainer instanceof MessageListHolder)
		{
			final MessageList messageList = ((MessageListHolder) dataContainer).getMessageList();

			if (messageList == null)
			{
				return;
			}

			for (final Message message : messageList)
			{
				if (message.getType() == Message.INITIAL)
				{
					throw new DataContainerRuntimeException("Initial Message!");
				}

				if (message.getType() == Message.DEBUG)
				{
					log.debug(message);
					continue;
				}

				addMessageToGlobalMessages(mvcModel, getMessageHolder(message), message.getResourceKey(), message.getResourceArgs());
			}
		}
	}

	/**
	 * Determines the proper messageHolder for the given message. Returns null, if none can be determined.
	 *
	 * @param message
	 *           the message to determine the MessageHolder for
	 * @return the proper messageHolder or null if none can be determined
	 */
	protected String getMessageHolder(final Message message)
	{
		if (message.getType() == Message.SUCCESS || message.getType() == Message.INFO)
		{
			return GlobalMessages.INFO_MESSAGES_HOLDER;
		}

		if (message.getType() == Message.WARNING || message.getType() == Message.ERROR)
		{
			return GlobalMessages.ERROR_MESSAGES_HOLDER;
		}

		return null;
	}

	/**
	 * Get the sessionService bean variable.
	 *
	 * @return SessionService the sessionService
	 */
	protected SessionService getSessionService()
	{
		return sessionService;
	}

	/**
	 * Set the sessionService bean variable.
	 *
	 * @param sessionService
	 *           the sessionService to set
	 */
	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * Set the springUtil bean variable.
	 *
	 * @param springUtil
	 *           the springUtil to set
	 */
	@Required
	public void setSpringUtil(final SpringUtil springUtil)
	{
		this.springUtil = springUtil;
	}

	/**
	 * Set the customerFacade bean variable.
	 *
	 * @param customerFacade
	 *           the customerFacade to set
	 */
	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	/**
	 * Set the customer360Facade bean variable.
	 *
	 * @param customer360Facade
	 *           the customer360Facade to set
	 */
	@Required
	public void setCustomer360Facade(final Customer360Facade customer360Facade)
	{
		this.customer360Facade = customer360Facade;
	}

	/**
	 * Set the dataContainerBeanAliases bean variable.
	 *
	 * @param dataContainerBeanAliases
	 *           the dataContainerBeanAliases to set
	 */
	public void setDataContainerBeanAliases(final String[] dataContainerBeanAliases)
	{
		this.dataContainerBeanAliases = dataContainerBeanAliases;
	}

}

/*****************************************************************************
 Class:        DataContainerDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageList;
import de.hybris.platform.sap.core.common.message.MessageListHolder;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.security.core.server.csi.XSSEncoder;


/**
 * Default implementation class for data container
 */

public abstract class DataContainerDefaultImpl implements DataContainer, MessageListHolder
{
	protected Boolean errorState = Boolean.FALSE;
	protected DataContainerContext dataContainerContext = null;
	protected String containerName = null;
	protected String localizedContainerName = null;
	protected I18NService i18nService = null;
	protected MessageSource messageSource = null;
	protected MessageList messageList = null;
	protected AbstractCMSComponentModel cmsComponentModel = null;

	private static final Logger LOG = Logger.getLogger(DataContainerDefaultImpl.class.getName());

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainer#setErrorState(java.lang.Boolean)
	 */
	@Override
	public void setErrorState(final Boolean errorState)
	{
		this.errorState = errorState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainer#getErrorState()
	 */
	@Override
	public Boolean getErrorState()
	{
		return this.errorState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainer#getContainerName()
	 */
	@Override
	public String getContainerName()
	{

		if (this.containerName == null || this.containerName.isEmpty())
		{
			LOG.error("Missing containerName in instance of class " + this.getClass().getName()
					+ ". DataContainerRuntimeException was thrown.");
			throw new DataContainerRuntimeException("Missing containerName in instance of class " + this.getClass().getName());
		}
		return this.containerName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.isce.container.DataContainer#setCMSComponentModel(de.hybris.platform.cms2.model.contents.components
	 * .AbstractCMSComponentModel)
	 */
	@Override
	public void setCMSComponentModel(final AbstractCMSComponentModel cmsComponentModel)
	{
		this.cmsComponentModel = cmsComponentModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainer#determineDataForCMSComponent()
	 */
	@Override
	public void determineDataForCMSComponent()
	{
		// derived data container that use properties with levels have to redefine this method
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainer#setContext(com.sap.retail.isce.container.DataContainerContext)
	 */
	@Override
	public void setDataContainerContext(final DataContainerContext dataContainerContext)
	{
		this.dataContainerContext = dataContainerContext;
	}

	/**
	 * Security encoding of a given input.
	 *
	 * @param input
	 *           the input to be encoded
	 * @return the encoded input
	 */
	protected String encodeHTML(final String input)
	{
		try
		{
			return encodeHTMLInner(input);
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("SECURITY: UnsupportedEncodingException occured during HTML Encoding");
			throw new DataContainerRuntimeException("HTML encoding exception occured", e);
		}
	}

	/**
	 * Security encoding of a given input.
	 *
	 * @param input
	 *           the input to be encoded
	 * @return the encoded input
	 * @throws UnsupportedEncodingException
	 */
	protected String encodeHTMLInner(final String input) throws UnsupportedEncodingException
	{
		return XSSEncoder.encodeHTML(input);
	}

	/**
	 * @param messageSource
	 *           The messageSource containing the resource bundle.
	 */
	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	/**
	 * @param i18nService
	 *           the internationalization service necessary to retrieve the current locale
	 */
	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	@Override
	public MessageList getMessageList()
	{
		return this.messageList;
	}

	@Override
	public void addMessage(final Message message)
	{
		initializeMessageList();

		this.messageList.add(message);
	}

	/**
	 * Initializes the messageList, also to an empty messageList.
	 */
	protected void initializeMessageList()
	{
		if (this.messageList == null)
		{
			this.messageList = new MessageList();
		}
	}

	@Override
	public void clearMessages()
	{
		if (this.messageList != null)
		{
			this.messageList.clear();
		}
	}

	/**
	 * returns a message string
	 *
	 * @param messageKey
	 *           key of message
	 * @param attributes
	 *           attributes to be used in message
	 * @return message text
	 */
	protected String getMessage(final String messageKey, final Object[] attributes)
	{
		return messageSource.getMessage(messageKey, attributes, this.i18nService.getCurrentLocale());
	}

	/**
	 * Update the description of the levels
	 *
	 * @param levels
	 */
	protected void updateLevelsDescription(final List<DataContainerPropertyLevel> levels)
	{
		final Object[] attributes = new Object[1];
		for (final DataContainerPropertyLevel level : levels)
		{
			attributes[0] = level.getLevelFlag();
			level.setDescription(messageSource.getMessage("instorecs.customer360.tilepopup.levelDescription", attributes,
					this.i18nService.getCurrentLocale()));
		}
	}

	/**
	 * Logs the exception and adds a message for the given property name
	 *
	 * @param e
	 *           the exception to be logged
	 * @param propertyNameResourceKey
	 *           the resource key that contains the properties name
	 */
	protected void logExceptionAndAddMessage(final DataContainerPropertyLevelException e, final String propertyNameResourceKey)
	{
		final String[] attributes = new String[1];
		attributes[0] = messageSource.getMessage(propertyNameResourceKey, null, this.i18nService.getCurrentLocale());

		LOG.error("Determining data container levels failed for property " + attributes[0], e);

		final Message message = new Message(Message.ERROR, "instorecs.customer360.dataContainerLevelError", attributes, null);
		this.addMessage(message);
	}

	/**
	 * Traces information about the container, if the trace levels is debug.
	 */
	@Override
	public void traceInformation()
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getContainerName()=" + this.getContainerName());
		}
	}

}

/*****************************************************************************
 Class:        SpringUtilDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util.impl;

import de.hybris.platform.core.Registry;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Default implementation class for the SpringUtil.
 */
public class SpringUtilDefaultImpl implements SpringUtil
{
	private static final Logger LOG = Logger.getLogger(SpringUtilDefaultImpl.class.getName());
	private static final String TEXT_NO_APPL_CTXT = "applicationContext is null";
	private static final String TEXT_NO_BEAN_CREATED = "Bean Factory could not provide the whished bean ";

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.facade.SpringUtil#getBean(java.lang.String)
	 */
	@Override
	public Object getBean(final String beanName)
	{
		final ApplicationContext applContext = getApplicationContext();
		if (applContext == null)
		{
			LOG.error(TEXT_NO_APPL_CTXT);
			throw new DataContainerRuntimeException(TEXT_NO_APPL_CTXT);
		}
		try
		{
			return applContext.getBean(beanName);
		}
		catch (final BeansException e)
		{
			LOG.error(TEXT_NO_BEAN_CREATED + beanName);
			throw new DataContainerRuntimeException(TEXT_NO_BEAN_CREATED + beanName + ". Cause: " + e.toString(), e);
		}
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.util.SpringUtil#getBean(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object getBean(final String beanName, final Object... args)
	{
		final ApplicationContext applContext = getApplicationContext();
		if (applContext == null)
		{
			LOG.error("TEXT_NO_APPL_CTXT");
			throw new DataContainerRuntimeException(TEXT_NO_APPL_CTXT);
		}
		try
		{
			return applContext.getBean(beanName, args);
		}
		catch (final BeansException e)
		{
			LOG.error(TEXT_NO_BEAN_CREATED + beanName);
			throw new DataContainerRuntimeException(TEXT_NO_BEAN_CREATED + beanName + ". Cause: " + e.toString(), e);
		}
	}

	protected ApplicationContext getApplicationContext()
	{
		return Registry.getApplicationContext();
	}
}

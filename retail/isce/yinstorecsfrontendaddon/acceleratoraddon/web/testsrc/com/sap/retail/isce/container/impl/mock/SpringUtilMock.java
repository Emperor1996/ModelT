/*****************************************************************************
 Class:        SpringUtilMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import java.math.BigDecimal;

import com.sap.retail.isce.service.mock.BaseStoreServiceMock;
import com.sap.retail.isce.service.mock.CommonI18NServiceMock;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Mock implementation class for the SpringUtil.
 */
public class SpringUtilMock implements SpringUtil
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.facade.SpringUtil#getBean(java.lang.String)
	 */
	@Override
	public Object getBean(final String beanName)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.util.SpringUtil#getBean(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object getBean(final String beanName, final Object... args)
	{

		switch (beanName)
		{
			case "dataContainerPropertyInteger":
				final DataContainerPropertyIntegerMock dcPropertyIntegerMock = new DataContainerPropertyIntegerMock(
						(Integer) args[0], (String) args[1], (String) args[1]);
				return dcPropertyIntegerMock;

			case "dataContainerPropertyCurrencyBD": // NOSONAR
				final DataContainerPropertyCurrencyBDMock dcPropertyCurrencyBDMock = new DataContainerPropertyCurrencyBDMock(
						(BigDecimal) args[0], (String) args[1], (String) args[2]);
				dcPropertyCurrencyBDMock.setBaseStoreService(new BaseStoreServiceMock());
				dcPropertyCurrencyBDMock.setCommonI18NService(new CommonI18NServiceMock());
				return dcPropertyCurrencyBDMock;

			default:
				return null;
		}
	}
}

/*****************************************************************************
 Interface:        SpringUtil
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util;

/**
 * Interface for Spring Utilities.
 */
public interface SpringUtil
{
	/**
	 * Retrieves the instance for a given Bean name.
	 *
	 * @param beanName
	 *           the bean name to be instantiated
	 * @return object representing the instance of the bean to be instantiated
	 */
	public Object getBean(String beanName);

	/**
	 *
	 * @param beanName
	 *           the bean name to be instantiated
	 * @param args
	 *           constructor arguments of bean
	 * @return object representing the instance of the bean to be instantiated
	 */
	public Object getBean(final String beanName, final Object... args);
}

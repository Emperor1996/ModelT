/*****************************************************************************
 Class:        SpringUtilDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.exception.DataContainerRuntimeException;


/**
 * Unit Test class for implementation class SpringUtilDefaultImpl.
 */
@UnitTest
public class SpringUtilDefaultImplUnitTest extends UnitTestBase
{
	private SpringUtilDefaultImplTest classUnderTest = null;

	private class SpringUtilDefaultImplTest extends SpringUtilDefaultImpl
	{
		private ApplicationContext applicationContext = null;

		public void setApplicationContext(final ApplicationContext applicationContext)
		{
			this.applicationContext = applicationContext;
		}

		@Override
		protected ApplicationContext getApplicationContext()
		{
			return applicationContext;
		}
	}

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		classUnderTest = new SpringUtilDefaultImplTest();

		ApplicationContext applicationContextMock = null;

		applicationContextMock = new GenericWebApplicationContext()
		{
			@Override
			public Object getBean(final String beanName) throws BeansException
			{
				return new String(beanName);
			}

			@Override
			public Object getBean(final String name, final Object... args) throws BeansException
			{
				return args;
			}
		};
		classUnderTest.setApplicationContext(applicationContextMock);
	}

	/**
	 * Tests getBean.
	 */
	@Test
	public void testGetBean()
	{
		assertEquals("Bean was created successfully", "myTestBeanName", classUnderTest.getBean("myTestBeanName").toString());

	}

	/**
	 * Tests getBean Exception Use Case.
	 */
	@Test
	public void testGetBeanException()
	{
		try
		{
			classUnderTest.setApplicationContext(null);
			classUnderTest.getBean("myTestBeanName");
			assertTrue("Exception should have been thrown", false);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Exception was thrown", true);
		}
	}

	/**
	 * Tests getBean Bean Exception Use Case.
	 */
	@Test
	public void testGetBeanBeanException()
	{
		this.suppressLogging(); // Currently unclear why logging must be suppressed here (actually the super.setup() takes care on that) !!
		final ApplicationContext applicationContextMock = new GenericWebApplicationContext()
		{
			@Override
			public Object getBean(final String beanName) throws BeansException
			{
				throw new NoSuchBeanDefinitionException("An error happened");
			}
		};
		classUnderTest.setApplicationContext(applicationContextMock);
		try
		{
			classUnderTest.getBean("myTestBeanName");
			assertTrue("Exception should have been thrown", false);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Exception was thrown", true);
		}
	}

	/**
	 * Tests getBean with arguments.
	 */
	@Test
	public void testGetBeanWithArgs()
	{
		final Object[] args =
		{ Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3) };

		assertEquals("Bean was created successfully", args, classUnderTest.getBean("myTestBeanName", args));

	}

	/**
	 * Tests getBean with arguments Exception Use Case.
	 */
	@Test
	public void testGetBeanWithArgsException()
	{
		final Object[] args = null;
		try
		{
			classUnderTest.setApplicationContext(null);
			classUnderTest.getBean("myTestBeanName", args);
			assertTrue("Exception should have been thrown", false);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Exception was thrown", true);
		}
	}

	/**
	 * Tests getBean with arguments BeanException Use Case.
	 */
	@Test
	public void testGetBeanWithArgsBeanException()
	{
		final Object[] args = null;
		final ApplicationContext applicationContextMock = new GenericWebApplicationContext()
		{
			@Override
			public Object getBean(final String beanName, final Object... args) throws BeansException
			{
				throw new NoSuchBeanDefinitionException("An error happened");
			}
		};
		classUnderTest.setApplicationContext(applicationContextMock);
		try
		{
			classUnderTest.getBean("myTestBeanName", args);
			assertTrue("Exception should have been thrown", false);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Exception was thrown", true);
		}
	}
}

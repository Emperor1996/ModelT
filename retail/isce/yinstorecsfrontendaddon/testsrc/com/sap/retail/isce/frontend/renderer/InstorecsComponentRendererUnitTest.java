/*****************************************************************************
 Class:        InstorecsComponentRendererUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.renderer;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.io.IOException;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.retail.isce.facade.BaseInstorecsFacadeTest;


@UnitTest
public class InstorecsComponentRendererUnitTest extends BaseInstorecsFacadeTest
{
	private final InstorecsComponentRenderer classUnderTest = new InstorecsComponentRenderer()
	{
		@Override
		protected String getView(final AbstractCMSComponentModel component)
		{
			return "";
		}

	};


	@Mock
	private AssistedServiceFacade assistedServiceFacade;



	@Before
	public void setUp()
	{
		Logger.getLogger(InstorecsComponentRenderer.class.getName()).setLevel(Level.DEBUG);

		MockitoAnnotations.initMocks(this);

		classUnderTest.setAssistedServiceFacade(assistedServiceFacade);
	}

	@Test
	public void testRenderComponent() throws ServletException, IOException
	{
		final AbstractCMSComponentModel component = null;
		Mockito.doReturn(Boolean.TRUE).when(assistedServiceFacade).isAssistedServiceAgentLoggedIn();

		classUnderTest.renderComponent(pageContextMock, component);
	}

	@Test
	public void testGetUIExperienceFolder()
	{
		assertEquals("getUIExperienceFolder not identical", "responsive", classUnderTest.getUIExperienceFolder());
	}

	private final PageContext pageContextMock = new PageContext()
	{

		@Override
		public void setAttribute(final String arg0, final Object arg1, final int arg2)
		{
			//
		}

		@Override
		public void setAttribute(final String arg0, final Object arg1)
		{
			//
		}

		@Override
		public void removeAttribute(final String arg0, final int arg1)
		{
			//
		}

		@Override
		public void removeAttribute(final String arg0)
		{
			//
		}

		@Override
		public VariableResolver getVariableResolver()
		{
			return null;
		}

		@Override
		public JspWriter getOut()
		{
			return null;
		}

		@Override
		public ExpressionEvaluator getExpressionEvaluator()
		{
			return null;
		}

		@Override
		public ELContext getELContext()
		{
			return null;
		}

		@Override
		public int getAttributesScope(final String arg0)
		{
			return 0;
		}

		@Override
		public Enumeration<String> getAttributeNamesInScope(final int arg0)
		{
			return null;
		}

		@Override
		public Object getAttribute(final String arg0, final int arg1)
		{
			return null;
		}

		@Override
		public Object getAttribute(final String arg0)
		{
			return null;
		}

		@Override
		public Object findAttribute(final String arg0)
		{
			return null;
		}

		@Override
		public void release()
		{
			//
		}

		@Override
		public void initialize(final Servlet arg0, final ServletRequest arg1, final ServletResponse arg2, final String arg3,
				final boolean arg4, final int arg5, final boolean arg6) throws IOException, IllegalStateException,
				IllegalArgumentException
		{
			//
		}

		@Override
		public void include(final String arg0, final boolean arg1) throws ServletException, IOException
		{
			//
		}

		@Override
		public void include(final String arg0) throws ServletException, IOException
		{
			// Do nothing with intention (mock)
		}

		@Override
		public void handlePageException(final Throwable arg0) throws ServletException, IOException
		{
			//
		}

		@Override
		public void handlePageException(final Exception arg0) throws ServletException, IOException
		{
			//
		}

		@Override
		public HttpSession getSession()
		{
			return null;
		}

		@Override
		public ServletContext getServletContext()
		{
			return null;
		}

		@Override
		public ServletConfig getServletConfig()
		{
			return null;
		}

		@Override
		public ServletResponse getResponse()
		{
			return null;
		}

		@Override
		public ServletRequest getRequest()
		{
			return null;
		}

		@Override
		public Object getPage()
		{
			return null;
		}

		@Override
		public Exception getException()
		{
			return null;
		}

		@Override
		public void forward(final String arg0) throws ServletException, IOException
		{
			//

		}
	};

}
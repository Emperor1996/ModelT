/*****************************************************************************
 Class:        CMSISCEComponentControllerUnitTestBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.servicelayer.session.SessionService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.mockito.Mock;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.impl.CARPurchaseHistoryCustomerOrders;
import com.sap.retail.isce.container.impl.CARPurchaseHistoryDataContainerCustomerOrders;
import com.sap.retail.isce.container.impl.DataContainerContextDefaultImpl;
import com.sap.retail.isce.container.impl.DataContainerDefaultImpl;
import com.sap.retail.isce.container.impl.mock.PriceDataFactoryMock;
import com.sap.retail.isce.facade.Customer360Facade;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.util.impl.SpringUtilDefaultImpl;


/**
 * Base class for ISCE Component Controller Tests
 */
public abstract class CMSISCEComponentControllerUnitTestBase
{
	protected static final String MARKETING_ATTRIBUTES_DATA_CONTAINER = "marketingAttributesDataContainer";

	@Mock
	protected CustomerFacade customerFacadeMock;
	@Mock
	protected SessionService sessionServiceMock;
	@Mock
	protected HttpServletRequest requestMock;
	@Mock
	protected SiteConfigService siteConfigServiceMock;
	@Mock
	protected ISCEConfigurationService isceConfigurationServiceMock;

	protected final LoggerTest loggerTest = new LoggerTest("");
	protected List<DataContainer> customerFacadeDataContainers;
	protected final Model mvcModel = new ExtendedModelMap();
	protected CustomerData customerData = null;
	protected final Customer360FacadeTest customer360FacadeMock = new Customer360FacadeTest();
	protected final SpringUtilTest springUtilMock = new SpringUtilTest();

	// Mock classes
	class SpringUtilTest extends SpringUtilDefaultImpl
	{
		private DataContainer marketingDC = null;
		private DataContainer historyDC = null;
		private final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();

		public void setMarketingDataContainer(final DataContainer dataContainer)
		{
			this.marketingDC = dataContainer;
		}

		public void setHistoryDataContainer(final DataContainer dataContainer)
		{
			this.historyDC = dataContainer;
		}

		@Override
		public Object getBean(final String beanName)
		{
			switch (beanName)
			{
				case CMSISCEPurchaseHistoryComponentController.PURCHASE_HISTORY_CONTAINER_NAME:
					return this.historyDC;
				case MARKETING_ATTRIBUTES_DATA_CONTAINER:
					return this.marketingDC;
				case CMSISCECComponentBaseController.DATA_CONTAINER_CONTEXT_BEAN_ALIAS:
					return this.dataContainerContext;
				default:
					return null;
			}
		}
	}

	class Customer360FacadeTest implements Customer360Facade
	{
		@Override
		public void readCustomer360Data(final List<DataContainer> dataContainers, final AbstractCMSComponentModel component)
		{
			//
		}

		@Override
		public void updateDataContainersForComponent(final List<DataContainer> dataContainers,
				final AbstractCMSComponentModel component)
		{
			customerFacadeDataContainers = dataContainers;
		}
	}

	protected class LoggerTest extends Logger
	{
		private Message message;

		protected LoggerTest(final String name)
		{
			super(name);
		}

		@Override
		public void debug(final Object message)
		{
			this.message = (Message) message;
		}

		//helper method
		public Message getMessage()
		{
			return message;
		}
	}

	class DataContainerTest extends DataContainerDefaultImpl
	{

		@Override
		public String getLocalizedContainerName()
		{
			return localizedContainerName;
		}

		@Override
		public void setDataInErrorState()
		{
			//
		}

		@Override
		public void encodeHTML()
		{
			//
		}

		protected void setLocalizedContainerName(final String name)
		{
			localizedContainerName = name;
		}

		@Override
		public String getContainerContextParamName()
		{
			return "dataContainerTest";
		}
	}

	class DataContainerPurchaseHistoryTest extends CARPurchaseHistoryDataContainerCustomerOrders
	{

		public DataContainerPurchaseHistoryTest(final ISCEConfigurationService isceConfigurationService)
		{
			super(isceConfigurationService);
		}

		protected void setDataContainerName(final String dataContainerName)
		{
			this.containerName = dataContainerName;
		}

		@Override
		public SearchPageData<CARPurchaseHistoryCustomerOrders> getPurchaseHistoryData()
		{
			final PriceData grossSalesVolumePrice = new PriceDataFactoryMock().create(PriceDataType.BUY, new BigDecimal(10),
					new CurrencyModel());

			final CARPurchaseHistoryCustomerOrders cARPurchaseHistoryCustomerOrders = new CARPurchaseHistoryCustomerOrders(
					new Date(), grossSalesVolumePrice, "purchaseType", "012345678");

			final List<CARPurchaseHistoryCustomerOrders> results = new ArrayList();
			for (int i = 0; i < 12; i++)
			{
				results.add(cARPurchaseHistoryCustomerOrders);
			}

			final SearchPageData searchPageData = new SearchPageData<CARPurchaseHistoryCustomerOrders>();
			searchPageData.setResults(results);
			return searchPageData;
		}
	}


}

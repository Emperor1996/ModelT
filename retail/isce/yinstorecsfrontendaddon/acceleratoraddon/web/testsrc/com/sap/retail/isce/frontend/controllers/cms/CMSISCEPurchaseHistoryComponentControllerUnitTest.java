/*****************************************************************************
 Class:        CMSISCEPurchaseHistoryComponentControllerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.urlresolver.impl.DefaultSiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController.ShowMode;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.site.BaseSiteService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.sap.retail.isce.container.impl.CARPurchaseHistoryCustomerOrders;
import com.sap.retail.isce.container.impl.CARPurchaseHistoryDataContainerCustomerOrders;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;


/**
 * Test for CMSISCEPurchaseHistoryComponentController
 */
@UnitTest
public class CMSISCEPurchaseHistoryComponentControllerUnitTest extends CMSISCEComponentControllerUnitTestBase
{
	private final CMSISCEPurchaseHistoryComponentControllerTest classUnderTest = new CMSISCEPurchaseHistoryComponentControllerTest();
	private CARPurchaseHistoryDataContainerCustomerOrders purchaseHistoryDataContainer;
	private static final String BASE_URL = "https://server:1234";

	@Mock
	protected BaseSiteService baseSiteServiceMock;

	protected DefaultSiteBaseUrlResolutionService siteBaseUrlResolutionServiceMock;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setCustomer360Facade(customer360FacadeMock);
		classUnderTest.setCustomerFacade(customerFacadeMock);
		springUtilMock.setHistoryDataContainer(purchaseHistoryDataContainer);
		classUnderTest.setSpringUtil(springUtilMock);
		classUnderTest.setSiteConfigService(siteConfigServiceMock);
		classUnderTest.setSessionService(sessionServiceMock);
		classUnderTest.setBaseSiteService(baseSiteServiceMock);

		siteBaseUrlResolutionServiceMock = new DefaultSiteBaseUrlResolutionServiceMock();
		classUnderTest.setSiteBaseUrlResolutionService(siteBaseUrlResolutionServiceMock);
		purchaseHistoryDataContainer = new DataContainerPurchaseHistoryTest(isceConfigurationServiceMock);

		final BaseSiteModel baseSiteModelMock = new BaseSiteModel();
		Mockito.doReturn(baseSiteModelMock).when(baseSiteServiceMock).getCurrentBaseSite();

		Mockito.doReturn(Integer.valueOf(3)).when(siteConfigServiceMock).getInt("pagination.number.results.count", 5);

		customerData = new CustomerData();
		customerData.setUid("johndoe");
		final String[] dataContainerBeanAliases =
		{ CMSISCEPurchaseHistoryComponentController.PURCHASE_HISTORY_CONTAINER_NAME };
		classUnderTest.setDataContainerBeanAliases(dataContainerBeanAliases);
	}

	@Test
	public void testDoPagination()
	{

		final int pageNumber = 3;
		final DataContainerPurchaseHistoryTest orderHistoryContainer = new DataContainerPurchaseHistoryTest(
				isceConfigurationServiceMock);
		orderHistoryContainer
				.setDataContainerName("com.sap.retail.isce.container.impl.CARPurchaseHistoryDataContainerCustomerOrders");
		orderHistoryContainer.setMaxEntriesDisplayedPerPage(5);

		//no data Container
		classUnderTest.doPagination(mvcModel, null, pageNumber, ShowMode.Page);
		assertFalse("DoPagination - no Container Spring model should be empty", mvcModel.containsAttribute("numberPagesShown"));

		// data Container
		classUnderTest.doPagination(mvcModel, orderHistoryContainer, pageNumber, ShowMode.Page);
		assertTrue("DoPagination - with Container Spring model should not be empty", mvcModel.containsAttribute("numberPagesShown"));
		assertTrue("DoPagination - with Container Spring model should not be empty", mvcModel.containsAttribute("searchPageData"));
		assertTrue("DoPagination - with Container Spring model should not be empty", mvcModel.containsAttribute("isShowAllAllowed"));
		assertTrue("DoPagination - with Container Spring model should not be empty",
				mvcModel.containsAttribute("isShowPageAllowed"));

		assertEquals("getMaxEntriesDisplayedPerPage not equals 5", orderHistoryContainer.getMaxEntriesDisplayedPerPage(), 5);

		final SearchPageData<CARPurchaseHistoryCustomerOrders> searchPageData = classUnderTest.getSearchPageData();
		assertNotNull("searchPageData is null", searchPageData);
		final List<CARPurchaseHistoryCustomerOrders> results = searchPageData.getResults();
		assertNotNull("results is null", results);
		assertEquals("searchPageData results not identical", results.size(), 12);
		final CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrders = results.get(0);
		assertNotNull("carPurchaseHistoryCustomerOrders is null", carPurchaseHistoryCustomerOrders);
		final PriceData grossSalesVolumePrice = carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice();
		assertNotNull("grossSalesVolumePrice is null", grossSalesVolumePrice);
		assertEquals("grossSalesVolumePrice.getValue not identical", grossSalesVolumePrice.getValue(), BigDecimal.valueOf(10));
		assertEquals("getOrderNumber not identical", carPurchaseHistoryCustomerOrders.getOrderNumber(), "012345678");
		assertEquals("getPurchaseType not identical", carPurchaseHistoryCustomerOrders.getPurchaseType(), "purchaseType");

		final List<SortData> sorts = searchPageData.getSorts();
		assertNotNull("sorts is null", sorts);
		final SortData sortData = sorts.get(0);
		assertNotNull("sortData is null", sortData);
		assertEquals("sort code not identical", sortData.getCode(), "byDate");

		final PaginationData pagination = searchPageData.getPagination();
		assertNotNull("pagination is null", pagination);

		assertEquals("getCurrentPage not identical", pagination.getCurrentPage(), 3);
		assertEquals("getNumberOfPages not identical", pagination.getNumberOfPages(), 3);
		assertEquals("getNumberOfPages not identical", pagination.getPageSize(), 5);
		assertEquals("getSort not identical", pagination.getSort(), "byDate");
		assertEquals("getTotalNumberOfResults not identical", pagination.getTotalNumberOfResults(), 12);
	}

	@Test
	public void testGetFullResponseUrl()
	{

		final String fullResponseUrl = classUnderTest.getFullResponseUrl("/my-account/", true);
		assertEquals("fullResponseUrl not identical", BASE_URL + "/my-account/", fullResponseUrl);
	}

	@Test
	public void testFillModel()
	{
		final DataContainerServiceUtil dataContainerUtil = new DataContainerServiceUtil();
		dataContainerUtil.setDataContainers(Arrays.asList(purchaseHistoryDataContainer));
		Mockito.doReturn(dataContainerUtil).when(sessionServiceMock).getAttribute("ISCECustomer360DataContainers");

		try
		{
			//no request parameters default must be used
			classUnderTest.fillModel(requestMock, mvcModel, null);
			assertTrue("fillModel - executed", true);
			assertEquals("fillModel - pageNumber should be 0", 0, classUnderTest.getDoPaginationPageNumber());
			assertEquals("fillModel - showMode should be Page", ShowMode.Page, classUnderTest.getDoPaginationShowMode());

			//specific request parameters
			Mockito.doReturn("3").when(requestMock).getParameter("page");
			Mockito.doReturn("All").when(requestMock).getParameter("show");

			classUnderTest.fillModel(requestMock, mvcModel, null);
			assertTrue("fillModel - executed", true);
			assertEquals("fillModel - pageNumber should be 3", 3, classUnderTest.getDoPaginationPageNumber());
			assertEquals("fillModel - showMode should be All", ShowMode.All, classUnderTest.getDoPaginationShowMode());
			assertEquals("fillModel - Conatiner muts be purchaseHistoryDataContainer", purchaseHistoryDataContainer,
					classUnderTest.getDoPaginationContainer());
		}
		catch (final DataContainerRuntimeException dataContainerRuntimeException)
		{
			assertTrue("No Exception should have occured", false);
		}
	}

	/**
	 * Mock for real class under Test
	 */
	class CMSISCEPurchaseHistoryComponentControllerTest extends CMSISCEPurchaseHistoryComponentController
	{
		private SearchPageData<CARPurchaseHistoryCustomerOrders> searchPageDataTest;
		private CARPurchaseHistoryDataContainerCustomerOrders doPaginationContainer;
		private ShowMode doPaginationShowMode;
		private int doPaginationPageNumber;

		public CMSISCEPurchaseHistoryComponentControllerTest()
		{
			super();
			log = new LoggerTest("");
		}

		@Override
		protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
		{
			super.populateModel(model, searchPageData, showMode);
			searchPageDataTest = (SearchPageData<CARPurchaseHistoryCustomerOrders>) searchPageData;
		}

		@Override
		protected void doPagination(final Model mvcModel,
				final CARPurchaseHistoryDataContainerCustomerOrders purchaseHistoryDataContainer, final int pageNumber,
				final ShowMode showMode)
		{
			this.doPaginationContainer = purchaseHistoryDataContainer;
			this.doPaginationPageNumber = pageNumber;
			this.doPaginationShowMode = showMode;
			super.doPagination(mvcModel, purchaseHistoryDataContainer, pageNumber, showMode);
		}

		protected SearchPageData<CARPurchaseHistoryCustomerOrders> getSearchPageData()
		{
			return searchPageDataTest;
		}

		public CARPurchaseHistoryDataContainerCustomerOrders getDoPaginationContainer()
		{
			return doPaginationContainer;
		}

		public ShowMode getDoPaginationShowMode()
		{
			return doPaginationShowMode;
		}

		public int getDoPaginationPageNumber()
		{
			return doPaginationPageNumber;
		}
	}

	/**
	 * Mock for real class under Test
	 */
	class DefaultSiteBaseUrlResolutionServiceMock extends DefaultSiteBaseUrlResolutionService
	{

		public DefaultSiteBaseUrlResolutionServiceMock()
		{
			super();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * de.hybris.platform.acceleratorservices.urlresolver.impl.DefaultSiteBaseUrlResolutionService#getWebsiteUrlForSite
		 * (de.hybris.platform.basecommerce.model.site.BaseSiteModel, boolean, java.lang.String)
		 */
		@Override
		public String getWebsiteUrlForSite(final BaseSiteModel site, final boolean secure, final String path)
		{
			return BASE_URL + path;
		}



	}

}
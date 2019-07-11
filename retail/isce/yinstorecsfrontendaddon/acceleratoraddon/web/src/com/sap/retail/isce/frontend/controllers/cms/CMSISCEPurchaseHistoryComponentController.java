/*****************************************************************************
 Class:        CMSISCEPurchaseHistoryComponentController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController.ShowMode;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.SortData;
import de.hybris.platform.site.BaseSiteService;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.retail.isce.container.impl.CARPurchaseHistoryCustomerOrders;
import com.sap.retail.isce.container.impl.CARPurchaseHistoryDataContainerCustomerOrders;
import com.sap.retail.isce.model.CMSISCEPurchaseHistoryComponentModel;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;


/**
 * Controller for CMS ISCEPurchaseHistoryComponent
 */
@Controller
@RequestMapping(value = "/view/CMSISCEPurchaseHistoryComponentController")
public class CMSISCEPurchaseHistoryComponentController extends
		CMSISCECComponentBaseController<CMSISCEPurchaseHistoryComponentModel>
{
	protected static Logger log = Logger.getLogger(CMSISCEPurchaseHistoryComponentController.class.getName());

	protected static final String BY_DATE = "byDate";
	public static final String PURCHASE_HISTORY_CONTAINER_NAME = "CARPurchaseHistoryDataContainerCustomerOrders";
	public static final int MAX_PAGE_LIMIT = 100; // should be configured
	private static final String PAGINATION_NUMBER_OF_RESULTS_COUNT = "pagination.number.results.count";

	private SiteConfigService siteConfigService;
	private BaseSiteService baseSiteService;
	private SiteBaseUrlResolutionService siteBaseUrlResolutionService;


	@Override
	protected void fillModel(final HttpServletRequest request, final Model model,
			final CMSISCEPurchaseHistoryComponentModel component)
	{
		super.fillModel(request, model, component);

		// Takes care of the Purchase History Pagination (Handle paged search results)
		int page = 0;
		if (request.getParameter("page") != null)
		{
			page = Integer.parseInt(request.getParameter("page"));
		}

		ShowMode show = ShowMode.valueOf("Page");
		if (request.getParameter("show") != null)
		{
			show = ShowMode.valueOf(request.getParameter("show"));
		}

		final DataContainerServiceUtil dataContainerServiceUtil = (DataContainerServiceUtil) getSessionService().getAttribute(
				DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE);

		doPagination(model,
				(CARPurchaseHistoryDataContainerCustomerOrders) dataContainerServiceUtil
						.getDataContainerForName(PURCHASE_HISTORY_CONTAINER_NAME), page, show);

		final String fullUrlToOrder = getFullResponseUrl("/my-account/order/", request.isSecure());
		model.addAttribute("fullUrlToOrder", fullUrlToOrder);
	}


	/**
	 * Resolves a given URL to a full URL including server and port, etc.
	 *
	 * @param responseUrl
	 *           - the URL to resolve
	 * @param isSecure
	 *           - flag to indicate whether the final URL should use a secure connection or not.
	 * @return a full URL including HTTP protocol, server, port, path etc.
	 */
	protected String getFullResponseUrl(final String responseUrl, final boolean isSecure)
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		final String fullResponseUrl = getSiteBaseUrlResolutionService().getWebsiteUrlForSite(currentBaseSite, isSecure,
				responseUrl);
		return fullResponseUrl == null ? "" : fullResponseUrl;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
	{
		return siteBaseUrlResolutionService;
	}

	@Required
	public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
	{
		this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
	}

	protected void doPagination(final Model mvcModel,
			final CARPurchaseHistoryDataContainerCustomerOrders purchaseHistoryDataContainer, final int pageNumber,
			final ShowMode showMode)
	{

		if (purchaseHistoryDataContainer == null)
		{
			return;
		}

		final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData = purchaseHistoryDataContainer
				.getPurchaseHistoryData();
		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryResults = purchaseHistoryData.getResults();

		final PageableData pageableData = createPageableData(pageNumber,
				purchaseHistoryDataContainer.getMaxEntriesDisplayedPerPage(), showMode);

		purchaseHistoryDataContainer.setPageNumberDisplayed(pageNumber);

		// Pagination Data
		final PaginationData paginationData = new PaginationData();
		paginationData.setPageSize(pageableData.getPageSize()); // 5 per default
		paginationData.setSort(pageableData.getSort()); // sortCode
		paginationData.setTotalNumberOfResults(purchaseHistoryResults.size());
		// Calculate the number of pages
		paginationData.setNumberOfPages((int) Math.ceil(((double) paginationData.getTotalNumberOfResults())
				/ paginationData.getPageSize()));

		// Work out the current page, fixing any invalid page values
		paginationData.setCurrentPage(Math.max(0, Math.min(paginationData.getNumberOfPages(), pageableData.getCurrentPage())));

		final SearchPageData<CARPurchaseHistoryCustomerOrders> searchPageData = new SearchPageData<>();
		searchPageData.setPagination(paginationData);
		// Specify which sort was used
		searchPageData.getPagination().setSort(BY_DATE);

		// Work out which sort and query to use - necessary for the UI rendering
		searchPageData.setSorts(Arrays.asList(createSort(BY_DATE)));

		searchPageData.setResults(purchaseHistoryResults);

		populateModel(mvcModel, searchPageData, showMode);
	}

	protected void populateModel(final Model model, final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		final int numberPagesShown = getSiteConfigService().getInt(PAGINATION_NUMBER_OF_RESULTS_COUNT, 5);

		model.addAttribute("numberPagesShown", Integer.valueOf(numberPagesShown));
		model.addAttribute("searchPageData", searchPageData);
		model.addAttribute("isShowAllAllowed", calculateShowAll(searchPageData, showMode));
		model.addAttribute("isShowPageAllowed", calculateShowPaged(searchPageData, showMode));
	}

	protected Boolean calculateShowAll(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf((showMode != ShowMode.All && //
				searchPageData.getPagination().getTotalNumberOfResults() > searchPageData.getPagination().getPageSize())
				&& isShowAllAllowed(searchPageData));
	}

	protected Boolean calculateShowPaged(final SearchPageData<?> searchPageData, final ShowMode showMode)
	{
		return Boolean.valueOf(showMode == ShowMode.All && isShowAllAllowed(searchPageData));
	}

	/**
	 * Special case, when total number of results > {@link #MAX_PAGE_LIMIT}
	 */
	protected boolean isShowAllAllowed(final SearchPageData<?> searchPageData)
	{
		return searchPageData.getPagination().getNumberOfPages() > 1
				&& searchPageData.getPagination().getTotalNumberOfResults() < MAX_PAGE_LIMIT;
	}

	protected SortData createSort(final String sortCode)
	{
		final SortData sortData = new SortData();
		sortData.setCode(sortCode);
		sortData.setSelected(true);
		return sortData;
	}

	protected PageableData createPageableData(final int pageNumber, final int pageSize, final ShowMode showMode)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(pageNumber);
		pageableData.setSort(BY_DATE);
		pageableData.setPageSize(MAX_PAGE_LIMIT);

		if (ShowMode.All != showMode)
		{
			pageableData.setPageSize(pageSize);
		}
		return pageableData;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

}
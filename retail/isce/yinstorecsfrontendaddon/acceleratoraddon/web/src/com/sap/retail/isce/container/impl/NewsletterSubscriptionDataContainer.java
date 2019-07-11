/*****************************************************************************
 Class:        NewsletterSubscriptionDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Retrieve information on newsletter subscription of the customer from Hybris marketing
 */
public class NewsletterSubscriptionDataContainer extends InteractionsDataContainerBase
{
	public static final String INTERATIONTYPE_NESLETTER_SUBSCRIPTED = "NEWSLETTER_SUBSCR";
	public static final String INTERATIONTYPE_NESLETTER_UN_SUBSCRIPTED = "NEWSLETTER_UNSUBSCR";

	protected static Logger log = Logger.getLogger(NewsletterSubscriptionDataContainer.class.getName());

	private Boolean newsletterSubscripted = null;

	/**
	 * Default constructor.
	 */
	public NewsletterSubscriptionDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.top = INTEGER_ONE;
		this.select = PROPERTY_INTERACTION_TYPE_CODE;
		this.orderBy = PROPERTY_TIMESTAMP + " desc";
		this.containerName = NewsletterSubscriptionDataContainer.class.getName();

	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		this.setDataInErrorState();

		if (httpODataResult == null || !HttpODataResult.StatusCode.OK.equals(httpODataResult.getStatusCode()))
		{
			return;
		}

		final List<ODataEntry> oDataEntries = httpODataResult.getEntities();

		if (oDataEntries == null || oDataEntries.isEmpty())
		{
			this.newsletterSubscripted = Boolean.FALSE;
			return;
		}

		final String interactionType = (String) oDataEntries.get(0).getProperties()
				.get(InteractionsDataContainerBase.PROPERTY_INTERACTION_TYPE_CODE);

		if (interactionType.equals(NewsletterSubscriptionDataContainer.INTERATIONTYPE_NESLETTER_SUBSCRIPTED))
		{
			this.newsletterSubscripted = Boolean.TRUE;
		}
		else if (interactionType.equals(NewsletterSubscriptionDataContainer.INTERATIONTYPE_NESLETTER_UN_SUBSCRIPTED))
		{
			this.newsletterSubscripted = Boolean.FALSE;
		}
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.profile", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public String getContainerContextParamName()
	{
		return "NewsletterSubscriptionDataContainer";
	}

	@Override
	public void setDataInErrorState()
	{
		this.newsletterSubscripted = null;
	}

	@Override
	public String getFilter()
	{
		return super.getFilter() + " and (" + PROPERTY_INTERACTION_TYPE_CODE + " eq '" + INTERATIONTYPE_NESLETTER_SUBSCRIPTED
				+ "' or " + PROPERTY_INTERACTION_TYPE_CODE + " eq '" + INTERATIONTYPE_NESLETTER_UN_SUBSCRIPTED + "')";
	}

	@Override
	public String getTraceableFilter()
	{
		return getFilter().replace(getCustomerData().getUid(), "<value not traced>");
	}

	/**
	 * Returns the flag if a newsletter is subscripted.
	 *
	 * @return the newsletterSubscripted
	 */
	public Boolean getNewsletterSubscripted()
	{
		return newsletterSubscripted;
	}

	/**
	 * Sets the flag if a newsletter is subscripted.
	 *
	 * @param newsletterSubscripted
	 *           the newsletterSubscripted to set
	 */
	public void setNewsletterSubscripted(final Boolean newsletterSubscripted)
	{
		this.newsletterSubscripted = newsletterSubscripted;
	}

	@Override
	public void encodeHTML()
	{
		// nothing do encode
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, getCustomerData().getUid());
	}

}

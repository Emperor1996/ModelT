/*****************************************************************************
    Interface:		OaaRestService
    Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest;

/**
 * @author SAP
 *
 */
public interface OaaRestService
{

	/**
	 * Set the REST service configuration
	 *
	 * @param restServiceConfiguration
	 *           the restServiceConfiguration to set
	 */
	void setRestServiceConfiguration(final RestServiceConfiguration restServiceConfiguration);

	/**
	 * Set the Attribute for BackendDown in the Session
	 *
	 * @param backendDown
	 *           the value to set in the session
	 */
	void setBackendDown(final boolean backendDown);

}

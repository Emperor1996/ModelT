/*****************************************************************************
    Class:        AuthenticationResult
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.impl;

import org.springframework.http.HttpHeaders;


/**
 * Container for Http Header
 */
public class AuthenticationResult
{


	private HttpHeaders responseHeader;

	/**
	 * @return the responseHeader
	 */
	public HttpHeaders getResponseHeader()
	{
		return responseHeader;
	}

	/**
	 * @param responseHeader
	 *           the responseHeader to set
	 */
	public void setResponseHeader(final HttpHeaders responseHeader)
	{
		this.responseHeader = responseHeader;
	}

}

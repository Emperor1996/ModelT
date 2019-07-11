/*****************************************************************************
    Class:        DefaultURLProvider
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.impl;

import java.net.URI;
import java.net.URISyntaxException;

import com.sap.retail.oaa.commerce.services.rest.util.URLProvider;


/**
 * Default implementation of URLProvider.
 */
public class DefaultURLProvider implements URLProvider
{

	@Override
	public URI compileURI(final String urlString, final String path, final String client) throws URISyntaxException
	{
		URI url;
		if (path.contains("?"))
		{
			url = new URI(urlString + path + "&sap-client=" + client);
		}
		else
		{
			url = new URI(urlString + path + "?sap-client=" + client);
		}
		return url;
	}

}

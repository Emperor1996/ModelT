/*****************************************************************************
    Class:        DefaultHttpHeaderProvider
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.impl;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.codec.Base64;

import com.sap.retail.oaa.commerce.services.rest.util.HttpHeaderProvider;


/**
 * Default implementation of HttpHeaderProvider.
 */
public class DefaultHttpHeaderProvider implements HttpHeaderProvider
{
	/** HTTP header X-CSRF-Token key */
	private static final String HTTP_PARAM_X_CSRF_TOKEN = "X-CSRF-Token";
	/** HTTP header Cookie key */
	private static final String HTTP_PARAM_COOKIE = "Cookie";

	@Override
	public HttpHeaders compileHttpHeader(final String user, final String password)
	{
		final String auth = user + ":" + password;
		final byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
		final String authHeader = "Basic " + new String(encodedAuth);

		final HttpHeaders header = new HttpHeaders();
		header.set("Authorization", authHeader);

		return header;
	}

	@Override
	public HttpHeaders appendCookieToHeader(final HttpHeaders header, final HttpHeaders responseHeader)
	{

		final List<String> cookies = responseHeader.get("set-cookie");

		for (final String cookie : cookies)
		{
			header.add(HTTP_PARAM_COOKIE, cookie);
		}

		return header;
	}

	@Override
	public HttpHeaders appendCsrfToHeader(final HttpHeaders header, final HttpHeaders responseHeader)
	{
		final String csrfToken = responseHeader.get(HTTP_PARAM_X_CSRF_TOKEN).get(0);
		header.add(HTTP_PARAM_X_CSRF_TOKEN, csrfToken);
		return header;
	}

}

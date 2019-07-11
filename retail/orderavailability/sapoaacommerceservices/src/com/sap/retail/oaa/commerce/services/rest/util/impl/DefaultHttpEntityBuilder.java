/*****************************************************************************
    Class:        DefaultHttpEntityBuilder
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.rest.util.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.sap.retail.oaa.commerce.services.reservation.jaxb.pojos.request.ReservationAbapRequest;
import com.sap.retail.oaa.commerce.services.rest.util.HttpEntityBuilder;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingAbapRequest;


/**
 * Default Implementation for HttpEntityBuilder
 */
public class DefaultHttpEntityBuilder implements HttpEntityBuilder
{

	@Override
	public HttpEntity<ReservationAbapRequest> createHttpEntity(final HttpHeaders header, final ReservationAbapRequest abap)
	{
		return new HttpEntity<>(abap, header);
	}

	@Override
	public HttpEntity<SourcingAbapRequest> createHttpEntityForSourcing(final HttpHeaders header, final SourcingAbapRequest abap)
	{
		return new HttpEntity<>(abap, header);
	}

	@Override
	public HttpEntity<String> createHttpEntity(final HttpHeaders header, final String string)
	{
		return new HttpEntity<>(string, header);
	}

	@Override
	public HttpEntity createHttpEntity(final HttpHeaders header)
	{
		return new HttpEntity(header);
	}

}

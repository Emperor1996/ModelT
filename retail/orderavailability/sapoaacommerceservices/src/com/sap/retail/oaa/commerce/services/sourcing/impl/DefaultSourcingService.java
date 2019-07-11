/*****************************************************************************
    Class:        DefaultSourcingService
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.sap.retail.oaa.commerce.services.constants.SapoaacommerceservicesConstants;
import com.sap.retail.oaa.commerce.services.rest.impl.DefaultAbstractRestService;
import com.sap.retail.oaa.commerce.services.rest.util.HttpEntityBuilder;
import com.sap.retail.oaa.commerce.services.rest.util.HttpHeaderProvider;
import com.sap.retail.oaa.commerce.services.rest.util.URLProvider;
import com.sap.retail.oaa.commerce.services.rest.util.exception.AuthenticationServiceException;
import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;
import com.sap.retail.oaa.commerce.services.rest.util.exception.RestInitializationException;
import com.sap.retail.oaa.commerce.services.rest.util.impl.AuthenticationResult;
import com.sap.retail.oaa.commerce.services.rest.util.impl.AuthenticationService;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingRequestMapper;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingResultHandler;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingService;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request.SourcingAbapRequest;
import com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.response.SourcingResponse;


/**
 * Default Sourcing Service
 */
public class DefaultSourcingService extends DefaultAbstractRestService implements SourcingService
{
	/**
	 *
	 */
	private static final String ERROR_WHEN_CALLING_SOURCING_WEB_SERVICE = "Error when calling sourcing web service.";

	private static final Logger LOG = Logger.getLogger(DefaultSourcingService.class);

	private static final String SERVICE_RESOURCE_PATH = "/sap/car/rest/oaa/sourcing";

	private SourcingResultHandler sourcingResultHandler;
	private SourcingRequestMapper cartMapper;
	private HttpHeaderProvider httpHeaderProvider;
	private AuthenticationService authenticationService;
	private URLProvider urlProvider;
	private HttpEntityBuilder httpEntityBuilder;

	private AuthenticationResult authenticationResult;


	@Override
	public void callRestServiceAndPersistResult(final AbstractOrderModel model) throws SourcingException, CARBackendDownException
	{
		// call REST Service
		SourcingResponse response = new SourcingResponse();
		if (model instanceof CartModel)
		{
			response = this.callRestService(model, false, true);
		}
		else if (model instanceof OrderModel)
		{
			response = this.callRestService(model, false, true);
		}
		sourcingResultHandler.persistSourcingResultInCart(response, model, getRestServiceConfiguration());
	}

	@Override
	public SourcingResponse callRestService(final AbstractOrderModel abstractOrderModel, final boolean execAllStrategies,
			final boolean reserve) throws SourcingException, CARBackendDownException
	{

		SourcingResponse sourcingResponse = null;

		try
		{
			initialize();
			beforeRestCall();

			SourcingAbapRequest sourcingAbapRequest = null;

			if (abstractOrderModel instanceof OrderModel)
			{
				final OrderModel orderModel = (OrderModel) abstractOrderModel;
				sourcingAbapRequest = cartMapper.mapOrderModelToSourcingRequest(orderModel, execAllStrategies, reserve,
						getRestServiceConfiguration());
			}
			else
			{
				final CartModel cartModel = (CartModel) abstractOrderModel;
				sourcingAbapRequest = cartMapper.mapCartModelToSourcingRequest(cartModel, execAllStrategies, reserve,
						getRestServiceConfiguration());
			}

			try
			{
				sourcingResponse = callSourcingRestService(sourcingAbapRequest);
			}
			catch (final HttpClientErrorException e)
			{
				checkHttpStatusCode(e);
				if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)
						|| (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED) && authenticationResult != null))
				{
					authenticationResult = null;
					sourcingResponse = callSourcingRestService(sourcingAbapRequest);
				}
				else
				{
					LOG.error(ERROR_WHEN_CALLING_SOURCING_WEB_SERVICE + "User: " + getRestServiceConfiguration().getUser()
							+ ", Error: " + e.getStatusCode());
					throw new SourcingException(e);
				}
			}
			catch (final ResourceAccessException e)
			{
				setBackendDown(true);
				throw new CARBackendDownException(SapoaacommerceservicesConstants.CAR_BACKEND_DOWN_MESSAGE, e);
			}

			return sourcingResponse;
		}
		catch (final HttpClientErrorException e)
		{
			checkHttpStatusCode(e);
			LOG.error(ERROR_WHEN_CALLING_SOURCING_WEB_SERVICE);
			throw new SourcingException(e);
		}
		catch (AuthenticationServiceException | URISyntaxException | SourcingException | RestInitializationException e)
		{
			authenticationResult = null;
			LOG.error(ERROR_WHEN_CALLING_SOURCING_WEB_SERVICE);
			throw new SourcingException(e);
		}
		catch (final ResourceAccessException e)
		{
			setBackendDown(true);
			throw new CARBackendDownException(SapoaacommerceservicesConstants.CAR_BACKEND_DOWN_MESSAGE, e);
		}
	}

	/**
	 * @param abap
	 * @return sourcingResponse
	 * @throws URISyntaxException
	 */
	private SourcingResponse callSourcingRestService(final SourcingAbapRequest abap)
			throws URISyntaxException, CARBackendDownException
	{
		//only execute authentication in case it wasn't executed before
		if (authenticationResult == null)
		{
			authenticationResult = authenticationService.execute(getRestServiceConfiguration().getUser(),
					getRestServiceConfiguration().getPassword(), getRestServiceConfiguration().getTargetUrl(), SERVICE_RESOURCE_PATH,
					getRestServiceConfiguration().getSapCarClient());
		}

		final HttpHeaders header = compileHTTPHeader(authenticationResult);
		final HttpEntity<SourcingAbapRequest> entity = httpEntityBuilder.createHttpEntityForSourcing(header, abap);

		final ResponseEntity<SourcingResponse> response = getRestTemplate()
				.exchange(urlProvider.compileURI(getRestServiceConfiguration().getTargetUrl(), SERVICE_RESOURCE_PATH,
						getRestServiceConfiguration().getSapCarClient()), HttpMethod.PUT, entity, SourcingResponse.class);

		if (response != null)
		{
			return response.getBody();
		}
		return null;
	}


	/**
	 * @param sourcingResultHandler
	 *           the sourcingResultHandler to set
	 */
	public void setSourcingResultHandler(final SourcingResultHandler sourcingResultHandler)
	{
		this.sourcingResultHandler = sourcingResultHandler;
	}

	/**
	 * @return the sourcingResultHandler
	 */
	protected SourcingResultHandler getSourcingResultHandler()
	{
		return sourcingResultHandler;
	}

	/**
	 * @param cartMapper
	 *           the cartMapper to set
	 */
	public void setCartMapper(final SourcingRequestMapper cartMapper)
	{
		this.cartMapper = cartMapper;
	}

	/**
	 * @return the cartMapper
	 */
	protected SourcingRequestMapper getCartMapper()
	{
		return cartMapper;
	}

	/**
	 * @param httpHeaderProvider
	 *           the httpHeaderProvider to set
	 */
	public void setHttpHeaderProvider(final HttpHeaderProvider httpHeaderProvider)
	{
		this.httpHeaderProvider = httpHeaderProvider;
	}

	/**
	 * @return the httpHeaderProvider
	 */
	protected HttpHeaderProvider getHttpHeaderProvider()
	{
		return httpHeaderProvider;
	}


	/**
	 * @param urlProvider
	 *           the urlProvider to set
	 */
	public void setUrlProvider(final URLProvider urlProvider)
	{
		this.urlProvider = urlProvider;
	}

	/**
	 * @return the urlProvider
	 */
	protected URLProvider getUrlProvider()
	{
		return urlProvider;
	}

	/**
	 * @param authenticationService
	 *           the authenticationService to set
	 */
	public void setAuthenticationService(final AuthenticationService authenticationService)
	{
		this.authenticationService = authenticationService;
	}

	/**
	 * @return the authenticationService
	 */
	protected AuthenticationService getAuthenticationService()
	{
		return authenticationService;
	}

	/**
	 * @param httpEntityBuilder
	 *           the httpEntityBuilder to set
	 */
	public void setHttpEntityBuilder(final HttpEntityBuilder httpEntityBuilder)
	{
		this.httpEntityBuilder = httpEntityBuilder;
	}

	/**
	 * @return the httpEntityBuilder
	 */
	protected HttpEntityBuilder getHttpEntityBuilder()
	{
		return httpEntityBuilder;
	}

	/**
	 * @param authenticationResult
	 * @return HttpHeaders
	 */
	private HttpHeaders compileHTTPHeader(final AuthenticationResult authenticationResult)
	{
		final HttpHeaders header = httpHeaderProvider.compileHttpHeader(getRestServiceConfiguration().getUser(),
				getRestServiceConfiguration().getPassword());
		httpHeaderProvider.appendCsrfToHeader(header, authenticationResult.getResponseHeader());
		httpHeaderProvider.appendCookieToHeader(header, authenticationResult.getResponseHeader());
		final List<MediaType> acceptList = new ArrayList<>();
		acceptList.add(MediaType.APPLICATION_XML);
		header.setAccept(acceptList);
		header.setContentType(MediaType.APPLICATION_XML);
		return header;
	}

}

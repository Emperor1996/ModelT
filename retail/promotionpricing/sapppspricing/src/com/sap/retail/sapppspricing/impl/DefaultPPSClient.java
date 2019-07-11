/*****************************************************************************
Class: DefaultPPSClient

@Copyright (c) 2016, SAP SE, Germany, All rights reserved.

 *****************************************************************************/

/**
 *
 */
package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.sap.core.configuration.enums.HTTPAuthenticationType;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.ppengine.client.dto.ARTSCommonHeaderType;
import com.sap.ppengine.client.dto.ARTSCommonHeaderType.Response;
import com.sap.ppengine.client.dto.PriceCalculate;
import com.sap.ppengine.client.dto.PriceCalculateResponse;
import com.sap.ppengine.client.dto.ResponseCommonData.BusinessError;
import com.sap.ppengine.client.impl.PromotionPricingServiceInternal;
import com.sap.ppengine.client.service.PromotionPricingService;
import com.sap.ppengine.core.util.impl.DefaultStringifier;
import com.sap.retail.sapppspricing.PPSClient;
import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;
import com.sap.retail.sapppspricing.enums.RemLocalPPS;


/**
 * PPS client supporting local calls against {@link PromotionPricingServiceInternal} interface as well as a remote call
 * via REST. For the remote call basic authentication is supported as of now, supporting JSESSIONIDs. Whether local or
 * remote PPS is called is controlled via configuration (see {@link PPSConfigService})
 */
public class DefaultPPSClient extends DefaultPPSClientBeanAccessor implements PPSClient
{
	// ID to relate this request to entry in server log
	protected static final String X_REQUEST_ID = "x-request-id";
	protected static final String AUTHORIZATION = "Authorization";
	// ConcurrentHashMap since we have a singleton bean. We expect that at each
	// base store needs a separate storage for the jSessionId
	private final Map<String, List<String>> sessionIds = new ConcurrentHashMap<>();

	private static final Logger LOG = LoggerFactory.getLogger(DefaultPPSClient.class);
	private HttpMessageConverter<?> jsonConverter;
	private BaseStoreService baseStoreService;

	// Threadsafe!
	private RestTemplate restTemplate;

	private <T> void traceIt(final String description, final T theData)
	{
		if (LOG.isTraceEnabled())
		{
			LOG.trace(description + new DefaultStringifier().asString(theData));
		}
	}

	public PriceCalculateResponse callPPS(final PriceCalculate priceCalculate, final SAPConfigurationModel sapConfig)
	{
		if (LOG.isTraceEnabled() && jsonConverter instanceof AbstractJackson2HttpMessageConverter)
		{
			try
			{
				final AbstractJackson2HttpMessageConverter jacksonConverter = (AbstractJackson2HttpMessageConverter) jsonConverter;
				LOG.trace("PriceCalculate request: {}", jacksonConverter.getObjectMapper().writeValueAsString(priceCalculate));
			}
			catch (final JsonProcessingException e)
			{
				throw new SapPPSPricingRuntimeException("Converting PriceCalculate request to JSON failed", e);
			}
		}
		if (isRemotePricingLocation(sapConfig))
		{
			return callPPSRemote(priceCalculate, sapConfig);
		}
		else
		{
			return callPPSLocal(priceCalculate);
		}
	}

	protected PriceCalculateResponse callPPSLocal(final PriceCalculate priceCalculate)
	{
		LOG.debug("callPPSLocal()");
		final PromotionPricingService pps = getPromotionPricingService();
		final PriceCalculateResponse responseEntity = pps.calculate(priceCalculate, null);
		traceIt("Response=", responseEntity);
		final ARTSCommonHeaderType artsHeader = responseEntity.getARTSHeader();
		if (((PromotionPricingServiceInternal) pps).isRequestRejected(artsHeader))
		{
			final Response response = artsHeader.getResponse();
			if (response != null && response.getBusinessError() != null && !response.getBusinessError().isEmpty())
			{
				final BusinessError firstError = response.getBusinessError().get(0);
				throw new SapPPSPricingRuntimeException("Request rejected , business error ID=" + firstError.getErrorID()
						+ ", description=" + firstError.getDescription().getValue());
			}
			else
			{
				throw new SapPPSPricingRuntimeException("Request rejected , response missing");
			}
		}
		LOG.debug("exiting");
		return responseEntity;
	}

	protected PriceCalculateResponse callPPSRemote(final PriceCalculate priceCalculate, final SAPConfigurationModel sapConfig)
	{
		LOG.debug("entering callPPSRemote()");
		final HttpHeaders headers = createHttpHeaders(sapConfig);
		final long t1 = System.nanoTime();
		final RestTemplate template = getRestTemplate(getJsonConverter());

		try
		{
			final ResponseEntity<PriceCalculateResponse> result = template.exchange(getUrl(sapConfig), HttpMethod.POST,
					new HttpEntity<PriceCalculate>(priceCalculate, headers), PriceCalculateResponse.class);
			final PriceCalculateResponse body = result.getBody();
			final HttpHeaders responseHeaders = result.getHeaders();
			if (!result.getStatusCode().is2xxSuccessful())
			{
				LOG.error("Call of PPS failed: URI={}, statusCode={}, {}={}, result body={}", getUrl(sapConfig), result
						.getStatusCode().value(), X_REQUEST_ID, responseHeaders.get(X_REQUEST_ID), new DefaultStringifier()
						.asString(body));
				throw new SapPPSPricingRuntimeException("Unexpected return code " + result.getStatusCode().value());
			}
			else
			{
				final long t2 = System.nanoTime();
				LOG.trace("Request duration in ms: " + (t2 - t1) / 1000000);
				traceIt("Response=", body);
				extractCookie(responseHeaders);
				return body;
			}
		}
		finally
		{
			LOG.debug("exiting");
		}
	}

	protected void extractCookie(final HttpHeaders headers)
	{
		final List<String> cookie = headers.get(HttpHeaders.SET_COOKIE);
		if (cookie != null && !cookie.isEmpty())
		{
			setSessionToken(cookie);
		}
	}

	protected HttpHeaders createHttpHeaders(final SAPConfigurationModel sapConfig)
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final SAPHTTPDestinationModel httpDestination = sapConfig.getSappps_HttpDestination();
		final HTTPAuthenticationType authType = httpDestination.getAuthenticationType();
		if (HTTPAuthenticationType.BASIC_AUTHENTICATION == authType)
		{
			final String userPassword = httpDestination.getUserid() + ":" + httpDestination.getPassword();
			headers.set(AUTHORIZATION, encodeForBasicAuth(userPassword));
			final List<String> sessionToken = getSessionToken();
			if (sessionToken != null)
			{
				for (final String part : sessionToken)
				{
					headers.add(HttpHeaders.COOKIE, part);
				}
			}
		}
		return headers;
	}

	private String encodeForBasicAuth(final String userPassword)
	{
		return "Basic " + new String(Base64.encodeBase64(userPassword.getBytes(Charset.forName("ISO-8859-1"))));
	}

	protected List<String> getSessionToken()
	{
		return sessionIds.get(sessionTokenKey());
	}

	protected void setSessionToken(final List<String> token)
	{
		sessionIds.put(sessionTokenKey(), token);
	}

	private String sessionTokenKey()
	{
		final BaseStoreModel currentBaseStore = getBaseStoreService().getCurrentBaseStore();
		return currentBaseStore == null ? "NONE" : currentBaseStore.getUid();
	}

	protected RestTemplate getRestTemplate(final HttpMessageConverter<?> converter)
	{
		if (restTemplate == null)
		{
			restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(converter));
		}
		return restTemplate;
	}

	protected String getUrl(final SAPConfigurationModel sapConfig)
	{
		final String url = sapConfig.getSappps_HttpDestination().getTargetURL();
		LOG.debug("Using URL {}", url);
		return url;
	}

	protected boolean isRemotePricingLocation(final SAPConfigurationModel sapConfig)
	{
		final RemLocalPPS ppsBackOfficeProperty = sapConfig.getSappps_localRemote();

		if (RemLocalPPS.REMOTEPPS.equals(ppsBackOfficeProperty))
		{
			LOG.debug("Using Remote Promotion Pricing Service");
			return true;
		}
		else
		// Nothing or local set -> use local as it requires less configuration
		{
			LOG.debug("Using Local Promotion Pricing Service");
			return false;
		}
	}

	@SuppressWarnings("javadoc")
	public HttpMessageConverter<?> getJsonConverter() // NOSONAR
	{
		return jsonConverter;
	}

	@SuppressWarnings("javadoc")
	public void setJsonConverter(final HttpMessageConverter<?> jsonConverter)
	{
		this.jsonConverter = jsonConverter;
	}

	@SuppressWarnings("javadoc")
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@SuppressWarnings("javadoc")
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

}

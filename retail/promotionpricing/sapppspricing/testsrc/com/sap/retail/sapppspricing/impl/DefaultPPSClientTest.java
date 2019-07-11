/*****************************************************************************
Class: DefaultPPSClientTest
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.enums.HTTPAuthenticationType;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sap.ppengine.client.dto.ARTSCommonHeaderType;
import com.sap.ppengine.client.dto.ARTSCommonHeaderType.Response;
import com.sap.ppengine.client.dto.DescriptionCommonData;
import com.sap.ppengine.client.dto.PriceCalculate;
import com.sap.ppengine.client.dto.PriceCalculateResponse;
import com.sap.ppengine.client.dto.ResponseCommonData.BusinessError;
import com.sap.ppengine.client.impl.PromotionPricingServiceInternal;
import com.sap.ppengine.client.service.PromotionPricingService;
import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;
import com.sap.retail.sapppspricing.impl.DefaultPPSClient;


@SuppressWarnings("javadoc")
@UnitTest
public class DefaultPPSClientTest
{
	private static final String MY_STORE = "myStore";

	private class DefaultPPSClientForTest extends DefaultPPSClient
	{
		@Override
		public PromotionPricingService getPromotionPricingService()
		{
			return pricingPromotionServiceMock;
		}

		@Override
		protected RestTemplate getRestTemplate(@SuppressWarnings("rawtypes") final HttpMessageConverter converter)
		{
			return restTemplateMock;
		}

	}

	private DefaultPPSClient cut;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private HttpMessageConverter<?> jsonConverter;
	@Mock
	private PromotionPricingServiceInternal pricingPromotionServiceMock;
	@Mock
	private RestTemplate restTemplateMock;
	@Mock
	private PPSConfigService configService;

	private BaseStoreModel baseStoreModel;
	private SAPHTTPDestinationModel httpDestination;
	private SAPConfigurationModel sapConfig;

	@Before
	public void setUp()
	{
		httpDestination = new SAPHTTPDestinationModel();
		MockitoAnnotations.initMocks(this);
		baseStoreModel = new BaseStoreModel();
		sapConfig = new SAPConfigurationModel();
		Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStoreModel);
		sapConfig.setSappps_HttpDestination(httpDestination);
		sapConfig.setSappps_active(Boolean.TRUE);
		cut = new DefaultPPSClientForTest();
		httpDestination.setTargetURL("myUri");
		cut.setBaseStoreService(baseStoreService);
		baseStoreModel.setUid(MY_STORE);
	}

	@Test
	public void testSetGetBaseStoreService()
	{
		cut = new DefaultPPSClientForTest();
		assertNull(cut.getBaseStoreService());
		cut.setBaseStoreService(baseStoreService);
		assertSame(baseStoreService, cut.getBaseStoreService());
	}

	@Test
	public void testSetGetJsonConverter()
	{
		assertNull(cut.getJsonConverter());
		cut.setJsonConverter(jsonConverter);
		assertSame(jsonConverter, cut.getJsonConverter());
	}

	@Test
	public void testGetUri()
	{
		assertEquals("myUri", cut.getUrl(sapConfig));
	}


	@Test
	public void testCallPPSLocal()
	{
		final PriceCalculate priceCalculate = new PriceCalculate();
		final PriceCalculateResponse expectedResponse = new PriceCalculateResponse();
		Mockito.when(pricingPromotionServiceMock.calculate(priceCalculate, null)).thenReturn(expectedResponse);

		assertSame(expectedResponse, cut.callPPSLocal(priceCalculate));
	}

	@Test(expected = NullPointerException.class)
	public void testCallPPSLocalThrowsException()
	{
		final PriceCalculate priceCalculate = new PriceCalculate();
		Mockito.when(pricingPromotionServiceMock.calculate(priceCalculate, null)).thenThrow(new NullPointerException("oha!"));

		cut.callPPSLocal(priceCalculate);
		fail("Should not arrive here!");
	}

	@Test
	public void testCallPPSLocalBusinessError()
	{
		final PriceCalculate priceCalculate = new PriceCalculate();
		final PriceCalculateResponse expectedResponse = new PriceCalculateResponse();
		final ARTSCommonHeaderType artsHeader = new ARTSCommonHeaderType();
		expectedResponse.setARTSHeader(artsHeader);
		final Response response = new Response();
		artsHeader.setResponse(response);
		final BusinessError businessError = new BusinessError();
		response.getBusinessError().add(businessError);
		businessError.setErrorID("4711");
		final DescriptionCommonData descriptionCommonData = new DescriptionCommonData();
		businessError.setDescription(descriptionCommonData);
		descriptionCommonData.setValue("Something terrible has happened!");
		Mockito.when(pricingPromotionServiceMock.calculate(priceCalculate, null)).thenReturn(expectedResponse);
		Mockito.when(pricingPromotionServiceMock.isRequestRejected(artsHeader)).thenReturn(true);
		try
		{
			cut.callPPSLocal(priceCalculate);
			fail("Should not arrive here!");
		}
		catch (final SapPPSPricingRuntimeException e)
		{
			assertTrue(e.getMessage().contains("4711"));
			assertTrue(e.getMessage().contains("Something terrible has happened!"));
		}
	}

	@Test
	public void testCallPPSRemote()
	{
		final PriceCalculate priceCalculate = new PriceCalculate();
		final PriceCalculateResponse body = new PriceCalculateResponse();
		final MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, "returnedToken");
		final ResponseEntity<PriceCalculateResponse> expectedResponse = new ResponseEntity<PriceCalculateResponse>(body, headers,
				HttpStatus.ACCEPTED);
		Mockito.when(
				restTemplateMock.exchange(Mockito.eq("myUri"), Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
						Mockito.eq(PriceCalculateResponse.class))).thenReturn(expectedResponse);

		assertSame(body, cut.callPPSRemote(priceCalculate, sapConfig));
		assertEquals(Arrays.asList("returnedToken"), cut.getSessionToken());
	}

	@Test(expected = SapPPSPricingRuntimeException.class)
	public void testCallPPSRemoteNoSuccess()
	{
		final PriceCalculate priceCalculate = new PriceCalculate();
		final PriceCalculateResponse body = new PriceCalculateResponse();
		final ResponseEntity<PriceCalculateResponse> expectedResponse = new ResponseEntity<PriceCalculateResponse>(body,
				HttpStatus.FORBIDDEN);

		Mockito.when(
				restTemplateMock.exchange(Mockito.eq("myUri"), Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
						Mockito.eq(PriceCalculateResponse.class))).thenReturn(expectedResponse);

		cut.callPPSRemote(priceCalculate, sapConfig);
		fail("Exception expected");
	}

	@Test
	public void testCallPPSRemoteException() throws Exception
	{
		final PriceCalculate priceCalculate = new PriceCalculate();

		final RuntimeException exception = new RestClientException("mist!");
		Mockito.when(
				restTemplateMock.exchange(Mockito.eq("myUri"), Mockito.eq(HttpMethod.POST), Mockito.any(HttpEntity.class),
						Mockito.eq(PriceCalculateResponse.class))).thenThrow(exception);

		try
		{
			cut.callPPSRemote(priceCalculate, sapConfig);
		}
		catch (final Exception e)
		{
			assertSame(exception, e);
		}
	}

	@Test
	public void testCreateHttpHeadersForNoAuth() throws Exception
	{
		final HttpHeaders result = cut.createHttpHeaders(sapConfig);
		assertEquals(2, result.size());
		assertEquals(MediaType.APPLICATION_JSON, result.getContentType());
		assertEquals(1, result.getAccept().size());
		assertEquals(MediaType.APPLICATION_JSON, result.getAccept().get(0));
	}

	@Test
	public void testCreateHttpHeadersForBasicAuthNoToken() throws Exception
	{
		httpDestination.setAuthenticationType(HTTPAuthenticationType.BASIC_AUTHENTICATION);
		httpDestination.setUserid("SWO_TEST");
		httpDestination.setPassword("Welcome1");
		final HttpHeaders result = cut.createHttpHeaders(sapConfig);
		assertEquals(3, result.size());
		assertEquals("Basic U1dPX1RFU1Q6V2VsY29tZTE=", result.get(DefaultPPSClient.AUTHORIZATION).get(0));
	}

	@Test
	public void testSetGetSessionToken() throws Exception
	{
		final List<String> token = Arrays.asList("myToken");
		cut.setSessionToken(token);
		assertEquals(token, cut.getSessionToken());
	}

	@Test
	public void testSessionTokenPerStore1() throws Exception
	{
		final List<String> token = Arrays.asList("myToken");
		cut.setSessionToken(token);
		baseStoreModel.setUid("otherStore");
		assertNull(cut.getSessionToken());
	}

	@Test
	public void testSessionTokenPerStore2() throws Exception
	{
		final List<String> token = Arrays.asList("myToken");
		cut.setSessionToken(token);
		baseStoreModel.setUid("otherStore");
		final List<String> otherToken = Arrays.asList("myToken");
		cut.setSessionToken(otherToken);
		baseStoreModel.setUid(MY_STORE);
		assertEquals(token, cut.getSessionToken());
		baseStoreModel.setUid("otherStore");
		assertEquals(otherToken, cut.getSessionToken());
	}

	@Test
	public void testCreateHttpHeadersForBasicAuthNoUserPwd() throws Exception
	{
		httpDestination.setAuthenticationType(HTTPAuthenticationType.BASIC_AUTHENTICATION);
		httpDestination.setUserid("");
		httpDestination.setPassword("");
		final HttpHeaders result = cut.createHttpHeaders(sapConfig);
		assertEquals(3, result.size());
		assertEquals("Basic Og==", result.get(DefaultPPSClient.AUTHORIZATION).get(0));
	}

	@Test
	public void testExtractToken() throws Exception
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, "myToken");
		cut.extractCookie(headers);
		assertEquals(Arrays.asList("myToken"), cut.getSessionToken());
	}

	@Test
	public void testGetRestTemplateReturnsSameInstance() throws Exception
	{
		final RestTemplate template1 = cut.getRestTemplate(jsonConverter);
		final RestTemplate template2 = cut.getRestTemplate(jsonConverter);
		assertSame(template1, template2);
	}

}

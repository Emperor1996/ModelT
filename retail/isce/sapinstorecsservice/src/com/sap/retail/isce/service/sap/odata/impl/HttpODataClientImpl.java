/*****************************************************************************
 Class:        HttpODataClientImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import de.hybris.platform.core.Registry;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.commons.HttpHeaders;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.feed.FeedMetadata;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.core.commons.ContentType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sap.retail.isce.sap.exception.SAPISCERuntimeException;
import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.HttpODataClient;
import com.sap.retail.isce.service.sap.odata.HttpODataCommonStorage;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;


/**
 * Default Implementation of HttpODataClient
 */
public class HttpODataClientImpl implements HttpODataClient
{
	protected static final String LOCATION = "Location";
	protected static final String X_CSRF_TOKEN_FETCH = "Fetch";
	protected static final String AUTHORISATION_BASIC = "Basic ";
	protected static final String EQUALS_SIGN = "=";
	protected static final String HTTP_HEADER_SET_COOKIE = "set-cookie";
	protected static final String HTTP_HEADER_COOKIE = "cookie";
	protected static final String HTTP_HEADER_X_CSRF_TOKEN = "x-csrf-token";
	protected static final String SLASH = "/";
	protected static final String METADATA = "$metadata";

	protected static final String BATCH = "$batch";
	protected static final String BOUNDARY = "batch_boundary";

	protected static final String CONTENT_TYPE_MULTIPART_MIXED = ContentType.MULTIPART_MIXED.toContentTypeString();
	protected static final String CONTENT_TYPE_BATCH = CONTENT_TYPE_MULTIPART_MIXED + "; boundary=" + BOUNDARY;
	protected static final String CONTENT_TYPE_APPLICATION_JSON = ContentType.APPLICATION_JSON.toContentTypeString();
	protected static final String CONTENT_TYPE_APPLICATION_XML = ContentType.APPLICATION_XML.toContentTypeString();


	protected static final String STORAGE_SERVICE_X_CSRF_TOKEN = "SERVICE_X-CSRF-TOKEN";
	protected static final String STORAGE_SERVICE_METADATA = "SERVICE_METADATA";
	protected static final String STORAGE_SERVICE_COOKIES = "SERVICE_COOKIES";
	protected static final String VALUE_NOT_TRACED_LABEL = "<value not traced>";
	protected static final String STATUS_CODE_LABEL = "StatusCode: ";
	protected static final String FAILED_DUE_TO_LABEL = "' failed, due to ";

	protected static Logger log = Logger.getLogger(HttpODataClientImpl.class.getName());

	protected HttpODataCommonStorage httpODataCommonStorage = null;
	protected CommonI18NService commonI18NService = null;
	protected ApplicationContext applicationContext = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataClient#readFeed(java.lang.String, java.lang.String,
	 * de.hybris.platform.sap.core.configuration.http.HTTPDestination, java.lang.String)
	 */
	@Override
	public HttpODataResult readFeed(final String entitySetName, final String filter, final HTTPDestination httpDestination,
			final String serviceURI) throws HttpODataException
	{
		return readFeedIntern(entitySetName, filter, httpDestination, serviceURI, true);
	}

	/**
	 * Reads entity data using HTTP-Get.
	 *
	 * @param entitySetName
	 *           name of the entity set to read
	 * @param filter
	 *           filter string
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @param retry
	 *           flag indicating whether a second try to read the feed should be done
	 * @return result containing the requested entity data, any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of a communication failure
	 */
	public HttpODataResult readFeedIntern(final String entitySetName, final String filter, final HTTPDestination httpDestination,
			final String serviceURI, final boolean retry) throws HttpODataException
	{
		HttpODataResultManipulator httpODataResultManipulator = null;
		HttpODataResult httpODataResult = null;

		final Map<String, Object> destination = getInitializedDestination(httpDestination, serviceURI);
		final Edm edm = getEDM(destination, serviceURI);
		InputStream httpInputStream = null;
		try
		{
			final URI uri = getFullQualifiedServiceURI(entitySetName, filter, httpDestination, serviceURI);

			final ConnectionWithTimeStamp conWithStamp = initializeConnection(uri, CONTENT_TYPE_APPLICATION_XML, HttpMethod.GET,
					destination, httpDestination, serviceURI);

			final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();

			checkStatus(conWithStamp.connection, httpDestination.getHttpDestinationName());
			traceCallTime(conWithStamp);
			httpODataResultManipulator = prepareResult(conWithStamp.connection, edm);
			try
			{
				httpInputStream = conWithStamp.connection.getInputStream();
				final ODataFeed oDataFeed = entityProviderReadFeed(entitySetName, entityContainer, httpInputStream);
				httpODataResultManipulator.setEntities(oDataFeed.getEntries());
				httpODataResult = (HttpODataResult) httpODataResultManipulator;
			}

			finally
			{
				IOUtils.closeQuietly(httpInputStream);
			}
		}
		catch (final HttpODataException ex)
		{
			if (checkSupportedHttpErrorCodes(ex) && retry)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Timeout detected. Retry to execute reedFeed again.");
				}
				this.httpODataCommonStorage.removeDestination(httpDestination.getHttpDestinationName());
				httpODataResult = readFeedIntern(entitySetName, filter, httpDestination, serviceURI, false);
			}
			else
			{
				throw ex;
			}
		}
		catch (final URISyntaxException | ODataException | IOException ex)
		{
			throw new HttpODataException("Getting entity via OData Service '" + serviceURI + FAILED_DUE_TO_LABEL + ex.getMessage(),
					ex);
		}
		return httpODataResult;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataClient#getMediaFile(java.lang.String, java.util.Map,
	 * java.lang.String, de.hybris.platform.sap.core.configuration.http.HTTPDestination, java.lang.String)
	 */
	@Override
	public HttpODataResult getMediaFile(final String entitySetName, final Map<String, Object> entityKeys, final String contentType,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
	{
		return getMediaFileIntern(entitySetName, entityKeys, contentType, httpDestination, serviceURI, true);
	}

	/**
	 * Gets a resource / media file using HTTP-Get
	 *
	 * @param entitySetName
	 *           name of the entity set to read
	 * @param entityKeys
	 *           key of the entity to read
	 * @param contentType
	 *           HTTP-Mime type of the requested data
	 * @param httpDestination
	 *           destination to be used The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @param retry
	 *           flag indicating whether a second try to get the media file should be done
	 * @return result containing the requested resource / media file, any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of a communication failure
	 */
	protected HttpODataResult getMediaFileIntern(final String entitySetName, final Map<String, Object> entityKeys,
			final String contentType, final HTTPDestination httpDestination, final String serviceURI, final boolean retry)
			throws HttpODataException
	{
		HttpODataResult httpODataResult = null;
		final Map<String, Object> destination = getInitializedDestination(httpDestination, serviceURI);
		final Edm edm = getEDM(destination, serviceURI);

		EdmEntityContainer entityContainer;
		try
		{
			entityContainer = edm.getDefaultEntityContainer();
			final URI rootUri = getFullQualifiedServiceURI(SLASH, httpDestination, serviceURI);

			final ODataResponse response = entityProviderWriteEntryForGetMediaFile(entitySetName, entityKeys, entityContainer,
					rootUri);

			// prepare connection
			final String fullQualifiedUri = response.getHeader(LOCATION) + "/$value";
			final ConnectionWithTimeStamp conWithStamp = initializeConnection(new URI(fullQualifiedUri), contentType, HttpMethod.GET,
					destination, httpDestination, serviceURI);

			httpODataResult = executeAndGetMediaFile(conWithStamp, edm, httpDestination.getHttpDestinationName());
		}
		catch (final HttpODataException ex)
		{
			if (checkSupportedHttpErrorCodes(ex) && retry)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Timeout detected. Retry to execute reedFeed again.");
				}
				this.httpODataCommonStorage.removeDestination(httpDestination.getHttpDestinationName());
				httpODataResult = getMediaFileIntern(entitySetName, entityKeys, contentType, httpDestination, serviceURI, false);
			}
			else
			{
				throw ex;
			}
		}
		catch (final EdmException | URISyntaxException | EntityProviderException | IOException ex)
		{
			throw new HttpODataException("Getting entity via OData Service '" + serviceURI + FAILED_DUE_TO_LABEL + ex.getMessage(),
					ex);
		}

		return httpODataResult;
	}

	/**
	 * Executes the get and handles the oData response and builds a HttpODataResult.
	 *
	 * @param conWithStamp
	 *           ConnectionWithTimeStamp the connection
	 * @param edm
	 *           Edm Entity Data Model metadata
	 * @param httpDestinationName
	 *           the http destination name
	 * @return the result of the oData call
	 * @throws IOException
	 *            in case of Input Output failure
	 * @throws HttpODataException
	 *            in case of oData failure
	 */

	protected HttpODataResult executeAndGetMediaFile(final ConnectionWithTimeStamp conWithStamp, final Edm edm,
			final String httpDestinationName) throws IOException, HttpODataException
	{
		// Now we require information from the established connection, which triggers the real oData read
		checkStatus(conWithStamp.connection, httpDestinationName);

		traceCallTime(conWithStamp);

		HttpODataResultManipulator httpODataResultManipulator = null;
		InputStream httpInputStream = null;

		try
		{
			httpODataResultManipulator = prepareResult(conWithStamp.connection, edm);

			httpInputStream = conWithStamp.connection.getInputStream();
			final byte[] content = IOUtils.toByteArray(httpInputStream);

			httpODataResultManipulator.setMediaContent(content);
		}
		finally
		{
			IOUtils.closeQuietly(httpInputStream);
		}
		return (HttpODataResult) httpODataResultManipulator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataClient#putEntity(java.lang.String, java.util.Map,
	 * de.hybris.platform.sap.core.configuration.http.HTTPDestination, java.lang.String)
	 */
	@Override
	public HttpODataResult putEntity(final String entitySetName, final Map<String, Object> entityData,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
	{
		return putEntityIntern(entitySetName, entityData, httpDestination, serviceURI, true);
	}

	/**
	 * Writes an entity using HTTP-Put.
	 *
	 * @param entitySetName
	 *           entity set name of the entity to write
	 * @param entityData
	 *           entity data to be written; must be provided as name-value pairs.
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @param retry
	 *           flag indicating whether a second try to put the entity should be done
	 * @return result of the call, contains any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of OData failure
	 */
	public HttpODataResult putEntityIntern(final String entitySetName, final Map<String, Object> entityData,
			final HTTPDestination httpDestination, final String serviceURI, final boolean retry) throws HttpODataException
	{
		HttpODataResult httpODataResult = null;
		final Map<String, Object> destination = getInitializedDestination(httpDestination, serviceURI);
		final Edm edm = getEDM(destination, serviceURI);

		try
		{
			final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();

			final URI rootUri = getFullQualifiedServiceURI(SLASH, httpDestination, serviceURI);

			final ODataResponse response = entityProviderWriteEntryForPutEntity(entitySetName, entityData, entityContainer, rootUri);

			// prepare connection
			final String fullQualifiedUri = response.getHeader(LOCATION);
			final ConnectionWithTimeStamp conWithStamp = initializeConnection(new URI(fullQualifiedUri),
					CONTENT_TYPE_APPLICATION_JSON, HttpMethod.PUT, destination, httpDestination, serviceURI);

			final OutputStream httpOutputStream = conWithStamp.connection.getOutputStream();
			try
			{
				// send the the actual HTTP PUT
				InputStream entityInputStream = (InputStream) response.getEntity();
				if (log.isDebugEnabled())
				{
					entityInputStream = logInputStream("sending data to service '" + serviceURI + "'", entityInputStream);
				}
				IOUtils.copy(entityInputStream, httpOutputStream);
				httpOutputStream.flush();
			}
			finally
			{
				IOUtils.closeQuietly(httpOutputStream);
			}
			httpODataResult = executePutEntityAndHandleResponse(conWithStamp, edm, httpDestination.getHttpDestinationName());
		}
		catch (final HttpODataException ex)
		{
			if (checkSupportedHttpErrorCodes(ex) && retry)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Timeout detected. Retry to execute metadata and putEntity.");
				}
				this.httpODataCommonStorage.removeDestination(httpDestination.getHttpDestinationName());
				httpODataResult = putEntityIntern(entitySetName, entityData, httpDestination, serviceURI, false);
			}
			else
			{
				throw ex;
			}
		}
		catch (final EdmException | URISyntaxException | EntityProviderException | IOException ex)
		{
			throw new HttpODataException("Putting entity via OData Service '" + serviceURI + FAILED_DUE_TO_LABEL + ex.getMessage(),
					ex);
		}

		return httpODataResult;
	}

	/**
	 * Executes the put and handles the oData response and builds a HttpODataResult.
	 *
	 * @param conWithStamp
	 *           ConnectionWithTimeStamp the connection
	 * @param edm
	 *           Edm Entity Data Model metadata
	 * @param httpDestinationName
	 *           the http destination name
	 * @return the result of the oData call
	 * @throws IOException
	 *            in case of Input Output failure
	 * @throws HttpODataException
	 *            in case of oData failure
	 */
	protected HttpODataResult executePutEntityAndHandleResponse(final ConnectionWithTimeStamp conWithStamp, final Edm edm,
			final String httpDestinationName) throws IOException, HttpODataException
	{
		// Now we require information from the established connection, which triggers the real oData read
		checkStatus(conWithStamp.connection, httpDestinationName);

		traceCallTime(conWithStamp);

		HttpODataResultManipulator httpODataResultManipulator = null;

		try
		{
			httpODataResultManipulator = prepareResult(conWithStamp.connection, edm);
		}
		finally
		{
			IOUtils.closeQuietly(conWithStamp.connection.getInputStream());
		}
		return (HttpODataResult) httpODataResultManipulator;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataClient#executeBatchCall(java.util.List,
	 * de.hybris.platform.sap.core.configuration.http.HTTPDestination,java.lang.String)
	 */
	@Override
	public Map<String, HttpODataResult> executeBatchCall(final List<DataContainerBatchPart> dcBatchParts,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
	{
		return this.executeBatchCallIntern(dcBatchParts, httpDestination, serviceURI, true);
	}

	/**
	 * Executes a batch call to a Service at a HttpDestination, composed of one or several batchs for calling this
	 * service parts.
	 *
	 * @param dcBatchParts
	 *           List of data container batch parts.
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @param retry
	 *           flag indicating whether a second try to execute the batch should be done
	 * @return a Map of HttpODataResult objects, one for every Batch parts.
	 * @throws HttpODataException
	 *            in case of OData failure
	 */
	protected Map<String, HttpODataResult> executeBatchCallIntern(final List<DataContainerBatchPart> dcBatchParts,
			final HTTPDestination httpDestination, final String serviceURI, final boolean retry) throws HttpODataException
	{
		Map<String, HttpODataResult> httpODataResultMap = null;
		final Map<String, Object> destination = getInitializedDestination(httpDestination, serviceURI);

		try
		{
			final Edm entityDataModel = getEDM(destination, serviceURI);
			final List<BatchPart> batchPartList = new ArrayList<>();

			final URI rootBatchUri = getFullQualifiedServiceURI(SLASH + BATCH, httpDestination, serviceURI);

			if (log.isDebugEnabled())
			{
				log.debug("rootBatchUri: " + rootBatchUri.toString());
			}

			// Prepare the Payload
			for (final DataContainerBatchPart dcBatchPart : dcBatchParts)
			{
				batchPartList.add(dcBatchPart.getBatchPart());
			}

			// Prepare connection
			final ConnectionWithTimeStamp conWithStamp = initializeConnection(rootBatchUri, CONTENT_TYPE_BATCH, HttpMethod.POST,
					destination, httpDestination, serviceURI);

			final OutputStream httpOutputStream = conWithStamp.connection.getOutputStream();
			try
			{
				final InputStream payload = EntityProvider.writeBatchRequest(batchPartList, BOUNDARY);
				IOUtils.copy(payload, httpOutputStream);
				httpOutputStream.flush();
			}
			finally
			{
				IOUtils.closeQuietly(httpOutputStream);
			}

			// Checking the response
			httpODataResultMap = executeAndHandleResponse(dcBatchParts, conWithStamp, entityDataModel,
					httpDestination.getHttpDestinationName());
		}
		catch (final EdmException | URISyntaxException | IOException ex)
		{
			log.error("EdmException, URISyntaxException or IOException thrown. Batch preparation via OData Service '" + serviceURI
					+ "' failed");
			throw new HttpODataException(
					"Batch preparation via OData Service '" + serviceURI + FAILED_DUE_TO_LABEL + ex.getMessage(), ex);
		}
		catch (final HttpODataException ex)
		{
			if (checkSupportedHttpErrorCodes(ex) && retry)
			{
				if (log.isDebugEnabled())
				{
					log.debug("Timeout detected. Retry to execute metadata and batch call.");
				}
				this.httpODataCommonStorage.removeDestination(httpDestination.getHttpDestinationName());
				httpODataResultMap = this.executeBatchCallIntern(dcBatchParts, httpDestination, serviceURI, false);
			}
			else
			{
				throw ex;
			}
		}
		return httpODataResultMap;
	}

	/**
	 * Retrieves a fully initialized destination. If the destination exists already in the synchronized cache and
	 * contains the EDM (Entity Data Model) for the current service, the cache entry is returned. Otherwise a new
	 * destination is created (in case the destination is not available in cache) resp. extended (in case the EDM is not
	 * available for the current service). Every parts (Cookies, X-CSRF-Token and EDM) are retrieved/copied and only then
	 * put into the cache, for thread safe reasons.
	 *
	 * @param httpDestination
	 *           HTTPDestination representing the backend system to connect to
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return Map gathering the Cookies as subsequent Map, the X-CSRF-Token as String, and the Entity Data Model as
	 *         subsequent Map.
	 * @throws HttpODataException
	 *            in case of connection initialization failure
	 */
	protected Map<String, Object> getInitializedDestination(final HTTPDestination httpDestination, final String serviceURI)
			throws HttpODataException
	{
		Map<String, Object> oldDestination;
		Map<String, Object> newDestination;
		Edm edm;
		ConnectionWithTimeStamp conWithStamp;
		final String destinationName = httpDestination.getHttpDestinationName();

		oldDestination = httpODataCommonStorage.getDestination(destinationName);

		// if destination exists and contains the current service we return this cached old destination
		if (oldDestination != null && oldDestination.containsKey(serviceURI))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Destination found with key: " + serviceURI);
				log.debug("Destination values: " + VALUE_NOT_TRACED_LABEL
						+ " (for security reasons as they contain X-CSRF-Token and cookies values)");
			}
			return oldDestination;
		}
		// the existing destination is not good enough (does not exist or does not contain the current service), we create a new destination/service
		try
		{
			conWithStamp = initializeConnectionIntern(getFullQualifiedServiceURI(METADATA, httpDestination, serviceURI),
					ContentType.APPLICATION_XML.toContentTypeString(), HttpMethod.GET, oldDestination, httpDestination, serviceURI);
		}
		catch (IOException | URISyntaxException e)
		{
			throw new HttpODataException("Connection initialization failed, due to " + e.getMessage(), e);
		}

		// read metadata via oData call
		edm = readMetadata(conWithStamp, destinationName, serviceURI);

		if (oldDestination == null)
		{
			// create new destination and fill it: cookies, X-CSRF token, EDMs
			newDestination = createDestinationFromParams(serviceURI, edm, conWithStamp.connection);
		}
		else
		{
			// create new service in destination
			newDestination = createNewServiceInDestinationFromParams(serviceURI, edm, oldDestination, conWithStamp.connection);
		}

		// add new destination instance to cache (thread safe)
		httpODataCommonStorage.putDestination(destinationName, newDestination);
		if (log.isDebugEnabled())
		{
			log.debug("New Destination inserted with key " + destinationName);
			log.debug("Destination values: " + VALUE_NOT_TRACED_LABEL
					+ " for security reasons as they contain X-CSRF-Token and cookies values");
		}
		return newDestination;
	}



	/**
	 * Constructs a destination object, based on a service object with a valid X-CSRF-Token, a Map of Cookies and the
	 * service Entity Data Model. The new destination is put into the synchronized storage Map.
	 *
	 * @param serviceURI
	 *           serviceURI of the backend service to be called
	 * @param edm
	 *           Edm as Entity Data Model (metadata of a service)
	 * @param connection
	 *           HttpUrlConnection the URL of the backend service
	 * @return Map gathering the Cookies as subsequent Map, the X-CSRF-Token as String, and the Entity Data Model as
	 *         subsequent MapMap.
	 */
	protected Map<String, Object> createDestinationFromParams(final String serviceURI, final Edm edm,
			final HttpURLConnection connection)
	{
		final Map<String, Object> destination = new HashMap<>();
		final Map<String, Object> service = new HashMap<>();
		final Map<String, String> cookies = new HashMap<>();
		String xCsrfToken;

		// X-Csrf-Token
		xCsrfToken = connection.getHeaderField(HTTP_HEADER_X_CSRF_TOKEN);
		service.put(STORAGE_SERVICE_X_CSRF_TOKEN, xCsrfToken);

		// cookies
		final List<String> headerCookies = connection.getHeaderFields().get(HTTP_HEADER_SET_COOKIE);
		if (headerCookies != null)
		{
			for (final String headerCookie : headerCookies)
			{
				final String cookieNameValue = headerCookie.substring(0, headerCookie.indexOf(';'));
				final String cookieName = cookieNameValue.substring(0, headerCookie.indexOf(EQUALS_SIGN));
				final String cookieValue = cookieNameValue.substring(headerCookie.indexOf(EQUALS_SIGN) + 1, cookieNameValue.length());
				cookies.put(cookieName, cookieValue);
				if (log.isDebugEnabled())
				{
					log.debug("Extracted cookie: " + cookieName + EQUALS_SIGN + VALUE_NOT_TRACED_LABEL
							+ " for security reasons (length = " + cookieValue.length() + " characters)");
				}
			}
		}
		service.put(STORAGE_SERVICE_COOKIES, cookies);

		// edm
		service.put(STORAGE_SERVICE_METADATA, edm);

		destination.put(serviceURI, service);
		return destination;
	}

	/**
	 * Creates new destination and fill it with parts of old destination (cookies, X-CSRF-Token, EDM) and add a new
	 * service Map with dedicated cookies, X-CSRF-Token and the new edm (thread safe).
	 *
	 * @param newServiceURI
	 *           String representing the new service URI
	 * @param newEdm
	 *           Entity Data Model for the new service name
	 * @param oldDestination
	 *           Map representing an existing destination, not containing the EDM for the new service name
	 * @param connection
	 *           HttpUrlConnection the URL of the backend service
	 * @return Map representing the new destination.
	 */
	protected Map<String, Object> createNewServiceInDestinationFromParams(final String newServiceURI, final Edm newEdm,
			final Map<String, Object> oldDestination, final HttpURLConnection connection)
	{
		final Map<String, Object> newDestination = new HashMap<>();
		Map<String, Object> oldService;
		Map<String, Object> newService;

		// For all services under this oldDestination - hard copy
		for (final Map.Entry<String, Object> services : oldDestination.entrySet())
		{
			oldService = (Map<String, Object>) services.getValue();
			newService = new HashMap<>();

			// Copy Cookies object for thread safe reasons. No reference
			final Map<String, String> newCookies = new HashMap<>();
			final Map<String, String> oldCookies = (Map<String, String>) oldService.get(STORAGE_SERVICE_COOKIES);
			for (final Map.Entry<String, String> serviceCookies : oldCookies.entrySet())
			{
				newCookies.put(serviceCookies.getKey(), serviceCookies.getValue());
			}
			newService.put(STORAGE_SERVICE_COOKIES, newCookies);

			// x-csrf-token
			newService.put(STORAGE_SERVICE_X_CSRF_TOKEN, oldService.get(STORAGE_SERVICE_X_CSRF_TOKEN));

			// edm
			newService.put(STORAGE_SERVICE_METADATA, oldService.get(STORAGE_SERVICE_METADATA));


			newDestination.put(services.getKey(), newService);
		}


		// cookies
		final Map<String, String> cookies = new HashMap<>();
		final List<String> headerCookies = connection.getHeaderFields().get(HTTP_HEADER_SET_COOKIE);
		if (headerCookies != null)
		{
			for (final String headerCookie : headerCookies)
			{
				final String cookieNameValue = headerCookie.substring(0, headerCookie.indexOf(';'));
				final String cookieName = cookieNameValue.substring(0, headerCookie.indexOf(EQUALS_SIGN));
				final String cookieValue = cookieNameValue.substring(headerCookie.indexOf(EQUALS_SIGN) + 1, cookieNameValue.length());
				cookies.put(cookieName, cookieValue);
				if (log.isDebugEnabled())
				{
					log.debug("Extracted cookie: " + cookieName + EQUALS_SIGN + VALUE_NOT_TRACED_LABEL
							+ " for security reasons (length = " + cookieValue.length() + " characters)");
				}
			}
		}
		newService = new HashMap<>();
		newService.put(STORAGE_SERVICE_COOKIES, cookies);

		// x-csrf-token
		final String xCsrfToken = connection.getHeaderField(HTTP_HEADER_X_CSRF_TOKEN);
		newService.put(STORAGE_SERVICE_X_CSRF_TOKEN, xCsrfToken);

		// edm
		newService.put(STORAGE_SERVICE_METADATA, newEdm);

		// Add the new Service, for the new Destination
		newDestination.put(newServiceURI, newService);

		return newDestination;
	}

	/**
	 * Retrieves the Entity Data Model (metadata) for a given destination and a given service name.
	 *
	 * @param destination
	 *           Map containing among other another Map of service and its corresponding metadata EDM object
	 * @param serviceURI
	 *           String for the name of the service URI
	 * @return Edm Entity Data Model object representing the metadata of the service if found in the Map, null otherwise.
	 * @throws HttpODataException
	 *            in case the service map or the Edm could not be found in the storage
	 */
	protected Edm getEDM(final Map<String, Object> destination, final String serviceURI) throws HttpODataException
	{

		final Map<String, Object> service = (Map<String, Object>) destination.get(serviceURI);
		if (service == null)
		{
			throw new HttpODataException("Service Map could not be found in the Storage Map");
		}
		return (Edm) service.get(STORAGE_SERVICE_METADATA);
	}

	/**
	 * Prepares the connection for a given HTTP destination. Several request parameters are set. If the default user is
	 * not yet logged on into the backend system, a basic authentication happens with the encoded HTTP Destination
	 * credentials and a valid X-CSRF-Token is fetched. The data are stored in cache. If the user is already logged on,
	 * the cookies and the valid X-CSRF-Token are retrieved from the cache and pass as request parameters. We
	 * differentiates between GET request and POST/PUT request.
	 *
	 * @param contentType
	 *           String indicating in which format the inputs are provided, mainly JSON.
	 * @param httpMethod
	 *           HttpMethod like GET or POST
	 * @param absoluteUri
	 *           URI targeting the backend system
	 * @param destination
	 *           Map containing all necessary information like cookies, X-CSRF-Token and Edm data
	 * @param httpDestination
	 *           HTTPDestination representing the target backend system
	 * @param fetchToken
	 *           boolean which force the fetch of a X-CSRF-Token in the request header
	 * @param serviceName
	 *           the name of the service as String
	 * @return HttpURLConnection object with the necessary request parameters set
	 * @throws IOException
	 *            in case of malformed exceptions, connection problems, ...
	 */
	protected HttpURLConnection prepareConnection(final String contentType, final HttpMethod httpMethod, final URI absoluteUri,
			final Map<String, Object> destination, final HTTPDestination httpDestination, final boolean fetchToken,
			final String serviceName) throws IOException
	{
		final HttpURLConnection connection = openUrlConnection(absoluteUri);

		connection.setRequestMethod(httpMethod.name());
		connection.setRequestProperty(HttpHeaders.ACCEPT, contentType);
		connection.setRequestProperty(HttpHeaders.ACCEPT_LANGUAGE, getCurrentLanguage());

		if (log.isDebugEnabled())
		{
			log.debug("Connection details:");
			log.debug("  connection.setRequestMethod:" + httpMethod.name());
			log.debug("  connection.setRequestProperty - HttpHeaders : " + HttpHeaders.ACCEPT + ", contentType : " + contentType);
			log.debug("  connection.setRequestProperty - HttpHeaders : " + HttpHeaders.ACCEPT_LANGUAGE + ", getCurrentLanguage() : "
					+ getCurrentLanguage());
		}

		if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod))
		{
			connection.setDoOutput(true);
			connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType + "; charset=" + Charset.defaultCharset());
		}

		// Logon required OR Token not already available
		if (destination == null || fetchToken)
		{
			connection.setRequestProperty(HttpHeaders.AUTHORIZATION, AUTHORISATION_BASIC + Base64.encodeBase64String(
					(httpDestination.getUserid() + ":" + httpDestination.getPassword()).getBytes(Charset.defaultCharset())));

			connection.setRequestProperty(HTTP_HEADER_X_CSRF_TOKEN, X_CSRF_TOKEN_FETCH);
			if (log.isDebugEnabled())
			{
				log.debug("  Logon required OR Token not already available");
				log.debug("  fetchToken:" + fetchToken);
				log.debug("  connection.setRequestProperty - " + HTTP_HEADER_X_CSRF_TOKEN + " : " + X_CSRF_TOKEN_FETCH);
			}
			return connection;
		}

		// Logon not required AND no fetchToken. Reuse cookies and Token
		final Map<String, Object> serviceMap = (Map<String, Object>) destination.get(serviceName);
		if (serviceMap != null)
		{
			setCookies(connection, serviceMap);

			final String xCsrfToken = (String) serviceMap.get(STORAGE_SERVICE_X_CSRF_TOKEN);
			if (xCsrfToken != null) // to support services without token
			{
				connection.setRequestProperty(HTTP_HEADER_X_CSRF_TOKEN, xCsrfToken);
			}
		}
		return connection;
	}


	/**
	 * Open the connection for a given URI.
	 *
	 * @param absoluteUri
	 *           URI targeting the backend system
	 * @return HttpURLConnection object with the opened connection
	 * @throws IOException
	 *            in case of malformed exceptions, connection problems, ...
	 */
	protected HttpURLConnection openUrlConnection(final URI absoluteUri) throws IOException
	{
		final URL absoluteUrl = absoluteUri.toURL();
		if (log.isDebugEnabled())
		{
			log.debug("absoluteUrl: " + absoluteUrl);
		}
		return (HttpURLConnection) absoluteUrl.openConnection();
	}

	/**
	 * Fills the header cookie parameter.
	 *
	 * @param connection
	 * @param serviceMap
	 */
	protected void setCookies(final HttpURLConnection connection, final Map<String, Object> serviceMap)
	{
		final Map<String, String> cookiesMap = (Map<String, String>) serviceMap.get(STORAGE_SERVICE_COOKIES);
		if (cookiesMap != null)
		{
			final StringBuilder builder = new StringBuilder();
			final Iterator<Entry<String, String>> cookieItr = cookiesMap.entrySet().iterator();
			while (cookieItr.hasNext())
			{
				final Entry<String, String> cookie = cookieItr.next();
				builder.append(cookie.getKey());
				builder.append(EQUALS_SIGN);
				builder.append(cookie.getValue());
				if (cookieItr.hasNext())
				{
					builder.append("; ");
				}
				if (log.isDebugEnabled())
				{
					log.debug("cookie.getKey():" + cookie.getKey());
					log.debug("cookie.getValue():" + VALUE_NOT_TRACED_LABEL);
				}

			}
			connection.setRequestProperty(HTTP_HEADER_COOKIE, builder.toString());
		}
	}

	/**
	 * Reads the metadata for a given connection, service and destination.
	 *
	 * @param conWithStamp
	 *           ConnectionWithTimeStamp connection object
	 * @param destinationName
	 *           String for the name of the destination
	 * @param serviceURI
	 *           the service URI
	 * @return Edm Entity Data Model
	 * @throws HttpODataException
	 *            in case of IOException or EntityProviderException
	 */
	protected Edm readMetadata(final ConnectionWithTimeStamp conWithStamp, final String destinationName, final String serviceURI)
			throws HttpODataException
	{
		Edm edm = null;
		try
		{
			InputStream httpInputStream = null;
			try
			{
				if (log.isDebugEnabled())
				{
					log.debug("destinationName: " + destinationName);
					log.debug("serviceURI: " + serviceURI);
				}
				// Now we require information from the established connection, which triggers the real oData read
				checkStatus(conWithStamp.connection, destinationName);

				traceCallTime(conWithStamp);
				httpInputStream = conWithStamp.connection.getInputStream();
				if (log.isDebugEnabled())
				{
					httpInputStream = logInputStream("Received meta data for service '" + serviceURI + "'", httpInputStream);
				}
				edm = entityProviderReadMetadata(httpInputStream);
			}
			finally
			{
				IOUtils.closeQuietly(httpInputStream);
			}
		}
		catch (IOException | EntityProviderException ex)
		{
			throw new HttpODataException("Getting meta data for destination " + destinationName + " and OData Service '" + serviceURI
					+ FAILED_DUE_TO_LABEL + ex.getMessage(), ex);
		}
		return edm;
	}

	/**
	 * Executes and handles the oData batch response and builds a Map of HttpODataResult for each batch part of the batch
	 * response.
	 *
	 * @param dcBatchParts
	 *           List of data container batch parts
	 * @param conWithStamp
	 *           ConnectionWithTimeStamp the connection
	 * @param edm
	 *           Edm Entity Data Model metadata
	 * @param httpDestinationName
	 *           the http destination name
	 * @return httpODataResultMap a Map with the originating data container name as key and the HttpODataResult for each
	 *         batch part response as value
	 * @throws IOException
	 *            in case of Input Output failure
	 * @throws HttpODataException
	 *            in case of oData failure
	 * @throws EdmException
	 *            in case of Entity Data Model failure
	 */
	protected Map<String, HttpODataResult> executeAndHandleResponse(final List<DataContainerBatchPart> dcBatchParts,
			final ConnectionWithTimeStamp conWithStamp, final Edm edm, final String httpDestinationName)
			throws IOException, HttpODataException, EdmException
	{
		// Now we require information from the established connection, which triggers the real oData read
		checkStatus(conWithStamp.connection, httpDestinationName);

		traceCallTime(conWithStamp);
		final Map<String, HttpODataResult> httpODataResultMap = new HashMap<>();
		HttpODataResultManipulator httpODataResultManipulator;
		InputStream contentResult = null;
		DataContainerBatchPart dcBatchPart = null;

		try
		{
			httpODataResultManipulator = prepareResult(conWithStamp.connection, edm);
			contentResult = conWithStamp.connection.getInputStream();
			final String httpResponseContentType = conWithStamp.connection.getContentType();
			final List<BatchSingleResponse> batchSingleResponses = getBatchSingleResponses(contentResult, httpResponseContentType);
			final Iterator dcBatchPartsIterator = dcBatchParts.iterator();

			for (final BatchSingleResponse response : batchSingleResponses)
			{
				// We expect the same order between dcBatchParts input and the single responses inside the oData batch response, also the same number of entries
				if (log.isDebugEnabled())
				{
					log.debug("batchSingleResponses.getHeaders(): " + response.getHeaders());
				}
				dcBatchPart = (DataContainerBatchPart) dcBatchPartsIterator.next();
				final String statusCode = response.getStatusCode();
				httpODataResultManipulator = createHttpODataResultInstance();

				if (log.isDebugEnabled())
				{
					log.debug(STATUS_CODE_LABEL + statusCode);
				}
				if ("200".equalsIgnoreCase(statusCode))
				{
					getResultFromResponse(edm, httpODataResultManipulator, dcBatchPart, response);
				}
				else
				{
					httpODataResultManipulator.setStatusCode(StatusCode.ERROR);
				}
				httpODataResultMap.put(dcBatchPart.getDataContainerName(), (HttpODataResult) httpODataResultManipulator);
				if (log.isDebugEnabled())
				{
					log.debug(dcBatchPart.getDataContainerName() + " inserted into httpODataResultMap");
				}
			}
		}
		catch (final BatchException | EntityProviderException | NoSuchElementException e)
		{
			log.error("BatchException, EntityProviderException or NoSuchElementException thrown.");
			throw new HttpODataException("Batch or EntityProvider Exception thrown, due to " + e.getMessage(), e);
		}
		finally
		{
			IOUtils.closeQuietly(contentResult);
		}
		if (log.isDebugEnabled())
		{
			log.debug("httpODataResultMap: " + httpODataResultMap.toString());
		}
		return httpODataResultMap;
	}

	/**
	 * Validates the response and fills the result object.
	 *
	 * @param edm
	 * @param httpODataResultManipulator
	 * @param dcBatchPart
	 * @param response
	 * @throws EdmException
	 * @throws EntityProviderException
	 */
	protected void getResultFromResponse(final Edm edm, final HttpODataResultManipulator httpODataResultManipulator,
			final DataContainerBatchPart dcBatchPart, final BatchSingleResponse response)
			throws EdmException, EntityProviderException
	{
		FeedMetadata feedMetaData;
		ODataFeed oDataFeed;
		InputStream responseAsInputStream;
		String headerContentType;
		headerContentType = response.getHeader(HttpHeaders.CONTENT_TYPE);
		httpODataResultManipulator.setResponseBody(response.getBody());

		responseAsInputStream = new ByteArrayInputStream(response.getBody().getBytes());
		final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		oDataFeed = getODataFeed(entityContainer, responseAsInputStream, headerContentType, dcBatchPart.getResultName());

		if (log.isDebugEnabled())
		{
			log.debug("response.getHeader(HttpHeaders.CONTENT_TYPE): " + headerContentType);
			log.debug("response.getBody(): " + VALUE_NOT_TRACED_LABEL + " (length : " + response.getBody().length() + ")");
			log.debug("dcBatchPart.getResultName() : " + dcBatchPart.getResultName());

		}

		if (oDataFeed != null)
		{
			httpODataResultManipulator.setEntities(oDataFeed.getEntries());
			feedMetaData = oDataFeed.getFeedMetadata();
			if (feedMetaData != null)
			{
				httpODataResultManipulator.setCount(feedMetaData.getInlineCount());
			}
			httpODataResultManipulator.setStatusCode(StatusCode.OK);
			if (log.isDebugEnabled())
			{
				if (feedMetaData != null)
				{
					log.debug("feedMetaData.getInlineCount(): " + feedMetaData.getInlineCount());
				}
				log.debug(STATUS_CODE_LABEL + StatusCode.OK);
				log.debug("oDataFeed.getEntries(): " + VALUE_NOT_TRACED_LABEL + " (length : " + oDataFeed.getEntries().size() + ")");

			}
		}
		else
		{
			httpODataResultManipulator.setStatusCode(StatusCode.ERROR);
			if (log.isDebugEnabled())
			{
				log.debug(STATUS_CODE_LABEL + StatusCode.ERROR);
			}
		}
	}

	/**
	 * Checks if we run into supported HTTP error codes.
	 *
	 * @param oEx
	 *           HttpODataException Exception containing the Http response code
	 *
	 * @return true if a supported error code is detected, false otherwise.
	 */
	protected boolean checkSupportedHttpErrorCodes(final HttpODataException oEx)
	{
		final int httpResponseCode = oEx.getHttpResponseCode();
		return 403 == httpResponseCode || 401 == httpResponseCode;
	}

	/**
	 * Prepares the returning object of an oData call.
	 *
	 * @param connection
	 *           HttpURLConnection the connection
	 * @param edm
	 *           Edm the entity data model metadata
	 * @return HttpODataResult as response of an oData call, initialized with metadata, http header files, http status
	 *         code and backend message (sap-message) in case these objects can be found
	 * @throws IOException
	 *            in case getting response code fails
	 */
	protected HttpODataResultManipulator prepareResult(final HttpURLConnection connection, final Edm edm) throws IOException
	{
		final HttpODataResultManipulator result = createHttpODataResultInstance();

		result.setMetaData(edm);
		result.setHttpHeaders(connection.getHeaderFields());
		result.setHttpStatusCode(HttpStatusCodes.fromStatusCode(connection.getResponseCode()));
		final List<BackendMessage> backendMessages = parseBackendMessages(connection);
		result.setMessages(backendMessages);
		logBackendMessages(backendMessages);
		if (log.isDebugEnabled())
		{
			log.debug("Result:");
			log.debug("  HttpHeaders: " + connection.getHeaderFields());
			log.debug("  HttpStatusCode: " + HttpStatusCodes.fromStatusCode(connection.getResponseCode()));
		}

		return result;
	}

	/**
	 * Retrieves the oData feed from the batch call. This extracted method call is intended to be overwritten for Unit
	 * Test purposes.
	 *
	 * @param entityContainer
	 *           The Entity Container
	 * @param responseAsInputStream
	 *           The Response as Input Stream
	 * @param headerContentType
	 *           the header content type
	 * @param entityName
	 *           the service entity name
	 * @return ODataFeed feed from the backend service
	 * @throws EntityProviderException
	 *            in case of entity provider failure
	 * @throws EdmException
	 *            in case of entity data model failure
	 */
	protected ODataFeed getODataFeed(final EdmEntityContainer entityContainer, final InputStream responseAsInputStream,
			final String headerContentType, final String entityName) throws EntityProviderException, EdmException
	{
		return EntityProvider.readFeed(headerContentType, entityContainer.getEntitySet(entityName), responseAsInputStream,
				EntityProviderReadProperties.init().build());
	}

	/**
	 * Retrieves an Entity Data Model object for a given InputStream. This extracted method call is intended to be
	 * overwritten for Unit Test purposes.
	 *
	 * @param httpInputStream
	 *           InputStream containing the metadata to be extracted
	 * @return Edm Entity Data Model object as metadata
	 * @throws EntityProviderException
	 *            in case of read failure
	 */
	protected Edm entityProviderReadMetadata(final InputStream httpInputStream) throws EntityProviderException
	{
		return EntityProvider.readMetadata(httpInputStream, false);
	}

	/**
	 * Retrieves a list of BatchSingleResponse for the given batch response. This extracted method call is intended to be
	 * overwritten for Unit Test purposes.
	 *
	 * @param contentResult
	 *           the content result of the batch call as input stream
	 * @param httpResponseContentType
	 *           the http response content type
	 * @return a list of BatchSingleResponse for the given batch response
	 * @throws BatchException
	 *            in case of batch failure
	 */
	protected List<BatchSingleResponse> getBatchSingleResponses(final InputStream contentResult,
			final String httpResponseContentType) throws BatchException
	{
		return EntityProvider.parseBatchResponse(contentResult, httpResponseContentType);
	}

	/**
	 * Serializes data into ODataResponse object. This extracted method call is intended to be overwritten for Unit Test
	 * purposes.
	 *
	 * @param entitySetName
	 *           Entity Set name
	 * @param entityData
	 *           Entity Data
	 * @param entityContainer
	 *           The Entity Container
	 * @param rootUri
	 *           root URI
	 * @return ODataResponse from the backend service
	 * @throws EntityProviderException
	 *            in case of entity provider failure
	 * @throws EdmException
	 *            in case of entity data model failure
	 */
	protected ODataResponse entityProviderWriteEntryForPutEntity(final String entitySetName, final Map<String, Object> entityData,
			final EdmEntityContainer entityContainer, final URI rootUri) throws EntityProviderException, EdmException
	{
		return EntityProvider.writeEntry(CONTENT_TYPE_APPLICATION_JSON, entityContainer.getEntitySet(entitySetName), entityData,
				EntityProviderWriteProperties.serviceRoot(rootUri).build());
	}

	/**
	 * Serializes data into ODataResponse object. This extracted method call is intended to be overwritten for Unit Test
	 * purposes.
	 *
	 * @param entitySetName
	 *           Entity Set name
	 * @param entityKeys
	 *           Entity Keys
	 * @param entityContainer
	 *           the Entity Container
	 * @param rootUri
	 *           root URI
	 * @return ODataResponse from the backend service
	 * @throws EntityProviderException
	 *            in case of entity provider failure
	 * @throws EdmException
	 *            in case of entity data model failure
	 */
	protected ODataResponse entityProviderWriteEntryForGetMediaFile(final String entitySetName,
			final Map<String, Object> entityKeys, final EdmEntityContainer entityContainer, final URI rootUri)
			throws EntityProviderException, EdmException
	{
		return EntityProvider.writeEntry(CONTENT_TYPE_APPLICATION_JSON, entityContainer.getEntitySet(entitySetName), entityKeys,
				EntityProviderWriteProperties.serviceRoot(rootUri).build());
	}


	/**
	 * Serializes data into ODataFeed object. This extracted method call is intended to be overwritten for Unit Test
	 * purposes.
	 *
	 * @param entitySetName
	 *           The Entity Set Name
	 * @param entityContainer
	 *           The Entity Container
	 * @param httpInputStream
	 *           the Input Stream
	 * @return ODataFeed from the backend service
	 * @throws EntityProviderException
	 *            in case of entity provider failure
	 * @throws EdmException
	 *            in case of entity data model failure
	 */
	protected ODataFeed entityProviderReadFeed(final String entitySetName, final EdmEntityContainer entityContainer,
			final InputStream httpInputStream) throws EntityProviderException, EdmException
	{
		return EntityProvider.readFeed(CONTENT_TYPE_APPLICATION_XML, entityContainer.getEntitySet(entitySetName), httpInputStream,
				EntityProviderReadProperties.init().build());
	}

	/**
	 * Returns the Application Context from the Registry. THis method is intended to be overwritten for Unit Tests
	 * purposes.
	 *
	 * @return ApplicationContext the Application Context instance.
	 */
	protected ApplicationContext getRegistryApplicationContext()
	{
		return Registry.getApplicationContext();
	}

	/**
	 * Initializes the connection for a given HTTP destination.
	 *
	 * @param absoluteUri
	 *           URI targeting the backend system
	 * @param contentType
	 *           String indicating in which format the inputs are provided, mainly JSON.
	 * @param httpMethod
	 *           HttpMethod like GET or POST
	 * @param destination
	 *           Map containing all necessary information like cookies, X-CSRF-Token and Edm data
	 * @param httpDestination
	 *           HTTPDestination representing the target backend system
	 * @param serviceName
	 *           the name of the service as String
	 * @return ConnectionWithTimeStamp an established connection to the backend with timestamp for performance
	 *         measurement.
	 * @throws IOException
	 *            in case of Input Output failure
	 */
	protected ConnectionWithTimeStamp initializeConnection(final URI absoluteUri, final String contentType,
			final HttpMethod httpMethod, final Map<String, Object> destination, final HTTPDestination httpDestination,
			final String serviceName) throws IOException
	{
		if (log.isDebugEnabled())
		{
			log.debug("absoluteUri: " + absoluteUri.toString());
			log.debug("contentType: " + contentType);
			log.debug("httpMethod: " + httpMethod.toString());
			log.debug("serviceName: " + serviceName);
		}
		final HttpURLConnection connection = prepareConnection(contentType, httpMethod, absoluteUri, destination, httpDestination,
				false, serviceName);
		final ConnectionWithTimeStamp conWithStamp = new ConnectionWithTimeStamp();
		conWithStamp.setConnection(connection);
		conWithStamp.setConnectTimeStamp(System.currentTimeMillis());
		connection.connect();

		return conWithStamp;
	}

	/**
	 * Initializes the connection for a given HTTP destination.
	 *
	 * @param absoluteUri
	 *           URI targeting the backend system
	 * @param contentType
	 *           String indicating in which format the inputs are provided, mainly JSON.
	 * @param httpMethod
	 *           HttpMethod like GET or POST
	 * @param destination
	 *           Map containing all necessary information like cookies, X-CSRF-Token and Edm data
	 * @param httpDestination
	 *           HTTPDestination representing the target backend system
	 * @param serviceName
	 *           the name of the service as String
	 * @return ConnectionWithTimeStamp an established connection to the backend with timestamp for performance
	 *         measurement.
	 * @throws IOException
	 *            in case of Input Output failure or as malformed URI
	 */
	protected ConnectionWithTimeStamp initializeConnectionIntern(final URI absoluteUri, final String contentType,
			final HttpMethod httpMethod, final Map<String, Object> destination, final HTTPDestination httpDestination,
			final String serviceName) throws IOException
	{
		final HttpURLConnection connection = prepareConnection(contentType, httpMethod, absoluteUri, destination, httpDestination,
				true, serviceName);
		final ConnectionWithTimeStamp conWithStamp = new ConnectionWithTimeStamp();
		conWithStamp.setConnection(connection);
		conWithStamp.setConnectTimeStamp(System.currentTimeMillis());
		connection.connect();

		return conWithStamp;
	}



	/**
	 * @return the current language
	 */
	protected String getCurrentLanguage()
	{
		if (this.commonI18NService != null && this.commonI18NService.getCurrentLanguage() != null)
		{
			return this.commonI18NService.getCurrentLanguage().getIsocode();
		}
		log.warn("got no current locale from i18nService");
		return null;
	}

	/**
	 * Checks the connection status. In HTTP Error case, the error stream is logged and an exception is thrown.
	 *
	 * @param connection
	 *           HttpURLConnection the established connection to the backend system
	 * @param httpDestinationName
	 *           the http destination name
	 * @throws HttpODataException
	 *            in case of Http connection issue
	 * @throws IOException
	 *            in case of Input Output failure
	 */
	protected void checkStatus(final HttpURLConnection connection, final String httpDestinationName)
			throws HttpODataException, IOException
	{
		final boolean httpError = isHttpError(connection);
		if (httpError)
		{
			if (log.isDebugEnabled())
			{
				InputStream httpErrorStream = null;
				try
				{
					httpErrorStream = connection.getErrorStream();
					logInputStream("HTTP Connection failed: ", httpErrorStream);
				}
				finally
				{
					IOUtils.closeQuietly(httpErrorStream);
				}
			}
			final String message = "Http Connection '" + httpDestinationName + "' failed with status " + connection.getResponseCode()
					+ " " + connection.getResponseMessage();
			log.error(message);
			throw new HttpODataException(message, connection.getResponseCode());
		}
	}

	/**
	 * Determines the response code of the oData call. If this is the first time that the established connection is
	 * queried, the real oData call happens now in the backend system.
	 *
	 * @param connection
	 *           HttpURLConnection the established connection to the backend system
	 * @return boolean true if the response code is comprised between 400 and 599, false otherwise.
	 * @throws IOException
	 *            in case of Input Output failure
	 */
	protected boolean isHttpError(final HttpURLConnection connection) throws IOException
	{
		final int responseCode = connection.getResponseCode();
		if (log.isDebugEnabled())
		{
			log.debug("Backend request triggered");
			log.debug("  connection.getResponseCode(): " + responseCode);
		}
		return 400 <= responseCode && responseCode <= 599;
	}

	/**
	 * Retrieves the full qualified service URI.
	 *
	 * @param suffix
	 *           String intended as potential suffix in the URI
	 * @param httpDestination
	 *           HTTPDestination containing the target URL of a destination
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return URI representing the full qualified service
	 * @throws URISyntaxException
	 *            in case of URI syntax exception
	 * @throws MalformedURLException
	 *            in case of malformed URL exception
	 */
	protected URI getFullQualifiedServiceURI(final String suffix, final HTTPDestination httpDestination, final String serviceURI)
			throws URISyntaxException, MalformedURLException
	{
		final StringBuilder urlBuilder = new StringBuilder();

		appendURLFragment(urlBuilder, adaptTargetUrl(httpDestination.getTargetURL()));
		appendURLFragment(urlBuilder, serviceURI);
		if (suffix != null)
		{
			appendURLFragment(urlBuilder, suffix);
		}
		return new URI(new URI(urlBuilder.toString()).normalize().toString());
	}

	/**
	 * Adapts the target URL by reducing to the protocol, server and potential port.
	 *
	 * @param targetUrl
	 *           String containing the target URL of a destination
	 * @return String the reduced URL containing only protocol, server and potential port
	 * @throws MalformedURLException
	 *            in case of malformed URL exception
	 */
	protected String adaptTargetUrl(final String targetUrl) throws MalformedURLException
	{
		final StringBuilder urlBuilder = new StringBuilder();

		final URL url = new URL(targetUrl);
		urlBuilder.append(url.getProtocol()).append("://").append(url.getHost());
		final int port = url.getPort();
		if (port > 0)
		{
			urlBuilder.append(":").append(port);
		}
		return urlBuilder.toString();
	}

	/**
	 *
	 * Retrieves the full qualified service URI.
	 *
	 * @param suffix
	 *           String intended as potential suffix in the URI
	 * @param filter
	 *           String added as filter to service uri
	 * @param httpDestination
	 *           HTTPDestination containing the target URL of of a destination
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return URI representing the full qualified service
	 * @throws URISyntaxException
	 *            in case of URI syntax exception
	 * @throws MalformedURLException
	 *            in case of malformed URL exception
	 */
	protected URI getFullQualifiedServiceURI(final String suffix, final String filter, final HTTPDestination httpDestination,
			final String serviceURI) throws MalformedURLException, URISyntaxException
	{
		final URI rootUri = getFullQualifiedServiceURI(suffix, httpDestination, serviceURI);
		final StringBuilder uriBuilder = new StringBuilder(rootUri.toString());
		uriBuilder.append("/?$filter=");
		uriBuilder.append(filter);

		return new URI(null, uriBuilder.toString(), null);
	}

	/**
	 *
	 * Retrieves the full qualified service URI.
	 *
	 * @param httpDestination
	 *           HTTPDestination containing the target URL of of a destination
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return URI representing the full qualified service
	 * @throws URISyntaxException
	 *            in case of URI syntax exception
	 * @throws MalformedURLException
	 *            in case of malformed URL exception
	 */
	protected URI getFullQualifiedServiceURI(final HTTPDestination httpDestination, final String serviceURI)
			throws URISyntaxException, MalformedURLException
	{
		return getFullQualifiedServiceURI(null, httpDestination, serviceURI);
	}

	/**
	 * Appends URL Fragment to a given StringBuilder. Potential slash is removed at the end of the String Builder if it's
	 * caused by fragment.
	 *
	 * @param urlBuilder
	 *           StringBuilder where the fragment will be appended
	 * @param fragment
	 *           String which will be added
	 */
	protected void appendURLFragment(final StringBuilder urlBuilder, final String fragment)
	{
		String resultingFragment;

		if (fragment == null || fragment.isEmpty())
		{
			return;
		}

		final boolean urlBuilderEndsWithSlash = urlBuilder.length() > 0
				&& SLASH.equals(urlBuilder.substring(urlBuilder.length() - 1));

		// avoid // in path
		if (urlBuilderEndsWithSlash && fragment.startsWith(SLASH))
		{
			urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		}
		// make sure path fragments are separated by /
		else if (urlBuilder.length() > 0 && !urlBuilderEndsWithSlash && !fragment.startsWith(SLASH))
		{
			urlBuilder.append(SLASH);
		}

		resultingFragment = removeTrailingSlash(fragment);
		urlBuilder.append(resultingFragment);
	}

	/**
	 * Removes the trailing slash if there is one and if the fragment is long than 1 character.
	 *
	 * @param fragment
	 * @return the fragment reduced by trailing slash
	 */
	protected String removeTrailingSlash(final String fragment)
	{
		String resultingFragment;
		// make sure no slash at the end of whole url that is caused by fragment
		if (fragment.length() > 1 && fragment.endsWith(SLASH))
		{
			resultingFragment = fragment.substring(0, fragment.length() - 1);
		}
		else
		{
			resultingFragment = fragment;
		}
		return resultingFragment;
	}

	/**
	 * Parse backend messages for a given connection, available under the sap-message header field.
	 *
	 * @param connection
	 *           HttpURLConnection to the backend system
	 * @return List of BackendMessage
	 */
	protected List<BackendMessage> parseBackendMessages(final HttpURLConnection connection)
	{
		final List<String> messageFields = connection.getHeaderFields().get("sap-message");
		List<BackendMessage> messages;
		if (messageFields == null || messageFields.isEmpty())
		{
			messages = Collections.emptyList();
		}
		else
		{
			messages = parseJSONMessages(messageFields.get(0));
		}
		return messages;
	}

	/**
	 * Parses JSON messages.
	 *
	 * @param headerFieldValue
	 *           String for the header field value containing the messages
	 * @return List of BackendMessage
	 */
	protected List<BackendMessage> parseJSONMessages(final String headerFieldValue)
	{
		final GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(List.class, new BackendMessageDeserializer());
		final Gson gson = gsonBuilder.create();
		return gson.fromJson(headerFieldValue, List.class);
	}

	/**
	 * Creates message from a given message node.
	 *
	 * @param messageNode
	 *           Node message
	 * @return BackendMessage object
	 */
	protected BackendMessage createMessageFromNode(final Node messageNode)
	{
		if (log.isDebugEnabled())
		{
			log.debug("processing xml node '" + messageNode.getNodeName() + "'");
		}
		final BackendMessageImpl message = new BackendMessageImpl();
		final NodeList messageTags = messageNode.getChildNodes();
		for (int jj = 0; jj < messageTags.getLength(); jj++)
		{
			final String tagValue = messageTags.item(jj).getTextContent();
			final String tagName = messageTags.item(jj).getNodeName();
			if (!"details".equals(tagName))
			{
				mapMessageTag(message, tagName, tagValue);
			}
		}
		return message;
	}

	/**
	 * Map message tag.
	 *
	 * @param message
	 *           BackendMessageImpl object message
	 * @param tagName
	 *           String for the name of the tag
	 * @param tagValue
	 *           String for the value of the tag
	 */
	protected void mapMessageTag(final BackendMessageImpl message, final String tagName, final String tagValue)
	{
		if (log.isDebugEnabled())
		{
			log.debug("processing tagName=" + tagName + "; tagValue=" + tagValue);
		}
		switch (tagName)
		{
			case "code":
				final String[] msgClassAndNumber = tagValue.split("/");
				message.setMessageClass(msgClassAndNumber[0]);
				message.setNumber(Integer.parseInt(msgClassAndNumber[1]));
				break;
			case "message":
				message.setText(tagValue);
				break;
			case "severity":
				message.setSeverity(Severity.valueOf(tagValue.toUpperCase()));
				break;
			case "target":
				//not mapped
				break;
			default:
				log.error(
						"Error while parsing message data from 'sap-message' header field: Unexpected message tag name: " + tagName);
				break;
		}
	}

	/**
	 * Logs backend messages.
	 *
	 * @param backendMessages
	 *           List of BackendMessage object to be logged.
	 */
	protected void logBackendMessages(final List<BackendMessage> backendMessages)
	{
		for (final BackendMessage message : backendMessages)
		{
			if (Severity.ERROR.equals(message.getSeverity()))
			{
				log.error(message.toString());
			}
			else if (Severity.WARNING.equals(message.getSeverity()))
			{
				log.warn(message.toString());
			}
			else
			{
				if (log.isDebugEnabled())
				{
					log.debug(message.toString());
				}
			}
		}
	}

	/**
	 * Logs input stream.
	 *
	 * @param logPrefix
	 *           String for the log prefix
	 * @param httpInputStream
	 *           InputStream object
	 * @return InputStream object
	 * @throws IOException
	 *            in case of IOException
	 */
	protected InputStream logInputStream(final String logPrefix, InputStream httpInputStream) throws IOException
	{
		final StringBuilder logMessage = new StringBuilder();
		final byte[] content = IOUtils.toByteArray(httpInputStream);
		logMessage.append(logPrefix);
		logMessage.append(": ");
		logMessage.append(new String(content, Charset.defaultCharset()));
		IOUtils.closeQuietly(httpInputStream);
		httpInputStream = new ByteArrayInputStream(content);
		log.debug(logMessage.toString());
		return httpInputStream;
	}

	/**
	 * Performance measurement are logged as Info.
	 *
	 * @param conWithTimeStamp
	 *           ConnectionWithTimeStamp connection object of timestamp measurement.
	 */
	protected void traceCallTime(final ConnectionWithTimeStamp conWithTimeStamp)
	{
		if (log.isInfoEnabled())
		{
			final long duration = System.currentTimeMillis() - conWithTimeStamp.getConnectTimeStamp();
			final NumberFormat format = DecimalFormat.getInstance(Locale.US);

			final String formattedDuration = format.format(duration);
			final StringBuilder logMessage = new StringBuilder();
			logMessage.append("HTTP ");
			logMessage.append(conWithTimeStamp.connection.getRequestMethod());
			logMessage.append(" on '");
			logMessage.append(conWithTimeStamp.connection.getURL().toExternalForm());
			logMessage.append("' took ");
			logMessage.append(formattedDuration);
			logMessage.append("ms");

			log.info(logMessage.toString());
		}
	}

	/**
	 * Inner class for backend message deserialization.
	 */
	private class BackendMessageDeserializer implements JsonDeserializer<List<BackendMessage>>
	{
		/*
		 * (non-Javadoc)
		 *
		 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type,
		 * com.google.gson.JsonDeserializationContext)
		 */
		@Override
		public List<BackendMessage> deserialize(final JsonElement json, final Type typeOfT,
				final JsonDeserializationContext context)
		{
			final JsonObject msgObj = json.getAsJsonObject();
			final JsonArray detailMessages = msgObj.get("details").getAsJsonArray();
			final List<BackendMessage> messages = new ArrayList<>(detailMessages.size() + 1);
			BackendMessage message = createMessageFromJsonObj(msgObj);
			messages.add(message);
			for (final JsonElement detailObj : detailMessages)
			{
				message = createMessageFromJsonObj(detailObj.getAsJsonObject());
				messages.add(message);
			}
			return messages;
		}

		/**
		 * Creates a BackendMessage from a JSON object.
		 *
		 * @param jsonObj
		 *           JsonObject
		 * @return BackendMessage
		 */
		protected BackendMessage createMessageFromJsonObj(final JsonObject jsonObj)
		{
			if (log.isDebugEnabled())
			{
				log.debug("processing jsonObj: " + jsonObj.toString());
			}
			final BackendMessageImpl message = new BackendMessageImpl();
			for (final Entry<String, JsonElement> elem : jsonObj.entrySet())
			{
				if (elem.getValue().isJsonPrimitive())
				{
					mapMessageTag(message, elem.getKey(), elem.getValue().getAsString());
				}
			}
			return message;
		}
	}

	/**
	 * Inner static class representing a connection and a timestamp for performance measurement.
	 */
	protected static class ConnectionWithTimeStamp
	{
		protected HttpURLConnection connection;
		protected long connectTimeStamp;

		/**
		 * @return the connection
		 */
		public HttpURLConnection getConnection()
		{
			return connection;
		}

		/**
		 * @param connection
		 *           the connection to set
		 */
		public void setConnection(final HttpURLConnection connection)
		{
			this.connection = connection;
		}

		/**
		 * @return the connectTimeStamp
		 */
		public long getConnectTimeStamp()
		{
			return connectTimeStamp;
		}

		/**
		 * @param connectTimeStamp
		 *           the connectTimeStamp to set
		 */
		public void setConnectTimeStamp(final long connectTimeStamp)
		{
			this.connectTimeStamp = connectTimeStamp;
		}
	}


	/**
	 * Creates a new object implementing HttpODataResultManipulator and HttpODataResult
	 *
	 * @return an HttpODataResultManipulator instance
	 */

	protected HttpODataResultManipulator createHttpODataResultInstance()
	{

		final String beanName = "httpODataResult";

		if (applicationContext == null)
		{
			applicationContext = getRegistryApplicationContext();
		}

		if (applicationContext == null)
		{
			log.error("applicationContext is null");
			throw new SAPISCERuntimeException("applicationContext is null");
		}
		try
		{
			return (HttpODataResultManipulator) applicationContext.getBean(beanName);
		}
		catch (final BeansException e)
		{
			log.error("Bean Factory could not provide the whished bean " + beanName);
			throw new SAPISCERuntimeException(
					"Bean Factory could not provide the whished bean " + beanName + ". Cause: " + e.toString(), e);
		}
	}

	/**
	 * Bean injection of the synchronized cache storage object.
	 *
	 * @param httpODataCommonStorage
	 *           HttpODataCommonStorage bean to be injected
	 */
	@Required
	public void setHttpODataCommonStorage(final HttpODataCommonStorage httpODataCommonStorage)
	{
		this.httpODataCommonStorage = httpODataCommonStorage;
	}

	/**
	 *
	 * @param commonI18NService
	 *           the internationalization service necessary to retrieve the current locale
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

}

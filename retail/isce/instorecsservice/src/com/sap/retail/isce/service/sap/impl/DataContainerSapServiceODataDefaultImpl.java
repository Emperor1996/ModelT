/*****************************************************************************
 Class:        DataContainerSapServiceODataDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.HTTPDestinationService;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerOData;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.DataContainerSapServiceOData;
import com.sap.retail.isce.service.sap.odata.HttpODataClient;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.impl.DataContainerBatchPart;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataException;
import com.sap.security.core.server.csi.XSSEncoder;
import com.sap.security.core.server.csi.util.URI;


/**
 * The default implementation class prepares and executes the oData calls.
 */
public class DataContainerSapServiceODataDefaultImpl implements DataContainerSapServiceOData
{
	protected static final String ODATA_QUERY_OPTION_INLINE_COUNT_ALLPAGES = "allpages";
	protected static final String ODATA_QUERY_OPTION_INLINE_COUNT_NONE = "none";
	protected static final String ODATA_QUERY_OPTION_FORMAT = "$format";
	protected static final String ODATA_QUERY_OPTION_EXPAND = "$expand";
	protected static final String ODATA_RESOURCE_PATH_COUNT = "$count";
	protected static final String BATCH_PART_JSON_FORMAT = ODATA_QUERY_OPTION_FORMAT + "=json";

	protected static Logger log = Logger.getLogger(DataContainerSapServiceODataDefaultImpl.class.getName());

	protected HTTPDestinationService httpDestinationService = null;
	protected HttpODataClient httpODataClient = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.DataContainerSapServiceOData#readDataContainers(java.util.List)
	 */
	@Override
	public void readDataContainers(final List<DataContainerOData> dataContainersOData)
	{
		if (dataContainersOData == null || dataContainersOData.isEmpty())
		{
			log.error("dataContainersOData shall neither be null nor empty");
			throw new DataContainerRuntimeException("Expected input data not provided properly");
		}
		HTTPDestination httpDestination;
		String httpDestinationName;
		String serviceURI;
		Map<String, HttpODataResult> httpODataResults;
		List<DataContainerBatchPart> dcBatchParts;

		// build tree of destinations, services, dataContainers
		final Map<String, Map<String, Map<String, DataContainerOData>>> destinations = buildDestinationTree(dataContainersOData);

		// Batch
		for (final Map.Entry<String, Map<String, Map<String, DataContainerOData>>> destinationEntry : destinations.entrySet())
		{
			for (final Map.Entry<String, Map<String, DataContainerOData>> serviceEntry : destinationEntry.getValue().entrySet())
			{
				httpDestinationName = destinationEntry.getKey();
				httpDestination = getHttpDestination(httpDestinationName);
				serviceURI = serviceEntry.getKey();
				if (log.isDebugEnabled())
				{
					log.debug("Service Entry:");
					log.debug("  serviceURI=" + serviceURI);
					log.debug("  httpDestinationName=" + httpDestinationName);
				}
				try
				{
					dcBatchParts = buildBatchParts(serviceEntry.getValue());
					httpODataResults = this.httpODataClient.executeBatchCall(dcBatchParts, httpDestination, serviceURI);
				}
				catch (final HttpODataException e)
				{
					log.error("HttpODataException thrown during batch call execution.", e);
					DataContainer dataContainer;
					for (final Map.Entry<String, DataContainerOData> dataContainerODataEntry : serviceEntry.getValue().entrySet())
					{
						dataContainer = (DataContainer) dataContainerODataEntry.getValue();
						dataContainer.setErrorState(Boolean.TRUE);
						dataContainer.setDataInErrorState();
					}
					// We don't break the execution of the read, because other pending DataContainers could retrieve their data properly
					continue;
				}
				if (httpODataResults != null)
				{
					reassignBatchPartResultsToDataContainer(httpODataResults, serviceEntry);
				}
			}
		}
	}

	/**
	 * Extracts oData BatchPart Results and assign them into corresponding calling DataContainers.
	 *
	 * @param httpODataResults
	 *           the list of batch results containing the data to be extracted from
	 * @param serviceEntry
	 *           the <String, Map<String, DataContainerOData>> corresponding to the service (<ServiceURI,
	 *           Map<DataContainerName, DataContainerOData instance>>))
	 */
	protected void reassignBatchPartResultsToDataContainer(final Map<String, HttpODataResult> httpODataResults,
			final Map.Entry<String, Map<String, DataContainerOData>> serviceEntry)
	{
		DataContainerOData dataContainerOrigin;
		String containerName;
		HttpODataResult httpODataResult;
		for (final Map.Entry<String, DataContainerOData> dataContainerODataEntryWithResults : serviceEntry.getValue().entrySet())
		{
			dataContainerOrigin = dataContainerODataEntryWithResults.getValue();
			containerName = ((DataContainer) dataContainerOrigin).getContainerName();
			httpODataResult = httpODataResults.get(containerName);

			if (httpODataResult != null && httpODataResult.getStatusCode().equals(StatusCode.OK))
			{
				dataContainerOrigin.extractOwnDataFromResult(httpODataResult);
			}
			else
			{
				final DataContainer dataContainer = (DataContainer) dataContainerOrigin;
				log.error("The DataContainerOData " + containerName + " could not read its OData data.");
				dataContainer.setErrorState(Boolean.TRUE);
				dataContainer.setDataInErrorState();
			}
		}
	}


	/**
	 * Checks if the given HTTP Destination is already contained. Retrieve it if available. Create it if not.
	 *
	 * @param destinations
	 *           the list of HTTP Destinations
	 * @param httpDestinationName
	 *           a given HTTP destination name
	 * @return a destination
	 */
	protected Map<String, Map<String, DataContainerOData>> getDestination(
			final Map<String, Map<String, Map<String, DataContainerOData>>> destinations, final String httpDestinationName)
	{
		Map<String, Map<String, DataContainerOData>> destination;
		if (!destinations.containsKey(httpDestinationName))
		{
			destination = new HashMap<>();
			destinations.put(httpDestinationName, destination);
		}
		else
		{
			destination = destinations.get(httpDestinationName);
		}
		return destination;
	}

	/**
	 * Checks if the given oData Service Name is already contained. Retrieve it if available. Create it if not.
	 *
	 * @param destination
	 *           a given HTTP Destination
	 * @param serviceURI
	 *           a given oData ServiceURI
	 * @return an oData Service
	 */
	protected Map<String, DataContainerOData> getService(final Map<String, Map<String, DataContainerOData>> destination,
			final String serviceURI)
	{
		Map<String, DataContainerOData> service;
		if (!destination.containsKey(serviceURI))
		{
			service = new HashMap<>();
			destination.put(serviceURI, service);
		}
		else
		{
			service = destination.get(serviceURI);
		}
		return service;
	}

	/**
	 * Build tree of destinations, services and data containers. Under the root node there are several destination nodes.
	 * Services are the sub nodes of destinations. Data containers are the sub nodes of services. Each data container
	 * occurs only once in the tree.<br/>
	 * <br />
	 * Example <br />
	 * Destinations<br/>
	 * - Destination 1<br/>
	 * -- Service 1<br/>
	 * --- DataContainer1<br/>
	 * --- DataContainer2<br/>
	 * -- Service 2<br/>
	 * --- DataContainer3<br/>
	 * --- DataContainer4<br/>
	 * - Destination 2<br/>
	 * -- Service 1<br/>
	 * --- DataContainer5<br/>
	 * --- DataContainer6<br/>
	 * -- Service 3<br/>
	 * --- DataContainer7<br/>
	 * --- DataContainer8<br/>
	 * Each DataContainer occurs only once in this list.
	 *
	 * @param dataContainersOData
	 *           List of OData data containers to be organized
	 * @return a tree of destinations, services and data containers
	 */
	protected Map<String, Map<String, Map<String, DataContainerOData>>> buildDestinationTree(
			final List<DataContainerOData> dataContainersOData)
	{
		String httpDestinationName;
		String serviceURI;
		String dcName;
		Map<String, DataContainerOData> serviceMap;
		Map<String, Map<String, DataContainerOData>> destinationMap;

		final Map<String, Map<String, Map<String, DataContainerOData>>> destinationsMap = new HashMap();

		for (final DataContainerOData dataContainerOData : dataContainersOData)
		{
			httpDestinationName = dataContainerOData.getHttpDestinationName();
			serviceURI = dataContainerOData.getServiceURI();
			dcName = ((DataContainer) dataContainerOData).getContainerName();

			destinationMap = this.getDestination(destinationsMap, httpDestinationName);
			serviceMap = this.getService(destinationMap, serviceURI);
			if (log.isDebugEnabled())
			{
				log.debug("Build DestinationTree for " + dataContainerOData.toString());
				log.debug("   destinationMap=" + destinationMap.toString());
				log.debug("   serviceMap=" + serviceMap.toString());
			}
			serviceMap.put(dcName, dataContainerOData);
		}
		return destinationsMap;
	}

	/**
	 * Builds the different GET batchParts of a batch call, based on the different data containers information. If
	 * present, the filter attribute value is intentionally URL encoded for security reasons. The uid injected here might
	 * be the email address provided by the user during registration. Each BatchPart results is expected in JSON format.
	 *
	 * @param dataContainerMapForService
	 *           Map of dataContainers belonging to the same service
	 * @return A list of data container batch parts
	 */
	protected List<DataContainerBatchPart> buildBatchParts(final Map<String, DataContainerOData> dataContainerMapForService)
	{
		final List<DataContainerBatchPart> dcBatchParts = new ArrayList<>();
		final Map<String, String> headers = new HashMap<>();
		DataContainerOData dataContainerOData;
		BatchPart batchPart;
		String resultEntityName;
		String dataContainerName;
		DataContainerBatchPart dcBatchPart;

		for (final Map.Entry<String, DataContainerOData> dataContainerODataEntry : dataContainerMapForService.entrySet())
		{
			dataContainerOData = dataContainerODataEntry.getValue();
			dataContainerName = ((DataContainer) dataContainerOData).getContainerName();
			resultEntityName = dataContainerOData.getResultName();

			batchPart = BatchQueryPart.method(HttpMethod.GET.toString()).uri(getURIStringforDataContainer(dataContainerOData))
					.headers(headers).build();

			dcBatchPart = new DataContainerBatchPart();
			dcBatchPart.setDataContainerName(dataContainerName);
			dcBatchPart.setResultName(resultEntityName);
			dcBatchPart.setBatchPart(batchPart);
			dcBatchParts.add(dcBatchPart);

			if (log.isDebugEnabled())
			{
				log.debug("BatchPart for " + dataContainerODataEntry.getKey());
				log.debug("  BatchQueryPart.method=" + HttpMethod.GET.toString());
				String uriFilter = getURIStringforDataContainer(dataContainerOData);
				final String traceableFilter = encodeURL(dataContainerOData.getTraceableFilter());
				final String filter = encodeURL(dataContainerOData.getFilter());
				if (traceableFilter != null && !traceableFilter.isEmpty())
				{
					uriFilter = uriFilter.replace(filter, traceableFilter);
				}
				log.debug("  BatchQueryPart.uri=" + uriFilter);
				log.debug("  BatchQueryPart.headers=" + headers.toString());
				log.debug("  dataContainerName=" + dataContainerName);
				log.debug("  resultEntityName=" + resultEntityName);
				log.debug("  dcBatchParts.size=" + dcBatchParts.size());
			}
		}
		return dcBatchParts;
	}

	/**
	 *
	 * @param dataContainerOData
	 *           the data container for which the UIR should be build
	 * @return a string for building an URI
	 */
	protected String getURIStringforDataContainer(final DataContainerOData dataContainerOData)
	{
		String serviceEndpointName;
		String keyPredicateString;
		String navigationPropertiesString;
		String urlEncodedFilterString;
		String selectString;
		String topString;
		String skipString;
		String inlineCountString;
		String orderByString;
		String urlEncodedFreeQueryString;
		String uriString;

		serviceEndpointName = dataContainerOData.getServiceEndpointName();
		checkUnsupportecResourcePathPart(serviceEndpointName);

		keyPredicateString = getKeyPredicateString(dataContainerOData);

		navigationPropertiesString = getNavigationPropertiesString(dataContainerOData);

		urlEncodedFilterString = getFilterQueryString(dataContainerOData);

		selectString = getSelectQueryString(dataContainerOData);

		topString = getTopQueryString(dataContainerOData);

		skipString = getSkipQueryString(dataContainerOData);

		inlineCountString = getInlineCountQueryString(dataContainerOData);

		orderByString = getOrderByQueryString(dataContainerOData);

		urlEncodedFreeQueryString = getFreeQueryMapString(dataContainerOData);

		uriString = serviceEndpointName + keyPredicateString + navigationPropertiesString + "?" + urlEncodedFilterString
				+ selectString + topString + skipString + inlineCountString + orderByString + urlEncodedFreeQueryString
				+ BATCH_PART_JSON_FORMAT;

		return uriString;
	}

	/**
	 * Retrieves URL encoded the KeyPredicate resource path string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the KeyPredicate information
	 * @return empty string if no KeyPredicate are available, otherwise the URL encoded KeyPredicate surrounded with
	 *         brackets () string
	 */
	protected String getKeyPredicateString(final DataContainerOData dataContainerOData)
	{
		final String keyPredicate = dataContainerOData.getKeyPredicate();
		if (keyPredicate == null || keyPredicate.isEmpty())
		{
			return "";
		}
		return "(" + encodeURL(keyPredicate) + ")";
	}

	/**
	 * Retrieves the Navigation Properties resource path string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the Navigation Properties information
	 * @return empty string if no Navigation Properties are available, otherwise the Navigation Properties string
	 */
	protected String getNavigationPropertiesString(final DataContainerOData dataContainerOData)
	{
		final String navigationProperties = dataContainerOData.getNavigationProperties();
		if (navigationProperties == null || navigationProperties.isEmpty())
		{
			return "";
		}
		checkUnsupportecResourcePathPart(navigationProperties);
		return navigationProperties;
	}


	/**
	 * Checks whether the given string contains parts that are not supported in resource path. If an unsupported part is
	 * determined an runtime exception is thrown. $count is not supported.
	 *
	 * @param stringToBeCheck
	 *           the string to be checked
	 */
	protected void checkUnsupportecResourcePathPart(final String stringToBeCheck)
	{
		if (stringToBeCheck == null || stringToBeCheck.isEmpty())
		{
			return;
		}

		if (stringToBeCheck.trim().toLowerCase(Locale.ROOT).endsWith(ODATA_RESOURCE_PATH_COUNT))
		{

			throw new DataContainerRuntimeException("unsupported content in resource path:  " + ODATA_RESOURCE_PATH_COUNT);
		}
	}

	/**
	 * Retrieves the URL encoded Filter query string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the Filter query option information
	 * @return empty string if no Filter query option is available. The URL encoded string with $filter= in front
	 *         otherwise and an ampersand & at its end
	 */
	protected String getFilterQueryString(final DataContainerOData dataContainerOData)
	{
		final String filter = dataContainerOData.getFilter();
		if (filter == null || filter.isEmpty())
		{
			return "";
		}
		return "$filter=" + encodeURL(filter) + "&";
	}

	/**
	 * Retrieves the URL encoded Select query option string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the Select query option information
	 * @return empty string if no Select query option is available, otherwise the string with $select= in front and an
	 *         ampersand & at its end
	 */
	protected String getSelectQueryString(final DataContainerOData dataContainerOData)
	{
		final String select = dataContainerOData.getSelect();
		if (select == null || select.isEmpty())
		{
			return "";
		}
		return "$select=" + select + "&";
	}

	/**
	 * Retrieves the Top query option string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the Top query option information
	 * @return empty string if no Top query option is available, otherwise the converted top string value with $top= in
	 *         front and an ampersand & at its end
	 */
	protected String getTopQueryString(final DataContainerOData dataContainerOData)
	{
		final Integer top = dataContainerOData.getTop();
		if (top == null)
		{
			return "";
		}
		return "$top=" + top.toString() + "&";
	}

	/**
	 * Retrieves the Skip query option string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the Skip query option information
	 * @return empty string if no Skip query option is available, otherwise the converted top string value with $skip= in
	 *         front and an ampersand & at its end
	 */
	protected String getSkipQueryString(final DataContainerOData dataContainerOData)
	{
		final Integer skip = dataContainerOData.getSkip();
		if (skip == null)
		{
			return "";
		}
		return "$skip=" + skip.toString() + "&";
	}

	/**
	 * Retrieves the InlineCount query option string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the InlineCount query option information
	 * @return empty string if no InlineCount query option is available or if it is not equal InlineCount.ALLPAGES or
	 *         InlineCount.NONE, otherwise the wished inlinecount with an ampersand & at its end
	 */
	protected String getInlineCountQueryString(final DataContainerOData dataContainerOData)
	{
		final InlineCount inlineCount = dataContainerOData.getInlineCount();

		if (InlineCount.ALLPAGES.equals(inlineCount))
		{
			return "$inlinecount=" + ODATA_QUERY_OPTION_INLINE_COUNT_ALLPAGES + "&";
		}
		else if (InlineCount.NONE.equals(inlineCount))
		{
			return "$inlinecount=" + ODATA_QUERY_OPTION_INLINE_COUNT_NONE + "&";
		}
		else
		{
			return "";
		}
	}

	/**
	 * Retrieves the URL encoded OrderBy query option string.
	 *
	 * @param dataContainerOData
	 *           the data container containing the OrderBy query option information
	 * @return empty string if no OrderBy query option is available, otherwise the string with $orderby= in front and an
	 *         ampersand & at its end
	 */
	protected String getOrderByQueryString(final DataContainerOData dataContainerOData)
	{
		final String orderBy = dataContainerOData.getOrderBy();
		if (orderBy == null || orderBy.isEmpty())
		{
			return "";
		}
		return "$orderby=" + encodeURL(orderBy) + "&";
	}

	/**
	 * Retrieves the FreeQuery string that is based on the FreeQueryMap.
	 *
	 * @param dataContainerOData
	 *           the data container containing the FreeQueryMap information
	 * @return empty string if no FreeQueryMap is available or the map has no entries; otherwise the FreeQueryString is
	 *         built by concatenating the keys and the URL encoded values in the following way:
	 *         key1=URLEncodedValue1&key2=URLEncodedValue2&..... The string ends with an ampersand &.
	 */
	protected String getFreeQueryMapString(final DataContainerOData dataContainerOData)
	{
		final Map<String, String> freeQueryMap = dataContainerOData.getFreeQueryMap();
		String name;
		String value;
		if (freeQueryMap == null || freeQueryMap.size() == 0)
		{
			return "";
		}
		String freeQueryString = "";

		for (final Map.Entry<String, String> queryNameValue : freeQueryMap.entrySet())
		{
			name = queryNameValue.getKey();
			if (name == null || name.isEmpty())
			{
				continue;
			}
			else
			{
				name = name.trim();
			}

			value = queryNameValue.getValue();
			if (value == null || value.isEmpty())
			{
				value = "";
			}
			checkUnsupportedQueryOptionName(name);

			if (value.isEmpty())
			{
				freeQueryString = freeQueryString + name + "&";
			}
			else
			{
				freeQueryString = freeQueryString + name + "=" + encodeURL(value) + "&";
			}
		}
		return freeQueryString;
	}

	/**
	 * Checks whether query option name is unsupported. If an unsupported name is determined an runtime exception is
	 * thrown. Explicitly not supported are $format, $count. Further query option might also cause problem or lead to
	 * conflicts with other parts of the final URI. So even if this method succeeds the oData call may fail.
	 *
	 *
	 * @param name
	 *           the query option name to be checked
	 */
	protected void checkUnsupportedQueryOptionName(final String name)
	{
		if (ODATA_QUERY_OPTION_FORMAT.equalsIgnoreCase(name))
		{

			throw new DataContainerRuntimeException("unsupported query option name " + ODATA_QUERY_OPTION_FORMAT
					+ " in free query map");
		}
		else if (ODATA_QUERY_OPTION_EXPAND.equalsIgnoreCase(name))
		{
			throw new DataContainerRuntimeException("unsupported query option name " + ODATA_QUERY_OPTION_EXPAND
					+ " in free query map");
		}
	}

	/**
	 * Security aspect against path traversal attack vector.
	 *
	 * @param serviceUri
	 *           the Uri which needs to be normalized
	 * @return the normalized URI, without any potential "../" anymore
	 */
	protected String normalizeUri(final String serviceUri)
	{
		try
		{
			return new URI(serviceUri).normalize().toString();
		}
		catch (final MalformedURLException e)
		{
			log.error("Security: Normalizing serviceUri for OData Service '" + serviceUri + "' failed.");
			throw new DataContainerRuntimeException("Security: Normalizing serviceUri for OData Service '" + serviceUri
					+ "' failed, due to " + e.getMessage(), e);
		}
	}


	/**
	 * Determines the HTTP destination for a given name
	 *
	 * @param destinationName
	 *           The HTTP Destination name
	 * @return the httpDestination necessary for the oData call.
	 */
	protected HTTPDestination getHttpDestination(final String destinationName)
	{
		return this.httpDestinationService.getHTTPDestination(destinationName);
	}

	/**
	 * URL encode the given string
	 *
	 * @param stringToBeEncoded
	 * @return the URL encoded string
	 */
	protected String encodeURL(final String stringToBeEncoded)
	{
		try
		{
			return encodeURLInner(stringToBeEncoded);
		}
		catch (final UnsupportedEncodingException e)
		{
			log.error("Security: encodeURL of attribute value failed. " + e.getMessage());
			throw new DataContainerRuntimeException("Security: encodeURL of KeyPredicate value failed.", e);
		}
	}

	/**
	 * URL encode the given string via a static call
	 *
	 * @param stringToBeEncoded
	 * @return the URL encoded string
	 * @throws UnsupportedEncodingException
	 */
	protected String encodeURLInner(final String stringToBeEncoded) throws UnsupportedEncodingException
	{
		return XSSEncoder.encodeURL(stringToBeEncoded);
	}

	/**
	 * @param httpDestinationService
	 *           the httpDestinationService to set
	 */
	@Required
	public void setHttpDestinationService(final HTTPDestinationService httpDestinationService)
	{
		this.httpDestinationService = httpDestinationService;
	}

	/**
	 * @param httpODataClient
	 *           the httpODataClient to set
	 */
	@Required
	public void setHttpODataClient(final HttpODataClient httpODataClient)
	{
		this.httpODataClient = httpODataClient;
	}
}

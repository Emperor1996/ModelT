/*****************************************************************************
 Interface:        DataContainerOData
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import java.util.Map;

import org.apache.olingo.odata2.api.commons.InlineCount;

import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Interface dedicated to oData Data Container. To understand the part of the URI please see
 * http://www.odata.org/documentation/odata-version-2-0/uri-conventions. A oData URI consists of service root part
 * followed by resource path and query options, e.g.
 * http://services.odata.org/OData/OData.svc/Category(1)/Products?$top=2&$orderby=name. Not all query options are
 * supported (e.g. $format and $expand are not supported). Additionally $count inside resource path is not supported.
 * Parts of the resulting URI will be URL encoded.
 */
public interface DataContainerOData
{
	/**
	 * @return the httpDestinationName used to determine the schema, host and port for building the first part of the
	 *         service root URI
	 */
	public String getHttpDestinationName();

	/**
	 * @return the service URI, used to build the second part of the service root URI (following the destination part (=
	 *         first part (build of schema, host and port)))
	 */
	public String getServiceURI();

	/**
	 * @return the service endpoint name, e.g. the name of the root collection, used to build the first part of the
	 *         resource path
	 */
	public String getServiceEndpointName();

	/**
	 * @return a predicate that identifies the value(s) of the key Properties of the root collection. It is used for the
	 *         first collection in the resource part of the oData URI. It becomes the second part of the resource path .
	 *         When this property is embedded in the URI in class DataContainerSapServiceODataDefaultImpl.java the it is
	 *         URL encoded and surrounded by brackets.
	 */
	public String getKeyPredicate();

	/**
	 * @return the third part of the resource part of the oData URI that defines the navigation (NavPropertySingle and
	 *         NavPropCollection (including its own key predicates)).
	 */
	public String getNavigationProperties();

	/**
	 * @return the orderby query option to be used in query option part of oData URI.
	 */
	public String getOrderBy();

	/**
	 * @return the top value to be used in top query option part of oData URI.
	 */
	public Integer getTop();

	/**
	 * @return the skip value to be used in skip query option part of oData URI.
	 */
	public Integer getSkip();

	/**
	 * @return the filter string to be used in filter query option part of oData URI. When this property is embedded in
	 *         the URI in class DataContainerSapServiceODataDefaultImpl.java the it is URL encoded.
	 */
	public String getFilter();

	/**
	 * @return null if no extra filter for tracing exists for this container and the default filter can be traced, else a
	 *         filter string where values that might not be traced are replace by '<value not traced>'.
	 */
	public String getTraceableFilter();

	/**
	 * @return the select string to be used in select query option part of oData URI.
	 */
	public String getSelect();

	/**
	 * @return the inlineCount value to be used in the inline count query option part of oData URI.
	 */
	public InlineCount getInlineCount();

	/**
	 * @return a map that is used to build an additional query string that is added ad the end of the query option part
	 *         of oData URI. It must not be used by SAP but is intended to be used by customers to add further query
	 *         options that the SAP implementation does not offer in this interface. This query string must not be used
	 *         to introduce $format or $expand to the query options as these options are not supported. SAP does not
	 *         guarantee that using the map to enhance the query options leads to success. Please test usage and
	 *         interference with the other parts of the whole URI. When the map is embedded in the URI in class
	 *         DataContainerSapServiceODataDefaultImpl.java the values inside the map will be URL encoded.
	 */
	public Map<String, String> getFreeQueryMap();

	/**
	 * @return the name of the entityset containing the result
	 */
	public String getResultName();

	/**
	 * Extracts own Data from httpODataResult. Container is not allowed to hold the httpODataResult reference.
	 *
	 * @param httpODataResult
	 *           the oData results
	 */
	public void extractOwnDataFromResult(HttpODataResult httpODataResult);

}

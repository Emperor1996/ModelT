/*****************************************************************************
 Class:        DataContainerSapServiceODataDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.HTTPDestinationService;
import de.hybris.platform.sap.core.configuration.http.impl.DefaultHTTPDestination;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.container.DataContainerOData;
import com.sap.retail.isce.container.impl.DataContainerODataDefaultImpl;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.DataContainerBatchPart;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataClientImpl;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test class for DataContainerSapServiceODataDefaultImpl using Mockito to mock the OData call.
 *
 */
@UnitTest
public class DataContainerSapServiceODataDefaultImplUnitTest
{
	private DataContainerSapServiceODataDefaultImpl classUnderTest;

	private HTTPDestinationService httpDestinationServiceMock;
	private DefaultHTTPDestination httpDestinationMock;

	private TestDataContainerOData testDataContainerOData;

	private static final String USER_ID = "USER_ID";
	private static final String TARGET_URL = "TARGET_URL/";
	private static final String TARGET_URL_EXCEPTION = "TARGET_      SECURITY ATTACK MALFORMED URL/";
	private static final String TARGET_URL_WITH_PATH_TRAVERSAL = "ATTACK_VECTOR../../TARGET_URL/";
	private static final String AUTHENTICATION_TYPE = "AUTHENTICATION_TYPE";
	private static final String EMAIL_ADDRESS = "tester@sap.com";
	private static final String HTTP_ODATA_RESULT_ATTRIBUTE_1 = "HTTP_ODATA_RESULT_ATTRIBUTE_1";

	private static final String HTTP_DESTINATION_NAME_TEST = "HTTP_DESTINATION_NAME_TEST";
	private static final String SERVICE_URI_TEST = "SERVICE_URI_TEST";
	private static final String SERVICE_END_POINT_NAME_TEST = "SERVICE_END_POINT_NAME_TEST";
	private static final String RESULT_ENTITY_NAME_TEST = "RESULT_ENTITY_NAME_TEST";
	private static final String DATA_CONTAINER_NAME_TEST = "DATA_CONTAINER_NAME_TEST";

	private static final String KEY_PREDICATE_TEST = "key1='value1',key2='value2'";
	private static final String NAV_PROPERTIES_TEST = "/navProp1/navProp2";
	private static final String STRING_TO_BE_ENCODED = "key1='value1',key2='value2'";
	private final String FILTER_TEST = "EMailAddress eq '" + EMAIL_ADDRESS + "'";
	private static final String SELECT_TEST = "Property1,Property2";
	private final Integer TOP_TEST = Integer.valueOf(20);
	private final Integer SKIP_TEST = Integer.valueOf(10);
	private final InlineCount INLINE_COUNT_TEST_ALLPAGES = InlineCount.ALLPAGES; // possible inlinecount values: allpages, none (-> count must not be part of response)
	private final InlineCount INLINE_COUNT_TEST_NONE = InlineCount.NONE;
	private static final String ORDER_BY_TEST = "property1,property2";

	private boolean readDataContainersException = false;
	private boolean throwUnsupportedEncodingException = false;

	//
	//** local class for mocking Logger **//
	//
	private class LoggerTest extends Logger
	{
		protected LoggerTest(final String name)
		{
			super(name);
		}

		@Override
		public boolean isInfoEnabled()
		{
			return true;
		}


		@Override
		public boolean isDebugEnabled()
		{
			return true;
		}

		@Override
		public void error(final Object message)
		{
			//
		}

		@Override
		public void warn(final Object message)
		{
			//
		}

		@Override
		public void error(final Object message, final Throwable t)
		{
			//
		}

		@Override
		public void debug(final Object message)
		{
			//
		}

		@Override
		public void info(final Object message)
		{
			//
		}
	}

	//
	//** local class for mocking HttpODataClientImpl **//
	//
	private class HttpODataClientImplTest extends HttpODataClientImpl
	{
		private Map<String, HttpODataResult> results = null;

		@Override
		public Map<String, HttpODataResult> executeBatchCall(final List<DataContainerBatchPart> dcBatchParts,
				final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
		{
			if (readDataContainersException)
			{
				throw new HttpODataException();
			}
			else
			{
				results = new HashMap<>();
				final HttpODataResultManipulator httpODataResultManipulator = new HttpODataResultImpl();
				httpODataResultManipulator.setStatusCode(StatusCode.OK);
				results.put(DATA_CONTAINER_NAME_TEST, (HttpODataResult) httpODataResultManipulator);
				return this.results;
			}
		}
	}


	//
	//** local class for mocking DataContainerODataDefaultImpl **//
	//
	private class TestDataContainerOData extends DataContainerODataDefaultImpl
	{
		public TestDataContainerOData()
		{
			super();
			this.httpDestinationName = HTTP_DESTINATION_NAME_TEST;
			this.serviceURI = SERVICE_URI_TEST;
			this.serviceEndpointName = SERVICE_END_POINT_NAME_TEST;
			this.containerName = DATA_CONTAINER_NAME_TEST;
			this.filter = FILTER_TEST;
			this.resultName = RESULT_ENTITY_NAME_TEST;
		}

		public TestDataContainerOData(final String containerName, final String httpDestinationName, final String serviceURI,
				final String serviceEndpointName)
		{
			super();
			this.httpDestinationName = httpDestinationName;
			this.serviceURI = serviceURI;
			this.serviceEndpointName = serviceEndpointName;
			this.containerName = containerName;
		}

		public TestDataContainerOData(final String containerName, final String httpDestinationName, final String serviceURI,
				final String serviceEndpointName, final String keyPredicate, final String navigationProperties, final String filter,
				final String select, final Integer top, final Integer skip, final InlineCount inlineCount, final String orderBy,
				final Map<String, String> freeQueryMap)
		{
			super();
			this.containerName = "TestDataContainerOData";
			this.httpDestinationName = httpDestinationName;
			this.serviceURI = serviceURI;
			this.serviceEndpointName = serviceEndpointName;
			this.containerName = containerName;

			this.keyPredicate = keyPredicate;
			this.navigationProperties = navigationProperties;
			this.filter = filter;
			this.select = select;
			this.top = top;
			this.skip = skip;
			this.inlineCount = inlineCount;
			this.orderBy = orderBy;
			this.freeQueryMap = freeQueryMap;
		}

		private String text1 = "";
		private String dataInErrorState = "";

		public String getText1()
		{
			return text1;
		}

		public String getDataInErrorState()
		{
			return dataInErrorState;
		}

		@Override
		public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
		{
			text1 = HTTP_ODATA_RESULT_ATTRIBUTE_1;
		}

		@Override
		public void setDataInErrorState()
		{
			dataInErrorState = "dataInErrorState";
		}

		@Override
		public void encodeHTML()
		{
			//
		}

		@Override
		public String getLocalizedContainerName()
		{
			return null;
		}

		@Override
		public String getContainerContextParamName()
		{
			return "TestDataContainerOData";
		}

	}

	//
	//** local class extending class under test **//
	//

	public class DataContainerSapServiceODataDefaultImplTest extends DataContainerSapServiceODataDefaultImpl
	{
		public DataContainerSapServiceODataDefaultImplTest()
		{
			super();
			log = new LoggerTest("");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImpl#encodeURLInner(java.lang.String)
		 */
		@Override
		protected String encodeURLInner(final String stringToBeEncoded) throws UnsupportedEncodingException
		{
			if (throwUnsupportedEncodingException)
			{
				throw new UnsupportedEncodingException();
			}
			else
			{

				return super.encodeURLInner(stringToBeEncoded);
			}
		}

	}

	//
	//** Tests **//
	//
	@Before
	public void setUp()
	{
		classUnderTest = new DataContainerSapServiceODataDefaultImplTest();

		classUnderTest.setHttpODataClient(new HttpODataClientImplTest());
		httpDestinationServiceMock = mock(HTTPDestinationService.class);
		classUnderTest.setHttpDestinationService(httpDestinationServiceMock);

		// Prepare output test data
		httpDestinationMock = new DefaultHTTPDestination();
		httpDestinationMock.setHttpDestinationName(HTTP_DESTINATION_NAME_TEST);
		httpDestinationMock.setUserid(USER_ID);
		httpDestinationMock.setTargetURL(TARGET_URL);
		httpDestinationMock.setAuthenticationType(AUTHENTICATION_TYPE);
	}

	/**
	 * Tests null and empty inputs.
	 */
	@Test
	public void testGetResultNullAndEmptyInputs()
	{
		try
		{
			classUnderTest.readDataContainers(null);
			assertFalse("An exception shoud have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}

		try
		{
			classUnderTest.readDataContainers(new ArrayList<DataContainerOData>());
			assertFalse("An exception shoud have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests the readDataContainers.
	 */
	@Test
	public void testReadDataContainers()
	{
		// Use Mockito to mock method calls and results
		when(httpDestinationServiceMock.getHTTPDestination(HTTP_DESTINATION_NAME_TEST)).thenReturn(httpDestinationMock);

		// Prepare input test data
		testDataContainerOData = new TestDataContainerOData();

		final List<DataContainerOData> dataContainersOData = new ArrayList<DataContainerOData>();
		dataContainersOData.add(testDataContainerOData);
		classUnderTest.readDataContainers(dataContainersOData);
		assertEquals("We should find one entry", HTTP_ODATA_RESULT_ATTRIBUTE_1, testDataContainerOData.getText1());
	}

	/**
	 * Tests the readDataContainers Exception.
	 */
	@Test
	public void testReadDataContainersException()
	{
		// Use Mockito to mock method calls and results
		when(httpDestinationServiceMock.getHTTPDestination(HTTP_DESTINATION_NAME_TEST)).thenReturn(httpDestinationMock);
		this.readDataContainersException = true;
		// Prepare input test data
		testDataContainerOData = new TestDataContainerOData();

		final List<DataContainerOData> dataContainersOData = new ArrayList<DataContainerOData>();
		dataContainersOData.add(testDataContainerOData);
		classUnderTest.readDataContainers(dataContainersOData);
		assertEquals("Error State should be true", Boolean.TRUE, testDataContainerOData.getErrorState());
		assertEquals("Data should be restored to default error state", "dataInErrorState",
				testDataContainerOData.getDataInErrorState());
	}

	/**
	 * Tests the reassignBatchPartResultsToDataContainer .
	 */
	@Test
	public void testReassignBatchPartResultsToDataContainer()
	{
		// Prepare input test data
		testDataContainerOData = new TestDataContainerOData();

		final Map<String, HttpODataResult> httpODataResults = new HashMap<>();
		final HttpODataResultManipulator httpODataResultManipulator = new HttpODataResultImpl();
		httpODataResultManipulator.setStatusCode(StatusCode.OK);
		httpODataResults.put(DATA_CONTAINER_NAME_TEST, (HttpODataResult) httpODataResultManipulator);

		final Map.Entry<String, Map<String, DataContainerOData>> serviceEntry = new Entry<String, Map<String, DataContainerOData>>()
		{

			@Override
			public Map<String, DataContainerOData> setValue(final Map<String, DataContainerOData> value)
			{
				return null;
			}

			@Override
			public Map<String, DataContainerOData> getValue()
			{
				final Map<String, DataContainerOData> dataContainerODataEntryWithResults = new HashMap<>();
				dataContainerODataEntryWithResults.put("TestDataContainerOData", testDataContainerOData);
				return dataContainerODataEntryWithResults;
			}

			@Override
			public String getKey()
			{
				return null;
			}
		};

		classUnderTest.reassignBatchPartResultsToDataContainer(httpODataResults, serviceEntry);
		assertEquals("We should find one entry", HTTP_ODATA_RESULT_ATTRIBUTE_1, testDataContainerOData.getText1());

		httpODataResults.replace(DATA_CONTAINER_NAME_TEST, null);
		classUnderTest.reassignBatchPartResultsToDataContainer(httpODataResults, serviceEntry);
		assertEquals("Error State should be true", Boolean.TRUE, testDataContainerOData.getErrorState());
		assertEquals("Data should be restored to default error state", "dataInErrorState",
				testDataContainerOData.getDataInErrorState());
	}

	/**
	 * Tests the getDestination.
	 */
	@Test
	public void testGetDestination()
	{
		final Map<String, Map<String, Map<String, DataContainerOData>>> destinations = new HashMap<>();
		final String httpDestinationName = "testDestination";
		final Map<String, Map<String, DataContainerOData>> returnedDestinations = classUnderTest.getDestination(destinations,
				httpDestinationName);
		assertNotNull("returnedDestinations should not be null", returnedDestinations);
		assertEquals("returnedDestinations should be emtpy", Boolean.TRUE, Boolean.valueOf(returnedDestinations.isEmpty()));

		final Map<String, Map<String, DataContainerOData>> returnedDestinations2 = classUnderTest.getDestination(destinations,
				httpDestinationName);
		assertNotNull("returnedDestinations2 should not be null", returnedDestinations2);
		assertEquals("returnedDestinations should be emtpy", Boolean.TRUE, Boolean.valueOf(returnedDestinations2.isEmpty()));
	}

	/**
	 * Tests the getService.
	 */
	@Test
	public void testGetService()
	{
		final Map<String, Map<String, DataContainerOData>> destination = new HashMap<>();
		final String serviceURI = "serviceURI/serviceName";

		final Map<String, DataContainerOData> serviceMap = classUnderTest.getService(destination, serviceURI);
		assertNotNull("serviceMap should not be null", serviceMap);
		assertEquals("serviceMap should be emtpy", Boolean.TRUE, Boolean.valueOf(serviceMap.isEmpty()));

		final Map<String, DataContainerOData> serviceMap2 = classUnderTest.getService(destination, serviceURI);
		assertNotNull("serviceMap2 should not be null", serviceMap2);
		assertEquals("serviceMap2 should be emtpy", Boolean.TRUE, Boolean.valueOf(serviceMap2.isEmpty()));

	}

	/**
	 * Tests buildDestinationTree.
	 */
	@Test
	public void testBuildDestinationTree()
	{
		final List<DataContainerOData> dataContainersOData = new ArrayList<>();
		Map<String, Map<String, Map<String, DataContainerOData>>> destinations = classUnderTest
				.buildDestinationTree(dataContainersOData);
		assertNotNull("destinations should not be null", destinations);
		assertEquals("destinations should be emtpy", Boolean.TRUE, Boolean.valueOf(destinations.isEmpty()));

		// Prepare input test data
		testDataContainerOData = new TestDataContainerOData();
		dataContainersOData.add(testDataContainerOData);
		destinations = classUnderTest.buildDestinationTree(dataContainersOData);
		assertNotNull("destinations should not be null", destinations);

		for (final Map.Entry<String, Map<String, Map<String, DataContainerOData>>> destinationEntry : destinations.entrySet())
		{
			assertNotNull("destinationEntry should not be null", destinationEntry);
			assertEquals("destinationKey should be HTTP_DESTINATION_NAME_TEST", "HTTP_DESTINATION_NAME_TEST",
					destinationEntry.getKey());

			for (final Map.Entry<String, Map<String, DataContainerOData>> serviceEntry : destinationEntry.getValue().entrySet())
			{
				assertNotNull("serviceEntry should not be null", serviceEntry);
				assertEquals("serviceKey should be SERVICE_URI_TEST", SERVICE_URI_TEST, serviceEntry.getKey());

				for (final Map.Entry<String, DataContainerOData> dcEntry : serviceEntry.getValue().entrySet())
				{
					assertNotNull("dcEntry should not be null", dcEntry);
					assertEquals("data Container Key should be DATA_CONTAINER_NAME_TEST", DATA_CONTAINER_NAME_TEST, dcEntry.getKey());
					assertNotNull("dcEntry.getValue() should not be null", dcEntry.getValue());
				}
			}
		}

		dataContainersOData.remove(testDataContainerOData);
		// Prepare input test data
		final TestDataContainerOData testDataContainerOData_1 = new TestDataContainerOData("containerName_1",
				"httpDestinationName_1", "serviceURI_1", "serviceEndPointName_1");
		final TestDataContainerOData testDataContainerOData_2 = new TestDataContainerOData("containerName_2",
				"httpDestinationName_1", "serviceURI_1", "serviceEndPointName_2");
		final TestDataContainerOData testDataContainerOData_3 = new TestDataContainerOData("containerName_5",
				"httpDestinationName_2", "serviceURI_3", "serviceEndPointName_3");
		final TestDataContainerOData testDataContainerOData_4 = new TestDataContainerOData("containerName_3",
				"httpDestinationName_2", "serviceURI_2", "serviceEndPointName_4");
		final TestDataContainerOData testDataContainerOData_5 = new TestDataContainerOData("containerName_4",
				"httpDestinationName_2", "serviceURI_2", "serviceEndPointName_5");
		dataContainersOData.add(testDataContainerOData_1);
		dataContainersOData.add(testDataContainerOData_2);
		dataContainersOData.add(testDataContainerOData_3);
		dataContainersOData.add(testDataContainerOData_4);
		dataContainersOData.add(testDataContainerOData_5);

		// We expect this hierarchical order while the order of nodes and the order of sub nodes inside a node is not fix.
		//		{httpDestinationName_1=
		//			{serviceURI_1=
		//				{containerName_1=com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImplUnitTest$TestDataContainerOData@68e5eea7,
		//				 containerName_2=com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImplUnitTest$TestDataContainerOData@762ef0ea}},
		//
		//		httpDestinationName_2=
		//			{serviceURI_2=
		//				{containerName_3=com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImplUnitTest$TestDataContainerOData@2d2ffcb7,
		//				 containerName_4=com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImplUnitTest$TestDataContainerOData@31f9b85e},
		//			serviceURI_3=
		//				{containerName_5=com.sap.retail.isce.service.sap.impl.DataContainerSapServiceODataDefaultImplUnitTest$TestDataContainerOData@291b4bf5}}}

		destinations = classUnderTest.buildDestinationTree(dataContainersOData);
		assertNotNull("sortedDC should not be null", destinations);

		// check httpDestinationName_1
		Map<String, Map<String, DataContainerOData>> destination = destinations.get("httpDestinationName_1");
		assertNotNull("destinationEntry should not be null", destination);

		Map<String, DataContainerOData> service = destination.get("serviceURI_1");
		assertNotNull("service should not be null", service);
		DataContainerOData dataContainer = service.get("containerName_1");
		assertNotNull("dataContainer should not be null", dataContainer);
		dataContainer = service.get("containerName_2");
		assertNotNull("dataContainer should not be null", dataContainer);

		service = destination.get("serviceURI_2");
		assertEquals("service should be null", null, service);

		// check httpDestinationName_2
		destination = destinations.get("httpDestinationName_2");
		assertNotNull("destinationEntry should not be null", destination);

		service = destination.get("serviceURI_2");
		assertNotNull("service should not be null", service);
		dataContainer = service.get("containerName_4");
		assertNotNull("dataContainer should not be null", dataContainer);
		dataContainer = service.get("containerName_3");
		assertNotNull("dataContainer should not be null", dataContainer);

		service = destination.get("serviceURI_3");
		assertNotNull("service should not be null", service);
		dataContainer = service.get("containerName_5");
		assertNotNull("dataContainer should not be null", dataContainer);


		//		int destinationNr = 0;
		//		int serviceNr = 0;
		//		int dcNr = 0;
		//		for (final Map.Entry<String, Map<String, Map<String, DataContainerOData>>> destinationEntry : sortedDC.entrySet())
		//		{
		//			destinationNr++;
		//			assertNotNull("destinationEntry should not be null", destinationEntry);
		//			assertEquals("destinationKey should be httpDestinationName_" + destinationNr, "httpDestinationName_" + destinationNr,
		//					destinationEntry.getKey());
		//
		//			for (final Map.Entry<String, Map<String, DataContainerOData>> serviceEntry : destinationEntry.getValue().entrySet())
		//			{
		//				serviceNr++;
		//				assertNotNull("serviceEntry should not be null", serviceEntry);
		//				assertEquals("serviceKey should be serviceURI_" + serviceNr, "serviceURI_" + serviceNr, serviceEntry.getKey());
		//
		//				for (final Map.Entry<String, DataContainerOData> dcEntry : serviceEntry.getValue().entrySet())
		//				{
		//					dcNr++;
		//					assertNotNull("dcEntry should not be null", dcEntry);
		//					assertEquals("data Container Key should be containerName_" + dcNr, "containerName_" + dcNr, dcEntry.getKey());
		//					assertNotNull("dcEntry.getValue() should not be null", dcEntry.getValue());
		//				}
		//			}
		//		}
	}

	/**
	 * Tests buildBatchParts.
	 */
	@Test
	public void testBuildBatchParts()
	{
		final Map<String, DataContainerOData> dataContainerMapForService = new HashMap<>();
		List<DataContainerBatchPart> dcBatchParts = classUnderTest.buildBatchParts(dataContainerMapForService);
		assertNotNull("dcBatchParts should not be null", dcBatchParts);
		assertEquals("dcBatchParts should be emtpy", Boolean.TRUE, Boolean.valueOf(dcBatchParts.isEmpty()));

		testDataContainerOData = new TestDataContainerOData();
		dataContainerMapForService.put(DATA_CONTAINER_NAME_TEST, testDataContainerOData);
		dcBatchParts = classUnderTest.buildBatchParts(dataContainerMapForService);
		assertNotNull("dcBatchParts should not be null", dcBatchParts);

		BatchPart batchPart = null;

		for (final DataContainerBatchPart dcBatchPart : dcBatchParts)
		{
			assertNotNull("dcBatchPart should not be null", dcBatchPart);

			assertEquals("DataContainerName of batch part should be DATA_CONTAINER_NAME_TEST", DATA_CONTAINER_NAME_TEST,
					dcBatchPart.getDataContainerName());
			assertEquals("DataContainerName of batch part should be RESULT_ENTITY_NAME_TEST", RESULT_ENTITY_NAME_TEST,
					dcBatchPart.getResultName());

			batchPart = dcBatchPart.getBatchPart();
			assertNotNull("batchQueryPart should not be null", batchPart);
			assertEquals("batchQueryPart method should be GET", "GET", ((BatchQueryPart) batchPart).getMethod());
			assertEquals(
					"batchQueryPart method should be SERVICE_END_POINT_NAME_TEST?$filter=EMailAddress%20eq%20%27tester%40sap.com%27&$format=json",
					"SERVICE_END_POINT_NAME_TEST?$filter=EMailAddress%20eq%20%27tester%40sap.com%27&$format=json",
					((BatchQueryPart) batchPart).getUri());
		}
	}

	/**
	 * Tests testGetURIStringforDataContainer.
	 */
	@Test
	public void testGetURIStringforDataContainer()
	{
		final LinkedHashMap<String, String> freeQueryMap = new LinkedHashMap();
		freeQueryMap.put("name1", "value1");
		freeQueryMap.put("name2", "value2");

		final TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1",
				"httpDestinationName_1", "serviceURI_1", "serviceEndPointName_1", KEY_PREDICATE_TEST, NAV_PROPERTIES_TEST,
				FILTER_TEST, SELECT_TEST, TOP_TEST, SKIP_TEST, INLINE_COUNT_TEST_ALLPAGES, ORDER_BY_TEST, freeQueryMap);

		final String pathString = classUnderTest.getURIStringforDataContainer(testDataContainerOData);
		assertEquals("pathString", "serviceEndPointName_1" + "(key1%3d%27value1%27%2ckey2%3d%27value2%27)" + "/navProp1/navProp2"
				+ "?" + "$filter=EMailAddress%20eq%20%27tester%40sap.com%27&" + "$select=Property1,Property2&" + "$top=20&"
				+ "$skip=10&" + "$inlinecount=allpages&" + "$orderby=property1%2cproperty2&"
				+ "name1=value1&name2=value2&$format=json", pathString);
	}

	/**
	 * Tests testGetKeyPredicateString.
	 */
	@Test
	public void testGetKeyPredicateString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String pathString = classUnderTest.getKeyPredicateString(testDataContainerOData);
		assertEquals("pathString should be empty", "", pathString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", "", null, null, null, null, null, null, null, null);
		pathString = classUnderTest.getKeyPredicateString(testDataContainerOData);
		assertEquals("pathString should be empty", "", pathString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", KEY_PREDICATE_TEST, null, null, null, null, null, null, null, null);
		pathString = classUnderTest.getKeyPredicateString(testDataContainerOData);
		assertEquals("pathString should be ((key1%3d%27value1%27%2ckey2%3d%27value2%27)",
				"(key1%3d%27value1%27%2ckey2%3d%27value2%27)", pathString);
	}


	/**
	 * Tests testGetNavigationPropertiesString.
	 */
	@Test
	public void testGetNavigationPropertiesString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String pathString = classUnderTest.getNavigationPropertiesString(testDataContainerOData);
		assertEquals("pathString should be empty", "", pathString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, "", null, null, null, null, null, null, null);
		pathString = classUnderTest.getNavigationPropertiesString(testDataContainerOData);
		assertEquals("pathString should be empty", "", pathString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, NAV_PROPERTIES_TEST, null, null, null, null, null, null, null);
		pathString = classUnderTest.getNavigationPropertiesString(testDataContainerOData);
		assertEquals("pathString should be /navProp1/navProp2", "/navProp1/navProp2", pathString);
	}

	/**
	 * Tests testCheckUnsupportecResourcePathPart.
	 */
	@Test
	public void testCheckUnsupportecResourcePathPart()
	{
		try
		{
			classUnderTest.checkUnsupportecResourcePathPart(null);
			classUnderTest.checkUnsupportecResourcePathPart("");
			classUnderTest.checkUnsupportecResourcePathPart("pathpart");
			classUnderTest.checkUnsupportecResourcePathPart("$count/xx");
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("DataContainerRuntimeException should NOT have been thrown", true);
		}
		try
		{
			classUnderTest.checkUnsupportecResourcePathPart("$count");
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			// we expect to come here
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");

		}
		try
		{
			classUnderTest.checkUnsupportecResourcePathPart("pathpart/ $count ");
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests getFilterQueryString.
	 */
	@Test
	public void testGetFilterQueryString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getFilterQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, "", null, null, null, null, null, null);
		queryString = classUnderTest.getFilterQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, FILTER_TEST, null, null, null, null, null, null);
		queryString = classUnderTest.getFilterQueryString(testDataContainerOData);
		assertEquals("queryString should be $filter=EMailAddress%20eq%20%27tester%40sap.com%27&",
				"$filter=EMailAddress%20eq%20%27tester%40sap.com%27&", queryString);
	}

	/**
	 * Tests testGetSelectQueryString.
	 */
	@Test
	public void testGetSelectQueryString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getSelectQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, "", null, null, null, null, null);
		queryString = classUnderTest.getSelectQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, SELECT_TEST, null, null, null, null, null);
		queryString = classUnderTest.getSelectQueryString(testDataContainerOData);
		assertEquals("queryString should be $select=Property1%2cProperty2&", "$select=Property1,Property2&", queryString);
	}

	/**
	 * Tests testGetTopQueryString.
	 */
	@Test
	public void testGetTopQueryString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getTopQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, TOP_TEST, null, null, null, null);
		queryString = classUnderTest.getTopQueryString(testDataContainerOData);
		assertEquals("queryString should be $top=20&", "$top=20&", queryString);

	}

	/**
	 * Tests testGetSkipQueryString.
	 */
	@Test
	public void testGetSkipQueryString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getSkipQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, SKIP_TEST, null, null, null);
		queryString = classUnderTest.getSkipQueryString(testDataContainerOData);
		assertEquals("queryString should be $skip=10&", "$skip=10&", queryString);
	}

	/**
	 * Tests testGetInlineCountQueryString.
	 */
	@Test
	public void testGetInlineCountQueryString()
	{
		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getInlineCountQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, INLINE_COUNT_TEST_NONE, null, null);
		queryString = classUnderTest.getInlineCountQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "$inlinecount=none&", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, INLINE_COUNT_TEST_ALLPAGES, null, null);
		queryString = classUnderTest.getInlineCountQueryString(testDataContainerOData);
		assertEquals("queryString should be $inlinecount=allpages&", "$inlinecount=allpages&", queryString);
	}

	/**
	 * Tests testGetOrderByQueryString.
	 */
	@Test
	public void testGetOrderByQueryString()
	{

		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getOrderByQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, "", null);
		queryString = classUnderTest.getOrderByQueryString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, ORDER_BY_TEST, null);
		queryString = classUnderTest.getOrderByQueryString(testDataContainerOData);
		assertEquals("queryString should be $orderby=property1%2cproperty2&", "$orderby=property1%2cproperty2&", queryString);

	}

	/**
	 * Tests testGetFreeQueryMapString.
	 */
	@Test
	public void testGetFreeQueryMapString()
	{
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, null);
		String queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		final LinkedHashMap<String, String> freeQueryMap = new LinkedHashMap();

		TestDataContainerOData testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1",
				"serviceURI_1", "serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be empty string", "", queryString);

		freeQueryMap.put("name1", "value1");
		freeQueryMap.put(" /name2 ", " /value2 ");
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be name1=value1&/name2=%20%2fvalue2%20&", "name1=value1&/name2=%20%2fvalue2%20&",
				queryString);

		freeQueryMap.clear();
		freeQueryMap.put(null, null);
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be empty", "", queryString);

		freeQueryMap.clear();
		freeQueryMap.put(null, "value");
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be empty", "", queryString);

		freeQueryMap.clear();
		freeQueryMap.put("", "value");
		freeQueryMap.put("", "");
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be empty", "", queryString);

		freeQueryMap.clear();
		freeQueryMap.put(" key1 ", null);
		freeQueryMap.put(" key2 ", "");
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
		assertEquals("queryString should be key1&key2&", "key1&key2&", queryString);
	}

	/**
	 * Tests testGetFreeQueryMapString.
	 */
	@Test
	public void testGetFreeQueryMapStringException()
	{
		final LinkedHashMap<String, String> freeQueryMap = new LinkedHashMap();
		testDataContainerOData = new TestDataContainerOData("containerName_1", "httpDestinationName_1", "serviceURI_1",
				"serviceEndPointName_1", null, null, null, null, null, null, null, null, freeQueryMap);
		String queryString = "";
		queryString = queryString + "";
		try
		{
			freeQueryMap.put("$format", " json ");
			queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			// We expect to come here
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}

		try
		{
			freeQueryMap.remove("$format");
			freeQueryMap.put("$expand", " /NavProp ");
			queryString = classUnderTest.getFreeQueryMapString(testDataContainerOData);
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			// We expect to come here
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests testCheckUnsupportedQueryOptionName.
	 */
	public void testCheckUnsupportedQueryOptionName()
	{
		try
		{
			classUnderTest.checkUnsupportedQueryOptionName(null);
			classUnderTest.checkUnsupportedQueryOptionName("");
			classUnderTest.checkUnsupportedQueryOptionName(" name1 ");
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("DataContainerRuntimeException should NOT have been thrown", true);
		}
		try
		{
			classUnderTest.checkUnsupportedQueryOptionName("$format");
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
		try
		{
			classUnderTest.checkUnsupportedQueryOptionName(" $EXPAND ");
			assertFalse("DataContainerRuntimeException should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests the Malformed Exception.
	 */
	@Test
	public void testNormalizeUriMalformedExceptionSecurity()
	{
		httpDestinationMock.setTargetURL(TARGET_URL_EXCEPTION);
		// Use Mockito to mock method calls and results
		when(httpDestinationServiceMock.getHTTPDestination(HTTP_DESTINATION_NAME_TEST)).thenReturn(httpDestinationMock);

		try
		{
			classUnderTest.normalizeUri(TARGET_URL_EXCEPTION);
			assertFalse("Malformed URL Exception should have been thrown", true);
		}
		catch (final DataContainerRuntimeException ex)
		{
			// We expect to come here
			assertEquals("DataContainerRuntimeException was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}
	}

	/**
	 * Tests Path Traversal Sanitization.
	 */
	@Test
	public void testNormalizeUriSanitizePathTraversalSecurity()
	{
		try
		{
			assertEquals("We should find a sanitized URI", TARGET_URL, classUnderTest.normalizeUri(TARGET_URL_WITH_PATH_TRAVERSAL));
		}
		catch (final DataContainerRuntimeException e)
		{
			assertFalse("Malformed URL Exception should not have been thrown", true);
		}
	}

	/**
	 * Test URL encoding.
	 */
	@Test
	public void testUncodeURL()
	{
		assertEquals("encodedString should be key1%3d%27value1%27%2ckey2%3d%27value2%27",
				"key1%3d%27value1%27%2ckey2%3d%27value2%27", classUnderTest.encodeURL(STRING_TO_BE_ENCODED));

		try
		{
			this.throwUnsupportedEncodingException = true;
			classUnderTest.encodeURL(STRING_TO_BE_ENCODED);
			assertFalse("Exception should have been thrown", true);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("Expected exception was thrown", true);
		}
		finally
		{
			this.throwUnsupportedEncodingException = true;
		}
	}
}

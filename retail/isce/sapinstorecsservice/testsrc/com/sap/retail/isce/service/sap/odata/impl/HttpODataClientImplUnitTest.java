/*****************************************************************************
 Class:        HttpODataClientImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.impl.DefaultHTTPDestination;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.batch.BatchException;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart;
import org.apache.olingo.odata2.api.client.batch.BatchQueryPart.BatchQueryPartBuilder;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.FeedMetadata;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.core.ODataResponseImpl;
import org.apache.olingo.odata2.core.edm.provider.EdmEntityContainerImplProv;
import org.apache.olingo.odata2.core.edm.provider.EdmImplProv;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

import com.sap.retail.isce.sap.exception.SAPISCERuntimeException;
import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataClientImpl.ConnectionWithTimeStamp;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;


@UnitTest
public class HttpODataClientImplUnitTest
{
	static final String DATA_CONTAINER_NAME1 = "dataContainerName1";
	static final String DATA_CONTAINER_NAME2 = "dataContainerName2";
	static final String RESULT_ENTITY_NAME1 = "resultEntityName1";
	static final String RESULT_ENTITY_NAME2 = "resultEntityName2";
	static final String SERVICE_URI = "/sap/opu/odata/sap/TEST_SERVICE_NAME";
	static final String DESTINATION_NAME = "TEST_HTTP_DESTINATION_NAME";

	static final String HEADER_COOKIE_SAP_SESSION = "1n8z7e92lEcOz4ggsHfxHmq49urotLkpM%3d";
	static final String HEADER_COOKIE_MYSAPSSO2 = "AjQxFFzrvYZx5BYoQY3amcg2p%21ZfmKcc";
	static final String HEADER_COOKIE_SAP_USER_CONTEXT = "sap-client=123";
	static final String X_CSRF_TOKEN = "x-crsf-token";

	static final String SAP_URL = "http://www.sap.com";
	static final String SAP_URL_WITH_PORT = SAP_URL + ":1234";


	private HttpODataClientImpl classUnderTest;
	private DefaultHTTPDestination destination = null;

	private Edm entityDataModelSpy;
	private Edm entityDataModel;

	private ODataResponse oDataResponseSpy;

	private ConnectionWithTimeStamp conWithStamp;

	private HttpURLConnection connectionSpy;
	private HttpURLConnection connection;

	private String cookieValue;
	private Map headerFieldsMap;

	private int code = 200;

	private boolean throwBatchException;
	private boolean throwHttpODataException;
	private boolean throwMalformedURLException;
	private boolean executeReadMetadata;
	private boolean throwEntityProviderException;
	private boolean executeInitializeConnection;
	private boolean executePrepareConnection;
	private boolean logError;
	private boolean logWarning;
	private boolean logDebug;
	private boolean executeOnce;
	private boolean executeGetCurrentLanguage;
	private boolean executeCreateHttpODataResultInstance;
	private boolean throwApplicationContextException;
	private boolean throwGetBeanException;
	private boolean oDataFeedNull;
	private int nbBatchPart;

	private final OutputStream httpOutputStream = new ByteOutputStream();
	private final InputStream contentResult = new ByteInputStream();

	private final ODataFeed mockODataFeed = new ODataFeed()
	{
		@Override
		public FeedMetadata getFeedMetadata()
		{
			final FeedMetadata fM = new FeedMetadata()
			{

				@Override
				public String getNextLink()
				{
					return null;
				}

				@Override
				public Integer getInlineCount()
				{
					return Integer.valueOf(3);
				}

				@Override
				public String getDeltaLink()
				{
					return null;
				}
			};
			return fM;
		}

		@Override
		public List<ODataEntry> getEntries()
		{
			final Map<String, Object> theMap = new HashMap<>();
			theMap.put("oDataEntryMapKey", null);
			final ODataEntry oDataEntry = new ODataEntryImpl(theMap, null, null, null);
			final List<ODataEntry> entryList = new ArrayList<>();
			entryList.add(oDataEntry);
			return entryList;
		}
	};

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
			logError = true;
		}

		@Override
		public void warn(final Object message)
		{
			logWarning = true;
		}

		@Override
		public void error(final Object message, final Throwable t)
		{
			//
		}

		@Override
		public void debug(final Object message)
		{
			logDebug = true;
		}

		@Override
		public void info(final Object message)
		{
			//
		}
	}

	private class TestApplicationContext implements ApplicationContext
	{

		@Override
		public Environment getEnvironment()
		{
			return null;
		}

		@Override
		public boolean containsBeanDefinition(final String arg0)
		{
			return false;
		}

		@Override
		public <A extends Annotation> A findAnnotationOnBean(final String arg0, final Class<A> arg1)
				throws NoSuchBeanDefinitionException
		{
			return null;
		}

		@Override
		public int getBeanDefinitionCount()
		{
			return 0;
		}

		@Override
		public String[] getBeanDefinitionNames()
		{
			return new String[0];
		}

		@Override
		public String[] getBeanNamesForAnnotation(final Class<? extends Annotation> arg0)
		{
			return new String[0];
		}

		@Override
		public String[] getBeanNamesForType(final Class<?> arg0)
		{
			return new String[0];
		}

		@Override
		public String[] getBeanNamesForType(final Class<?> arg0, final boolean arg1, final boolean arg2)
		{
			return new String[0];
		}

		@Override
		public <T> Map<String, T> getBeansOfType(final Class<T> arg0) throws BeansException
		{
			return null;
		}

		@Override
		public <T> Map<String, T> getBeansOfType(final Class<T> arg0, final boolean arg1, final boolean arg2) throws BeansException
		{
			return null;
		}

		@Override
		public Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> arg0) throws BeansException
		{
			return null;
		}

		@Override
		public boolean containsBean(final String arg0)
		{
			return false;
		}

		@Override
		public String[] getAliases(final String arg0)
		{
			return new String[0];
		}

		@Override
		public Object getBean(final String arg0) throws BeansException
		{
			if (throwGetBeanException)
			{
				throw new BeansException(arg0)
				{
					//
				};

			}
			return new HttpODataResultManipulator()
			{

				@Override
				public void setStatusCode(final StatusCode statusCode)
				{
					//
				}

				@Override
				public void setMetaData(final Edm metaData)
				{
					//
				}

				@Override
				public void setMessages(final List<BackendMessage> messages)
				{
					//
				}

				@Override
				public void setMediaContent(final byte[] content)
				{
					//
				}

				@Override
				public void setHttpStatusCode(final HttpStatusCodes httpStatusCode)
				{
					//
				}

				@Override
				public void setHttpHeaders(final Map<String, List<String>> httpHeaders)
				{
					//
				}

				@Override
				public void setEntities(final List<ODataEntry> entities)
				{
					//
				}

				@Override
				public void setCount(final Integer count)
				{
					//
				}

				@Override
				public void setResponseBody(final String body)
				{
					//
				}

				@Override
				public void setEntity(final ODataEntry entity)
				{
					//
				}
			};
		}

		@Override
		public <T> T getBean(final Class<T> arg0) throws BeansException
		{
			return null;
		}

		@Override
		public <T> T getBean(final String arg0, final Class<T> arg1) throws BeansException
		{
			return null;
		}

		@Override
		public Object getBean(final String arg0, final Object... arg1) throws BeansException
		{
			return null;
		}

		@Override
		public <T> T getBean(final Class<T> arg0, final Object... arg1) throws BeansException
		{
			return null;
		}

		@Override
		public Class<?> getType(final String arg0) throws NoSuchBeanDefinitionException
		{
			return null;
		}

		@Override
		public boolean isPrototype(final String arg0) throws NoSuchBeanDefinitionException
		{
			return false;
		}

		@Override
		public boolean isSingleton(final String arg0) throws NoSuchBeanDefinitionException
		{
			return false;
		}

		@Override
		public boolean isTypeMatch(final String arg0, final Class<?> arg1) throws NoSuchBeanDefinitionException
		{
			return false;
		}

		@Override
		public boolean containsLocalBean(final String arg0)
		{
			return false;
		}

		@Override
		public BeanFactory getParentBeanFactory()
		{
			return null;
		}

		@Override
		public String getMessage(final MessageSourceResolvable arg0, final Locale arg1) throws NoSuchMessageException
		{
			return null;
		}

		@Override
		public String getMessage(final String arg0, final Object[] arg1, final Locale arg2) throws NoSuchMessageException
		{
			return null;
		}

		@Override
		public String getMessage(final String arg0, final Object[] arg1, final String arg2, final Locale arg3)
		{
			return null;
		}

		@Override
		public void publishEvent(final ApplicationEvent arg0)
		{
			//
		}

		@Override
		public Resource[] getResources(final String arg0) throws IOException
		{
			return new Resource[0];
		}

		@Override
		public ClassLoader getClassLoader()
		{
			return null;
		}

		@Override
		public Resource getResource(final String arg0)
		{
			return null;
		}

		@Override
		public String getApplicationName()
		{
			return null;
		}

		@Override
		public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException
		{
			return null;
		}

		@Override
		public String getDisplayName()
		{
			return null;
		}

		@Override
		public String getId()
		{
			return null;
		}

		@Override
		public ApplicationContext getParent()
		{
			return null;
		}

		@Override
		public long getStartupDate()
		{
			return 0;
		}

		@Override
		public String[] getBeanNamesForType(final ResolvableType typeToMatch)
		{
			return null;
		}

		@Override
		public boolean isTypeMatch(final String name, final ResolvableType typeToMatch)
		{
			return false;
		}

		@Override
		public void publishEvent(final Object event)
		{
			//
		}

	}

	private class TestHttpODataClientImpl extends HttpODataClientImpl
	{
		@Override
		protected HttpODataResultManipulator createHttpODataResultInstance()
		{
			if (executeCreateHttpODataResultInstance)
			{
				return super.createHttpODataResultInstance();
			}
			return new HttpODataResultImpl();
		}

		@Override
		protected ApplicationContext getRegistryApplicationContext()
		{
			if (throwApplicationContextException)
			{
				return null;
			}
			return new TestApplicationContext();
		}

		@Override
		protected String getCurrentLanguage()
		{
			if (executeGetCurrentLanguage)
			{
				return super.getCurrentLanguage();
			}
			return "en";
		}

		public TestHttpODataClientImpl()
		{
			super();
			log = new LoggerTest("");
		}

		@Override
		protected ODataFeed entityProviderReadFeed(final String entitySetName, final EdmEntityContainer entityContainer,
				final InputStream httpInputStream) throws EntityProviderException, EdmException
		{
			return mockODataFeed;
		}

		@Override
		protected ODataResponse entityProviderWriteEntryForGetMediaFile(final String entitySetName,
				final Map<String, Object> entityKeys, final EdmEntityContainer entityContainer, final URI rootUri)
				throws EntityProviderException, EdmException
		{
			return oDataResponseSpy;
		}

		@Override
		protected ODataResponse entityProviderWriteEntryForPutEntity(final String entitySetName,
				final Map<String, Object> entityData, final EdmEntityContainer entityContainer, final URI rootUri)
				throws EntityProviderException, EdmException
		{
			return oDataResponseSpy;
		}

		@Override
		protected Edm readMetadata(final ConnectionWithTimeStamp conWithStamp, final String destinationName,
				final String serviceURI) throws HttpODataException
		{
			if (executeReadMetadata)
			{
				return super.readMetadata(conWithStamp, destinationName, serviceURI);
			}
			return entityDataModelSpy;
		}

		@Override
		protected Edm entityProviderReadMetadata(final InputStream httpInputStream) throws EntityProviderException
		{
			if (throwEntityProviderException)
			{
				throw new EntityProviderException(null);
			}
			return entityDataModelSpy;
		}

		@Override
		protected ConnectionWithTimeStamp initializeConnection(final URI absoluteUri, final String contentType,
				final HttpMethod httpMethod, final Map<String, Object> destination, final HTTPDestination httpDestination,
				final String serviceUri) throws MalformedURLException, IOException
		{
			if (throwMalformedURLException)
			{
				throw new MalformedURLException();
			}
			if (executeInitializeConnection)
			{
				return super.initializeConnection(absoluteUri, contentType, httpMethod, destination, httpDestination, serviceUri);
			}
			conWithStamp.setConnection(connectionSpy);
			conWithStamp.setConnectTimeStamp(System.currentTimeMillis());
			return conWithStamp;
		}

		@Override
		protected ConnectionWithTimeStamp initializeConnectionIntern(final URI absoluteUri, final String contentType,
				final HttpMethod httpMethod, final Map<String, Object> destination, final HTTPDestination httpDestination,
				final String serviceName) throws MalformedURLException, IOException
		{
			if (throwMalformedURLException)
			{
				throw new MalformedURLException();
			}
			if (executeInitializeConnection)
			{
				return super.initializeConnectionIntern(absoluteUri, contentType, httpMethod, destination, httpDestination,
						serviceName);
			}
			conWithStamp.setConnection(connectionSpy);
			conWithStamp.setConnectTimeStamp(System.currentTimeMillis());
			return conWithStamp;
		}

		@Override
		protected List<BatchSingleResponse> getBatchSingleResponses(final InputStream contentResult,
				final String httpResponseContentType) throws BatchException
		{
			if (throwBatchException)
			{
				throw new BatchException(null);
			}
			final BatchSingleResponse bsr = new BatchSingleResponse()
			{
				@Override
				public String getStatusInfo()
				{
					return null;
				}

				@Override
				public String getStatusCode()
				{
					switch (code)
					{
						case 200:
							code = 403;
							return "200";
						case 403:
							code = 200;
							return "403";
						case 2000: // to prevent endless loop
							code = 2000;
							return "200";
						default:
							code = 200;
							return "200";
					}
				}

				@Override
				public Map<String, String> getHeaders()
				{
					return null;
				}

				@Override
				public Set<String> getHeaderNames()
				{
					return null;
				}

				@Override
				public String getHeader(final String arg0)
				{
					return "ContentType";
				}

				@Override
				public String getContentId()
				{
					return null;
				}

				@Override
				public String getBody()
				{
					return "This is the Body";
				}
			};

			final List<BatchSingleResponse> batchResp = new ArrayList<>();

			batchResp.add(bsr);
			if (nbBatchPart == 2)
			{
				batchResp.add(bsr);
			}
			return batchResp;
		}

		@Override
		protected HttpURLConnection prepareConnection(final String contentType, final HttpMethod httpMethod, final URI absoluteUri,
				final Map<String, Object> destination, final HTTPDestination httpDestination, final boolean fetchToken,
				final String serviceUri) throws MalformedURLException, IOException
		{
			if (executePrepareConnection)
			{
				return super.prepareConnection(contentType, httpMethod, absoluteUri, destination, httpDestination, fetchToken,
						serviceUri);
			}
			return new HttpURLConnection(new URL("http://www.sap.com"))
			{

				@Override
				public void connect() throws IOException
				{
					//
				}

				@Override
				public boolean usingProxy()
				{
					return false;
				}

				@Override
				public void disconnect()
				{
					//
				}
			};

		}

		@Override
		protected boolean checkSupportedHttpErrorCodes(final HttpODataException oEx)
		{
			code = 2000;
			if (!throwHttpODataException || executeOnce)
			{
				if (executeOnce)
				{
					try
					{
						Mockito.doReturn(Integer.valueOf(200)).when(connectionSpy).getResponseCode();
					}
					catch (final IOException e)
					{
						assertTrue("Execution went bad", false);
					}
					executeOnce = false;
				}
				return super.checkSupportedHttpErrorCodes(oEx);
			}
			else
			{
				return false;
			}
		}

		@Override
		protected ODataFeed getODataFeed(final EdmEntityContainer entityContainer, final InputStream responseAsInputStream,
				final String headerContentType, final String entityName) throws EntityProviderException, EdmException
		{
			if (oDataFeedNull)
			{
				return null;
			}
			return mockODataFeed;
		}
	}

	private final Node messageNode = new Node()
	{

		@Override
		public Object setUserData(final String key, final Object data, final UserDataHandler handler)
		{
			return null;
		}

		@Override
		public void setTextContent(final String textContent) throws DOMException
		{
			//
		}

		@Override
		public void setPrefix(final String prefix) throws DOMException
		{
			//
		}

		@Override
		public void setNodeValue(final String nodeValue) throws DOMException
		{
			//
		}

		@Override
		public Node replaceChild(final Node newChild, final Node oldChild) throws DOMException
		{
			return null;
		}

		@Override
		public Node removeChild(final Node oldChild) throws DOMException
		{
			return null;
		}

		@Override
		public void normalize()
		{
			//
		}

		@Override
		public String lookupPrefix(final String namespaceURI)
		{
			return null;
		}

		@Override
		public String lookupNamespaceURI(final String prefix)
		{
			return null;
		}

		@Override
		public boolean isSupported(final String feature, final String version)
		{
			return false;
		}

		@Override
		public boolean isSameNode(final Node other)
		{
			return false;
		}

		@Override
		public boolean isEqualNode(final Node arg)
		{
			return false;
		}

		@Override
		public boolean isDefaultNamespace(final String namespaceURI)
		{
			return false;
		}

		@Override
		public Node insertBefore(final Node newChild, final Node refChild) throws DOMException
		{
			return null;
		}

		@Override
		public boolean hasChildNodes()
		{
			return false;
		}

		@Override
		public boolean hasAttributes()
		{
			return false;
		}

		@Override
		public Object getUserData(final String key)
		{
			return null;
		}

		@Override
		public String getTextContent() throws DOMException
		{
			return "messageClass/123";
		}

		@Override
		public Node getPreviousSibling()
		{
			return null;
		}

		@Override
		public String getPrefix()
		{
			return null;
		}

		@Override
		public Node getParentNode()
		{
			return null;
		}

		@Override
		public Document getOwnerDocument()
		{
			return null;
		}

		@Override
		public String getNodeValue() throws DOMException
		{
			return null;
		}

		@Override
		public short getNodeType()
		{
			return 0;
		}

		@Override
		public String getNodeName()
		{
			return "code";
		}

		@Override
		public Node getNextSibling()
		{
			return null;
		}

		@Override
		public String getNamespaceURI()
		{
			return null;
		}

		@Override
		public String getLocalName()
		{
			return null;
		}

		@Override
		public Node getLastChild()
		{
			return null;
		}

		@Override
		public Node getFirstChild()
		{
			return null;
		}

		@Override
		public Object getFeature(final String feature, final String version)
		{
			return null;
		}

		@Override
		public NodeList getChildNodes()
		{
			final NodeList nodeList = new NodeList()
			{

				@Override
				public Node item(final int index)
				{
					return messageNode;
				}

				@Override
				public int getLength()
				{
					return 1;
				}
			};
			return nodeList;
		}

		@Override
		public String getBaseURI()
		{
			return null;
		}

		@Override
		public NamedNodeMap getAttributes()
		{
			return null;
		}

		@Override
		public short compareDocumentPosition(final Node other) throws DOMException
		{
			return 0;
		}

		@Override
		public Node cloneNode(final boolean deep)
		{
			return null;
		}

		@Override
		public Node appendChild(final Node newChild) throws DOMException
		{
			return null;
		}
	};

	//
	// Tests
	//

	@Before
	public void setUp()
	{
		entityDataModel = new EdmImplProv(null);
		entityDataModelSpy = Mockito.spy(entityDataModel);

		conWithStamp = new ConnectionWithTimeStamp();

		final ODataResponse oDataResponse = new ODataResponseImpl();
		oDataResponseSpy = Mockito.spy(oDataResponse);

		classUnderTest = new TestHttpODataClientImpl();

		destination = new DefaultHTTPDestination();
		destination.setHttpDestinationName(DESTINATION_NAME);
		destination.setTargetURL("http://www.sap.com/");

		final List<String> headerCookies = new ArrayList<String>();
		headerCookies.add("SAP_SESSIONID_ABC_123=" + HEADER_COOKIE_SAP_SESSION + ";");
		headerCookies.add("MYSAPSSO2=" + HEADER_COOKIE_MYSAPSSO2 + ";");
		headerCookies.add("sap-usercontext=" + HEADER_COOKIE_SAP_USER_CONTEXT + ";");
		cookieValue = "{MYSAPSSO2=" + HEADER_COOKIE_MYSAPSSO2 + ", SAP_SESSIONID_ABC_123=" + HEADER_COOKIE_SAP_SESSION
				+ ", sap-usercontext=" + HEADER_COOKIE_SAP_USER_CONTEXT + "}";

		headerFieldsMap = new HashMap();
		headerFieldsMap.put(HttpODataClientImpl.HTTP_HEADER_SET_COOKIE, headerCookies);

		classUnderTest.setHttpODataCommonStorage(new HttpODataCommonStorageImpl());

	}

	@Test
	public void testExecuteBatchCallhappyPath()
	{

		final BatchQueryPartBuilder bqpBuilder = BatchQueryPart.uri("localhost:8080").method("GET");
		final BatchQueryPart bp = bqpBuilder.build();

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		final DataContainerBatchPart dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME1);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		this.nbBatchPart = 1;

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();


		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			final Map<String, HttpODataResult> batchResults = classUnderTest.executeBatchCall(dcBatchParts, destination,
					SERVICE_URI);

			Mockito.verify(connectionSpy, Mockito.times(1)).getOutputStream();
			Mockito.verify(connectionSpy, Mockito.times(8)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(4)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();

			assertNotNull("batchResults object shall not be null", batchResults);
			assertEquals("Number of batchResults entries should be 1", 1, batchResults.size());
			assertTrue("batchResults should contain Key DATA_CONTAINER_NAME1", batchResults.containsKey(DATA_CONTAINER_NAME1));
			final HttpODataResult batchResult = batchResults.get(DATA_CONTAINER_NAME1);
			assertNotNull("batchResult shall not be null", batchResult);
			assertEquals("Number of entries should be 1", 1, batchResult.getEntities().size());
			for (final ODataEntry oDataEntry : batchResult.getEntities())
			{
				final Map<String, Object> theMap = oDataEntry.getProperties();
				assertNotNull("Property object shall not be null", theMap);
				assertNull("Property with name property1 should not be found in map", theMap.get("property1"));
			}
		}
		catch (final IOException | HttpODataException | EdmException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testExecuteBatchCallwith404Codesimulation()
	{
		final BatchQueryPartBuilder bqpBuilder = BatchQueryPart.uri("localhost:8080").method("GET");
		final BatchQueryPart bp = bqpBuilder.build();

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		DataContainerBatchPart dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME1);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME1);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME2);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME2);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		this.nbBatchPart = 2;

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			classUnderTest.executeBatchCall(dcBatchParts, destination, SERVICE_URI);
		}
		catch (final HttpODataException | EdmException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(1)).getOutputStream();
				Mockito.verify(connectionSpy, Mockito.times(7)).getInputStream();
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			}
			catch (final EdmException | IOException e1)
			{
				assertTrue("Execution went bad", false);
			}
			assertTrue("Execution went well", true);
		}
	}

	@Test
	public void testExecuteBatchCallException()
	{
		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();

		mockConnectionSpy();
		try
		{
			Mockito.doThrow(new IOException()).when(connectionSpy).getOutputStream();
			classUnderTest.executeBatchCall(dcBatchParts, destination, SERVICE_URI);
		}
		catch (final HttpODataException | IOException e)
		{
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			assertTrue("Execution went well", true);
		}
	}

	@Test
	public void testExecuteBatchCallHttpODataException()
	{
		final BatchQueryPartBuilder bqpBuilder = BatchQueryPart.uri("localhost:8080").method("GET");
		final BatchQueryPart bp = bqpBuilder.build();

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		DataContainerBatchPart dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME1);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME1);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME2);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME2);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		this.nbBatchPart = 2;

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			Mockito.doReturn(Integer.valueOf(401)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();

			this.throwHttpODataException = false;
			this.throwBatchException = true;
			this.executeOnce = true;
			classUnderTest.executeBatchCall(dcBatchParts, destination, SERVICE_URI);
		}
		catch (final HttpODataException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(2)).getOutputStream();
				Mockito.verify(connectionSpy, Mockito.times(2)).getInputStream();
				Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
				Mockito.verify(connectionSpy, Mockito.times(5)).getHeaderFields();

				Mockito.verify(connectionSpy, Mockito.times(7)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getErrorStream();
			}
			catch (final IOException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		catch (final EdmException | IOException e1)
		{
			assertTrue("Execution went bad", false);
		}
		this.throwHttpODataException = false;
		this.throwBatchException = false;
		this.executeOnce = false;

		assertTrue("Execution went well", true);
	}

	@Test
	public void testExecuteBatchCallBatch1OKBatch2ERROR()
	{
		final BatchQueryPartBuilder bqpBuilder = BatchQueryPart.uri("localhost:8080").method("GET");
		final BatchQueryPart bp = bqpBuilder.build();

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		DataContainerBatchPart dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME1);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME1);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		dcBatchPart = new DataContainerBatchPart();
		dcBatchPart.setDataContainerName(DATA_CONTAINER_NAME2);
		dcBatchPart.setResultName(RESULT_ENTITY_NAME2);
		dcBatchPart.setBatchPart(bp);
		dcBatchParts.add(dcBatchPart);

		this.nbBatchPart = 2;

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			classUnderTest.executeBatchCall(dcBatchParts, destination, SERVICE_URI);

			this.throwHttpODataException = false;
			Mockito.verify(connectionSpy, Mockito.times(1)).getOutputStream();
			Mockito.verify(connectionSpy, Mockito.times(8)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(4)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
		}
		catch (final EdmException | IOException | HttpODataException e1)
		{
			assertTrue("Execution went bad", false);
		}
		assertTrue("Execution went well", true);
	}

	@Test
	public void testSetI18NService()
	{
		final CommonI18NService commonI18NService = null;
		classUnderTest.setCommonI18NService(commonI18NService);
		assertEquals("i18NService not identical", commonI18NService, classUnderTest.commonI18NService);
	}

	@Test
	public void testGetCurrentLanguagewithoutLocale()
	{
		executeGetCurrentLanguage = true;
		classUnderTest.setCommonI18NService(null);
		assertNull("getCurrentLanguage not null", classUnderTest.getCurrentLanguage());
		executeGetCurrentLanguage = false;
	}


	@Test
	public void testGetInitializedDestinationHappyPath()
	{
		mockConnectionSpy();
		try
		{
			final Map<String, Object> dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);

			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);
		}
		catch (final HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetInitializedDestinationHappyPathRetrieveCachedDestination()
	{
		mockConnectionSpy();
		try
		{
			Map<String, Object> dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);

			dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);
		}
		catch (final HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetInitializedDestinationException()
	{
		try
		{
			throwMalformedURLException = true;
			classUnderTest.getInitializedDestination(destination, SERVICE_URI);
			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException e)
		{
			throwMalformedURLException = false;
			assertTrue("Execution went well", true);
		}
	}

	@Test
	public void testGetInitializedDestinationHappyPathNewService()
	{
		mockConnectionSpy();
		try
		{
			Map<String, Object> dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);

			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);

			dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI + "_NEW");

			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderFields();
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI + "_NEW", 2, X_CSRF_TOKEN);

		}
		catch (final HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testCreateDestinationFromParamsHappyPath()
	{
		mockConnectionSpy();
		final Map<String, Object> createDestinationFromParams = classUnderTest.createDestinationFromParams(SERVICE_URI,
				entityDataModelSpy, connectionSpy);

		Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
		Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
		testCookieTokenAndServiceMetadata(createDestinationFromParams, SERVICE_URI, 1, X_CSRF_TOKEN);
	}

	@Test
	public void testCreateNewServiceInDestinationFromParams()
	{
		mockConnectionSpy();
		Map<String, Object> oldDestination = null;
		oldDestination = classUnderTest.createDestinationFromParams(SERVICE_URI, entityDataModelSpy, connectionSpy);
		testCookieTokenAndServiceMetadata(oldDestination, SERVICE_URI, 1, X_CSRF_TOKEN);

		final Map<String, Object> newDest = classUnderTest.createNewServiceInDestinationFromParams(SERVICE_URI + "_NEW",
				entityDataModelSpy, oldDestination, connectionSpy);

		Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
		Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderFields();
		testCookieTokenAndServiceMetadata(newDest, SERVICE_URI + "_NEW", 2, X_CSRF_TOKEN);
	}

	@Test
	public void testCreateNewServiceInDestinationFromParamswithNullToken()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			connection = (HttpURLConnection) rootBatchUri.toURL().openConnection();
			connectionSpy = Mockito.spy(connection);
			Mockito.doReturn(null).when(connectionSpy).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.doReturn(headerFieldsMap).when(connectionSpy).getHeaderFields();

			final Map<String, Object> oldDestination = classUnderTest.createDestinationFromParams(SERVICE_URI, entityDataModelSpy,
					connectionSpy);
			testCookieTokenAndServiceMetadata(oldDestination, SERVICE_URI, 1, null);

			Mockito.doReturn("123456").when(connectionSpy).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			final Map<String, Object> newDest = classUnderTest.createNewServiceInDestinationFromParams(SERVICE_URI + "_NEW",
					entityDataModelSpy, oldDestination, connectionSpy);

			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderFields();
			testCookieTokenAndServiceMetadata(newDest, SERVICE_URI + "_NEW", 2, "123456");


		}
		catch (final URISyntaxException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetEDMHappyPath()
	{
		mockConnectionSpy();
		try
		{
			final Map<String, Object> dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);

			final Edm edm = classUnderTest.getEDM(dest, SERVICE_URI);

			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			assertNotNull("edm shall not be null", edm);
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);
		}
		catch (final HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetEDMException()
	{
		try
		{
			final Map<String, Object> dest = new HashMap<>();
			classUnderTest.getEDM(dest, SERVICE_URI);
			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException e)
		{
			assertTrue("Execution went well", true);
		}
	}

	@Test
	public void testReadMetadataHappyPath()
	{
		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);
		executeReadMetadata = true;
		try
		{
			final Edm readMetadata = classUnderTest.readMetadata(initializeConnection, DESTINATION_NAME, SERVICE_URI);

			Mockito.verify(connectionSpy, Mockito.times(1)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(1)).getInputStream();
			assertEquals("metadata is not equals", entityDataModelSpy, readMetadata);
		}
		catch (HttpODataException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeReadMetadata = false;
	}

	@Test
	public void testReadMetadataException()
	{
		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);
		executeReadMetadata = true;
		throwEntityProviderException = true;
		try
		{
			classUnderTest.readMetadata(initializeConnection, DESTINATION_NAME, SERVICE_URI);

			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(1)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getInputStream();
			}
			catch (final IOException e1)
			{
				assertTrue("Execution went bad", false);
			}
			assertTrue("Execution went well", true);
		}
		throwEntityProviderException = false;
		executeReadMetadata = false;
	}

	@Test
	public void testExecuteAndHandleResponseHappyPath()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		final DataContainerBatchPart dcBachPart = new DataContainerBatchPart();
		dcBachPart.setDataContainerName("dataContainerName1");
		dcBachPart.setResultName("resultEntityName1");
		dcBatchParts.add(dcBachPart);

		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			final Map<String, HttpODataResult> httpResults = classUnderTest.executeAndHandleResponse(dcBatchParts,
					initializeConnection, entityDataModelSpy, DESTINATION_NAME);

			Mockito.verify(connectionSpy, Mockito.times(5)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();

			assertNotNull("Results shall not be null", httpResults);
			assertEquals("Number of entries should be 1", 1, httpResults.size());

			assertTrue("Results shall contains entry for container with name DATA_CONTAINER_NAME1",
					httpResults.containsKey(DATA_CONTAINER_NAME1));
			final HttpODataResult httpODataResult = httpResults.get(DATA_CONTAINER_NAME1);
			assertNotNull("httpODataResult shall not be null", httpODataResult);
			assertEquals("Number of entries should be 1", 1, httpODataResult.getEntities().size());
			for (final ODataEntry oDataEntry : httpODataResult.getEntities())
			{
				final Map<String, Object> theMap = oDataEntry.getProperties();
				assertNotNull("Property object shall not be null", theMap);
				assertNull("Property with name property1 shall be null", theMap.get("property1"));
			}
			assertEquals("Number of entries should be 3", Integer.valueOf(3), httpODataResult.getCount());
			assertEquals("StatusCode should be OK", StatusCode.OK, httpODataResult.getStatusCode());
		}
		catch (EdmException | IOException | HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testExecuteAndHandleResponseStatusCodeERROR()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);

		final List<DataContainerBatchPart> dcBatchParts = new ArrayList();
		final DataContainerBatchPart dcBachPart = new DataContainerBatchPart();
		dcBachPart.setDataContainerName("dataContainerName1");
		dcBachPart.setResultName("resultEntityName1");
		dcBatchParts.add(dcBachPart);

		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();

			oDataFeedNull = true;

			final Map<String, HttpODataResult> httpResults = classUnderTest.executeAndHandleResponse(dcBatchParts,
					initializeConnection, entityDataModelSpy, DESTINATION_NAME);

			Mockito.verify(connectionSpy, Mockito.times(5)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();

			assertNotNull("Results shall not be null", httpResults);
			assertEquals("Number of entries should be 1", 1, httpResults.size());

			assertTrue("Results shall contains entry for container with name DATA_CONTAINER_NAME1",
					httpResults.containsKey(DATA_CONTAINER_NAME1));
			final HttpODataResult httpODataResult = httpResults.get(DATA_CONTAINER_NAME1);
			assertNotNull("httpODataResult shall not be null", httpODataResult);
			assertEquals("StatusCode should be OK", StatusCode.ERROR, httpODataResult.getStatusCode());
		}
		catch (EdmException | IOException | HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
		oDataFeedNull = false;
	}

	@Test
	public void testExecuteAndHandleResponseException()
	{
		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);
		throwBatchException = true;
		try
		{
			classUnderTest.executeAndHandleResponse(null, initializeConnection, entityDataModelSpy, DESTINATION_NAME);
			assertTrue("Execution went bad", false);
		}
		catch (EdmException | IOException | HttpODataException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(5)).getInputStream();
				Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
			}
			catch (final IOException e1)
			{
				assertTrue("Execution went bad", false);
			}
			assertTrue("Execution went well", true);
		}
		throwBatchException = false;
	}

	@Test
	public void testCheckSupportedHttpErrorCodes()
	{
		assertTrue("401 was not detected", classUnderTest.checkSupportedHttpErrorCodes(new HttpODataException("", 401)));
		assertTrue("403 was not detected", classUnderTest.checkSupportedHttpErrorCodes(new HttpODataException("", 403)));
	}

	@Test
	public void testPrepareResult()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			connection = (HttpURLConnection) rootBatchUri.toURL().openConnection();
			connectionSpy = Mockito.spy(connection);
			Mockito.doReturn(headerFieldsMap).when(connectionSpy).getHeaderFields();
			Mockito.doReturn(Integer.valueOf(200)).when(connectionSpy).getResponseCode();

			final HttpODataResultManipulator resultManipulator = classUnderTest.prepareResult(connectionSpy, entityDataModelSpy);
			final HttpODataResult httpODataResult = (HttpODataResult) resultManipulator;

			Mockito.verify(connectionSpy, Mockito.times(3)).getHeaderFields();
			Mockito.verify(connectionSpy, Mockito.times(2)).getResponseCode();
			assertNotNull("result shall not be null", resultManipulator);
			assertEquals("Metadata are not identical", entityDataModelSpy, httpODataResult.getMetaData());
			assertEquals("HeaderFields are not identical", headerFieldsMap, httpODataResult.getHttpHeaders());
			assertEquals("HttpStatusCodes are not identical", HttpStatusCodes.OK, httpODataResult.getHttpStatusCode());
			final List resultMessage = new ArrayList();
			assertEquals("Messages are not identical", resultMessage, httpODataResult.getMessages());
		}
		catch (IOException | URISyntaxException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testInitializeConnection()
	{
		try
		{
			executeInitializeConnection = true;
			final ConnectionWithTimeStamp initializeConnection = classUnderTest.initializeConnection(new URI(""), "contentType",
					HttpMethod.GET, null, destination, SERVICE_URI);
			assertNotNull("connection should not be null", initializeConnection);
			assertNotNull("initializeConnection.connection should not be null", initializeConnection.getConnection());
			assertEquals("Url not found", new URL("http://www.sap.com"), initializeConnection.getConnection().getURL());
		}
		catch (final IOException | URISyntaxException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeInitializeConnection = false;
	}

	@Test
	public void testInitializeConnectionIntern()
	{
		try
		{
			executeInitializeConnection = true;
			final ConnectionWithTimeStamp initializeConnection = classUnderTest.initializeConnectionIntern(new URI(""),
					"contentType", HttpMethod.GET, null, destination, SERVICE_URI);
			assertNotNull("connection should not be null", initializeConnection);
			assertNotNull("initializeConnection.connection should not be null", initializeConnection.getConnection());
			assertEquals("Url not found", new URL("http://www.sap.com"), initializeConnection.getConnection().getURL());
		}
		catch (final IOException | URISyntaxException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeInitializeConnection = false;
	}

	@Test
	public void testPrepareConnectionLogonRequired()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			executePrepareConnection = true;
			final HttpURLConnection preparedConnection = classUnderTest.prepareConnection("contentType", HttpMethod.POST,
					rootBatchUri, null, destination, false, SERVICE_URI);
			assertEquals("Fetch token parameter not found", HttpODataClientImpl.X_CSRF_TOKEN_FETCH,
					preparedConnection.getRequestProperty(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN));
			assertEquals("POST Method parameter not found", HttpMethod.POST.toString(), preparedConnection.getRequestMethod());
			assertEquals("Content Type parameter not found", "contentType",
					preparedConnection.getRequestProperty(HttpHeaders.ACCEPT));
			assertTrue("Content Type parameter not found",
					preparedConnection.getRequestProperty(HttpHeaders.CONTENT_TYPE).contains("contentType"));
			assertTrue("charset parameter not found",
					preparedConnection.getRequestProperty(HttpHeaders.CONTENT_TYPE).contains("charset"));
			assertNull("Authorization Type parameter not found", preparedConnection.getRequestProperty(HttpHeaders.AUTHORIZATION));

		}
		catch (IOException | URISyntaxException e)
		{
			assertTrue("Execution went bad", false);
		}
		executePrepareConnection = false;
	}

	@Test
	public void testPrepareConnectionCookie()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			connection = (HttpURLConnection) rootBatchUri.toURL().openConnection();
			connectionSpy = Mockito.spy(connection);
			Mockito.doReturn(X_CSRF_TOKEN).when(connectionSpy).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.doReturn(headerFieldsMap).when(connectionSpy).getHeaderFields();

			final Map<String, Object> dest = classUnderTest.getInitializedDestination(destination, SERVICE_URI);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
			testCookieTokenAndServiceMetadata(dest, SERVICE_URI, 1, X_CSRF_TOKEN);
			executePrepareConnection = true;

			final HttpURLConnection preparedConnection = classUnderTest.prepareConnection("contentType", HttpMethod.POST,
					rootBatchUri, dest, destination, false, SERVICE_URI);

			assertEquals("X_CSRF_Token token parameter not found", X_CSRF_TOKEN,
					preparedConnection.getRequestProperty(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN));
			assertEquals("POST Method parameter not found", HttpMethod.POST.toString(), preparedConnection.getRequestMethod());
			assertEquals("Content Type parameter not found", "contentType",
					preparedConnection.getRequestProperty(HttpHeaders.ACCEPT));
			assertTrue("Content Type parameter not found",
					preparedConnection.getRequestProperty(HttpHeaders.CONTENT_TYPE).contains("contentType"));
			assertTrue("charset parameter not found",
					preparedConnection.getRequestProperty(HttpHeaders.CONTENT_TYPE).contains("charset"));
			assertNull("Authorization Type parameter not found", preparedConnection.getRequestProperty(HttpHeaders.AUTHORIZATION));
			assertEquals("Header cookie parameter not found",
					"MYSAPSSO2=AjQxFFzrvYZx5BYoQY3amcg2p%21ZfmKcc; SAP_SESSIONID_ABC_123=1n8z7e92lEcOz4ggsHfxHmq49urotLkpM%3d; sap-usercontext=sap-client=123",
					preparedConnection.getRequestProperty(HttpODataClientImpl.HTTP_HEADER_COOKIE));
		}
		catch (IOException | URISyntaxException | HttpODataException e)
		{
			assertTrue("Execution went bad", false);
		}
		executePrepareConnection = false;
	}

	@Test
	public void testCheckStatusException()
	{
		prepareConnectionSpy();
		try
		{
			Mockito.doReturn(Integer.valueOf(400)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			Mockito.doReturn("A message").when(connectionSpy).getResponseMessage();
			classUnderTest.checkStatus(connectionSpy, DESTINATION_NAME);
			assertTrue("Execution went bad", false);
		}
		catch (HttpODataException | IOException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
				Mockito.verify(connectionSpy, Mockito.times(1)).getErrorStream();
			}
			catch (final IOException e1)
			{
				assertTrue("Execution went bad", false);
			}
			assertTrue("Execution went well", true);
		}
	}

	@Test
	public void testIsHttpError()
	{
		prepareConnectionSpy();
		try
		{
			Mockito.doReturn(Integer.valueOf(400)).when(connectionSpy).getResponseCode();
			final boolean httpError = classUnderTest.isHttpError(connectionSpy);
			Mockito.verify(connectionSpy, Mockito.times(1)).getResponseCode();
			assertTrue("isHttpError didn't succeed", httpError);
		}
		catch (final IOException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetFullQualifiedServiceURISuffixHttpDestinationServiceName()
	{
		try
		{
			URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			assertEquals("URI was not constructed properly", "http://www.sap.com/sap/opu/odata/sap/TEST_SERVICE_NAME/$batch",
					rootBatchUri.toString());
			rootBatchUri = classUnderTest.getFullQualifiedServiceURI("../$batch", destination, SERVICE_URI);
			assertEquals("URI was not constructed properly", "http://www.sap.com/sap/opu/odata/sap/$batch", rootBatchUri.toString());
		}
		catch (final URISyntaxException | MalformedURLException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetFullQualifiedServiceURISuffixFilterHttpDestinationServiceName()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", "filterCriteria", destination,
					SERVICE_URI);
			assertEquals("URI was not constructed properly",
					"http://www.sap.com/sap/opu/odata/sap/TEST_SERVICE_NAME/$batch/?$filter=filterCriteria", rootBatchUri.toString());
		}
		catch (final URISyntaxException | MalformedURLException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testGetFullQualifiedServiceURIHttpDestinationServiceName()
	{
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI(destination, SERVICE_URI);
			assertEquals("URI was not constructed properly", "http://www.sap.com/sap/opu/odata/sap/TEST_SERVICE_NAME",
					rootBatchUri.toString());
		}
		catch (final URISyntaxException | MalformedURLException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testAdaptTargetUrl()
	{
		try
		{
			assertEquals("adaptTargetUrl was not constructed properly", SAP_URL,
					classUnderTest.adaptTargetUrl(SAP_URL + "/thisMustGoAway"));
			assertEquals("adaptTargetUrl was not constructed properly", SAP_URL_WITH_PORT,
					classUnderTest.adaptTargetUrl(SAP_URL_WITH_PORT + "/thisMustGoAway"));
			assertEquals("adaptTargetUrl was not constructed properly", SAP_URL_WITH_PORT,
					classUnderTest.adaptTargetUrl(SAP_URL_WITH_PORT));
		}
		catch (final MalformedURLException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testAdaptTargetUrlException()
	{
		try
		{
			classUnderTest.adaptTargetUrl("ht   tp://sap.com:1234");
			assertTrue("Execution went bad", false);

		}
		catch (final MalformedURLException e)
		{
			assertTrue("Execution went well", true);
		}
	}



	@Test
	public void testAppendURLFragmentHappyPath()
	{
		// 1
		StringBuilder urlBuilder = new StringBuilder("");
		String fragment = "";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "", urlBuilder.toString());

		urlBuilder = new StringBuilder("");
		fragment = "end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "end", urlBuilder.toString());

		urlBuilder = new StringBuilder("");
		fragment = "/end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("");
		fragment = "/end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("");
		fragment = "end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "end", urlBuilder.toString());

		// 2
		urlBuilder = new StringBuilder("/");
		fragment = "";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/", urlBuilder.toString());

		urlBuilder = new StringBuilder("/");
		fragment = "end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/");
		fragment = "/end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/");
		fragment = "/end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/");
		fragment = "end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/end", urlBuilder.toString());

		// 3
		urlBuilder = new StringBuilder("/start");
		fragment = "";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start");
		fragment = "/end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start");
		fragment = "/end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start");
		fragment = "end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());

		// 4
		urlBuilder = new StringBuilder("/start/");
		fragment = "";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start/");
		fragment = "/end";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start/");
		fragment = "/end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start/");
		fragment = "end/";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/end", urlBuilder.toString());
	}

	@Test
	public void testAppendURLFragmentNull()
	{
		StringBuilder urlBuilder = new StringBuilder("/start/");
		String fragment = "";
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/", urlBuilder.toString());

		urlBuilder = new StringBuilder("/start/");
		fragment = null;
		classUnderTest.appendURLFragment(urlBuilder, fragment);
		assertEquals("Fragment not added properly", "/start/", urlBuilder.toString());
	}

	@Test
	public void testRemoveTrailingSlash()
	{
		String fragment = "/";
		String correctedFragment = classUnderTest.removeTrailingSlash(fragment);
		assertEquals("Reworking fragment failed", "/", correctedFragment);

		fragment = "x/";
		correctedFragment = classUnderTest.removeTrailingSlash(fragment);
		assertEquals("Reworking fragment failed", "x", correctedFragment);

		fragment = "/start";
		correctedFragment = classUnderTest.removeTrailingSlash(fragment);
		assertEquals("Reworking fragment failed", fragment, correctedFragment);

		fragment = "start//";
		correctedFragment = classUnderTest.removeTrailingSlash(fragment);
		assertEquals("Reworking fragment failed", "start/", correctedFragment);

	}

	@Test
	public void testParseBackendMessagesHappyPathJSON()
	{
		prepareConnectionSpy();
		final Map<String, List<String>> localHeaderFieldsMap = new HashMap<>();
		Mockito.doReturn(localHeaderFieldsMap).when(connectionSpy).getHeaderFields();

		List<BackendMessage> backendMessages = classUnderTest.parseBackendMessages(connectionSpy);

		Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
		assertNotNull("backendMessages is null", backendMessages);
		assertEquals("backend message is not empty", Collections.EMPTY_LIST, backendMessages);

		final List<String> backendMessageList = new ArrayList<>();
		final String jsonData = "{" + "  \"details\": [" + "    {" + "      \"code\": \"messageClass/123\","
				+ "      \"message\": \"This is the message\"," + "      \"dummy\": \"dummy\"," + "      \"severity\" : \"ERROR\""
				+ "    }," + "    {" + "      \"code\": \"messageClass/456\"," + "      \"message\": \"This is another message\","
				+ "      \"dummy\": \"dummy\"," + "      \"severity\" : \"WARNING\"" + "    }" + "  ]" + "}";
		backendMessageList.add(jsonData);
		localHeaderFieldsMap.put("sap-message", backendMessageList);

		backendMessages = classUnderTest.parseBackendMessages(connectionSpy);

		Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderFields();
		testBackendMessagesResults(backendMessages);
	}

	@Test
	public void testLogBackendMessages()
	{
		BackendMessage bm = new BackendMessageImpl(Severity.ERROR, "messageClass", 123, "This is an error message");
		final List<BackendMessage> backendMessages = new ArrayList<>();
		backendMessages.add(bm);
		bm = new BackendMessageImpl(Severity.WARNING, "messageClass", 456, "This is another error message");
		backendMessages.add(bm);
		bm = new BackendMessageImpl(Severity.INFO, "messageClass", 789, "This is a third error message");
		backendMessages.add(bm);
		logError = false;
		logWarning = false;
		logDebug = false;
		classUnderTest.logBackendMessages(backendMessages);
		assertTrue("Error was not logged", logError);
		assertTrue("Warning was not logged", logWarning);
		assertTrue("Debug info was not logged", logDebug);
		logError = false;
		logWarning = false;
		logDebug = false;
	}

	@Test
	public void testCreateMessageFromNode()
	{
		final BackendMessage backendMessage = classUnderTest.createMessageFromNode(messageNode);
		assertNotNull("backendMessage is null", backendMessage);
		assertEquals("Message class is not messageClass", "messageClass", backendMessage.getMessageClass());
		assertEquals("Message number is not 123", 123, backendMessage.getNumber());

	}

	@Test
	public void testExecutePutEntityAndHandleResponse()
	{
		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);
		try
		{
			final HttpODataResult response = classUnderTest.executePutEntityAndHandleResponse(initializeConnection,
					entityDataModelSpy, DESTINATION_NAME);
			assertNotNull("response is null", response);
			assertEquals("getHttpStatusCode is not OK", HttpStatusCodes.OK, response.getHttpStatusCode());
			assertEquals("Wrong Edm found", entityDataModelSpy, response.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(4)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(3)).getHeaderFields();
			Mockito.verify(connectionSpy, Mockito.times(1)).getURL();
			Mockito.verify(connectionSpy, Mockito.times(1)).getRequestMethod();
		}
		catch (IOException | HttpODataException e)
		{
			assertTrue("Test execution went bad", false);
		}
	}

	@Test
	public void testPutEntityHappyPath()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityData = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();

			final HttpODataResult putEntity = classUnderTest.putEntity(entitySetName, entityData, destination, SERVICE_URI);

			assertNotNull("response is null", putEntity);
			assertNotNull("response is null", putEntity.getHttpHeaders());
			assertEquals("Wrong Edm found", entityDataModelSpy, putEntity.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(1)).getOutputStream();
			Mockito.verify(connectionSpy, Mockito.times(7)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(4)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			Mockito.verify(oDataResponseSpy, Mockito.times(1)).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.verify(oDataResponseSpy, Mockito.times(1)).getEntity();

		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			assertTrue("Test execution went bad", false);
		}
	}

	@Test
	public void testPutEntityWith403Simulation()
	{

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityData = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(403)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;
			executeOnce = true;
			final HttpODataResult putEntity = classUnderTest.putEntity(entitySetName, entityData, destination, SERVICE_URI);

			assertNotNull("response is null", putEntity);
			assertNotNull("response is null", putEntity.getHttpHeaders());
			assertEquals("Wrong Edm found", entityDataModelSpy, putEntity.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(2)).getOutputStream();
			Mockito.verify(connectionSpy, Mockito.times(7)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
			Mockito.verify(connectionSpy, Mockito.times(1)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(5)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(2)).getDefaultEntityContainer();
			Mockito.verify(oDataResponseSpy, Mockito.times(2)).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.verify(oDataResponseSpy, Mockito.times(2)).getEntity();
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeOnce = false;
		throwHttpODataException = false;
	}

	@Test
	public void testPutEntityHttpODataException()
	{

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityData = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(404)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;
			classUnderTest.putEntity(entitySetName, entityData, destination, SERVICE_URI);
			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(1)).getOutputStream();
				Mockito.verify(connectionSpy, Mockito.times(4)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
				Mockito.verify(connectionSpy, Mockito.times(1)).getErrorStream();
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
				Mockito.verify(oDataResponseSpy, Mockito.times(1)).getHeader(HttpODataClientImpl.LOCATION);
				Mockito.verify(oDataResponseSpy, Mockito.times(1)).getEntity();
			}
			catch (IOException | EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testPutEntityException()
	{


		final String entitySetName = null;
		final Map<String, Object> entityData = null;
		mockConnectionSpy();
		try
		{
			Mockito.doThrow(new EdmException(null)).when(entityDataModelSpy).getDefaultEntityContainer();

			classUnderTest.putEntity(entitySetName, entityData, destination, SERVICE_URI);
			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException e)
		{
			try
			{
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			}
			catch (final EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testExecuteAndGetMediaFile()
	{
		final ConnectionWithTimeStamp initializeConnection = mockConnectionSpyAndInitializeConnectionWithGET200(contentResult);
		try
		{
			final HttpODataResult response = classUnderTest.executeAndGetMediaFile(initializeConnection, entityDataModelSpy,
					DESTINATION_NAME);
			assertNotNull("response is null", response);
			assertEquals("getHttpStatusCode is not OK", HttpStatusCodes.OK, response.getHttpStatusCode());
			assertNotNull("getMediaContent is null", response.getMediaContent());
			assertEquals("Wrong Edm found", entityDataModelSpy, response.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(3)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(4)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(3)).getHeaderFields();
			Mockito.verify(connectionSpy, Mockito.times(1)).getURL();
			Mockito.verify(connectionSpy, Mockito.times(1)).getRequestMethod();
		}
		catch (IOException | HttpODataException e)
		{
			assertTrue("Test execution went bad", false);
		}
	}

	@Test
	public void testGetMediaFileHappyPath()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityKeys = null;
		final String contentType = null;

		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();

			final HttpODataResult mediaFile = classUnderTest.getMediaFile(entitySetName, entityKeys, contentType, destination,
					SERVICE_URI);

			assertNotNull("mediaFile is null", mediaFile);
			assertNotNull("mediaFile is null", mediaFile.getHttpHeaders());
			assertEquals("Wrong Edm found", entityDataModelSpy, mediaFile.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(7)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(4)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			Mockito.verify(oDataResponseSpy, Mockito.times(1)).getHeader(HttpODataClientImpl.LOCATION);
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			assertTrue("Test execution went bad", false);
		}
	}

	@Test
	public void testGetMediaFileWith403Simulation()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityKeys = null;
		final String contentType = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(403)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;
			executeOnce = true;

			final HttpODataResult mediaFile = classUnderTest.getMediaFile(entitySetName, entityKeys, contentType, destination,
					SERVICE_URI);

			assertNotNull("mediaFile is null", mediaFile);
			assertNotNull("getHttpHeaders is null", mediaFile.getHttpHeaders());
			assertNotNull("getMediaContent is null", mediaFile.getMediaContent());
			assertEquals("Wrong Edm found", entityDataModelSpy, mediaFile.getMetaData());

			Mockito.verify(connectionSpy, Mockito.times(7)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(5)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(2)).getDefaultEntityContainer();
			Mockito.verify(oDataResponseSpy, Mockito.times(2)).getHeader(HttpODataClientImpl.LOCATION);
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeOnce = false;
		throwHttpODataException = false;
	}

	@Test
	public void testGetMediaFileHttpODataException()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final Map<String, Object> entityKeys = null;
		final String contentType = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(404)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;

			classUnderTest.getMediaFile(entitySetName, entityKeys, contentType, destination, SERVICE_URI);

			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(4)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
				Mockito.verify(oDataResponseSpy, Mockito.times(1)).getHeader(HttpODataClientImpl.LOCATION);
			}
			catch (IOException | EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testGetMediaFileException()
	{
		final String entitySetName = null;
		final Map<String, Object> entityKeys = null;
		final String contentType = null;
		mockConnectionSpy();
		try
		{
			Mockito.doThrow(new EdmException(null)).when(entityDataModelSpy).getDefaultEntityContainer();

			classUnderTest.getMediaFile(entitySetName, entityKeys, contentType, destination, SERVICE_URI);

			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException e)
		{
			try
			{
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			}
			catch (final EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testReadFeedHappyPath()
	{
		final String entitySetName = null;
		final String filter = null;

		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		mockConnectionSpy();

		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();

			final HttpODataResult readFeed = classUnderTest.readFeed(entitySetName, filter, destination, SERVICE_URI);

			assertNotNull("readFeed is null", readFeed);
			assertNotNull("getHttpHeaders is null", readFeed.getHttpHeaders());
			assertEquals("Wrong Edm found", entityDataModelSpy, readFeed.getMetaData());
			for (final ODataEntry oDataEntry : readFeed.getEntities())
			{
				final Map<String, Object> theMap = oDataEntry.getProperties();
				assertNotNull("Property object shall not be null", theMap);
				assertNull("Data Container Name should be null", theMap.get("oDataEntryMapKey"));
			}

			Mockito.verify(connectionSpy, Mockito.times(7)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(4)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();

		}
		catch (HttpODataException | EdmException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
	}

	@Test
	public void testReadFeedWith403Simulation()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final String filter = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(403)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;
			executeOnce = true;

			final HttpODataResult readFeed = classUnderTest.readFeed(entitySetName, filter, destination, SERVICE_URI);

			assertNotNull("readFeed is null", readFeed);
			assertNotNull("getHttpHeaders is null", readFeed.getHttpHeaders());
			assertEquals("Wrong Edm found", entityDataModelSpy, readFeed.getMetaData());
			for (final ODataEntry oDataEntry : readFeed.getEntities())
			{
				final Map<String, Object> theMap = oDataEntry.getProperties();
				assertNotNull("Property object shall not be null", theMap);
				assertNull("Data Container Name should be null", theMap.get("oDataEntryMapKey"));
			}

			Mockito.verify(connectionSpy, Mockito.times(1)).getInputStream();
			Mockito.verify(connectionSpy, Mockito.times(7)).getResponseCode();
			Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
			Mockito.verify(connectionSpy, Mockito.times(2)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
			Mockito.verify(connectionSpy, Mockito.times(5)).getHeaderFields();
			Mockito.verify(entityDataModelSpy, Mockito.times(2)).getDefaultEntityContainer();
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			assertTrue("Execution went bad", false);
		}
		executeOnce = false;
		throwHttpODataException = false;
	}

	@Test
	public void testReadFeedHttpODataException()
	{
		final EdmEntityContainerImplProv edmEntityContainerImplProv;
		final EntityContainerInfo entityContainerInfo = new EntityContainerInfo();

		final String entitySetName = null;
		final String filter = null;
		mockConnectionSpy();
		try
		{
			edmEntityContainerImplProv = new EdmEntityContainerImplProv((EdmImplProv) entityDataModel, entityContainerInfo);
			Mockito.doReturn(edmEntityContainerImplProv).when(entityDataModelSpy).getDefaultEntityContainer();
			Mockito.doReturn("http://www.sap.com/FullQualifiedUrl").when(oDataResponseSpy).getHeader(HttpODataClientImpl.LOCATION);
			Mockito.doReturn(contentResult).when(oDataResponseSpy).getEntity();
			Mockito.doReturn(Integer.valueOf(404)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getErrorStream();
			throwHttpODataException = true;

			classUnderTest.readFeed(entitySetName, filter, destination, SERVICE_URI);

			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException | IOException e)
		{
			try
			{
				Mockito.verify(connectionSpy, Mockito.times(4)).getResponseCode();
				Mockito.verify(connectionSpy, Mockito.times(1)).getResponseMessage();
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
				Mockito.verify(connectionSpy, Mockito.times(1)).getHeaderFields();
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			}
			catch (IOException | EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testReadFeedException()
	{
		final String entitySetName = null;
		final String filter = null;
		mockConnectionSpy();
		try
		{
			Mockito.doThrow(new EdmException(null)).when(entityDataModelSpy).getDefaultEntityContainer();

			classUnderTest.readFeed(entitySetName, filter, destination, SERVICE_URI);

			assertTrue("Execution went bad", false);
		}
		catch (final HttpODataException | EdmException e)
		{
			try
			{
				Mockito.verify(entityDataModelSpy, Mockito.times(1)).getDefaultEntityContainer();
			}
			catch (final EdmException e1)
			{
				assertTrue("Execution went bad", false);
			}
		}
		throwHttpODataException = false;
	}

	@Test
	public void testCreateHttpODataResultInstanceHappyPath()
	{
		executeCreateHttpODataResultInstance = true;
		final HttpODataResultManipulator createHttpODataResultInstance = classUnderTest.createHttpODataResultInstance();
		assertNotNull("createHttpODataResultInstance is null", createHttpODataResultInstance);
		executeCreateHttpODataResultInstance = false;
	}

	@Test
	public void testCreateHttpODataResultInstanceException1()
	{
		executeCreateHttpODataResultInstance = true;
		throwApplicationContextException = true;
		try
		{
			classUnderTest.createHttpODataResultInstance();
			assertTrue("Execution went bad", false);
		}
		catch (final SAPISCERuntimeException e)
		{
			assertTrue("Execution went well", true);
		}
		executeCreateHttpODataResultInstance = false;
		throwApplicationContextException = false;
	}

	@Test
	public void testCreateHttpODataResultInstanceException2()
	{
		executeCreateHttpODataResultInstance = true;
		throwGetBeanException = true;
		try
		{
			classUnderTest.createHttpODataResultInstance();
			assertTrue("Execution went bad", false);
		}
		catch (final SAPISCERuntimeException e)
		{
			assertTrue("Execution went well", true);
		}
		executeCreateHttpODataResultInstance = false;
		throwGetBeanException = false;
	}



	// ========================================
	// === Reuse coding INPUTS ================
	// ========================================

	protected void prepareConnectionSpy()
	{
		URI rootBatchUri;
		try
		{
			rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			connection = (HttpURLConnection) rootBatchUri.toURL().openConnection();
		}
		catch (URISyntaxException | IOException e)
		{
			assertTrue("prepareConnectionSpy execution went bad", false);
		}
		connectionSpy = Mockito.spy(connection);
	}

	protected void mockConnectionSpy()
	{
		prepareConnectionSpy();
		try
		{
			Mockito.doReturn(httpOutputStream).when(connectionSpy).getOutputStream();
			Mockito.doReturn(contentResult).when(connectionSpy).getInputStream();
		}
		catch (final IOException e)
		{
			assertTrue("mockConnectionSpy execution went bad", false);
		}
		Mockito.doReturn(X_CSRF_TOKEN).when(connectionSpy).getHeaderField(HttpODataClientImpl.HTTP_HEADER_X_CSRF_TOKEN);
		Mockito.doReturn(headerFieldsMap).when(connectionSpy).getHeaderFields();
	}

	protected ConnectionWithTimeStamp mockConnectionSpyAndInitializeConnectionWithGET200(final InputStream contentResult)
	{
		ConnectionWithTimeStamp initializeConnection = null;
		try
		{
			final URI rootBatchUri = classUnderTest.getFullQualifiedServiceURI("/$batch", destination, SERVICE_URI);
			connection = (HttpURLConnection) rootBatchUri.toURL().openConnection();
			connectionSpy = Mockito.spy(connection);
			Mockito.doReturn(Integer.valueOf(200)).when(connectionSpy).getResponseCode();
			Mockito.doReturn(contentResult).when(connectionSpy).getInputStream();
			initializeConnection = classUnderTest.initializeConnection(rootBatchUri, "contentType", HttpMethod.GET, null,
					destination, SERVICE_URI);
		}
		catch (URISyntaxException | IOException e)
		{
			assertTrue("mockConnectionSpyAndInitializeConnectionWithGET200 execution went bad", false);
		}
		return initializeConnection;
	}

	// ========================================
	// === Reuse coding RESULTS CHECKS ========
	// ========================================
	protected void testBackendMessagesResults(final List<BackendMessage> backendMessages)
	{
		assertNotNull("backendMessages is null", backendMessages);
		assertEquals("Number of backend messages is not 3", 3, backendMessages.size());
		BackendMessage backendMessage = backendMessages.get(1);
		assertNotNull("backendMessage is null", backendMessage);
		assertEquals("Message number is not 123", 123, backendMessage.getNumber());
		assertEquals("Message class is not messageClass", "messageClass", backendMessage.getMessageClass());
		assertEquals("Message Severity is not ERROR", Severity.ERROR, backendMessage.getSeverity());
		assertEquals("Message text not found", "This is the message", backendMessage.getText());

		backendMessage = backendMessages.get(2);
		assertNotNull("backendMessage is null", backendMessage);
		assertEquals("Message number is not 456", 456, backendMessage.getNumber());
		assertEquals("Message class is not messageClass", "messageClass", backendMessage.getMessageClass());
		assertEquals("Message Severity is not WARNING", Severity.WARNING, backendMessage.getSeverity());
		assertEquals("Message text not found", "This is another message", backendMessage.getText());
	}

	protected void testCookieTokenAndServiceMetadata(final Map<String, Object> theDestination, final String serviceUriToCheck,
			final int numberOfEntryExpected, final String tokenValueExpected)
	{
		assertNotNull("Destination shall not be null", theDestination);
		assertEquals("Number of entries should be " + numberOfEntryExpected, numberOfEntryExpected, theDestination.size());
		final Map<String, Object> serviceMap = (Map<String, Object>) theDestination.get(serviceUriToCheck);
		assertNotNull("serviceMap shall not be null", serviceMap);
		assertEquals("Number of entries should be 3", 3, serviceMap.size());
		assertTrue("serviceMap shall contains COOKIES", serviceMap.containsKey(HttpODataClientImpl.STORAGE_SERVICE_COOKIES));
		assertTrue("serviceMap shall contains X.CSRF.Token",
				serviceMap.containsKey(HttpODataClientImpl.STORAGE_SERVICE_X_CSRF_TOKEN));
		assertTrue("serviceMap shall contains Service Metadata",
				serviceMap.containsKey(HttpODataClientImpl.STORAGE_SERVICE_METADATA));
		final Map<String, String> serviceCookies = (Map<String, String>) serviceMap
				.get(HttpODataClientImpl.STORAGE_SERVICE_COOKIES);
		assertNotNull("serviceCookies shall not be null", serviceCookies);
		assertEquals("Number of entries should be 3", 3, serviceCookies.size());
		assertEquals("cookieValue should be " + cookieValue, cookieValue, serviceCookies.toString());
		final String tokenToCheck = (String) serviceMap.get(HttpODataClientImpl.STORAGE_SERVICE_X_CSRF_TOKEN);
		assertEquals("X_CSRF_Token should be " + tokenValueExpected, tokenValueExpected, tokenToCheck);
		assertEquals("EDM Value not identical", entityDataModelSpy, serviceMap.get(HttpODataClientImpl.STORAGE_SERVICE_METADATA));
	}


}

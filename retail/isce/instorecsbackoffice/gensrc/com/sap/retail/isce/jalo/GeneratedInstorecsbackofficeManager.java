/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.isce.jalo;

import com.sap.retail.isce.constants.InstorecsbackofficeConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPHTTPDestination;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>InstorecsbackofficeManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedInstorecsbackofficeManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("instorecsassistedservicestorefront_CAR_HTTPDestination", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_yMkt_HTTPDestination", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_CAR_client", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_CAR_serviceName", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_CAR_posChannelList", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_CAR_onlineChannelList", AttributeMode.INITIAL);
		tmp.put("instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	@Override
	public String getName()
	{
		return InstorecsbackofficeConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_client</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_client - Client for CAR System
	 */
	public String getInstorecsassistedservicestorefront_CAR_client(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_CLIENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_client</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_client - Client for CAR System
	 */
	public String getInstorecsassistedservicestorefront_CAR_client(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_client( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_client</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_client - Client for CAR System
	 */
	public void setInstorecsassistedservicestorefront_CAR_client(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_CLIENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_client</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_client - Client for CAR System
	 */
	public void setInstorecsassistedservicestorefront_CAR_client(final SAPConfiguration item, final String value)
	{
		setInstorecsassistedservicestorefront_CAR_client( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_HTTPDestination</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_HTTPDestination - HTTP Destination for OData (CAR)
	 */
	public SAPHTTPDestination getInstorecsassistedservicestorefront_CAR_HTTPDestination(final SessionContext ctx, final GenericItem item)
	{
		return (SAPHTTPDestination)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_HTTPDESTINATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_HTTPDestination</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_HTTPDestination - HTTP Destination for OData (CAR)
	 */
	public SAPHTTPDestination getInstorecsassistedservicestorefront_CAR_HTTPDestination(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_HTTPDestination( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_HTTPDestination</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_HTTPDestination - HTTP Destination for OData (CAR)
	 */
	public void setInstorecsassistedservicestorefront_CAR_HTTPDestination(final SessionContext ctx, final GenericItem item, final SAPHTTPDestination value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_HTTPDESTINATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_HTTPDestination</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_HTTPDestination - HTTP Destination for OData (CAR)
	 */
	public void setInstorecsassistedservicestorefront_CAR_HTTPDestination(final SAPConfiguration item, final SAPHTTPDestination value)
	{
		setInstorecsassistedservicestorefront_CAR_HTTPDestination( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_onlineChannelList</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_onlineChannelList - Online Channel List
	 */
	public String getInstorecsassistedservicestorefront_CAR_onlineChannelList(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_ONLINECHANNELLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_onlineChannelList</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_onlineChannelList - Online Channel List
	 */
	public String getInstorecsassistedservicestorefront_CAR_onlineChannelList(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_onlineChannelList( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_onlineChannelList</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_onlineChannelList - Online Channel List
	 */
	public void setInstorecsassistedservicestorefront_CAR_onlineChannelList(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_ONLINECHANNELLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_onlineChannelList</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_onlineChannelList - Online Channel List
	 */
	public void setInstorecsassistedservicestorefront_CAR_onlineChannelList(final SAPConfiguration item, final String value)
	{
		setInstorecsassistedservicestorefront_CAR_onlineChannelList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_posChannelList</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_posChannelList - POS Channel List
	 */
	public String getInstorecsassistedservicestorefront_CAR_posChannelList(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_POSCHANNELLIST);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_posChannelList</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_posChannelList - POS Channel List
	 */
	public String getInstorecsassistedservicestorefront_CAR_posChannelList(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_posChannelList( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_posChannelList</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_posChannelList - POS Channel List
	 */
	public void setInstorecsassistedservicestorefront_CAR_posChannelList(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_POSCHANNELLIST,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_posChannelList</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_posChannelList - POS Channel List
	 */
	public void setInstorecsassistedservicestorefront_CAR_posChannelList(final SAPConfiguration item, final String value)
	{
		setInstorecsassistedservicestorefront_CAR_posChannelList( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public Integer getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SessionContext ctx, final GenericItem item)
	{
		return (Integer)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_PURCHASEHISTORYCUSTOMERORDERS_MAXNUMBEROFTRANSACTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public Integer getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @return the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public int getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactionsAsPrimitive(final SessionContext ctx, final SAPConfiguration item)
	{
		Integer value = getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @return the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public int getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactionsAsPrimitive(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactionsAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public void setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SessionContext ctx, final GenericItem item, final Integer value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_PURCHASEHISTORYCUSTOMERORDERS_MAXNUMBEROFTRANSACTIONS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public void setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SAPConfiguration item, final Integer value)
	{
		setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public void setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SessionContext ctx, final SAPConfiguration item, final int value)
	{
		setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions - Purchase History: maximum number of displayed Customers Orders
	 */
	public void setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions(final SAPConfiguration item, final int value)
	{
		setInstorecsassistedservicestorefront_CAR_purchaseHistoryCustomerOrders_maxNumberofTransactions( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_serviceName</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_serviceName - Service Name
	 */
	public String getInstorecsassistedservicestorefront_CAR_serviceName(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_SERVICENAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_serviceName</code> attribute.
	 * @return the instorecsassistedservicestorefront_CAR_serviceName - Service Name
	 */
	public String getInstorecsassistedservicestorefront_CAR_serviceName(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_CAR_serviceName( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_serviceName</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_serviceName - Service Name
	 */
	public void setInstorecsassistedservicestorefront_CAR_serviceName(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_CAR_SERVICENAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_CAR_serviceName</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_CAR_serviceName - Service Name
	 */
	public void setInstorecsassistedservicestorefront_CAR_serviceName(final SAPConfiguration item, final String value)
	{
		setInstorecsassistedservicestorefront_CAR_serviceName( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_yMkt_HTTPDestination</code> attribute.
	 * @return the instorecsassistedservicestorefront_yMkt_HTTPDestination - HTTP Destination for yMkt OData(ANA)
	 */
	public SAPHTTPDestination getInstorecsassistedservicestorefront_yMkt_HTTPDestination(final SessionContext ctx, final GenericItem item)
	{
		return (SAPHTTPDestination)item.getProperty( ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_YMKT_HTTPDESTINATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.instorecsassistedservicestorefront_yMkt_HTTPDestination</code> attribute.
	 * @return the instorecsassistedservicestorefront_yMkt_HTTPDestination - HTTP Destination for yMkt OData(ANA)
	 */
	public SAPHTTPDestination getInstorecsassistedservicestorefront_yMkt_HTTPDestination(final SAPConfiguration item)
	{
		return getInstorecsassistedservicestorefront_yMkt_HTTPDestination( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_yMkt_HTTPDestination</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_yMkt_HTTPDestination - HTTP Destination for yMkt OData(ANA)
	 */
	public void setInstorecsassistedservicestorefront_yMkt_HTTPDestination(final SessionContext ctx, final GenericItem item, final SAPHTTPDestination value)
	{
		item.setProperty(ctx, InstorecsbackofficeConstants.Attributes.SAPConfiguration.INSTORECSASSISTEDSERVICESTOREFRONT_YMKT_HTTPDESTINATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.instorecsassistedservicestorefront_yMkt_HTTPDestination</code> attribute. 
	 * @param value the instorecsassistedservicestorefront_yMkt_HTTPDestination - HTTP Destination for yMkt OData(ANA)
	 */
	public void setInstorecsassistedservicestorefront_yMkt_HTTPDestination(final SAPConfiguration item, final SAPHTTPDestination value)
	{
		setInstorecsassistedservicestorefront_yMkt_HTTPDestination( getSession().getSessionContext(), item, value );
	}
	
}

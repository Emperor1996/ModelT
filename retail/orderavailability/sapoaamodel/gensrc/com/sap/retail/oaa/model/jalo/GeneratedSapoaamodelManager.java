/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 30, 2019 3:02:53 PM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.oaa.model.jalo;

import com.sap.retail.oaa.model.constants.SapoaamodelConstants;
import com.sap.retail.oaa.model.jalo.ScheduleLine;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ordersplitting.jalo.StockLevel;
import de.hybris.platform.sap.core.configuration.constants.SapcoreconfigurationConstants;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPGlobalConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPHTTPDestination;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>SapoaamodelManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSapoaamodelManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n SAPOAA_GLOBALCONFIGURATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SAPGlobalConfiguration> SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATIONSAPOAA_GLOBALCONFIGURATIONSHANDLER = new OneToManyHandler<SAPGlobalConfiguration>(
	SapcoreconfigurationConstants.TC.SAPGLOBALCONFIGURATION,
	false,
	"sapoaa_carHttpDestination",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapoaa_oaaProfile", AttributeMode.INITIAL);
		tmp.put("sapoaa_salesChannel", AttributeMode.INITIAL);
		tmp.put("sapoaa_consumerId", AttributeMode.INITIAL);
		tmp.put("sapoaa_vendorItemCategory", AttributeMode.INITIAL);
		tmp.put("sapoaa_mode", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapoaa_carClient", AttributeMode.INITIAL);
		tmp.put("sapoaa_carHttpDestination", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.sap.core.configuration.jalo.SAPGlobalConfiguration", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapoaa_roughStockIndicator", AttributeMode.INITIAL);
		tmp.put("sapoaa_roughStockIndicator_availabilityDate", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.ordersplitting.jalo.StockLevel", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapoaa_cacShippingPoint", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.storelocator.jalo.PointOfService", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("sapCarReservation", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrder", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("scheduleLines", AttributeMode.INITIAL);
		tmp.put("sapSource", AttributeMode.INITIAL);
		tmp.put("sapVendor", AttributeMode.INITIAL);
		tmp.put("sapCarReservation", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
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
	
	public ScheduleLine createScheduleLine(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapoaamodelConstants.TC.SCHEDULELINE );
			return (ScheduleLine)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating ScheduleLine : "+e.getMessage(), 0 );
		}
	}
	
	public ScheduleLine createScheduleLine(final Map attributeValues)
	{
		return createScheduleLine( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SapoaamodelConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sapCarReservation</code> attribute.
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public Boolean isSapCarReservation(final SessionContext ctx, final AbstractOrder item)
	{
		return (Boolean)item.getProperty( ctx, SapoaamodelConstants.Attributes.AbstractOrder.SAPCARRESERVATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sapCarReservation</code> attribute.
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public Boolean isSapCarReservation(final AbstractOrder item)
	{
		return isSapCarReservation( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public boolean isSapCarReservationAsPrimitive(final SessionContext ctx, final AbstractOrder item)
	{
		Boolean value = isSapCarReservation( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public boolean isSapCarReservationAsPrimitive(final AbstractOrder item)
	{
		return isSapCarReservationAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final SessionContext ctx, final AbstractOrder item, final Boolean value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.AbstractOrder.SAPCARRESERVATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final AbstractOrder item, final Boolean value)
	{
		setSapCarReservation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final SessionContext ctx, final AbstractOrder item, final boolean value)
	{
		setSapCarReservation( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final AbstractOrder item, final boolean value)
	{
		setSapCarReservation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute.
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public Boolean isSapCarReservation(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPCARRESERVATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute.
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public Boolean isSapCarReservation(final AbstractOrderEntry item)
	{
		return isSapCarReservation( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public boolean isSapCarReservationAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isSapCarReservation( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @return the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public boolean isSapCarReservationAsPrimitive(final AbstractOrderEntry item)
	{
		return isSapCarReservationAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPCARRESERVATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final AbstractOrderEntry item, final Boolean value)
	{
		setSapCarReservation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setSapCarReservation( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapCarReservation</code> attribute. 
	 * @param value the sapCarReservation - Indicates if a reservation is in the SAP CAR System
	 */
	public void setSapCarReservation(final AbstractOrderEntry item, final boolean value)
	{
		setSapCarReservation( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.sapoaa_cacShippingPoint</code> attribute.
	 * @return the sapoaa_cacShippingPoint - Shipping Point for Click and Collect
	 */
	public String getSapoaa_cacShippingPoint(final SessionContext ctx, final PointOfService item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.PointOfService.SAPOAA_CACSHIPPINGPOINT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PointOfService.sapoaa_cacShippingPoint</code> attribute.
	 * @return the sapoaa_cacShippingPoint - Shipping Point for Click and Collect
	 */
	public String getSapoaa_cacShippingPoint(final PointOfService item)
	{
		return getSapoaa_cacShippingPoint( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.sapoaa_cacShippingPoint</code> attribute. 
	 * @param value the sapoaa_cacShippingPoint - Shipping Point for Click and Collect
	 */
	public void setSapoaa_cacShippingPoint(final SessionContext ctx, final PointOfService item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.PointOfService.SAPOAA_CACSHIPPINGPOINT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PointOfService.sapoaa_cacShippingPoint</code> attribute. 
	 * @param value the sapoaa_cacShippingPoint - Shipping Point for Click and Collect
	 */
	public void setSapoaa_cacShippingPoint(final PointOfService item, final String value)
	{
		setSapoaa_cacShippingPoint( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPGlobalConfiguration.sapoaa_carClient</code> attribute.
	 * @return the sapoaa_carClient - SAP CAR Client
	 */
	public String getSapoaa_carClient(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPGlobalConfiguration.SAPOAA_CARCLIENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPGlobalConfiguration.sapoaa_carClient</code> attribute.
	 * @return the sapoaa_carClient - SAP CAR Client
	 */
	public String getSapoaa_carClient(final SAPGlobalConfiguration item)
	{
		return getSapoaa_carClient( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPGlobalConfiguration.sapoaa_carClient</code> attribute. 
	 * @param value the sapoaa_carClient - SAP CAR Client
	 */
	public void setSapoaa_carClient(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPGlobalConfiguration.SAPOAA_CARCLIENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPGlobalConfiguration.sapoaa_carClient</code> attribute. 
	 * @param value the sapoaa_carClient - SAP CAR Client
	 */
	public void setSapoaa_carClient(final SAPGlobalConfiguration item, final String value)
	{
		setSapoaa_carClient( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPGlobalConfiguration.sapoaa_carHttpDestination</code> attribute.
	 * @return the sapoaa_carHttpDestination
	 */
	public SAPHTTPDestination getSapoaa_carHttpDestination(final SessionContext ctx, final GenericItem item)
	{
		return (SAPHTTPDestination)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPGlobalConfiguration.SAPOAA_CARHTTPDESTINATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPGlobalConfiguration.sapoaa_carHttpDestination</code> attribute.
	 * @return the sapoaa_carHttpDestination
	 */
	public SAPHTTPDestination getSapoaa_carHttpDestination(final SAPGlobalConfiguration item)
	{
		return getSapoaa_carHttpDestination( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPGlobalConfiguration.sapoaa_carHttpDestination</code> attribute. 
	 * @param value the sapoaa_carHttpDestination
	 */
	public void setSapoaa_carHttpDestination(final SessionContext ctx, final GenericItem item, final SAPHTTPDestination value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPGlobalConfiguration.SAPOAA_CARHTTPDESTINATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPGlobalConfiguration.sapoaa_carHttpDestination</code> attribute. 
	 * @param value the sapoaa_carHttpDestination
	 */
	public void setSapoaa_carHttpDestination(final SAPGlobalConfiguration item, final SAPHTTPDestination value)
	{
		setSapoaa_carHttpDestination( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_consumerId</code> attribute.
	 * @return the sapoaa_consumerId - OAA Consumer ID
	 */
	public String getSapoaa_consumerId(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_CONSUMERID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_consumerId</code> attribute.
	 * @return the sapoaa_consumerId - OAA Consumer ID
	 */
	public String getSapoaa_consumerId(final SAPConfiguration item)
	{
		return getSapoaa_consumerId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_consumerId</code> attribute. 
	 * @param value the sapoaa_consumerId - OAA Consumer ID
	 */
	public void setSapoaa_consumerId(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_CONSUMERID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_consumerId</code> attribute. 
	 * @param value the sapoaa_consumerId - OAA Consumer ID
	 */
	public void setSapoaa_consumerId(final SAPConfiguration item, final String value)
	{
		setSapoaa_consumerId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPHTTPDestination.sapoaa_globalConfigurations</code> attribute.
	 * @return the sapoaa_globalConfigurations
	 */
	public Collection<SAPGlobalConfiguration> getSapoaa_globalConfigurations(final SessionContext ctx, final GenericItem item)
	{
		return SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATIONSAPOAA_GLOBALCONFIGURATIONSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPHTTPDestination.sapoaa_globalConfigurations</code> attribute.
	 * @return the sapoaa_globalConfigurations
	 */
	public Collection<SAPGlobalConfiguration> getSapoaa_globalConfigurations(final SAPHTTPDestination item)
	{
		return getSapoaa_globalConfigurations( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPHTTPDestination.sapoaa_globalConfigurations</code> attribute. 
	 * @param value the sapoaa_globalConfigurations
	 */
	public void setSapoaa_globalConfigurations(final SessionContext ctx, final GenericItem item, final Collection<SAPGlobalConfiguration> value)
	{
		SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATIONSAPOAA_GLOBALCONFIGURATIONSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPHTTPDestination.sapoaa_globalConfigurations</code> attribute. 
	 * @param value the sapoaa_globalConfigurations
	 */
	public void setSapoaa_globalConfigurations(final SAPHTTPDestination item, final Collection<SAPGlobalConfiguration> value)
	{
		setSapoaa_globalConfigurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sapoaa_globalConfigurations. 
	 * @param value the item to add to sapoaa_globalConfigurations
	 */
	public void addToSapoaa_globalConfigurations(final SessionContext ctx, final GenericItem item, final SAPGlobalConfiguration value)
	{
		SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATIONSAPOAA_GLOBALCONFIGURATIONSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sapoaa_globalConfigurations. 
	 * @param value the item to add to sapoaa_globalConfigurations
	 */
	public void addToSapoaa_globalConfigurations(final SAPHTTPDestination item, final SAPGlobalConfiguration value)
	{
		addToSapoaa_globalConfigurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sapoaa_globalConfigurations. 
	 * @param value the item to remove from sapoaa_globalConfigurations
	 */
	public void removeFromSapoaa_globalConfigurations(final SessionContext ctx, final GenericItem item, final SAPGlobalConfiguration value)
	{
		SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATIONSAPOAA_GLOBALCONFIGURATIONSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sapoaa_globalConfigurations. 
	 * @param value the item to remove from sapoaa_globalConfigurations
	 */
	public void removeFromSapoaa_globalConfigurations(final SAPHTTPDestination item, final SAPGlobalConfiguration value)
	{
		removeFromSapoaa_globalConfigurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_mode</code> attribute.
	 * @return the sapoaa_mode - OAA Mode
	 */
	public EnumerationValue getSapoaa_mode(final SessionContext ctx, final GenericItem item)
	{
		return (EnumerationValue)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_MODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_mode</code> attribute.
	 * @return the sapoaa_mode - OAA Mode
	 */
	public EnumerationValue getSapoaa_mode(final SAPConfiguration item)
	{
		return getSapoaa_mode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_mode</code> attribute. 
	 * @param value the sapoaa_mode - OAA Mode
	 */
	public void setSapoaa_mode(final SessionContext ctx, final GenericItem item, final EnumerationValue value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_MODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_mode</code> attribute. 
	 * @param value the sapoaa_mode - OAA Mode
	 */
	public void setSapoaa_mode(final SAPConfiguration item, final EnumerationValue value)
	{
		setSapoaa_mode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_oaaProfile</code> attribute.
	 * @return the sapoaa_oaaProfile - OAA Profile
	 */
	public String getSapoaa_oaaProfile(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_OAAPROFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_oaaProfile</code> attribute.
	 * @return the sapoaa_oaaProfile - OAA Profile
	 */
	public String getSapoaa_oaaProfile(final SAPConfiguration item)
	{
		return getSapoaa_oaaProfile( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_oaaProfile</code> attribute. 
	 * @param value the sapoaa_oaaProfile - OAA Profile
	 */
	public void setSapoaa_oaaProfile(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_OAAPROFILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_oaaProfile</code> attribute. 
	 * @param value the sapoaa_oaaProfile - OAA Profile
	 */
	public void setSapoaa_oaaProfile(final SAPConfiguration item, final String value)
	{
		setSapoaa_oaaProfile( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.sapoaa_roughStockIndicator</code> attribute.
	 * @return the sapoaa_roughStockIndicator - Rough Stock Indicator
	 */
	public String getSapoaa_roughStockIndicator(final SessionContext ctx, final StockLevel item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.StockLevel.SAPOAA_ROUGHSTOCKINDICATOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.sapoaa_roughStockIndicator</code> attribute.
	 * @return the sapoaa_roughStockIndicator - Rough Stock Indicator
	 */
	public String getSapoaa_roughStockIndicator(final StockLevel item)
	{
		return getSapoaa_roughStockIndicator( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StockLevel.sapoaa_roughStockIndicator</code> attribute. 
	 * @param value the sapoaa_roughStockIndicator - Rough Stock Indicator
	 */
	public void setSapoaa_roughStockIndicator(final SessionContext ctx, final StockLevel item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.StockLevel.SAPOAA_ROUGHSTOCKINDICATOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StockLevel.sapoaa_roughStockIndicator</code> attribute. 
	 * @param value the sapoaa_roughStockIndicator - Rough Stock Indicator
	 */
	public void setSapoaa_roughStockIndicator(final StockLevel item, final String value)
	{
		setSapoaa_roughStockIndicator( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.sapoaa_roughStockIndicator_availabilityDate</code> attribute.
	 * @return the sapoaa_roughStockIndicator_availabilityDate - Rough Stock Indicator - availability date
	 */
	public Date getSapoaa_roughStockIndicator_availabilityDate(final SessionContext ctx, final StockLevel item)
	{
		return (Date)item.getProperty( ctx, SapoaamodelConstants.Attributes.StockLevel.SAPOAA_ROUGHSTOCKINDICATOR_AVAILABILITYDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>StockLevel.sapoaa_roughStockIndicator_availabilityDate</code> attribute.
	 * @return the sapoaa_roughStockIndicator_availabilityDate - Rough Stock Indicator - availability date
	 */
	public Date getSapoaa_roughStockIndicator_availabilityDate(final StockLevel item)
	{
		return getSapoaa_roughStockIndicator_availabilityDate( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StockLevel.sapoaa_roughStockIndicator_availabilityDate</code> attribute. 
	 * @param value the sapoaa_roughStockIndicator_availabilityDate - Rough Stock Indicator - availability date
	 */
	public void setSapoaa_roughStockIndicator_availabilityDate(final SessionContext ctx, final StockLevel item, final Date value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.StockLevel.SAPOAA_ROUGHSTOCKINDICATOR_AVAILABILITYDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>StockLevel.sapoaa_roughStockIndicator_availabilityDate</code> attribute. 
	 * @param value the sapoaa_roughStockIndicator_availabilityDate - Rough Stock Indicator - availability date
	 */
	public void setSapoaa_roughStockIndicator_availabilityDate(final StockLevel item, final Date value)
	{
		setSapoaa_roughStockIndicator_availabilityDate( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_salesChannel</code> attribute.
	 * @return the sapoaa_salesChannel - Sales Channel
	 */
	public String getSapoaa_salesChannel(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_SALESCHANNEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_salesChannel</code> attribute.
	 * @return the sapoaa_salesChannel - Sales Channel
	 */
	public String getSapoaa_salesChannel(final SAPConfiguration item)
	{
		return getSapoaa_salesChannel( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_salesChannel</code> attribute. 
	 * @param value the sapoaa_salesChannel - Sales Channel
	 */
	public void setSapoaa_salesChannel(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_SALESCHANNEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_salesChannel</code> attribute. 
	 * @param value the sapoaa_salesChannel - Sales Channel
	 */
	public void setSapoaa_salesChannel(final SAPConfiguration item, final String value)
	{
		setSapoaa_salesChannel( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_vendorItemCategory</code> attribute.
	 * @return the sapoaa_vendorItemCategory - Item Category
	 */
	public String getSapoaa_vendorItemCategory(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_VENDORITEMCATEGORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sapoaa_vendorItemCategory</code> attribute.
	 * @return the sapoaa_vendorItemCategory - Item Category
	 */
	public String getSapoaa_vendorItemCategory(final SAPConfiguration item)
	{
		return getSapoaa_vendorItemCategory( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_vendorItemCategory</code> attribute. 
	 * @param value the sapoaa_vendorItemCategory - Item Category
	 */
	public void setSapoaa_vendorItemCategory(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.SAPConfiguration.SAPOAA_VENDORITEMCATEGORY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sapoaa_vendorItemCategory</code> attribute. 
	 * @param value the sapoaa_vendorItemCategory - Item Category
	 */
	public void setSapoaa_vendorItemCategory(final SAPConfiguration item, final String value)
	{
		setSapoaa_vendorItemCategory( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapSource</code> attribute.
	 * @return the sapSource - Result from Sourcing determination which will be sent to as site SAP ERP
	 */
	public PointOfService getSapSource(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (PointOfService)item.getProperty( ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPSOURCE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapSource</code> attribute.
	 * @return the sapSource - Result from Sourcing determination which will be sent to as site SAP ERP
	 */
	public PointOfService getSapSource(final AbstractOrderEntry item)
	{
		return getSapSource( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapSource</code> attribute. 
	 * @param value the sapSource - Result from Sourcing determination which will be sent to as site SAP ERP
	 */
	public void setSapSource(final SessionContext ctx, final AbstractOrderEntry item, final PointOfService value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPSOURCE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapSource</code> attribute. 
	 * @param value the sapSource - Result from Sourcing determination which will be sent to as site SAP ERP
	 */
	public void setSapSource(final AbstractOrderEntry item, final PointOfService value)
	{
		setSapSource( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapVendor</code> attribute.
	 * @return the sapVendor - Result from Sourcing determination which will be sent as vendor to SAP ERP
	 */
	public String getSapVendor(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPVENDOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.sapVendor</code> attribute.
	 * @return the sapVendor - Result from Sourcing determination which will be sent as vendor to SAP ERP
	 */
	public String getSapVendor(final AbstractOrderEntry item)
	{
		return getSapVendor( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapVendor</code> attribute. 
	 * @param value the sapVendor - Result from Sourcing determination which will be sent as vendor to SAP ERP
	 */
	public void setSapVendor(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SAPVENDOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.sapVendor</code> attribute. 
	 * @param value the sapVendor - Result from Sourcing determination which will be sent as vendor to SAP ERP
	 */
	public void setSapVendor(final AbstractOrderEntry item, final String value)
	{
		setSapVendor( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.scheduleLines</code> attribute.
	 * @return the scheduleLines - Schedule Lines
	 */
	public List<ScheduleLine> getScheduleLines(final SessionContext ctx, final AbstractOrderEntry item)
	{
		List<ScheduleLine> coll = (List<ScheduleLine>)item.getProperty( ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SCHEDULELINES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.scheduleLines</code> attribute.
	 * @return the scheduleLines - Schedule Lines
	 */
	public List<ScheduleLine> getScheduleLines(final AbstractOrderEntry item)
	{
		return getScheduleLines( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.scheduleLines</code> attribute. 
	 * @param value the scheduleLines - Schedule Lines
	 */
	public void setScheduleLines(final SessionContext ctx, final AbstractOrderEntry item, final List<ScheduleLine> value)
	{
		item.setProperty(ctx, SapoaamodelConstants.Attributes.AbstractOrderEntry.SCHEDULELINES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.scheduleLines</code> attribute. 
	 * @param value the scheduleLines - Schedule Lines
	 */
	public void setScheduleLines(final AbstractOrderEntry item, final List<ScheduleLine> value)
	{
		setScheduleLines( getSession().getSessionContext(), item, value );
	}
	
}

/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.sapppspricing.jalo;

import com.sap.retail.sapppspricing.constants.SapppspricingConstants;
import com.sap.retail.sapppspricing.jalo.DeletePricesCronJob;
import com.sap.retail.sapppspricing.jalo.DeletePromotionsCronJob;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.sap.core.configuration.constants.SapcoreconfigurationConstants;
import de.hybris.platform.sap.core.configuration.jalo.SAPConfiguration;
import de.hybris.platform.sap.core.configuration.jalo.SAPHTTPDestination;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SapppspricingManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSapppspricingManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n SAPPPS_CONFIGURATIONS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SAPConfiguration> SAPHTTPDESTINATIONFORPPSSAPPPS_CONFIGURATIONSHANDLER = new OneToManyHandler<SAPConfiguration>(
	SapcoreconfigurationConstants.TC.SAPCONFIGURATION,
	false,
	"sappps_HttpDestination",
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
		tmp.put("sappps_localRemote", AttributeMode.INITIAL);
		tmp.put("sappps_active", AttributeMode.INITIAL);
		tmp.put("sappps_cacheCatalogPrices", AttributeMode.INITIAL);
		tmp.put("sappps_businessUnitId", AttributeMode.INITIAL);
		tmp.put("sappps_interfaceVersion", AttributeMode.INITIAL);
		tmp.put("sappps_HttpDestination", AttributeMode.INITIAL);
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
	
	public DeletePricesCronJob createDeletePricesCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapppspricingConstants.TC.DELETEPRICESCRONJOB );
			return (DeletePricesCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating DeletePricesCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public DeletePricesCronJob createDeletePricesCronJob(final Map attributeValues)
	{
		return createDeletePricesCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public DeletePromotionsCronJob createDeletePromotionsCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SapppspricingConstants.TC.DELETEPROMOTIONSCRONJOB );
			return (DeletePromotionsCronJob)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating DeletePromotionsCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public DeletePromotionsCronJob createDeletePromotionsCronJob(final Map attributeValues)
	{
		return createDeletePromotionsCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return SapppspricingConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_active</code> attribute.
	 * @return the sappps_active - PPS active
	 */
	public Boolean isSappps_active(final SessionContext ctx, final GenericItem item)
	{
		return (Boolean)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_ACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_active</code> attribute.
	 * @return the sappps_active - PPS active
	 */
	public Boolean isSappps_active(final SAPConfiguration item)
	{
		return isSappps_active( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @return the sappps_active - PPS active
	 */
	public boolean isSappps_activeAsPrimitive(final SessionContext ctx, final SAPConfiguration item)
	{
		Boolean value = isSappps_active( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @return the sappps_active - PPS active
	 */
	public boolean isSappps_activeAsPrimitive(final SAPConfiguration item)
	{
		return isSappps_activeAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @param value the sappps_active - PPS active
	 */
	public void setSappps_active(final SessionContext ctx, final GenericItem item, final Boolean value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_ACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @param value the sappps_active - PPS active
	 */
	public void setSappps_active(final SAPConfiguration item, final Boolean value)
	{
		setSappps_active( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @param value the sappps_active - PPS active
	 */
	public void setSappps_active(final SessionContext ctx, final SAPConfiguration item, final boolean value)
	{
		setSappps_active( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_active</code> attribute. 
	 * @param value the sappps_active - PPS active
	 */
	public void setSappps_active(final SAPConfiguration item, final boolean value)
	{
		setSappps_active( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_businessUnitId</code> attribute.
	 * @return the sappps_businessUnitId - ID of Business Unit for which calculation shall be done
	 */
	public String getSappps_businessUnitId(final SessionContext ctx, final GenericItem item)
	{
		return (String)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_BUSINESSUNITID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_businessUnitId</code> attribute.
	 * @return the sappps_businessUnitId - ID of Business Unit for which calculation shall be done
	 */
	public String getSappps_businessUnitId(final SAPConfiguration item)
	{
		return getSappps_businessUnitId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_businessUnitId</code> attribute. 
	 * @param value the sappps_businessUnitId - ID of Business Unit for which calculation shall be done
	 */
	public void setSappps_businessUnitId(final SessionContext ctx, final GenericItem item, final String value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_BUSINESSUNITID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_businessUnitId</code> attribute. 
	 * @param value the sappps_businessUnitId - ID of Business Unit for which calculation shall be done
	 */
	public void setSappps_businessUnitId(final SAPConfiguration item, final String value)
	{
		setSappps_businessUnitId( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute.
	 * @return the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public Boolean isSappps_cacheCatalogPrices(final SessionContext ctx, final GenericItem item)
	{
		return (Boolean)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_CACHECATALOGPRICES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute.
	 * @return the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public Boolean isSappps_cacheCatalogPrices(final SAPConfiguration item)
	{
		return isSappps_cacheCatalogPrices( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @return the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public boolean isSappps_cacheCatalogPricesAsPrimitive(final SessionContext ctx, final SAPConfiguration item)
	{
		Boolean value = isSappps_cacheCatalogPrices( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @return the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public boolean isSappps_cacheCatalogPricesAsPrimitive(final SAPConfiguration item)
	{
		return isSappps_cacheCatalogPricesAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @param value the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public void setSappps_cacheCatalogPrices(final SessionContext ctx, final GenericItem item, final Boolean value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_CACHECATALOGPRICES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @param value the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public void setSappps_cacheCatalogPrices(final SAPConfiguration item, final Boolean value)
	{
		setSappps_cacheCatalogPrices( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @param value the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public void setSappps_cacheCatalogPrices(final SessionContext ctx, final SAPConfiguration item, final boolean value)
	{
		setSappps_cacheCatalogPrices( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_cacheCatalogPrices</code> attribute. 
	 * @param value the sappps_cacheCatalogPrices - Cache catalog prices
	 */
	public void setSappps_cacheCatalogPrices(final SAPConfiguration item, final boolean value)
	{
		setSappps_cacheCatalogPrices( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPHTTPDestination.sappps_Configurations</code> attribute.
	 * @return the sappps_Configurations
	 */
	public Collection<SAPConfiguration> getSappps_Configurations(final SessionContext ctx, final GenericItem item)
	{
		return SAPHTTPDESTINATIONFORPPSSAPPPS_CONFIGURATIONSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPHTTPDestination.sappps_Configurations</code> attribute.
	 * @return the sappps_Configurations
	 */
	public Collection<SAPConfiguration> getSappps_Configurations(final SAPHTTPDestination item)
	{
		return getSappps_Configurations( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPHTTPDestination.sappps_Configurations</code> attribute. 
	 * @param value the sappps_Configurations
	 */
	public void setSappps_Configurations(final SessionContext ctx, final GenericItem item, final Collection<SAPConfiguration> value)
	{
		SAPHTTPDESTINATIONFORPPSSAPPPS_CONFIGURATIONSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPHTTPDestination.sappps_Configurations</code> attribute. 
	 * @param value the sappps_Configurations
	 */
	public void setSappps_Configurations(final SAPHTTPDestination item, final Collection<SAPConfiguration> value)
	{
		setSappps_Configurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sappps_Configurations. 
	 * @param value the item to add to sappps_Configurations
	 */
	public void addToSappps_Configurations(final SessionContext ctx, final GenericItem item, final SAPConfiguration value)
	{
		SAPHTTPDESTINATIONFORPPSSAPPPS_CONFIGURATIONSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to sappps_Configurations. 
	 * @param value the item to add to sappps_Configurations
	 */
	public void addToSappps_Configurations(final SAPHTTPDestination item, final SAPConfiguration value)
	{
		addToSappps_Configurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sappps_Configurations. 
	 * @param value the item to remove from sappps_Configurations
	 */
	public void removeFromSappps_Configurations(final SessionContext ctx, final GenericItem item, final SAPConfiguration value)
	{
		SAPHTTPDESTINATIONFORPPSSAPPPS_CONFIGURATIONSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from sappps_Configurations. 
	 * @param value the item to remove from sappps_Configurations
	 */
	public void removeFromSappps_Configurations(final SAPHTTPDestination item, final SAPConfiguration value)
	{
		removeFromSappps_Configurations( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_HttpDestination</code> attribute.
	 * @return the sappps_HttpDestination
	 */
	public SAPHTTPDestination getSappps_HttpDestination(final SessionContext ctx, final GenericItem item)
	{
		return (SAPHTTPDestination)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_HTTPDESTINATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_HttpDestination</code> attribute.
	 * @return the sappps_HttpDestination
	 */
	public SAPHTTPDestination getSappps_HttpDestination(final SAPConfiguration item)
	{
		return getSappps_HttpDestination( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_HttpDestination</code> attribute. 
	 * @param value the sappps_HttpDestination
	 */
	public void setSappps_HttpDestination(final SessionContext ctx, final GenericItem item, final SAPHTTPDestination value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_HTTPDESTINATION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_HttpDestination</code> attribute. 
	 * @param value the sappps_HttpDestination
	 */
	public void setSappps_HttpDestination(final SAPConfiguration item, final SAPHTTPDestination value)
	{
		setSappps_HttpDestination( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_interfaceVersion</code> attribute.
	 * @return the sappps_interfaceVersion - Client Interface Version
	 */
	public EnumerationValue getSappps_interfaceVersion(final SessionContext ctx, final GenericItem item)
	{
		return (EnumerationValue)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_INTERFACEVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_interfaceVersion</code> attribute.
	 * @return the sappps_interfaceVersion - Client Interface Version
	 */
	public EnumerationValue getSappps_interfaceVersion(final SAPConfiguration item)
	{
		return getSappps_interfaceVersion( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_interfaceVersion</code> attribute. 
	 * @param value the sappps_interfaceVersion - Client Interface Version
	 */
	public void setSappps_interfaceVersion(final SessionContext ctx, final GenericItem item, final EnumerationValue value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_INTERFACEVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_interfaceVersion</code> attribute. 
	 * @param value the sappps_interfaceVersion - Client Interface Version
	 */
	public void setSappps_interfaceVersion(final SAPConfiguration item, final EnumerationValue value)
	{
		setSappps_interfaceVersion( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_localRemote</code> attribute.
	 * @return the sappps_localRemote - PPS Mode (Local/Remote)
	 */
	public EnumerationValue getSappps_localRemote(final SessionContext ctx, final GenericItem item)
	{
		return (EnumerationValue)item.getProperty( ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_LOCALREMOTE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SAPConfiguration.sappps_localRemote</code> attribute.
	 * @return the sappps_localRemote - PPS Mode (Local/Remote)
	 */
	public EnumerationValue getSappps_localRemote(final SAPConfiguration item)
	{
		return getSappps_localRemote( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_localRemote</code> attribute. 
	 * @param value the sappps_localRemote - PPS Mode (Local/Remote)
	 */
	public void setSappps_localRemote(final SessionContext ctx, final GenericItem item, final EnumerationValue value)
	{
		item.setProperty(ctx, SapppspricingConstants.Attributes.SAPConfiguration.SAPPPS_LOCALREMOTE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SAPConfiguration.sappps_localRemote</code> attribute. 
	 * @param value the sappps_localRemote - PPS Mode (Local/Remote)
	 */
	public void setSappps_localRemote(final SAPConfiguration item, final EnumerationValue value)
	{
		setSappps_localRemote( getSession().getSessionContext(), item, value );
	}
	
}

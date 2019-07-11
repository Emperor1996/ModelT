/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.isce.frontend.jalo;

import com.sap.retail.isce.frontend.constants.YinstorecsfrontendaddonConstants;
import com.sap.retail.isce.frontend.jalo.actions.TakeHomeNowAction;
import com.sap.retail.isce.frontend.jalo.components.OrderDetailPickupComponent;
import com.sap.retail.isce.jalo.CMSISCECustomer360Component;
import com.sap.retail.isce.jalo.CMSISCEPurchaseHistoryComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>YinstorecsfrontendaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedYinstorecsfrontendaddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
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
	
	public CMSISCECustomer360Component createCMSISCECustomer360Component(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( YinstorecsfrontendaddonConstants.TC.CMSISCECUSTOMER360COMPONENT );
			return (CMSISCECustomer360Component)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CMSISCECustomer360Component : "+e.getMessage(), 0 );
		}
	}
	
	public CMSISCECustomer360Component createCMSISCECustomer360Component(final Map attributeValues)
	{
		return createCMSISCECustomer360Component( getSession().getSessionContext(), attributeValues );
	}
	
	public CMSISCEPurchaseHistoryComponent createCMSISCEPurchaseHistoryComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( YinstorecsfrontendaddonConstants.TC.CMSISCEPURCHASEHISTORYCOMPONENT );
			return (CMSISCEPurchaseHistoryComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating CMSISCEPurchaseHistoryComponent : "+e.getMessage(), 0 );
		}
	}
	
	public CMSISCEPurchaseHistoryComponent createCMSISCEPurchaseHistoryComponent(final Map attributeValues)
	{
		return createCMSISCEPurchaseHistoryComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public OrderDetailPickupComponent createOrderDetailPickupComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( YinstorecsfrontendaddonConstants.TC.ORDERDETAILPICKUPCOMPONENT );
			return (OrderDetailPickupComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating OrderDetailPickupComponent : "+e.getMessage(), 0 );
		}
	}
	
	public OrderDetailPickupComponent createOrderDetailPickupComponent(final Map attributeValues)
	{
		return createOrderDetailPickupComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public TakeHomeNowAction createTakeHomeNowAction(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( YinstorecsfrontendaddonConstants.TC.TAKEHOMENOWACTION );
			return (TakeHomeNowAction)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating TakeHomeNowAction : "+e.getMessage(), 0 );
		}
	}
	
	public TakeHomeNowAction createTakeHomeNowAction(final Map attributeValues)
	{
		return createTakeHomeNowAction( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return YinstorecsfrontendaddonConstants.EXTENSIONNAME;
	}
	
}

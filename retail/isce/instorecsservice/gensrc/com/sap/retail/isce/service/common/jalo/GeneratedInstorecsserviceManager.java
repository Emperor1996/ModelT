/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.isce.service.common.jalo;

import com.sap.retail.isce.service.common.constants.InstorecsserviceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>InstorecsserviceManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedInstorecsserviceManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("employeeId", AttributeMode.INITIAL);
		tmp.put("createdInAsmMode", AttributeMode.INITIAL);
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute.
	 * @return the createdInAsmMode - Store Associate User Id
	 */
	public Boolean isCreatedInAsmMode(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (Boolean)item.getProperty( ctx, InstorecsserviceConstants.Attributes.AbstractOrderEntry.CREATEDINASMMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute.
	 * @return the createdInAsmMode - Store Associate User Id
	 */
	public Boolean isCreatedInAsmMode(final AbstractOrderEntry item)
	{
		return isCreatedInAsmMode( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @return the createdInAsmMode - Store Associate User Id
	 */
	public boolean isCreatedInAsmModeAsPrimitive(final SessionContext ctx, final AbstractOrderEntry item)
	{
		Boolean value = isCreatedInAsmMode( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @return the createdInAsmMode - Store Associate User Id
	 */
	public boolean isCreatedInAsmModeAsPrimitive(final AbstractOrderEntry item)
	{
		return isCreatedInAsmModeAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @param value the createdInAsmMode - Store Associate User Id
	 */
	public void setCreatedInAsmMode(final SessionContext ctx, final AbstractOrderEntry item, final Boolean value)
	{
		item.setProperty(ctx, InstorecsserviceConstants.Attributes.AbstractOrderEntry.CREATEDINASMMODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @param value the createdInAsmMode - Store Associate User Id
	 */
	public void setCreatedInAsmMode(final AbstractOrderEntry item, final Boolean value)
	{
		setCreatedInAsmMode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @param value the createdInAsmMode - Store Associate User Id
	 */
	public void setCreatedInAsmMode(final SessionContext ctx, final AbstractOrderEntry item, final boolean value)
	{
		setCreatedInAsmMode( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.createdInAsmMode</code> attribute. 
	 * @param value the createdInAsmMode - Store Associate User Id
	 */
	public void setCreatedInAsmMode(final AbstractOrderEntry item, final boolean value)
	{
		setCreatedInAsmMode( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.employeeId</code> attribute.
	 * @return the employeeId - Store Associate User Id
	 */
	public String getEmployeeId(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (String)item.getProperty( ctx, InstorecsserviceConstants.Attributes.AbstractOrderEntry.EMPLOYEEID);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.employeeId</code> attribute.
	 * @return the employeeId - Store Associate User Id
	 */
	public String getEmployeeId(final AbstractOrderEntry item)
	{
		return getEmployeeId( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.employeeId</code> attribute. 
	 * @param value the employeeId - Store Associate User Id
	 */
	public void setEmployeeId(final SessionContext ctx, final AbstractOrderEntry item, final String value)
	{
		item.setProperty(ctx, InstorecsserviceConstants.Attributes.AbstractOrderEntry.EMPLOYEEID,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.employeeId</code> attribute. 
	 * @param value the employeeId - Store Associate User Id
	 */
	public void setEmployeeId(final AbstractOrderEntry item, final String value)
	{
		setEmployeeId( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return InstorecsserviceConstants.EXTENSIONNAME;
	}
	
}

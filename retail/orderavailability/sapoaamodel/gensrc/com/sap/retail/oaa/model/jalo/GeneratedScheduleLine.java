/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 30, 2019 3:02:53 PM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.oaa.model.jalo;

import com.sap.retail.oaa.model.constants.SapoaamodelConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.sap.retail.oaa.model.jalo.ScheduleLine ScheduleLine}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedScheduleLine extends GenericItem
{
	/** Qualifier of the <code>ScheduleLine.confirmedQuantity</code> attribute **/
	public static final String CONFIRMEDQUANTITY = "confirmedQuantity";
	/** Qualifier of the <code>ScheduleLine.confirmedDate</code> attribute **/
	public static final String CONFIRMEDDATE = "confirmedDate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CONFIRMEDQUANTITY, AttributeMode.INITIAL);
		tmp.put(CONFIRMEDDATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedDate</code> attribute.
	 * @return the confirmedDate - Date
	 */
	public Date getConfirmedDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, CONFIRMEDDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedDate</code> attribute.
	 * @return the confirmedDate - Date
	 */
	public Date getConfirmedDate()
	{
		return getConfirmedDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedDate</code> attribute. 
	 * @param value the confirmedDate - Date
	 */
	public void setConfirmedDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, CONFIRMEDDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedDate</code> attribute. 
	 * @param value the confirmedDate - Date
	 */
	public void setConfirmedDate(final Date value)
	{
		setConfirmedDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedQuantity</code> attribute.
	 * @return the confirmedQuantity - Confirmed Quantity
	 */
	public Double getConfirmedQuantity(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, CONFIRMEDQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedQuantity</code> attribute.
	 * @return the confirmedQuantity - Confirmed Quantity
	 */
	public Double getConfirmedQuantity()
	{
		return getConfirmedQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @return the confirmedQuantity - Confirmed Quantity
	 */
	public double getConfirmedQuantityAsPrimitive(final SessionContext ctx)
	{
		Double value = getConfirmedQuantity( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @return the confirmedQuantity - Confirmed Quantity
	 */
	public double getConfirmedQuantityAsPrimitive()
	{
		return getConfirmedQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @param value the confirmedQuantity - Confirmed Quantity
	 */
	public void setConfirmedQuantity(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, CONFIRMEDQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @param value the confirmedQuantity - Confirmed Quantity
	 */
	public void setConfirmedQuantity(final Double value)
	{
		setConfirmedQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @param value the confirmedQuantity - Confirmed Quantity
	 */
	public void setConfirmedQuantity(final SessionContext ctx, final double value)
	{
		setConfirmedQuantity( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ScheduleLine.confirmedQuantity</code> attribute. 
	 * @param value the confirmedQuantity - Confirmed Quantity
	 */
	public void setConfirmedQuantity(final double value)
	{
		setConfirmedQuantity( getSession().getSessionContext(), value );
	}
	
}

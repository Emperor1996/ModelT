/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.sapppspricing.jalo;

import com.sap.retail.sapppspricing.constants.SapppspricingConstants;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.sap.retail.sapppspricing.jalo.DeletePromotionsCronJob DeletePromotionsCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedDeletePromotionsCronJob extends CronJob
{
	/** Qualifier of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute **/
	public static final String DAYSSINCEEXPIRY = "daysSinceExpiry";
	/** Qualifier of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute **/
	public static final String DELETEACTIVEPROMOTIONS = "deleteActivePromotions";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(DAYSSINCEEXPIRY, AttributeMode.INITIAL);
		tmp.put(DELETEACTIVEPROMOTIONS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute.
	 * @return the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public Integer getDaysSinceExpiry(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, DAYSSINCEEXPIRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute.
	 * @return the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public Integer getDaysSinceExpiry()
	{
		return getDaysSinceExpiry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @return the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public int getDaysSinceExpiryAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDaysSinceExpiry( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @return the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public int getDaysSinceExpiryAsPrimitive()
	{
		return getDaysSinceExpiryAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @param value the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public void setDaysSinceExpiry(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, DAYSSINCEEXPIRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @param value the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public void setDaysSinceExpiry(final Integer value)
	{
		setDaysSinceExpiry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @param value the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public void setDaysSinceExpiry(final SessionContext ctx, final int value)
	{
		setDaysSinceExpiry( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.daysSinceExpiry</code> attribute. 
	 * @param value the daysSinceExpiry - Minimum number of days since the promotion has expired
	 */
	public void setDaysSinceExpiry(final int value)
	{
		setDaysSinceExpiry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute.
	 * @return the deleteActivePromotions - Delete also active promotions
	 */
	public Boolean isDeleteActivePromotions(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, DELETEACTIVEPROMOTIONS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute.
	 * @return the deleteActivePromotions - Delete also active promotions
	 */
	public Boolean isDeleteActivePromotions()
	{
		return isDeleteActivePromotions( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @return the deleteActivePromotions - Delete also active promotions
	 */
	public boolean isDeleteActivePromotionsAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isDeleteActivePromotions( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @return the deleteActivePromotions - Delete also active promotions
	 */
	public boolean isDeleteActivePromotionsAsPrimitive()
	{
		return isDeleteActivePromotionsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @param value the deleteActivePromotions - Delete also active promotions
	 */
	public void setDeleteActivePromotions(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, DELETEACTIVEPROMOTIONS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @param value the deleteActivePromotions - Delete also active promotions
	 */
	public void setDeleteActivePromotions(final Boolean value)
	{
		setDeleteActivePromotions( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @param value the deleteActivePromotions - Delete also active promotions
	 */
	public void setDeleteActivePromotions(final SessionContext ctx, final boolean value)
	{
		setDeleteActivePromotions( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePromotionsCronJob.deleteActivePromotions</code> attribute. 
	 * @param value the deleteActivePromotions - Delete also active promotions
	 */
	public void setDeleteActivePromotions(final boolean value)
	{
		setDeleteActivePromotions( getSession().getSessionContext(), value );
	}
	
}

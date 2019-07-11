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
 * Generated class for type {@link com.sap.retail.sapppspricing.jalo.DeletePricesCronJob DeletePricesCronJob}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedDeletePricesCronJob extends CronJob
{
	/** Qualifier of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute **/
	public static final String DAYSSINCEEXPIRYPRICE = "daysSinceExpiryPrice";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(DAYSSINCEEXPIRYPRICE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute.
	 * @return the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public Integer getDaysSinceExpiryPrice(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, DAYSSINCEEXPIRYPRICE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute.
	 * @return the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public Integer getDaysSinceExpiryPrice()
	{
		return getDaysSinceExpiryPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @return the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public int getDaysSinceExpiryPriceAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDaysSinceExpiryPrice( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @return the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public int getDaysSinceExpiryPriceAsPrimitive()
	{
		return getDaysSinceExpiryPriceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @param value the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public void setDaysSinceExpiryPrice(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, DAYSSINCEEXPIRYPRICE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @param value the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public void setDaysSinceExpiryPrice(final Integer value)
	{
		setDaysSinceExpiryPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @param value the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public void setDaysSinceExpiryPrice(final SessionContext ctx, final int value)
	{
		setDaysSinceExpiryPrice( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>DeletePricesCronJob.daysSinceExpiryPrice</code> attribute. 
	 * @param value the daysSinceExpiryPrice - Minimum number of days since the regular price has expired
	 */
	public void setDaysSinceExpiryPrice(final int value)
	{
		setDaysSinceExpiryPrice( getSession().getSessionContext(), value );
	}
	
}

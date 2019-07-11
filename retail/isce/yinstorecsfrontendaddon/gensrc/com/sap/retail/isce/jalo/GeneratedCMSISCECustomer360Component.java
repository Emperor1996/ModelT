/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.isce.jalo;

import com.sap.retail.isce.frontend.constants.YinstorecsfrontendaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.sap.retail.isce.jalo.CMSISCECustomer360Component CMSISCECustomer360Component}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCMSISCECustomer360Component extends SimpleCMSComponent
{
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360GenericScores</code> attribute **/
	public static final String CMSISCECUSTOMER360GENERICSCORES = "CMSISCECustomer360GenericScores";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute **/
	public static final String CMSISCECUSTOMER360SALESVOLUMELEVEL01 = "CMSISCECustomer360SalesVolumeLevel01";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute **/
	public static final String CMSISCECUSTOMER360SALESVOLUMELEVEL02 = "CMSISCECustomer360SalesVolumeLevel02";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute **/
	public static final String CMSISCECUSTOMER360SALESVOLUMELEVEL03 = "CMSISCECustomer360SalesVolumeLevel03";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute **/
	public static final String CMSISCECUSTOMER360SALESVOLUMELEVEL04 = "CMSISCECustomer360SalesVolumeLevel04";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute **/
	public static final String CMSISCECUSTOMER360STOREONLINERATIOLEVEL01 = "CMSISCECustomer360StoreOnlineRatioLevel01";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute **/
	public static final String CMSISCECUSTOMER360STOREONLINERATIOLEVEL02 = "CMSISCECustomer360StoreOnlineRatioLevel02";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute **/
	public static final String CMSISCECUSTOMER360STOREONLINERATIOLEVEL03 = "CMSISCECustomer360StoreOnlineRatioLevel03";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute **/
	public static final String CMSISCECUSTOMER360STOREONLINERATIOLEVEL04 = "CMSISCECustomer360StoreOnlineRatioLevel04";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute **/
	public static final String CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL01 = "CMSISCECustomer360AveragePurchaseVolumeLevel01";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute **/
	public static final String CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL02 = "CMSISCECustomer360AveragePurchaseVolumeLevel02";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute **/
	public static final String CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL03 = "CMSISCECustomer360AveragePurchaseVolumeLevel03";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute **/
	public static final String CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL04 = "CMSISCECustomer360AveragePurchaseVolumeLevel04";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute **/
	public static final String CMSISCECUSTOMER360ACTIVITYSCORELEVEL01 = "CMSISCECustomer360ActivityScoreLevel01";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute **/
	public static final String CMSISCECUSTOMER360ACTIVITYSCORELEVEL02 = "CMSISCECustomer360ActivityScoreLevel02";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute **/
	public static final String CMSISCECUSTOMER360ACTIVITYSCORELEVEL03 = "CMSISCECustomer360ActivityScoreLevel03";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute **/
	public static final String CMSISCECUSTOMER360ACTIVITYSCORELEVEL04 = "CMSISCECustomer360ActivityScoreLevel04";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute **/
	public static final String CMSISCECUSTOMER360LASTPURCHASEDATELEVEL01 = "CMSISCECustomer360LastPurchaseDateLevel01";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute **/
	public static final String CMSISCECUSTOMER360LASTPURCHASEDATELEVEL02 = "CMSISCECustomer360LastPurchaseDateLevel02";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute **/
	public static final String CMSISCECUSTOMER360LASTPURCHASEDATELEVEL03 = "CMSISCECustomer360LastPurchaseDateLevel03";
	/** Qualifier of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute **/
	public static final String CMSISCECUSTOMER360LASTPURCHASEDATELEVEL04 = "CMSISCECustomer360LastPurchaseDateLevel04";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CMSISCECUSTOMER360GENERICSCORES, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360SALESVOLUMELEVEL01, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360SALESVOLUMELEVEL02, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360SALESVOLUMELEVEL03, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360SALESVOLUMELEVEL04, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360STOREONLINERATIOLEVEL01, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360STOREONLINERATIOLEVEL02, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360STOREONLINERATIOLEVEL03, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360STOREONLINERATIOLEVEL04, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL01, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL02, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL03, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL04, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360ACTIVITYSCORELEVEL01, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360ACTIVITYSCORELEVEL02, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360ACTIVITYSCORELEVEL03, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360ACTIVITYSCORELEVEL04, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360LASTPURCHASEDATELEVEL01, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360LASTPURCHASEDATELEVEL02, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360LASTPURCHASEDATELEVEL03, AttributeMode.INITIAL);
		tmp.put(CMSISCECUSTOMER360LASTPURCHASEDATELEVEL04, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel01(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL01);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel01()
	{
		return getCMSISCECustomer360ActivityScoreLevel01( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel01AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360ActivityScoreLevel01( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel01AsPrimitive()
	{
		return getCMSISCECustomer360ActivityScoreLevel01AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel01(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL01,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel01(final Integer value)
	{
		setCMSISCECustomer360ActivityScoreLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel01(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel01( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel01 - Attribute that stores the Activity Score Level 1.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel01(final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel02(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL02);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel02()
	{
		return getCMSISCECustomer360ActivityScoreLevel02( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel02AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360ActivityScoreLevel02( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel02AsPrimitive()
	{
		return getCMSISCECustomer360ActivityScoreLevel02AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel02(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL02,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel02(final Integer value)
	{
		setCMSISCECustomer360ActivityScoreLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel02(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel02( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel02 - Attribute that stores the Activity Score Level 2.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel02(final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel03(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL03);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel03()
	{
		return getCMSISCECustomer360ActivityScoreLevel03( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel03AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360ActivityScoreLevel03( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel03AsPrimitive()
	{
		return getCMSISCECustomer360ActivityScoreLevel03AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel03(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL03,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel03(final Integer value)
	{
		setCMSISCECustomer360ActivityScoreLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel03(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel03( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel03 - Attribute that stores the Activity Score Level 3.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel03(final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel04(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL04);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute.
	 * @return the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public Integer getCMSISCECustomer360ActivityScoreLevel04()
	{
		return getCMSISCECustomer360ActivityScoreLevel04( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel04AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360ActivityScoreLevel04( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @return the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public int getCMSISCECustomer360ActivityScoreLevel04AsPrimitive()
	{
		return getCMSISCECustomer360ActivityScoreLevel04AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel04(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360ACTIVITYSCORELEVEL04,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel04(final Integer value)
	{
		setCMSISCECustomer360ActivityScoreLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel04(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel04( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360ActivityScoreLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360ActivityScoreLevel04 - Attribute that stores the Activity Score Level 4.
	 */
	public void setCMSISCECustomer360ActivityScoreLevel04(final int value)
	{
		setCMSISCECustomer360ActivityScoreLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel01(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL01);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel01()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel01( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel01AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360AveragePurchaseVolumeLevel01( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel01AsPrimitive()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel01AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel01(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL01,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel01(final Integer value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel01(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel01( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel01 - Attribute that stores the Average Purchase Volume Level 1.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel01(final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel02(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL02);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel02()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel02( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel02AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360AveragePurchaseVolumeLevel02( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel02AsPrimitive()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel02AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel02(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL02,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel02(final Integer value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel02(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel02( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel02 - Attribute that stores the Average Purchase Volume Level 2.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel02(final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel03(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL03);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel03()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel03( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel03AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360AveragePurchaseVolumeLevel03( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel03AsPrimitive()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel03AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel03(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL03,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel03(final Integer value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel03(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel03( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel03 - Attribute that stores the Average Purchase Volume Level 3.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel03(final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel04(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL04);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute.
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public Integer getCMSISCECustomer360AveragePurchaseVolumeLevel04()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel04( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel04AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360AveragePurchaseVolumeLevel04( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @return the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public int getCMSISCECustomer360AveragePurchaseVolumeLevel04AsPrimitive()
	{
		return getCMSISCECustomer360AveragePurchaseVolumeLevel04AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel04(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360AVERAGEPURCHASEVOLUMELEVEL04,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel04(final Integer value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel04(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel04( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360AveragePurchaseVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360AveragePurchaseVolumeLevel04 - Attribute that stores the Average Purchase Volume Level 4.
	 */
	public void setCMSISCECustomer360AveragePurchaseVolumeLevel04(final int value)
	{
		setCMSISCECustomer360AveragePurchaseVolumeLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360GenericScores</code> attribute.
	 * @return the CMSISCECustomer360GenericScores - Attribute that stores the generic scores.
	 */
	public String getCMSISCECustomer360GenericScores(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CMSISCECUSTOMER360GENERICSCORES);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360GenericScores</code> attribute.
	 * @return the CMSISCECustomer360GenericScores - Attribute that stores the generic scores.
	 */
	public String getCMSISCECustomer360GenericScores()
	{
		return getCMSISCECustomer360GenericScores( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360GenericScores</code> attribute. 
	 * @param value the CMSISCECustomer360GenericScores - Attribute that stores the generic scores.
	 */
	public void setCMSISCECustomer360GenericScores(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CMSISCECUSTOMER360GENERICSCORES,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360GenericScores</code> attribute. 
	 * @param value the CMSISCECustomer360GenericScores - Attribute that stores the generic scores.
	 */
	public void setCMSISCECustomer360GenericScores(final String value)
	{
		setCMSISCECustomer360GenericScores( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel01(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL01);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel01()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel01( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel01AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360LastPurchaseDateLevel01( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel01AsPrimitive()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel01AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel01(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL01,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel01(final Integer value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel01(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel01( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel01 - Attribute that stores the Purchase Date Level 1.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel01(final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel02(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL02);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel02()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel02( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel02AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360LastPurchaseDateLevel02( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel02AsPrimitive()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel02AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel02(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL02,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel02(final Integer value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel02(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel02( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel02 - Attribute that stores the Last Purchase Date Level 2.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel02(final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel03(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL03);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel03()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel03( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel03AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360LastPurchaseDateLevel03( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel03AsPrimitive()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel03AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel03(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL03,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel03(final Integer value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel03(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel03( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel03 - Attribute that stores the Last Purchase Date Level 3.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel03(final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel04(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL04);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute.
	 * @return the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public Integer getCMSISCECustomer360LastPurchaseDateLevel04()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel04( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel04AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360LastPurchaseDateLevel04( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @return the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public int getCMSISCECustomer360LastPurchaseDateLevel04AsPrimitive()
	{
		return getCMSISCECustomer360LastPurchaseDateLevel04AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel04(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360LASTPURCHASEDATELEVEL04,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel04(final Integer value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel04(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel04( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360LastPurchaseDateLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360LastPurchaseDateLevel04 - Attribute that stores the Last Purchase Date Level 4.
	 */
	public void setCMSISCECustomer360LastPurchaseDateLevel04(final int value)
	{
		setCMSISCECustomer360LastPurchaseDateLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel01(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL01);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel01()
	{
		return getCMSISCECustomer360SalesVolumeLevel01( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel01AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360SalesVolumeLevel01( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel01AsPrimitive()
	{
		return getCMSISCECustomer360SalesVolumeLevel01AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel01(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL01,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel01(final Integer value)
	{
		setCMSISCECustomer360SalesVolumeLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel01(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel01( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel01 - Attribute that stores the Sales Volume Level 1.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel01(final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel02(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL02);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel02()
	{
		return getCMSISCECustomer360SalesVolumeLevel02( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel02AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360SalesVolumeLevel02( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel02AsPrimitive()
	{
		return getCMSISCECustomer360SalesVolumeLevel02AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel02(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL02,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel02(final Integer value)
	{
		setCMSISCECustomer360SalesVolumeLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel02(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel02( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel02 - Attribute that stores the Sales Volume Level 2.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel02(final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel03(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL03);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel03()
	{
		return getCMSISCECustomer360SalesVolumeLevel03( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel03AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360SalesVolumeLevel03( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel03AsPrimitive()
	{
		return getCMSISCECustomer360SalesVolumeLevel03AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel03(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL03,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel03(final Integer value)
	{
		setCMSISCECustomer360SalesVolumeLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel03(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel03( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel03 - Attribute that stores the Sales Volume Level 3.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel03(final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel04(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL04);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute.
	 * @return the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public Integer getCMSISCECustomer360SalesVolumeLevel04()
	{
		return getCMSISCECustomer360SalesVolumeLevel04( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel04AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360SalesVolumeLevel04( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @return the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public int getCMSISCECustomer360SalesVolumeLevel04AsPrimitive()
	{
		return getCMSISCECustomer360SalesVolumeLevel04AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel04(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360SALESVOLUMELEVEL04,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel04(final Integer value)
	{
		setCMSISCECustomer360SalesVolumeLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel04(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel04( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360SalesVolumeLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360SalesVolumeLevel04 - Attribute that stores the Sales Volume Level 4.
	 */
	public void setCMSISCECustomer360SalesVolumeLevel04(final int value)
	{
		setCMSISCECustomer360SalesVolumeLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel01(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL01);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel01()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel01( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel01AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360StoreOnlineRatioLevel01( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel01AsPrimitive()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel01AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel01(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL01,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel01(final Integer value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel01(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel01( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel01</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel01 - Attribute that stores the Store Online Ratio Level 1.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel01(final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel01( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel02(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL02);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel02()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel02( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel02AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360StoreOnlineRatioLevel02( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel02AsPrimitive()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel02AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel02(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL02,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel02(final Integer value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel02(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel02( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel02</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel02 - Attribute that stores the Store Online Ratio Level 2.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel02(final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel02( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel03(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL03);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel03()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel03( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel03AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360StoreOnlineRatioLevel03( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel03AsPrimitive()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel03AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel03(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL03,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel03(final Integer value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel03(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel03( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel03</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel03 - Attribute that stores the Store Online Ratio Level 3.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel03(final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel03( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel04(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL04);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute.
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public Integer getCMSISCECustomer360StoreOnlineRatioLevel04()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel04( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel04AsPrimitive(final SessionContext ctx)
	{
		Integer value = getCMSISCECustomer360StoreOnlineRatioLevel04( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @return the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public int getCMSISCECustomer360StoreOnlineRatioLevel04AsPrimitive()
	{
		return getCMSISCECustomer360StoreOnlineRatioLevel04AsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel04(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, CMSISCECUSTOMER360STOREONLINERATIOLEVEL04,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel04(final Integer value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel04( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel04(final SessionContext ctx, final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel04( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSISCECustomer360Component.CMSISCECustomer360StoreOnlineRatioLevel04</code> attribute. 
	 * @param value the CMSISCECustomer360StoreOnlineRatioLevel04 - Attribute that stores the Store Online Ratio Level 4.
	 */
	public void setCMSISCECustomer360StoreOnlineRatioLevel04(final int value)
	{
		setCMSISCECustomer360StoreOnlineRatioLevel04( getSession().getSessionContext(), value );
	}
	
}

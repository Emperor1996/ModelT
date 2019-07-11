/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.commercesuite.saparticlemodel.jalo;

import com.sap.retail.commercesuite.saparticlemodel.constants.SaparticlemodelConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem ArticleComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedArticleComponent extends GenericItem
{
	/** Qualifier of the <code>ArticleComponent.component</code> attribute **/
	public static final String COMPONENT = "component";
	/** Qualifier of the <code>ArticleComponent.quantity</code> attribute **/
	public static final String QUANTITY = "quantity";
	/** Qualifier of the <code>ArticleComponent.unit</code> attribute **/
	public static final String UNIT = "unit";
	/** Qualifier of the <code>ArticleComponent.structuredArticlePOS</code> attribute **/
	public static final String STRUCTUREDARTICLEPOS = "structuredArticlePOS";
	/** Qualifier of the <code>ArticleComponent.structuredArticle</code> attribute **/
	public static final String STRUCTUREDARTICLE = "structuredArticle";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n STRUCTUREDARTICLE's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedArticleComponent> STRUCTUREDARTICLEHANDLER = new BidirectionalOneToManyHandler<GeneratedArticleComponent>(
	SaparticlemodelConstants.TC.ARTICLECOMPONENT,
	false,
	"structuredArticle",
	"structuredArticlePOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(COMPONENT, AttributeMode.INITIAL);
		tmp.put(QUANTITY, AttributeMode.INITIAL);
		tmp.put(UNIT, AttributeMode.INITIAL);
		tmp.put(STRUCTUREDARTICLEPOS, AttributeMode.INITIAL);
		tmp.put(STRUCTUREDARTICLE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.component</code> attribute.
	 * @return the component - Component Article
	 */
	public Product getComponent(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, COMPONENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.component</code> attribute.
	 * @return the component - Component Article
	 */
	public Product getComponent()
	{
		return getComponent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.component</code> attribute. 
	 * @param value the component - Component Article
	 */
	public void setComponent(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, COMPONENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.component</code> attribute. 
	 * @param value the component - Component Article
	 */
	public void setComponent(final Product value)
	{
		setComponent( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		STRUCTUREDARTICLEHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.quantity</code> attribute.
	 * @return the quantity - Quantity
	 */
	public Integer getQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, QUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.quantity</code> attribute.
	 * @return the quantity - Quantity
	 */
	public Integer getQuantity()
	{
		return getQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @return the quantity - Quantity
	 */
	public int getQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @return the quantity - Quantity
	 */
	public int getQuantityAsPrimitive()
	{
		return getQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, QUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final Integer value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final SessionContext ctx, final int value)
	{
		setQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.quantity</code> attribute. 
	 * @param value the quantity - Quantity
	 */
	public void setQuantity(final int value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticle</code> attribute.
	 * @return the structuredArticle
	 */
	public Product getStructuredArticle(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, STRUCTUREDARTICLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticle</code> attribute.
	 * @return the structuredArticle
	 */
	public Product getStructuredArticle()
	{
		return getStructuredArticle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticle</code> attribute. 
	 * @param value the structuredArticle
	 */
	protected void setStructuredArticle(final SessionContext ctx, final Product value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+STRUCTUREDARTICLE+"' is not changeable", 0 );
		}
		STRUCTUREDARTICLEHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticle</code> attribute. 
	 * @param value the structuredArticle
	 */
	protected void setStructuredArticle(final Product value)
	{
		setStructuredArticle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticlePOS</code> attribute.
	 * @return the structuredArticlePOS
	 */
	 Integer getStructuredArticlePOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, STRUCTUREDARTICLEPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticlePOS</code> attribute.
	 * @return the structuredArticlePOS
	 */
	 Integer getStructuredArticlePOS()
	{
		return getStructuredArticlePOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @return the structuredArticlePOS
	 */
	 int getStructuredArticlePOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getStructuredArticlePOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @return the structuredArticlePOS
	 */
	 int getStructuredArticlePOSAsPrimitive()
	{
		return getStructuredArticlePOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @param value the structuredArticlePOS
	 */
	 void setStructuredArticlePOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, STRUCTUREDARTICLEPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @param value the structuredArticlePOS
	 */
	 void setStructuredArticlePOS(final Integer value)
	{
		setStructuredArticlePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @param value the structuredArticlePOS
	 */
	 void setStructuredArticlePOS(final SessionContext ctx, final int value)
	{
		setStructuredArticlePOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.structuredArticlePOS</code> attribute. 
	 * @param value the structuredArticlePOS
	 */
	 void setStructuredArticlePOS(final int value)
	{
		setStructuredArticlePOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.unit</code> attribute.
	 * @return the unit - Unit of measure
	 */
	public Unit getUnit(final SessionContext ctx)
	{
		return (Unit)getProperty( ctx, UNIT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ArticleComponent.unit</code> attribute.
	 * @return the unit - Unit of measure
	 */
	public Unit getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.unit</code> attribute. 
	 * @param value the unit - Unit of measure
	 */
	public void setUnit(final SessionContext ctx, final Unit value)
	{
		setProperty(ctx, UNIT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ArticleComponent.unit</code> attribute. 
	 * @param value the unit - Unit of measure
	 */
	public void setUnit(final Unit value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
}

/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.commercesuite.saparticlemodel.jalo;

import com.sap.retail.commercesuite.saparticlemodel.constants.SaparticlemodelConstants;
import com.sap.retail.commercesuite.saparticlemodel.jalo.ArticleComponent;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SaparticlemodelManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSaparticlemodelManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n COMPONENT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ArticleComponent> ARTICLECOMPONENTSCOMPONENTHANDLER = new OneToManyHandler<ArticleComponent>(
	SaparticlemodelConstants.TC.ARTICLECOMPONENT,
	true,
	"structuredArticle",
	"structuredArticlePOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("structuredArticleType", AttributeMode.INITIAL);
		tmp.put("discountable", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
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
	 * <i>Generated method</i> - Getter of the <code>Product.component</code> attribute.
	 * @return the component
	 */
	public Collection<ArticleComponent> getComponent(final SessionContext ctx, final Product item)
	{
		return ARTICLECOMPONENTSCOMPONENTHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.component</code> attribute.
	 * @return the component
	 */
	public Collection<ArticleComponent> getComponent(final Product item)
	{
		return getComponent( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.component</code> attribute. 
	 * @param value the component
	 */
	public void setComponent(final SessionContext ctx, final Product item, final Collection<ArticleComponent> value)
	{
		ARTICLECOMPONENTSCOMPONENTHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.component</code> attribute. 
	 * @param value the component
	 */
	public void setComponent(final Product item, final Collection<ArticleComponent> value)
	{
		setComponent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to component. 
	 * @param value the item to add to component
	 */
	public void addToComponent(final SessionContext ctx, final Product item, final ArticleComponent value)
	{
		ARTICLECOMPONENTSCOMPONENTHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to component. 
	 * @param value the item to add to component
	 */
	public void addToComponent(final Product item, final ArticleComponent value)
	{
		addToComponent( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from component. 
	 * @param value the item to remove from component
	 */
	public void removeFromComponent(final SessionContext ctx, final Product item, final ArticleComponent value)
	{
		ARTICLECOMPONENTSCOMPONENTHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from component. 
	 * @param value the item to remove from component
	 */
	public void removeFromComponent(final Product item, final ArticleComponent value)
	{
		removeFromComponent( getSession().getSessionContext(), item, value );
	}
	
	public ArticleComponent createArticleComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SaparticlemodelConstants.TC.ARTICLECOMPONENT );
			return (ArticleComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ArticleComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ArticleComponent createArticleComponent(final Map attributeValues)
	{
		return createArticleComponent( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discountable</code> attribute.
	 * @return the discountable - Discountable
	 */
	public Boolean isDiscountable(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, SaparticlemodelConstants.Attributes.Product.DISCOUNTABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discountable</code> attribute.
	 * @return the discountable - Discountable
	 */
	public Boolean isDiscountable(final Product item)
	{
		return isDiscountable( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discountable</code> attribute. 
	 * @return the discountable - Discountable
	 */
	public boolean isDiscountableAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isDiscountable( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.discountable</code> attribute. 
	 * @return the discountable - Discountable
	 */
	public boolean isDiscountableAsPrimitive(final Product item)
	{
		return isDiscountableAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discountable</code> attribute. 
	 * @param value the discountable - Discountable
	 */
	public void setDiscountable(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, SaparticlemodelConstants.Attributes.Product.DISCOUNTABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discountable</code> attribute. 
	 * @param value the discountable - Discountable
	 */
	public void setDiscountable(final Product item, final Boolean value)
	{
		setDiscountable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discountable</code> attribute. 
	 * @param value the discountable - Discountable
	 */
	public void setDiscountable(final SessionContext ctx, final Product item, final boolean value)
	{
		setDiscountable( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.discountable</code> attribute. 
	 * @param value the discountable - Discountable
	 */
	public void setDiscountable(final Product item, final boolean value)
	{
		setDiscountable( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return SaparticlemodelConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.structuredArticleType</code> attribute.
	 * @return the structuredArticleType - Article Type
	 */
	public EnumerationValue getStructuredArticleType(final SessionContext ctx, final Product item)
	{
		return (EnumerationValue)item.getProperty( ctx, SaparticlemodelConstants.Attributes.Product.STRUCTUREDARTICLETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.structuredArticleType</code> attribute.
	 * @return the structuredArticleType - Article Type
	 */
	public EnumerationValue getStructuredArticleType(final Product item)
	{
		return getStructuredArticleType( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.structuredArticleType</code> attribute. 
	 * @param value the structuredArticleType - Article Type
	 */
	public void setStructuredArticleType(final SessionContext ctx, final Product item, final EnumerationValue value)
	{
		item.setProperty(ctx, SaparticlemodelConstants.Attributes.Product.STRUCTUREDARTICLETYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.structuredArticleType</code> attribute. 
	 * @param value the structuredArticleType - Article Type
	 */
	public void setStructuredArticleType(final Product item, final EnumerationValue value)
	{
		setStructuredArticleType( getSession().getSessionContext(), item, value );
	}
	
}

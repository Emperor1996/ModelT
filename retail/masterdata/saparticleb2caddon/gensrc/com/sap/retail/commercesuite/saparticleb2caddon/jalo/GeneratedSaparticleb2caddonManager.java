/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.commercesuite.saparticleb2caddon.jalo;

import com.sap.retail.commercesuite.saparticleb2caddon.constants.Saparticleb2caddonConstants;
import com.sap.retail.commercesuite.saparticleb2caddon.jalo.ArticleComponentTabParagraphComponent;
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
 * Generated class for type <code>Saparticleb2caddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSaparticleb2caddonManager extends Extension
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
	
	public ArticleComponentTabParagraphComponent createArticleComponentTabParagraphComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Saparticleb2caddonConstants.TC.ARTICLECOMPONENTTABPARAGRAPHCOMPONENT );
			return (ArticleComponentTabParagraphComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ArticleComponentTabParagraphComponent : "+e.getMessage(), 0 );
		}
	}
	
	public ArticleComponentTabParagraphComponent createArticleComponentTabParagraphComponent(final Map attributeValues)
	{
		return createArticleComponentTabParagraphComponent( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return Saparticleb2caddonConstants.EXTENSIONNAME;
	}
	
}

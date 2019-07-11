/*****************************************************************************
Class: DefaultPPSConfigService
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
 *****************************************************************************/

package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.enums.InterfaceVersion;



/**
 * {@link PPSConfigService} accessing the configuration via the {@link BaseStoreService}
 */
public class DefaultPPSConfigService implements PPSConfigService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPPSConfigService.class);
	private BaseStoreService baseStoreService;

	@SuppressWarnings("javadoc")
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	@Override
	public boolean isPpsActive(final ProductModel prod)
	{
		final SAPConfigurationModel sapConfiguration = getSapConfig(prod);
		return isActiveInConfig(sapConfiguration);
	}

	@Override
	public boolean isPpsActive(final AbstractOrderModel order)
	{
		final SAPConfigurationModel sapConfiguration = getSapConfig(order);
		return isActiveInConfig(sapConfiguration);
	}

	private boolean isActiveInConfig(final SAPConfigurationModel sapConfiguration)
	{
		return sapConfiguration != null && Boolean.TRUE.equals(sapConfiguration.getSappps_active());
	}

	private SAPConfigurationModel getSapConfig(final AbstractOrderModel order)
	{
		return order.getStore().getSAPConfiguration();
	}

	@Override
	public boolean isCacheCatalogPrices(final ProductModel prod)
	{
		final SAPConfigurationModel sapConfiguration = getSapConfig(prod);
		return isActiveInConfig(sapConfiguration) && Boolean.TRUE.equals(sapConfiguration.getSappps_cacheCatalogPrices());
	}

	@Override
	public String getBusinessUnitId(final ProductModel prod)
	{
		return getSapConfig(prod).getSappps_businessUnitId();
	}

	@Override
	public String getBusinessUnitId(final AbstractOrderModel order)
	{
		return getSapConfig(order).getSappps_businessUnitId();
	}

	

	public InterfaceVersion getClientInterfaceVersion(final ProductModel prod)
	{
		return getSapConfig(prod).getSappps_interfaceVersion();
	}


	public InterfaceVersion getClientInterfaceVersion(final AbstractOrderModel order)
	{
		return getSapConfig(order).getSappps_interfaceVersion();
	}
	
	
	@Override
	public SAPConfigurationModel getSapConfig(final ProductModel prod)
	{
		// Case #1: We browse a web shop
		BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
		if (baseStore == null)
		{
			// Case #2: SOLR index / Backoffice / ... --> Just take 1st base store via catalog
			Collection<BaseStoreModel> baseStores = prod.getCatalogVersion().getCatalog().getBaseStores();
			if (baseStores == null || !baseStores.iterator().hasNext())
			{
				LOG.warn("Could not determine any base store for catalog {} which contains product {} with PK {}. PPS cannot be used", prod
						.getCatalogVersion().getCatalog().getId(), prod.getCode(), prod.getPk().getLong());
				return null;
			}
			else
			{
				baseStore = baseStores.iterator().next();
			}
		}
		return baseStore.getSAPConfiguration();
	}

}

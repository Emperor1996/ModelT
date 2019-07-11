/*****************************************************************************
 Class:        BaseInstorecsFacadeTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade;

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.model.user.StoreEmployeeGroupModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.sap.retail.isce.facade.constants.InstorecsfacadeConstants;


public abstract class BaseInstorecsFacadeTest
{
	public static final String STORE_NAME = "Walldorf";
	public static final String AGENT_UID = "sally";
	public static final String AGENT_NAME = "Sally";

	private final PointOfServiceData posData = new PointOfServiceData();
	private final PointOfServiceModel posModel = new PointOfServiceModel();

	private final EmployeeModel employeeModel = new EmployeeModel()
	{
		private Set<PrincipalGroupModel> groups;

		@Override
		public void setGroups(final Set<PrincipalGroupModel> groups)
		{
			this.groups = groups;
		}

		@Override
		public final Set<PrincipalGroupModel> getAllGroups()
		{
			return groups;
		}
	};

	protected PointOfServiceModel getStore()
	{
		posModel.setName(STORE_NAME);
		posModel.setDisplayName(STORE_NAME);
		return posModel;
	}

	protected PointOfServiceData getStoreDAO()
	{
		posData.setName(STORE_NAME);
		posData.setDisplayName(STORE_NAME);
		return posData;
	}

	protected Map<String, Object> getAssistedServiceSessionAttributes()
	{
		final Map<String, Object> attributes = new HashMap<>();

		attributes.put(InstorecsfacadeConstants.AGENTUID, AGENT_UID);

		return attributes;
	}

	protected EmployeeModel getStoreEmployee()
	{
		employeeModel.setGroups(getStoreEmployeeGroups());
		return employeeModel;
	}

	protected EmployeeModel getStoreEmployeeWithoutGroups()
	{
		employeeModel.setGroups(new HashSet<PrincipalGroupModel>());
		return employeeModel;
	}

	protected Set<PrincipalGroupModel> getStoreEmployeeGroups()
	{
		final Set<PrincipalGroupModel> groups = new HashSet<>();

		final StoreEmployeeGroupModel storeGroupModel = new StoreEmployeeGroupModel();
		storeGroupModel.setStore(getStore());
		groups.add(storeGroupModel);

		return groups;
	}
}

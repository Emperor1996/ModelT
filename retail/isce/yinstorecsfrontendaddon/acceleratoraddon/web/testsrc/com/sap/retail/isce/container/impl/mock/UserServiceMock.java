/*****************************************************************************
 Class:        UserServiceMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.model.user.UserPasswordChangeAuditModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.user.exceptions.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.user.exceptions.PasswordEncoderNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Unit test Mock for UserService
 */
public class UserServiceMock implements UserService
{
	private static final String CUSTOMER_UID_VALUE = "uid1";
	private final CustomerModel customerModel = new CustomerModel();

	@Override
	public EmployeeModel getAdminUser()
	{
		return null;
	}

	@Override
	public UserGroupModel getAdminUserGroup()
	{
		return null;
	}

	// no more part of interface in 1811	
	//	@Override
	//	public Set<UserGroupModel> getAllGroups(final UserModel arg0)
	//	{
	//		return null;
	//	}

	@Override
	public Collection<TitleModel> getAllTitles()
	{
		return null;
	}

	@Override
	public Set<UserGroupModel> getAllUserGroupsForUser(final UserModel arg0)
	{
		return null;
	}

	@Override
	public <T extends UserGroupModel> Set<T> getAllUserGroupsForUser(final UserModel arg0, final Class<T> arg1)
	{
		return null;
	}

	@Override
	public CustomerModel getAnonymousUser()
	{
		return null;
	}

	@Override
	public UserModel getCurrentUser()
	{
		customerModel.setCustomerID(CUSTOMER_UID_VALUE);
		((UserModel) customerModel).setProperty("CustomerID", "0000001221");
		return customerModel;
	}

	@Override
	public String getPassword(final String arg0) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
	{
		return null;
	}

	@Override
	public String getPassword(final UserModel arg0) throws CannotDecodePasswordException, PasswordEncoderNotFoundException
	{
		return null;
	}

	@Override
	public TitleModel getTitleForCode(final String arg0)
	{
		return null;
	}

	@Override
	public UserModel getUser(final String arg0)
	{
		return null;
	}

	@Override
	public UserModel getUserForUID(final String arg0)
	{
		return null;
	}

	@Override
	public <T extends UserModel> T getUserForUID(final String arg0, final Class<T> arg1)
	{
		return null;
	}

	@Override
	public UserGroupModel getUserGroup(final String arg0)
	{
		return null;
	}

	@Override
	public UserGroupModel getUserGroupForUID(final String arg0)
	{
		return null;
	}

	@Override
	public <T extends UserGroupModel> T getUserGroupForUID(final String arg0, final Class<T> arg1)
	{
		return null;
	}

	@Override
	public boolean isAdmin(final UserModel arg0)
	{
		return false;
	}

	@Override
	public boolean isAnonymousUser(final UserModel arg0)
	{
		return false;
	}

	@Override
	public boolean isMemberOfGroup(final UserModel arg0, final UserGroupModel arg1)
	{
		return false;
	}

	@Override
	public boolean isMemberOfGroup(final UserGroupModel arg0, final UserGroupModel arg1)
	{
		return false;
	}

	@Override
	public boolean isUserExisting(final String arg0)
	{
		return false;
	}

	@Override
	public void setCurrentUser(final UserModel arg0)
	{
		final String fake = "";
		fake.length();
	}

	@Override
	public void setPassword(final String arg0, final String arg1) throws PasswordEncoderNotFoundException
	{
		final String fake = "";
		fake.length();
	}

	@Override
	public void setPassword(final String arg0, final String arg1, final String arg2) throws PasswordEncoderNotFoundException
	{
		final String fake = "";
		fake.length();
	}

	@Override
	public void setPassword(final UserModel arg0, final String arg1, final String arg2) throws PasswordEncoderNotFoundException
	{
		final String fake = "";
		fake.length();
	}

	@Override
	public void setPasswordWithDefaultEncoding(final UserModel arg0, final String arg1) throws PasswordEncoderNotFoundException
	{
		final String fake = "";
		fake.length();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.user.UserService#getUserAudits(de.hybris.platform.core.model.user.UserModel)
	 */
	@Override
	public List<AbstractUserAuditModel> getUserAudits(final UserModel arg0)
	{
		// YTODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.user.UserService#isAdminEmployee(de.hybris.platform.core.model.user.UserModel)
	 */
	@Override
	public boolean isAdminEmployee(final UserModel arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.user.UserService#isPasswordIdenticalToAudited(de.hybris.platform.core.model.user
	 * .UserModel, java.lang.String, de.hybris.platform.core.model.user.UserPasswordChangeAuditModel)
	 */
	@Override
	public boolean isPasswordIdenticalToAudited(final UserModel arg0, final String arg1, final UserPasswordChangeAuditModel arg2)
	{
		// YTODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.user.UserService#setEncodedPassword(de.hybris.platform.core.model.user.UserModel,
	 * java.lang.String)
	 */
	@Override
	public void setEncodedPassword(final UserModel arg0, final String arg1)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.user.UserService#setEncodedPassword(de.hybris.platform.core.model.user.UserModel,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void setEncodedPassword(final UserModel arg0, final String arg1, final String arg2)
	{
		// YTODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.user.UserService#setPassword(de.hybris.platform.core.model.user.UserModel,
	 * java.lang.String)
	 */
	@Override
	public void setPassword(final UserModel arg0, final String arg1) throws PasswordEncoderNotFoundException
	{
		// YTODO Auto-generated method stub

	}

	@Override
	public boolean isAdminGroup(final UserGroupModel userGroupModel)
	{
		return false;
	}

	@Override
	public boolean isMemberOfGroup(final UserGroupModel userGroupModel1, final UserGroupModel userGroupModel2, final boolean value)
	{
		return false;
	}

	@Override
	public boolean isMemberOfGroup(final UserModel userModel, final UserGroupModel userGroupMoudel, final boolean value)
	{
		return false;
	}


	@Override
	public <T extends UserGroupModel> Set<T> getAllUserGroupsForUserGroup(final UserGroupModel userGroup, final Class<T> clazz)
	{
		return null;
	}
}

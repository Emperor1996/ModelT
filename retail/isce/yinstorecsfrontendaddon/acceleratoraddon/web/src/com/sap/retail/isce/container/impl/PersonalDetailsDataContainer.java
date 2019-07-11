/*****************************************************************************
 Class:        PersonalDetailsDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerServiceException;


/**
 * Implementation class for oData based container providing "Personal Details" of the customer.
 *
 */
public class PersonalDetailsDataContainer extends DataContainerSmartDefaultImpl
{

	protected static final String DATA_CONTAINER_CONTEXT_PARAM_NAME = "personalDetailsDataContainer";

	protected static Logger log = Logger.getLogger(MarketingAttributesDataContainer.class.getName());

	protected Boolean errorState = Boolean.FALSE;
	protected String containerName = null;
	protected UserService userService;


	protected String firstName = null;
	protected String lastName = null;
	protected String street = null;
	protected String houseNumber = null;
	protected String town = null;
	protected String country = null;
	protected String postalCode = null;
	protected String phone = null;
	protected String email = null;
	protected String title = null;
	protected String imageSource = null;
	protected Date registeredSince = null;

	/**
	 * Default constructor.
	 */
	public PersonalDetailsDataContainer()
	{
		this.containerName = this.getClass().getName();
	}

	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME;
	}

	@Override
	public Boolean getErrorState()
	{
		return this.errorState;
	}

	@Override
	public String getContainerName()
	{
		return this.containerName;
	}

	/**
	 * The customer's first name.
	 *
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * The customer's last name.
	 *
	 * @return the lastName
	 */
	public String getLastName()
	{
		return this.lastName;
	}

	/**
	 *
	 * The customer's street.
	 *
	 * @return the street
	 */
	public String getStreet()
	{
		return this.street;
	}

	/**
	 * The customer's house number.
	 *
	 * @return the houseNumber
	 */
	public String getHouseNumber()
	{
		return this.houseNumber;
	}

	/**
	 * The customer's town.
	 *
	 * @return the town
	 */
	public String getTown()
	{
		return this.town;
	}

	/**
	 * The customer's country.
	 *
	 * @return the country
	 */
	public String getCountry()
	{
		return this.country;
	}

	/**
	 * The customer's postal code.
	 *
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return this.postalCode;
	}

	/**
	 * The customer's phone.
	 *
	 * @return the phone
	 */
	public String getPhone()
	{
		return this.phone;
	}

	/**
	 * The customer's email.
	 *
	 * @return the email
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * The customer's title.
	 *
	 * @return the title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * The image source of the customer's pic.
	 *
	 * @return the imageSource
	 */
	public String getImageSource()
	{
		return this.imageSource;
	}

	/**
	 * The customer's registration date.
	 *
	 * @return the registeredSince
	 */
	public Date getRegisteredSince()
	{
		return this.registeredSince;
	}

	/**
	 * @return the userService
	 */
	protected UserService getUserService()
	{
		return this.userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void readData() throws DataContainerServiceException
	{
		final CustomerData customerData = getCustomerData();

		final String displayUID = customerData.getDisplayUid();
		final UserModel customerModel = this.userService.getUserForUID(displayUID);
		final AddressModel addressModel = getAddressDataFromCustomerModel(customerModel);

		this.title = getTitleFromCustomerData(customerData);
		this.firstName = customerData.getFirstName();
		this.lastName = customerData.getLastName();
		this.imageSource = getProfilePictureUrlFromCustomerModel(customerModel);
		this.registeredSince = getOnlineRegistrationDateOfCustomerFromCustomerModel(customerModel);
		if (addressModel != null)
		{
			this.street = addressModel.getStreetname();
			this.houseNumber = addressModel.getStreetnumber();
			this.postalCode = addressModel.getPostalcode();
			this.town = addressModel.getTown();
			this.country = getCountryOfCustomerFromAddressModel(addressModel);
			this.phone = addressModel.getPhone1();
			this.email = addressModel.getEmail();
		}

		if (log.isDebugEnabled())
		{
			log.debug("getContainerName()=" + getContainerName());
			log.debug("getFirstName()=" + getFirstName());
			log.debug("getLastName()= <value not traced>");
			log.debug("getStreet()= <value not traced>");
			log.debug("getHouseNumber()=" + getHouseNumber());
			log.debug("getTown= " + getTown());
			log.debug("getCountry=" + getCountry());
			log.debug("getPostalCode=" + getPostalCode());
			log.debug("getPhone= <value not traced>");
			log.debug("getEmail= <value not traced>");
			log.debug("getTitle=" + getTitle());
			log.debug("getImageSource=" + getImageSource());
		}
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.details", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public void setDataInErrorState()
	{
		this.firstName = null;
		this.lastName = null;
		this.street = null;
		this.houseNumber = null;
		this.town = null;
		this.country = null;
		this.postalCode = null;
		this.phone = null;
		this.email = null;
		this.title = null;
		this.imageSource = null;
	}

	@Override
	public void encodeHTML()
	{
		this.firstName = this.encodeHTML(this.firstName);
		this.lastName = this.encodeHTML(this.lastName);
		this.street = this.encodeHTML(this.street);
		this.houseNumber = this.encodeHTML(this.houseNumber);
		this.town = this.encodeHTML(this.town);
		this.country = this.encodeHTML(this.country);
		this.postalCode = this.encodeHTML(this.postalCode);
		this.phone = this.encodeHTML(this.phone);
		this.email = this.encodeHTML(this.email);
		this.title = this.encodeHTML(this.title);
		this.imageSource = this.encodeHTML(this.imageSource);
	}

	/**
	 *
	 * Get customer data from context map and check for being null. If yes, throw exception.
	 *
	 * @return customerData the customerData after checking for null
	 *
	 * @throws DataContainerServiceException
	 */
	protected CustomerData getCustomerData() throws DataContainerServiceException
	{
		final CustomerData customerData = (CustomerData) this.dataContainerContext.getContextMap().get(
				DataContainerContext.CUSTOMER_DATA);
		if (customerData == null)
		{
			throw new DataContainerServiceException();
		}
		return customerData;
	}

	/**
	 * Get the customer's country from address model.
	 *
	 * @param addressModel
	 *
	 * @return country of customer for locale
	 */
	protected String getCountryOfCustomerFromAddressModel(final AddressModel addressModel)
	{
		final Locale locale = getLocale();

		if (addressModel != null && addressModel.getCountry() != null && addressModel.getCountry().getName(locale) != null)
		{
			return addressModel.getCountry().getName(locale);
		}

		return null;
	}

	/**
	 * Get registration date of customer from user model.
	 *
	 * @param customerModel
	 *
	 * @return registration date of customer for locale
	 */
	protected Date getOnlineRegistrationDateOfCustomerFromCustomerModel(final UserModel customerModel)
	{
		if (customerModel != null && customerModel.getCreationtime() != null)
		{
			return customerModel.getCreationtime();
		}

		return null;
	}

	/**
	 * Get address model for customer by retrieving contact address from user model.
	 *
	 * @param customerModel
	 *
	 * @return addressData the customer's contact address
	 */
	protected AddressModel getAddressDataFromCustomerModel(final UserModel customerModel)
	{
		if (customerModel == null)
		{
			return null;
		}

		final Collection<AddressModel> addressModels = customerModel.getAddresses();

		if (addressModels == null)
		{
			return null;
		}

		for (final AddressModel adressModel : addressModels)
		{
			if (adressModel.getContactAddress() != null && adressModel.getContactAddress().booleanValue())
			{
				return adressModel;
			}
		}
		return null;
	}

	/**
	 * Get the customer's profile picture URL by retrieving it from user model.
	 *
	 * @param customerModel
	 *
	 * @return profilePictureUrl the normalized profile picture URL
	 */
	protected String getProfilePictureUrlFromCustomerModel(final UserModel customerModel) throws DataContainerServiceException
	{
		if (customerModel == null)
		{
			return null;
		}

		if (customerModel.getProfilePicture() == null)
		{
			return null;
		}

		if (customerModel.getProfilePicture().getURL() == null)
		{
			return null;
		}

		try
		{
			return new URI(customerModel.getProfilePicture().getURL()).normalize().toString();
		}
		catch (final URISyntaxException e)
		{
			throw new DataContainerServiceException(e);
		}
	}

	/**
	 * Get the customer's title by retrieving it from customer data.
	 *
	 * @param customerData
	 *
	 * @return title
	 */
	protected String getTitleFromCustomerData(final CustomerData customerData)
	{
		if (customerData == null)
		{
			return null;
		}
		final String titleFromCustomerData = customerData.getTitleCode();
		if (titleFromCustomerData == null)
		{
			return null;
		}

		final TitleModel titleModel = this.userService.getTitleForCode(titleFromCustomerData);
		if (titleModel != null)
		{
			return titleModel.getName();
		}

		return titleFromCustomerData;
	}

	/**
	 * Determine default locale.
	 *
	 * @return locale
	 */
	protected Locale getLocale()
	{
		return Locale.getDefault();
	}

}

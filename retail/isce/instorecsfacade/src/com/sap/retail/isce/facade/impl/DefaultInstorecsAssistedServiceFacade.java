/*****************************************************************************
 Class:        DefaultInstorecsAssistedServiceFacade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import de.hybris.platform.assistedservicefacades.impl.DefaultAssistedServiceFacade;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.storelocator.StoreLocatorFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.model.user.StoreEmployeeGroupModel;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.facade.InstorecsContextFacade;
import com.sap.retail.isce.facade.InstorecsOrderIntegrationFacade;
import com.sap.retail.isce.facade.constants.InstorecsfacadeConstants;
import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput;
import com.sap.retail.isce.service.sap.OrderHandOverOutput.Status;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.OrderSapRetailIntegrationService;
import com.sap.retail.isce.service.sap.impl.OrderHandOverInputImpl;



public class DefaultInstorecsAssistedServiceFacade extends DefaultAssistedServiceFacade implements InstorecsContextFacade,
		InstorecsOrderIntegrationFacade
{
	private StoreLocatorFacade storeLocatorFacade;
	private CommerceStockService commerceStockService;
	private OrderSapRetailIntegrationService orderIntegrationSerivce;
	private BaseStoreService baseStoreService;

	private boolean autoCreateDeliveryAndPick = false;


	private static final Logger LOG = Logger.getLogger(DefaultInstorecsAssistedServiceFacade.class.getName());

	@Override
	public void loginAssistedServiceAgent(final String username, final String password) throws AssistedServiceException
	{
		superLoginAssistedServiceAgent(username, password);
		try
		{
			determineStoreContext();
		}
		catch (final StoreContextNotDefinedException ex)
		{
			throw new AssistedServiceException(ex.getMessage(), ex);
		}
	}

	/**
	 * @param username
	 * @param password
	 * @throws AssistedServiceException
	 */
	protected void superLoginAssistedServiceAgent(final String username, final String password) throws AssistedServiceException
	{
		super.loginAssistedServiceAgent(username, password);
	}


	@Override
	public void logoutAssistedServiceAgent() throws AssistedServiceException
	{
		superLogoutAssistedServiceAgent();
		getSessionService().getCurrentSession().removeAttribute(InstorecsfacadeConstants.AGENTSTORE);
		getSessionService().getCurrentSession().removeAttribute(InstorecsfacadeConstants.AGENTSTORE_MODEL);
	}


	protected void superLogoutAssistedServiceAgent() throws AssistedServiceException
	{
		super.logoutAssistedServiceAgent();
	}

	@Override
	public PointOfServiceData getEmployeeStore() throws AssistedServiceAgentNotLoggedInException, StoreContextNotDefinedException
	{
		if (!isAssistedServiceAgentLoggedIn())
		{
			throw new AssistedServiceAgentNotLoggedInException("Assisted Service Agent is not logged in.");
		}

		PointOfServiceData posData = getSessionService().getCurrentSession().getAttribute(InstorecsfacadeConstants.AGENTSTORE);

		if (posData == null)
		{
			posData = determineStoreContext();
		}
		return posData;
	}

	@Override
	public PointOfServiceData determineStoreContext() throws AssistedServiceAgentNotLoggedInException,
			StoreContextNotDefinedException
	{
		if (!isAssistedServiceAgentLoggedIn())
		{
			throw new AssistedServiceAgentNotLoggedInException("Assisted Service Agent is not logged in.");
		}

		final String agentUID = getAsmSession().getAgent().getUid();

		final PointOfServiceData posData = getPointOfServiceData(agentUID);

		if (posData == null)
		{
			throw new StoreContextNotDefinedException("No Store assigned to Service Agent");
		}
		else
		{
			return posData;
		}
	}

	private PointOfServiceData getPointOfServiceData(final String agentUID)
	{
		PointOfServiceData posData;

		final PointOfServiceModel posModel = getPointOfServiceModel(agentUID);
		if (posModel == null)
		{
			return null;
		}
		posData = getStoreLocatorFacade().getPOSForName(posModel.getName());

		getSessionService().getCurrentSession().setAttribute(InstorecsfacadeConstants.AGENTSTORE, posData);
		return posData;
	}

	private PointOfServiceModel getPointOfServiceModel(final String agentUID)
	{
		final UserModel userForUID = getUserService().getUserForUID(agentUID);
		if (userForUID == null)
		{
			LOG.debug("get User for UID " + agentUID + " is null. Break processing of getPointOfServiceModel");
			return null;
		}
		final Set<PrincipalGroupModel> groups = userForUID.getAllGroups();

		PointOfServiceModel posModel = null;
		for (final PrincipalGroupModel group : groups)
		{
			if (group instanceof StoreEmployeeGroupModel)
			{
				final StoreEmployeeGroupModel storeGroup = (StoreEmployeeGroupModel) group;
				posModel = storeGroup.getStore();
				final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
				if (baseStore.getPointsOfService().contains(posModel))
				{
					break;
				}
				LOG.debug("Store " + posModel.getDisplayName() + " is assigned to agent " + agentUID
						+ ", but not assigned to baseStore " + baseStore.getUid());
				posModel = null;
			}
		}

		getSessionService().getCurrentSession().setAttribute(InstorecsfacadeConstants.AGENTSTORE_MODEL, posModel);
		return posModel;
	}

	@Override
	public Map<String, Object> getAssistedServiceSessionAttributes()
	{
		final Map<String, Object> attributes = getSuperAssistedServiceSessionAttributes();
		if (isAssistedServiceAgentLoggedIn())
		{
			attributes.put("agentUID", getAsmSession().getAgent().getUid());
			attributes.put("agentName", getAsmSession().getAgent().getName());
		}
		return attributes;
	}

	protected Map<String, Object> getSuperAssistedServiceSessionAttributes()
	{
		return super.getAssistedServiceSessionAttributes();
	}

	@Override
	public Long getProductQtyInCurrentStore(final ProductModel productModel)
	{
		if (!isAssistedServiceAgentLoggedIn())
		{
			return null;
		}


		final PointOfServiceModel pointOfService = getSessionService().getCurrentSession().getAttribute(
				InstorecsfacadeConstants.AGENTSTORE_MODEL);

		if (pointOfService == null)
		{
			return null;
		}

		return getCommerceStockService().getStockLevelForProductAndPointOfService(productModel, pointOfService);
	}

	private OrderModel getOrderForCode(final String orderCode)
	{
		final BaseStoreModel baseStoreModel = getBaseStoreService().getCurrentBaseStore();
		return ((DefaultInstorecsAssistedServiceService) getAssistedServiceService()).getCustomerAccountService().getOrderForCode(
				orderCode, baseStoreModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.facade.InstorecsOrderIntegrationFacade#handOverOrder(java.lang.String)
	 */
	@Override
	public OrderHandOverOutput handOverOrder(final String orderCode) throws SAPISCEException
	{
		final OrderHandOverInputImpl input = prepareHandover(orderCode);
		final OrderHandOverOutput output = orderIntegrationSerivce.handoverOrder(input);
		updateOrderStatus(orderCode, output);
		getModelService().saveAll();
		return output;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.facade.InstorecsOrderIntegrationFacade#getInvoiceDocumentForOrderCode(java.lang.String)
	 */
	@Override
	public byte[] getInvoiceDocumentForOrderCode(final String orderCode) throws SAPISCEException
	{
		return orderIntegrationSerivce.getInvoiceForOrder(orderCode);
	}

	protected void updateOrderStatus(final String orderCode, final OrderHandOverOutput output)
	{
		final OrderModel orderModel = getOrderForCode(orderCode);
		if (Status.OK.equals(output.getStatus()))
		{
			orderModel.setStatus(OrderStatus.COMPLETED);
			orderModel.setDeliveryStatus(DeliveryStatus.SHIPPED);
			for (final ConsignmentModel consignment : orderModel.getConsignments())
			{
				consignment.setStatus(ConsignmentStatus.SHIPPED);
			}
		}
		else
		{
			orderModel.setStatus(OrderStatus.PROCESSING_ERROR);
		}
	}

	protected OrderHandOverInputImpl prepareHandover(final String orderCode)
	{
		final OrderHandOverInputImpl input = new OrderHandOverInputImpl();
		input.setOrderId(orderCode);
		input.setGetInvoiceAsPDF(true);
		if (autoCreateDeliveryAndPick)
		{
			input.addOperation(Operation.DELIVERY);
			input.addOperation(Operation.PICK);
		}
		input.addOperation(Operation.GOODSISSUE);
		input.addOperation(Operation.INVOICE);
		return input;
	}

	protected StoreLocatorFacade getStoreLocatorFacade()
	{
		return storeLocatorFacade;
	}

	@Required
	public void setStoreLocatorFacade(final StoreLocatorFacade storeLocatorFacade)
	{
		this.storeLocatorFacade = storeLocatorFacade;
	}


	protected OrderSapRetailIntegrationService getOrderIntegrationSerivce()
	{
		return orderIntegrationSerivce;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setOrderIntegrationSerivce(final OrderSapRetailIntegrationService orderIntegrationSerivce)
	{
		this.orderIntegrationSerivce = orderIntegrationSerivce;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}

	public void setAutoCreateDeliveryAndPick(final boolean autoCreateDeliveryAndPick)
	{
		this.autoCreateDeliveryAndPick = autoCreateDeliveryAndPick;
	}
}

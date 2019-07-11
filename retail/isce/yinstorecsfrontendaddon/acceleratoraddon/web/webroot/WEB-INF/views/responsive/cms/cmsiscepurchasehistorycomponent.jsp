<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<c:set var="searchUrl" value="/my-account/customer360?show=Page"/>

<div class="isce-history" role="presentation">

	<ycommerce:testId code="customer360PurchaseHistory">
	</ycommerce:testId>


	<h3 id="customer360PurchaseHistory" class="isce-headline" aria-labelledby="customer360PurchaseHistory">
	<spring:theme code="instorecs.customer360.purchaseHistory" htmlEscape="true" text="Purchase History"/>
	</h3>	
	
			
	<common:globalMessages/>
			
<c:if test="${empty CARPurchaseHistoryDataContainerCustomerOrders.purchaseHistoryData.results}">
	<div class="account-section-content	col-md-6 col-md-push-3 content-empty">
		<ycommerce:testId code="orderHistory_noOrders_label">
			<spring:theme code="instorecs.customer360.noPurchaseHistory" htmlEscape="true"/>
		</ycommerce:testId>
	</div>
</c:if>
			
<c:if test="${not empty CARPurchaseHistoryDataContainerCustomerOrders.purchaseHistoryData.results}">
	<div class="account-section-content	">
		<div class="account-orderhistory">

			<script type="text/javascript">
if (typeof com === 'undefined' || com === null) {
	var com = {sap : {ISCE : {}}};
}
if (typeof com.sap === 'undefined' || com.sap === null) {
	com.sap = {ISCE : {}};
}
if (typeof com.sap.ISCE === 'undefined' || com.sap.ISCE === null) {
	com.sap.ISCE = {};
}    
com.sap.ISCE.numberOfResultsPerPage = ${CARPurchaseHistoryDataContainerCustomerOrders.maxEntriesDisplayedPerPage};
com.sap.ISCE.pageNumberDisplayed = ${CARPurchaseHistoryDataContainerCustomerOrders.pageNumberDisplayed};
com.sap.ISCE.maxNumberOfPages = ${searchPageData.pagination.numberOfPages-1};
com.sap.ISCE.fullUrlToOrder = "${fullUrlToOrder}";
com.sap.ISCE.purchaseHistoryDataArray=[
<c:forEach items="${CARPurchaseHistoryDataContainerCustomerOrders.purchaseHistoryData.results}" var="purchaseHistoryArray">["${purchaseHistoryArray.purchaseType}","${purchaseHistoryArray.orderNumber}","<format:price priceData="${purchaseHistoryArray.grossSalesVolumePrice}" />","<fmt:formatDate value="${purchaseHistoryArray.date}" dateStyle="long" timeStyle="short" type="both"/>"],
</c:forEach>];
			</script>
			<%--
			<div class="account-orderhistory-pagination">
				<nav:pagination top="true" msgKey="text.account.orderHistory.page" showCurrentPageInfo="false" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>
			 --%>			
            <div class="account-overview-table">
				<table class="orderhistory-list-table responsive-table">
					<tr class="account-orderhistory-table-head responsive-table-head hidden-xs">
						<th class="col-sm-4 col-md-4"><spring:theme code="instorecs.customer360.purchaseHistory.purchase" htmlEscape="true" text="Purchase"/></th>
						<th><spring:theme code="instorecs.customer360.purchaseHistory.grossSalesVolume" htmlEscape="true" text="Gross Sales Volume"/></th>
						<th><spring:theme code="instorecs.customer360.purchaseHistory.date" htmlEscape="true" text="Date"/></th>
					</tr>				
					<c:forEach items="${CARPurchaseHistoryDataContainerCustomerOrders.purchaseHistoryData.results}" var="purchaseHistory" begin="0" end="${searchPageData.pagination.pageSize-1}" varStatus="counter">
						<tr class="responsive-table-item">
							<ycommerce:testId code="orderHistoryItem_orderDetails_link">
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="instorecs.customer360.purchaseHistory.purchase" htmlEscape="true" text="Purchase"/></td>
								<td id="purchaseHistory_Type_${counter.count}" class="responsive-table-cell">
									${purchaseHistory.purchaseType}
									<c:if test="${not empty purchaseHistory.orderNumber}">
										<a href="${fullOrderUrl}${purchaseHistory.orderNumber}" class="responsive-table-link">${purchaseHistory.orderNumber}</a>
									</c:if>
								</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="instorecs.customer360.purchaseHistory.grossSalesVolume" htmlEscape="true" text="Gross Sales Volume"/></td>																
								<td id="purchaseHistory_grossSalesVolumePrice_${counter.count}" class="responsive-table-cell">
									<format:price priceData="${purchaseHistory.grossSalesVolumePrice}" />
								</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="instorecs.customer360.purchaseHistory.date" htmlEscape="true" text="Date"/></td>
								<td id="purchaseHistory_Date_${counter.count}" class="responsive-table-cell">
									<fmt:formatDate value="${purchaseHistory.date}" dateStyle="long" timeStyle="short" type="both"/>
								</td>
							</ycommerce:testId>
						</tr>
					</c:forEach>
				</table>
            </div>
		</div>
		<div class="account-orderhistory-pagination">
			<nav:pagination top="false" msgKey="text.account.orderHistory.page" showCurrentPageInfo="false" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
		</div>
		
	</div>
	<script type="text/javascript">
				$(document).ready(function() {
				// This coding will be executed within each page rendering, therefore we cut off the processing as soon as possible if the mandatory prerequisites are not met. 
						if (com.sap.ISCE.PaginatorHandler.checkPrerequisites()) {
							com.sap.ISCE.PaginatorHandler.currentPage = undefined; // Due to new popup
							com.sap.ISCE.PaginatorHandler.changeISCETransactionHistoryPaginationLinks();
						}
				});
	</script>
</c:if>

</div>

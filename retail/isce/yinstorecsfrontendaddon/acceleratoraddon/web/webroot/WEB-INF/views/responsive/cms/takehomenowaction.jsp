<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:url value="/takehomenowaction/act" var="takeHomeNowUrl"/>
<div id="addToCartTitle" style="display:none"><spring:theme code="basket.added.to.basket"/></div>
<form:form method="post" id="takeHomeNow" class="add_to_cart_form" action="${takeHomeNowUrl}">
	<c:if test="${product.purchasable}">
		<input type="hidden" maxlength="3" size="1" id="qty" name="qty" class="qty js-qty-selector-input" value="1">
	</c:if>
								
	<input type="hidden" name="productCodePost" value="${product.code}"/>
	
	<c:if test="${empty showAddToCart ? true : showAddToCart}">
		<c:set var="buttonType">button</c:set>
		<c:if test="${product.purchasable and product.stock.stockLevelStatus.code ne 'outOfStock' }">
			<c:set var="buttonType">submit</c:set>
		</c:if>
		<c:if test="${product.qtyInCurrentStore > 0}">
		<button id="takeHomeNowButton" type="${buttonType}" class="btn btn-block isce-btn-primary btn-block js-enable-btn btn-icon glyphicon-new-window" disabled="disabled">
			<spring:theme code="instorecs.takeHomeNowButton"/>
		</button>
		</c:if>
		<c:if test="${product.qtyInCurrentStore <= 0}">
		<button id="takeHomeNowButton" type="${buttonType}" class="btn btn-block isce-btn-primary btn-block js-disable-btn btn-icon glyphicon-new-window" disabled="disabled">
			<spring:theme code="instorecs.takeHomeNowButton"/>
		</button>
		</c:if>
		<spring:theme code="instorecs.availableStockInCurrentStore"/>: ${product.qtyInCurrentStore}
	</c:if>
</form:form>
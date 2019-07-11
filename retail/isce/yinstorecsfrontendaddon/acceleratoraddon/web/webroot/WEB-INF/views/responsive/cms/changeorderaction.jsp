<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="instorecs" uri="/WEB-INF/tld/addons/yinstorecsfrontendaddon/instorecs.tld" %>

<c:if test="${instorecs:showHandoverButton(orderData)}">
<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4 pull-right">
	<div class="isce-change-order">
		<c:url value="/changeOrder/act" var="changeOrderUrl"/>
		<c:set var="orderCodeForChange" scope="session" value="${orderData.code}"></c:set>
		<form:form method="post" id="handOver" class="form" action="${changeOrderUrl}">
			<button id="ChangeOrderButton" type="submit" class="btn isce-btn-primary btn-block js-add-to-cart">
				<spring:theme code="instorecs.changeOrder"/>
			</button>
		</form:form>
	</div>
</div>
</c:if>
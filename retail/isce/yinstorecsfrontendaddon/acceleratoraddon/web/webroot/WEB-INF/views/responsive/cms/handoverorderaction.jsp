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
	<div class="isce-handover-order">
		<c:url value="/handover/act" var="handOverUrl"/>
		<c:set var="orderCodeForHandover" scope="session" value="${orderData.code}"></c:set>
		<form:form method="post" id="handOver" class="form" action="${handOverUrl}">
			<button id="handOverButton" type="submit" class="btn isce-btn-primary btn-block js-add-to-cart">
				<spring:theme code="instorecs.handoverOrder"/>
			</button>
		</form:form>
	</div>
</div>
</c:if>
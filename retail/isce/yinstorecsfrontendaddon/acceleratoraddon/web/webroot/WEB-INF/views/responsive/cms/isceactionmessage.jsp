<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:if test="${not empty errorMessageCode and empty messgeWasShown}">	
	<div class="global-alerts">
		<div class="alert alert-info" role="alert">
			<div class="error-msg-alert"><spring:theme code="${errorMessageCode}"/></div>
		</div>
	</div>
	<c:set var="messgeWasShown" value="true" scope="request"/>	     
</c:if>
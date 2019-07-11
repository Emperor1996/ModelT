<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<ycommerce:testId code="customer360GenericAttributes">
</ycommerce:testId>

<h3 id="genericScoresTitle" class="isce-headline" aria-labelledby="genericScoresTitle">
	<spring:theme code="instorecs.customer360.genericScoresAttr" htmlEscape="true" />
</h3>

<div role="presentation">
	<c:forEach items="${genericScoresDataContainer.genericScoresList}" var="item">
		<div class="isce-profile-label" aria-labelledby="genericLabel1">${item.scoreDescription}:</div>
		<div class="isce-profile-value" aria-labelledby="generic1">${item.scoreValue}</div>
		<br />
	</c:forEach>
</div>


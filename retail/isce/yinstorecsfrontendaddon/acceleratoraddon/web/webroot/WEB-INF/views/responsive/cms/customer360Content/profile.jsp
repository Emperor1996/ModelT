<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ycommerce:testId code="customer360Profile">
</ycommerce:testId>

<h3 id="genericScoresTitle" class="isce-headline" aria-labelledby="genericScoresTitle">
	<spring:theme code="instorecs.customer360.profile" htmlEscape="true" />
</h3>

<div role="presentation">

	<div id="registeredSinceLabel" class="isce-profile-label" aria-labelledby="registeredSinceLabel">
		<spring:theme code="instorecs.customer360.profile.registeredSince" htmlEscape="true"/>
	</div>
	<div id="registeredSince" class="isce-profile-value" aria-labelledby="registeredSince">
		<fmt:formatDate value="${personalDetailsDataContainer.registeredSince}" dateStyle="long" type="date"/>
	</div>	
	<br/>
	
	<div id="ageLabel" class="isce-profile-label" aria-labelledby="ageLabel">
		<spring:theme code="instorecs.customer360.profile.age" htmlEscape="true"/>
	</div>
	<div id="age" class="isce-profile-value" aria-labelledby="age">${objectScoresDataContainerAge.ageInYearsDescription}&nbsp;&nbsp;&nbsp;
		<c:if test="${marketingAttributesDataContainer.isBirthDay}"><span class="glyphicon glyphicon-flag"></span> <spring:theme
			code="instorecs.customer360.profile.isBirthDay" htmlEscape="true"/></c:if>
	</div>					
	<br/>
	
	<div id="genderLabel" class="isce-profile-label" aria-labelledby="genderLabel">
		<spring:theme code="instorecs.customer360.profile.gender" htmlEscape="true"/>
	</div>
	<div id="gender" class="isce-profile-value" aria-labelledby="gender">${marketingAttributesDataContainer.genderDescription}</div>
	<br/>
	
	<div id="maritalLabel" class="isce-profile-label" aria-labelledby="maritalLabel">
		<spring:theme code="instorecs.customer360.profile.marital" htmlEscape="true"/>
	</div>
	<div id="marital" class="isce-profile-value" aria-labelledby="marital">${marketingAttributesDataContainer.maritalStatusDescription}</div>
	<br/>
	
	<div id="newsletterLabel" class="isce-profile-label" aria-labelledby="newsletterLabel">
		<spring:theme code="instorecs.customer360.profile.newsletter" htmlEscape="true"/>
	</div>
	<div id="newsletter" class="isce-profile-value" aria-labelledby="newsletter">
		<c:if test="${NewsletterSubscriptionDataContainer.newsletterSubscripted != null && NewsletterSubscriptionDataContainer.newsletterSubscripted == true}">
			<spring:theme code="instorecs.customer360.yes" htmlEscape="true"/>
		</c:if>
		<c:if test="${NewsletterSubscriptionDataContainer.newsletterSubscripted != null && NewsletterSubscriptionDataContainer.newsletterSubscripted == false}">
			<spring:theme code="instorecs.customer360.no" htmlEscape="true"/>
		</c:if>
	</div>						
	<br/>
	
</div>

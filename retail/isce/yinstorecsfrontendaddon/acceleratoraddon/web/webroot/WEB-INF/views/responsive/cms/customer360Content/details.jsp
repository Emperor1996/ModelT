<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<ycommerce:testId code="customer360Details">
</ycommerce:testId>

<h3 id="personalDetails" class="isce-headline" aria-labelledby="personalDetails">
	<spring:theme code="instorecs.customer360.details" htmlEscape="true"/>
</h3>

<div id="personalAddress" class="isce-personal-address" aria-labelledby="personalAddress">
<strong>${personalDetailsDataContainer.title}&nbsp;${personalDetailsDataContainer.firstName}&nbsp;${personalDetailsDataContainer.lastName}</strong><br />
        ${personalDetailsDataContainer.street}<br />
        ${personalDetailsDataContainer.houseNumber}<br />
        ${personalDetailsDataContainer.town}<br /> 
        ${personalDetailsDataContainer.country}&nbsp;${personalDetailsDataContainer.postalCode}<br />
        ${personalDetailsDataContainer.phone}<br /> 
<a href="mailto:${personalDetailsDataContainer.email}">${personalDetailsDataContainer.email}</a><br />
</div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<ycommerce:testId code="customer360Interests">
</ycommerce:testId>

<h3 id="areaOfInterestTitle" class="isce-headline" aria-labelledby="areaOfInterestTitle">
	<spring:theme code="instorecs.customer360.interests" htmlEscape="true" />
</h3>

<div id="tagCloud" class="isce-center isce-pointer" role="presentation">
	<c:forEach items="${itemsOfInterestDataContainer.itemsOfInterestList}" var="item">	
		<span id="areaOfInterest" class="isce-tag-level-${item.valuationAverage}" aria-labelledby="areaOfInterest">${item.interestDescription}</span>
	</c:forEach>
</div>

<div class="modal modal-vertical-centered" id="asmModalTagCloud" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 id="modalTagCloudTitle" class="modal-title isce-center" aria-labelledby="modalTagCloudTitle">
				<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
			</h4></div>
			<div  class="modal-body">
				<strong><span id="modalTagCloudLabel" aria-labelledby="modalTagCloudLabel">
					<spring:theme code="instorecs.customer360.interests" htmlEscape="true" />
				</span></strong>
				<br/><br/>
				<span id="modalTagCloudDesc" aria-labelledby="modalTagCloudDesc">
					<spring:theme code="instorecs.customer360.tagcloudpopup.description" htmlEscape="true" />
				</span>
				<br/><br/>
			</div>
			<div class="modal-footer"><button type="button" class="btn btn-default" data-dismiss="modal">
				<spring:theme code="instorecs.customer360.tilepopup.close" htmlEscape="true" />
			</button></div>
		</div>
	</div>
</div>

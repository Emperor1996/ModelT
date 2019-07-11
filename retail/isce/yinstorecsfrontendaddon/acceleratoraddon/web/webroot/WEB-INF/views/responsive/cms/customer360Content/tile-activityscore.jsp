<%@include file="./tile-activityscoreIncl.html"%>

<div class="modal modal-vertical-centered" id="activityScorePopup" tabindex="-1" role="dialog" aria-labelledby="activityScorePopup" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="activityScorePopupTitle" class="modal-title isce-center" aria-labelledby="activityScorePopupTitle">
					<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="activityScorePopupLabel" class="isce-modal-label" aria-labelledby="activityScorePopupLabel">
					<spring:theme code="instorecs.customer360.tilepopup.activityScore" htmlEscape="true" />
				</div>
				<br />
				<div id="activityScorePopupDesc" class="isce-modal-desc" aria-labelledby="activityScorePopupDesc">
					<spring:theme code="instorecs.customer360.tilepopup.activityScoreDesc" htmlEscape="true" />
				</div>
				<br />
				<div id="activityScoreTilePopup" class="isce-modal-left">
					<%@include file="./tile-activityscoreIncl.html"%>
				</div>
				<div class="isce-modal-right">
					<table class="isce-modal-table">
						<tr class="isce-modal-tr">
						<c:forEach var="level" varStatus="index" items="${objectScoresDataContainerActivity.activityScoreProperty.levels}">
						<tr class="isce-modal-tr">
							<td class="isce-modal-td1">
								<spring:theme code="instorecs.customer360.tilepopup.amountLevel${level.levelFlag}" htmlEscape="true" />
							</td>
						<c:choose>
						<c:when test="${index.last}">
							<td class="isce-modal-td2"></td>
							<td class="isce-modal-td3"><spring:theme code="instorecs.customer360.tilepopup.greater" htmlEscape="true" /></td>
							<td class="isce-modal-td4">${level.adaptedUILowValue}</td>
						</c:when>
						<c:otherwise>
							<td class="isce-modal-td2">${level.adaptedUILowValue}</td>
							<td class="isce-modal-td3"><spring:theme code="instorecs.customer360.tilepopup.to" htmlEscape="true" />
							<td class="isce-modal-td4">${level.adaptedUIHighValue}</td>
						</c:otherwise>
						</c:choose>
							<td class="isce-modal-td6"> ${level.description}</td>
						</tr>
						</c:forEach>					
					</table>
				</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:theme code="instorecs.customer360.tilepopup.close" htmlEscape="true" />
				</button>
			</div>
		</div>
	</div>
</div>

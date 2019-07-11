<%@include file="./tile-sentimentscoreIncl.html"%>

<div class="modal modal-vertical-centered" id="sentimentScorePopup" tabindex="-1" role="dialog" aria-labelledby="sentimentScorePopup" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="sentimentScorePopupTitle" class="modal-title isce-center" aria-labelledby="sentimentScorePopupTitle">
					<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="sentimentScorePopupLabel" class="isce-modal-label" aria-labelledby="sentimentScorePopupLabel">
					<spring:theme code="instorecs.customer360.tilepopup.sentimentScore" htmlEscape="true" />
				</div>
				<br />
				<div id="sentimentScorePopupDesc" class="isce-modal-desc" aria-labelledby="sentimentScorePopupDesc">
					<spring:theme code="instorecs.customer360.tilepopup.senitmentScoreDesc" htmlEscape="true" />
				</div>
				<br />
				<div id="sentimentScoreTilePopup" class="isce-modal-left">
					<%@include file="./tile-sentimentscoreIncl.html"%>
				</div>
				<div class="isce-modal-right">
					<table class="isce-modal-table">
						<c:forEach var="level" varStatus="index" items="${objectScoresDataContainerSentiment.sentimentScoreProperty.levels}">
						<tr class="isce-modal-tr">
							<td class="isce-modal-td1">
								<spring:theme code="instorecs.customer360.tilepopup.evaluationLevel${level.levelFlag}" htmlEscape="true" />
							</td>
							<td class="isce-modal-td4">${level.highValue}</td>
							<td class="isce-modal-td5">${level.unit}</td>
							<td class="isce-modal-td6"> ${level.description} </td>
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

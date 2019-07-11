<%@include file="./tile-storepurchaseratioIncl.html"%>

<div class="modal modal-vertical-centered" id="storePurchaseRatioPopup" tabindex="-1" role="dialog" aria-labelledby="storePurchaseRatioPopup" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="storePurchaseRatioPopupTitle" class="modal-title isce-center" aria-labelledby="storePurchaseRatioPopupTitle">
					<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="storePurchaseRatioPopupLabel" class="isce-modal-label" aria-labelledby="storePurchaseRatioPopupLabel">
					<spring:theme code="instorecs.customer360.tilepopup.storeOnlineRatio" htmlEscape="true" />
				</div>
				<br />
				<div id="storePurchaseRatioPopupDesc" class="isce-modal-desc" aria-labelledby="storePurchaseRatioPopupDesc">
					<spring:theme code="instorecs.customer360.tilepopup.storeOnlineRatioDesc" htmlEscape="true" />
				</div>
				<br />
				<div id="storePurchaseRatioTilePopup" class="isce-modal-left">
					<%@include file="./tile-storepurchaseratioIncl.html"%>
				</div>
				<div class="isce-modal-right">
					<table class="isce-modal-table">
						<c:forEach var="level" varStatus="index" items="${statisticalDataContainer.storePurchaseRatioProperty.levels}">
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

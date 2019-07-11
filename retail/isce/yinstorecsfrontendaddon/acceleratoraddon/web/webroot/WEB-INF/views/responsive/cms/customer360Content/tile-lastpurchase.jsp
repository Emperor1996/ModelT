<%@include file="./tile-lastpurchaseIncl.html"%>

<div class="modal modal-vertical-centered" id="lastPurchasePopup" tabindex="-1" role="dialog" aria-labelledby="lastPurchasePopup" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="lastPurchasePopupTitle" class="modal-title isce-center" aria-labelledby="lastPurchasePopupTitle">
					<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="lastPurchasePopupLabel" class="isce-modal-label" aria-labelledby="lastPurchasePopupLabel">
					<spring:theme code="instorecs.customer360.tilepopup.lastPurchase" htmlEscape="true" />
				</div>
				<br />
				<div id="lastPurchaseTilePopup" class="isce-modal-left">
					<%@include file="./tile-lastpurchaseIncl.html"%>
				</div>
				<div class="isce-modal-right">
					<table class="isce-modal-table">

						<c:forEach var="level" varStatus="index" items="${statisticalDataContainer.lastPurchaseDateProperty.levels}">
						<tr class="isce-modal-tr">
							<td class="isce-modal-td1">
								<spring:theme code="instorecs.customer360.tilepopup.pastLevel${level.levelFlag}" htmlEscape="true" />
							</td>
						<c:choose>
						<c:when test="${index.last}">
							<td class="isce-modal-td2"></td>
							<td class="isce-modal-td3"><spring:theme code="instorecs.customer360.tilepopup.smaller" htmlEscape="true" /></td>
							<td class="isce-modal-td4">${level.adaptedUIHighValue}</td>
						</c:when>
						<c:when test="${index.first}">
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
							<td class="isce-modal-td6">${level.description}</td>
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

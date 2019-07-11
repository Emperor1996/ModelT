<%@include file="./tile-salesvolumeIncl.html"%>

<div class="modal modal-vertical-centered" id="salesVolumePopup" tabindex="-1" role="dialog" aria-labelledby="salesVolumePopup" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header isce-modal-title">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 id="salesVolumePopupTitle" class="modal-title isce-center" aria-labelledby="salesVolumePopupTitle">
					<spring:theme code="instorecs.customer360.tilepopup.title" htmlEscape="true" />
				</h4>
			</div>
			<div class="modal-body">
				<div id="salesVolumePopupLabel" class="isce-modal-label" aria-labelledby="salesVolumePopupLabel">
					<spring:theme code="instorecs.customer360.tilepopup.salesVolume" htmlEscape="true" />
				</div>
				<br />
				<div id="salesVolumeTilePopup" class="isce-modal-left">
					<%@include file="./tile-salesvolumeIncl.html"%>
				</div>
				<div class="isce-modal-right">
					<table class="isce-modal-table">
						<c:forEach var="level" varStatus="index" items="${statisticalDataContainer.salesVolumeProperty.levels}">
						<tr class="isce-modal-tr">
							<td class="isce-modal-td1">
								<spring:theme code="instorecs.customer360.tilepopup.amountLevel${level.levelFlag}" htmlEscape="true" />
							</td>
						<c:choose>
						<c:when test="${index.last}"> 
							<td class="isce-modal-td2"></td>
							<td class="isce-modal-td3"><spring:theme code="instorecs.customer360.tilepopup.greater" htmlEscape="true" /></td>
							<td class="isce-modal-td4"><format:price priceData="${level.adaptedUILowValue}" /></td>
						</c:when>
						<c:otherwise>
							<td class="isce-modal-td2"><format:price priceData="${level.adaptedUILowValue}" /></td>
							<td class="isce-modal-td3"><spring:theme code="instorecs.customer360.tilepopup.to" htmlEscape="true" />
							<td class="isce-modal-td4"><format:price priceData="${level.adaptedUIHighValue}" /></td>
						</c:otherwise>
						</c:choose>
							<td class="isce-modal-td6"> ${level.description} </td>
						</tr>
						</c:forEach>						
					</table>
				</div>

				<hr />

				<div id="salesVolumePopupAddInfoTitle" class="isce-modal-info-label" aria-labelledby="salesVolumePopupAddInfoTitle">
					<spring:theme code="instorecs.customer360.tilepopup.addInformationLabel" htmlEscape="true" />
				</div>
				<div class="isce-modal-info">
					<span id="salesVolumeLastSixMonthsLabel" aria-labelledby="salesVolumeLastSixMonthsLabel"><spring:theme code="instorecs.customer360.statistical.salesVolumeLastSixMonths" htmlEscape="true" /></span> 
					<strong><span id="salesVolumeLastSixMonths" aria-labelledby="salesVolumeLastSixMonths"><format:price priceData="${statisticalDataContainer.salesVolumePriceLastSixMonths}" /></span></strong>
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

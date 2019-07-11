<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="isce-customer360" role="presentation">
		
	<common:globalMessages/>

  <script type="text/javascript">
  $(document).ready(function(){
      
      // open popups
      $("#salesVolumeTile").click(function()            { $("#salesVolumePopup").show(); return false; });
      $("#lastPurchaseTile").click(function()           { $("#lastPurchasePopup").show(); return false; });
      $("#storePurchaseRatioTile").click(function()     { $("#storePurchaseRatioPopup").show(); return false; });
      $("#averagePurchaseVolumeTile").click(function()  { $("#averagePurchaseVolumePopup").show(); return false; });
      $("#sentimentScoreTile").click(function()         { $("#sentimentScorePopup").show(); return false; });
      $("#activityScoreTile").click(function()          { $("#activityScorePopup").show(); return false; });
      $("#tagCloud").click(function()                   { $("#asmModalTagCloud").show(); return false; });
      // close popups
      $(".isce-customer360 .close").click(function()            { $(".isce-customer360 .modal").hide(); return false; });
      $(".isce-customer360 .btn.btn-default").click(function()  { $(".isce-customer360 .modal").hide(); return false; });
      
   });
  </script>

	<div class="row" role="presentation">
		<div class="col-sm-12 col-md-12 col-lg-12" role="presentation">
		
			<h3 id="quickViewTiles" class="isce-headline" aria-labelledby="quickViewTiles">
				<spring:theme code="instorecs.customer360.quickViewTiles" htmlEscape="true" />
			</h3>
      
      <div class="col-sm-0 col-md-1 col-lg-2" role="presentation" />
      <div class="col-sm-12 col-md-10 col-lg-8 isce-center" role="presentation">
				<%@include file="./customer360Content/tile-salesvolume.jsp"%>
				<%@include file="./customer360Content/tile-lastpurchase.jsp"%>
				<%@include file="./customer360Content/tile-storepurchaseratio.jsp"%>
				<%@include file="./customer360Content/tile-averagepurchasevolume.jsp"%>
				<%@include file="./customer360Content/tile-sentimentscore.jsp"%>
				<%@include file="./customer360Content/tile-activityscore.jsp"%>
			</div>
      <div class="col-sm-0 col-md-1 col-lg-2" role="presentation" />

		</div>
	</div>

	<c:if test="${genericScoresDataContainer.genericScoresList.size() > 0}">
		<div class="row" role="presentation">
			<div class="col-sm-6 col-md-6 col-lg-6" role="presentation">
        <%@include file="./customer360Content/generic.jsp"%>
			</div>
			<div class="col-sm-6 col-md-6 col-lg-6" role="presentation">
				<%@include file="./customer360Content/interests.jsp"%>
			</div>
		</div>
	</c:if>
	
	<c:if test="${genericScoresDataContainer.genericScoresList.size() == 0}">
		<div class="row" role="presentation">
			<div class="col-sm-12 col-md-12 col-lg-12" role="presentation">
			<%@include file="./customer360Content/interests.jsp"%>
			</div>
		</div>
	</c:if>
	
	<br/>
	
	<div class="row" role="presentation">
		<div class="col-sm-6 col-md-6 col-lg-6" role="presentation">
			<%@include file="./customer360Content/details.jsp"%>
		</div>
		<div class="col-sm-6 col-md-6 col-lg-6" role="presentation">
			<%@include file="./customer360Content/profile.jsp"%>
		</div>
	</div>

</div>

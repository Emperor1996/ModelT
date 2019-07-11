<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>


<script type="text/javascript">
	$.ajax({
		url: ACC.config.encodedContextPath + "/my-account/customer360",
      type: "POST",
		success : function(data) {
			$("#isceCustomerContent").html(data);
			$(document).ready(function() {
				if(typeof loadRecommendation == 'function'){
					loadRecommendation();
				}
				if(typeof loadOffers == 'function'){
					loadOffers();
				}
			});
			$.colorbox.resize();
		},
		error : function(xht, textStatus, ex) {
			console.error("Failed to get isce customer info. %s", ex);
			document.location.reload();
		}
	});
</script>

<div id="section">
	<div id="isceCustomerContent">
		<div class="loader"></div>
	</div>
	<div id="sectionPlaceholder"></div>
</div>

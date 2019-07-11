<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%@ taglib prefix="instorecs"
	uri="/WEB-INF/tld/addons/yinstorecsfrontendaddon/instorecs.tld"%>

<c:if test="${instorecs:showInvoiceLink(orderData)}">
	<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4 pull-right">
		<div class="isce-show-invoice">
			<c:set var="orderCodeForInvoicePDF" scope="session"
				value="${orderData.code}"></c:set>
			<a class="isce-show-invoice-link" id="showInvoiceLink" onClick="window.open('displayinvoicepdf/act', '_blank').focus(); return false;"><spring:theme code="instorecs.showInvoice" /></a>
		</div>
	</div>
	
	<script type="text/javascript">
		var orderCodeForInvoice = '${orderCodeForInvoice}';
		// avoid that invoice is opened more than once, if button is included multiple times
		if(!alreadyOpened){
			var alreadyOpened = false;
		}
		if (orderCodeForInvoice && !alreadyOpened)
		{	
			alreadyOpened = true;
			var win = window.open('displayinvoicepdf/act', '_blank');
			win.focus();
		}
	</script>
</c:if>
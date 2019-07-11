<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>

<div class="row isce-row">
<jsp:include page="handoverorderaction.jsp" />
<jsp:include page="showinvoiceaction.jsp" />
<jsp:include page="changeorderaction.jsp" />
</div>
<div class="row isce-row-msg">
<jsp:include page="isceactionmessage.jsp" />
</div>
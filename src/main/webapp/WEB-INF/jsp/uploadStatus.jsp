
<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<html>
<body>
<form method="POST" action="${pageContext.request.contextPath}/file/uploadStatus" enctype="multipart/form-data">
    <h1>Upload Status</h1>
    <h2>Message : ${message}</h2>
</form>

</body>
</html>
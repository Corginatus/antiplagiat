<jsp:include page="header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<html>
<head>
    <title>File Uploading Form</title>
</head>

<body>
<h3>File Upload:</h3>
Select a file to upload: <br />
<form action = "UploadServlet" method = "post" enctype = "multipart/form-data">
    <input type = "file" name = "file" size = "50" />
    <br />
    <input type = "submit" value = "Upload File" />
</form>
</body>
</html>
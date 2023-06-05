<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Ваш результат:</title>
</head>
<body>
<div>
    <h2>Ваш результат:</h2>
</div>
<div>
    <c:forEach items="${result}" var="text">
        <div>
            <tr>
                <h4>${text}</h4>
            </tr>
        </div>
    </c:forEach>
    <a href="/file/">Главная</a>
</div>
</body>
</html>
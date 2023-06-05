<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Ваши текста</title>
</head>
<body>
<div>
    <h2>Ваши текста</h2>
</div>
<div>
    <table>

        <tbody>
        <c:forEach items="${texts}" var="text">
            <div>
                <tr>
                    <h4>${text} <form action="/file/text" method="post">
                        <input type="hidden" name="text" value="${text}"/>
                        <button type="submit">Подробнее</button>
                    </form> </h4>
                </tr>
            </div>
        </c:forEach>
        </tbody>
    </table>
    <a href="/file/">Главная</a>
</div>
</body>
</html>
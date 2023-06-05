<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Сравнить ${name} с ...</title>
</head>
<body>
<div>
    <h2>Сравнить ${name} с ...</h2>
</div>
<div>
    <table>

        <tbody>
        <c:forEach items="${texts}" var="text">
            <div>
                <tr>
                    <h4>${text} <form action="/file/text_final" method="post">
                        <input type="hidden" name="name1" value="${name}"/>
                        <input type="hidden" name="name2" value="${text}"/>
                        <button type="submit">Сравнить</button>
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
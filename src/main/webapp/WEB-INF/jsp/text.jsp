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
    <h2>Ваш текст</h2>
</div>
<div>
    ${text}
    <h4><form action="/file/texts_compare" method="post">
        <input type="hidden" name="name" value="${name}"/>
        <button type="submit">Сравнить</button>
    </form> </h4>
    <a href="/file/">Главная</a>
</div>
</body>
</html>
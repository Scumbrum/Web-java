<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 14.04.2022
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h1 class="title">Registe, please</h1>
        <form action="." method="post" class="login-form">
            <input type="hidden" name="action" value="doReg">
            <label>Login:<input type="text" name="login"></label>
            <label>Name:<input type="text" name="name"></label>
            <label>Surname:<input type="text" name="surname"></label>
            <label>Patronymic<input type="text" name="patronymic"></label>
            <label>Region<input type="text" name="region"></label>
            <label>City<input type="text" name="city"></label>
            <label>Education<input type="text" name="education"></label>
            <label>Password<input type="password" name="password"></label>
            <label>Password<input type="password" name="passwordSecond"></label>
            <button type="submit">Register</button>
        </form>
        <c:if test="${!empty requestScope.error}">
            <h2 class="error">
                <c:out value="${requestScope.error}"/>
            </h2>
        </c:if>
        <form action="." class="bottom-button">
            <input type="hidden" name="action" value="toLogin">
            <button type="submit">Login</button>
        </form>
    </div>
</body>
</html>

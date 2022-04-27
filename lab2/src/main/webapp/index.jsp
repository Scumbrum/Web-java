<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="container">
    <h1 class="title">
        Login, please
    </h1>
    <form action="." method="post" class="login-form">
        <input type="hidden" name="action" value="doLogin">
        <label>Login:<input type="text" name="login"></label>
        <label>Password:<input type="password" name="password"></label>
        <button type="submit">Sign in</button>
    </form>

    <c:if test="${!empty requestScope.error}">
        <h2 class="error">
            <c:out value="${requestScope.error}"/>
        </h2>
    </c:if>

    <form action="." class="bottom-button">
        <input type="hidden" name="action" value="toReg">
        <button type="submit">Registration</button>
    </form>
</div>

</body>
</html>
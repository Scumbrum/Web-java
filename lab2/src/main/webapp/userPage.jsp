<%@ page import="java.util.ArrayList" %>
<%@ page import="connection.entities.Discipline" %>
<%@ page import="Config.Pages" %>
<%@ page import="Config.Params" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 21.04.2022
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="styles/styles.css">
</head>
<body>
<header>
    <div class="container">
        <nav>
            <ul>
                <li>
                    <form action="./user" method="post">
                        <input type="hidden" name="action" value="toFaculties">
                        <button type="submit" class="nav-item">Faculties</button>
                    </form>
                </li>
                <li>
                    <form action="./user" method="post">
                        <input type="hidden" name="action" value="toStatement">
                        <button type="submit" class="nav-item">My Statements</button>
                    </form>
                </li>
            </ul>
            <ul>
                <li>
                    <form action="./user" method="post">
                        <input type="hidden" name="action" value="toAccount">
                        <button type="submit" class="nav-item">My Account</button>
                    </form>
                </li>
                <li>
                    <form action="." method="post">
                        <input type="hidden" name="action" value="logout">
                        <button type="submit" class="logout">Logout</button>
                    </form>
                </li>
            </ul>
        </nav>
    </div>
</header>
<main>
    <c:if test="${!empty requestScope.faculties}">
        <section class="faculties container">
            <c:if test="${!empty requestScope.error}">
                <h2 class="error center">${requestScope.error}</h2>
            </c:if>
            <h1>
                Faculty list
            </h1>
            <form class="sorted-form" method="post">
                <label>
                    Sort by:
                    <select action="./user" name="action">
                        <option selected value="sortByAll">
                            All places
                        </option >
                        <option value="sortByBudget">
                            Budget places
                        </option>
                        <option value="sortByName">
                            Name
                        </option>
                        <option value="sortByReverseName">
                            Reverse Name
                        </option>
                    </select>
                </label>
                <button type="submit">Show</button>
            </form>
            <ul class="faculties-list list">
                <c:forEach  var="faculty" items="${requestScope.faculties}">
                    <li>
                        <h2 class="name">
                            <c:out value="${faculty.getName()}"/>
                        </h2>
                        <p>
                            <c:out value="${faculty.getDescription()}"/>
                        </p>
                        <p>
                            All: <c:out value="${faculty.getAllPlace()}"/>
                        </p>
                        <p>
                            Budget: <c:out value="${faculty.getBudgetPlace()}"/>
                        </p>
                        <form action="./user" method="post">
                            <input type="hidden" value="${faculty.getId()}" name="id">
                            <input type="hidden" name="action" value="toFacultyReg">
                            <button type="submit">Register</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>
    <c:if test="${!empty requestScope.disciplines}">
        <section class="faculty-register container">
            <h1>
                Registration on ${requestScope.targetFaculty.getName()}
            </h1>
            <form action="./user" class="top-form" method="post">
                <input type="hidden" name="id" value="${requestScope.targetFaculty.getId()}">
                <input type="hidden" name="action" value="addDiscipline">
                <c:forEach var="discipline" items="${requestScope.disciplines}">
                    <input type="hidden" name="exists" value="${discipline.getId()}">
                </c:forEach>
                <select name="discipline" class="disciplines">
                <c:forEach var="addition" items="${requestScope.additional}">
                    <option value="${addition.getId()}">${addition.getName()}</option>
                </c:forEach>
                </select>
                <button type="submit" class="add-btn">Add more</button>
            </form>
            <form action="./user" class="faculty-form registration" method="post">
                <input type="hidden" name="id" value="${requestScope.targetFaculty.getId()}">
                <c:forEach var="discipline" items="${requestScope.disciplines}">
                    <label>${discipline.getName()}: <input name="${discipline.getId()}" type="number"></label>
                </c:forEach>
                <div class="button-group">
                    <button type="submit" name="action" value="doFacultyReg">Register</button>
                    <button type="submit" name="action" value="cancelReg" class="delete">Cancel</button>
                </div>
            </form>
        </section>
    </c:if>
    <c:if test="${!empty requestScope.statements}">
        <section class="faculties container">
            <h1>
                Statement list
            </h1>
            <ul class="statement-list list view">
                <c:forEach var="statement" items="${requestScope.statements}">
                    <li>
                        <h2 class="name">
                            ${statement.getFaculty().getName()}
                        </h2>
                        <c:if test="${statement.getStatus() == 1}">
                            <p class="info">
                                Processed...
                            </p>
                        </c:if>
                        <c:if test="${statement.getStatus() == 2}">
                            <p class="info">
                                Contract
                            </p>
                        </c:if>
                        <c:if test="${  statement.getStatus() == 3}">
                            <p class="error">
                                Rejected
                            </p>
                        </c:if>
                        <c:if test="${  statement.getStatus() == 4}">
                            <p class="success">
                                Budget
                            </p>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>
    <c:if test="${!empty requestScope.user}">
        <section class="account container">
            <h1>Your data</h1>
            <form action="./user"  class="account-form login-form" method="post">
                <input type="hidden" name="action" value="changeAccountData">
                <label>Login: <input name="login" value="vlad"></label>
                <label>Login: <input name="login" value="vlad"></label>
                <label>Login: <input name="login" value="vlad"></label>
                <label>Login: <input name="login" value="vlad"></label>
                <button type="submit">Apply</button>
            </form>
        </section>
    </c:if>
</main>
</body>
</html>

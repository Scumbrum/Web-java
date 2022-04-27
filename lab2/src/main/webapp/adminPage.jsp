<%--
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
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<header>
    <div class="container">
        <nav>
            <ul>
                <li>
                    <form action=".">
                        <input type="hidden" name="action" value="toFaculties">
                        <button type="submit" class="nav-item">Faculties</button>
                    </form>
                </li>
                <li>
                    <form action=".">
                        <input type="hidden" name="action" value="toStatement">
                        <button type="submit" class="nav-item">Statements</button>
                    </form>
                </li>
            </ul>
            <ul>
                <li>
                    <form action=".">
                        <input type="hidden" name="action" value="toAccount">
                        <button type="submit" class="nav-item admin">Admin</button>
                    </form>
                </li>
                <li>
                    <form action=".">
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
            <h1>
                Faculty list
            </h1>
            <form class="form">
                <input type="hidden" name="action" value="toFacultyForm">
                <button type="submit">Add</button>
            </form>
            <c:if test="${!empty requestScope.isOpen}">

                <c:set var="faculty" value="${requestScope.targetFaculty}"/>

                <c:if test="${!empty faculty.getId()}">
                    <c:set var="action" value="editFaculty"/>
                </c:if>

                <c:if test="${empty faculty.getId()}">
                    <c:set var="action" value="addFaculty"/>
                </c:if>

                <form class="add-form">
                    <input type="hidden" name="id" value="${faculty.getId()}">
                    <label>Name: <input type="text" name="name" value="${faculty.getName()}"></label>
                    <label>Description: <input type="text" name="description" value="${faculty.getDescription()}"></label>
                    <label>All places: <input type="number" name="allPlaces" value="${faculty.getAllPlace()}"></label>
                    <label>Budget places: <input type="number" name="budgetPlaces" value="${faculty.getBudgetPlace()}"></label>
                    <button type="submit" name="action" value="${action}">Confirm</button>
                    <button type="submit" name="action" value="cancelFaculty" class="delete">Cancel</button>
                </form>

            </c:if>
            <ul class="faculties-list list">
                <c:forEach var="faculty" items="${requestScope.faculties}">
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
                        <form>
                            <input type="hidden" value="${faculty.getId()}" name="id">
                            <button type="submit" class="delete" name="action" value="deleteFaculty">Delete</button>
                        </form>
                        <form>
                            <input type="hidden" value="${faculty.getId()}" name="id">
                            <button type="submit" name="action" value="toEditFaculty">Edit</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>
    <c:if test="${!empty requestScope.statements}">
        <section class="faculties container">
            <h1>
                Statement list
            </h1>
            <ul class="statement-list list">
                <c:forEach var="statement" items="${requestScope.statements}">
                    <li>
                        <div class="statement-data">
                            <h2 class="name">
                                ${statement.getName()}
                            </h2>
                            <form>
                                <input type="hidden" name="id" value="${statement.getId()}">
                                <button type="submit" name="action" value="toFacultyStatements">Show Entrant</button>
                            </form>
                        </div>
                        <form action="." class="discipline-chooser">
                            <input type="hidden" name="id" value="${statement.getId()}">
                            <div class="check-box-container">
                                <c:forEach var="discipline" items="${requestScope.disciplines}">
                                    <label>${discipline.getName()}: <input type="checkbox" name="discipline" value="${discipline.getId()}"></label>
                                </c:forEach>
                            </div>
                            <button type="submit" name="action" value="finalise">Finalise</button>
                        </form>
                        <c:if test="${statement.getId() == requestScope.current}">
                            <c:if test="${requestScope.entrants.size()==0}">
                                <h1 class="info">No Statement</h1>
                            </c:if>
                            <c:if test="${requestScope.entrants.size()>0}">
                                <ul class="e-list">
                                    <c:forEach var="entrant" items="${requestScope.entrants}">
                                        <li class="e-box">
                                            <div class="e-preview">
                                                <h3>${entrant.getUser().getName()}</h3>
                                                <form>
                                                    <input type="hidden" name="id" value="${statement.getId()}">
                                                    <input type="hidden" name="target" value="${entrant.getId()}">
                                                    <button type="submit" name="action" value="showUserData">+</button>
                                                    <button type="submit" name="action" value="deleteUser" class="delete">&#10006</button>
                                                </form>
                                            </div>
                                            <c:if test="${entrant.getId() == requestScope.target}">
                                                <ul>
                                                    <c:forEach var="score" items="${requestScope.scores}">
                                                        <li>
                                                            <span class="discipline">${score.getDiscipline().getName()}:</span>
                                                                ${score.getMark()}
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </c:if>

                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:if>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </section>
    </c:if>
    <c:if test="${!empty requestScope.user}">
        <section class="account container">
            <h1>Admin data</h1>
            <form class="account-form login-form">
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

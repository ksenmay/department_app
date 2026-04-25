<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Расписание кафедры</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="header">
    <div class="titles">
        <h1>Кафедра</h1>
        <h2>Расписание</h2>
    </div>
    <div class="auth">
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <button type="submit">Выйти</button>
        </form>
        Пользователь:
        <c:out value="${sessionScope.userInfo.surname}"/>
        <c:out value="${sessionScope.userInfo.name}"/>
        <c:out value="${sessionScope.userInfo.patronymic}"/>
    </div>
</div>

<div class="container">

    <h2 class="section-title">Расписание дисциплин</h2>

    <c:if test="${sessionScope.userInfo.role.name() == 'ADMIN'}">
        <div class="actions">
            <a href="${pageContext.request.contextPath}/add-group">
                <button type="button">Добавить группу</button>
            </a>
            <!-- <a href="${pageContext.request.contextPath}/add-user">
                                <button type="button">Добавить пользователя</button>
            </a> -->
            <a href="${pageContext.request.contextPath}/add-discipline">
                                    <button type="button">Добавить дисциплину</button>
            </a>
        </div>
    </c:if>

    <form method="get" action="${pageContext.request.contextPath}/disciplines">
        <div class="form-item">
            <input type="checkbox" name="showAll" value="true"
                   <c:if test="${param.showAll == 'true'}">checked</c:if> />
            <span>Показать все дисциплины кафедры</span>
        </div>
        <button type="submit">Обновить</button>
    </form>

    <c:if test="${not empty schedule}">
        <table>
            <thead>
            <tr>
                <th>Дисциплина</th>
                <th>Преподаватель</th>
                <th>Группа</th>
                <th>Лекции</th>
                <th>Практика</th>
                <th>Лабораторные</th>
                <th>Экзамен</th>
                <th>Зачёт</th>
                <c:if test="${sessionScope.userInfo.role.name() == 'ADMIN'}">
                    <th>Действия</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="row" items="${schedule}">
                <tr>
                    <td><c:out value="${row['discipline'].name}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty row['teacher']}">
                                <c:out value="${row['teacher'].surname} ${row['teacher'].name} ${row['teacher'].patronymic}" />
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty row['group']}">
                                <c:out value="${row['group'].groupNumber}" />
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                    <td><c:out value="${row['discipline'].lectureHours}" /></td>
                    <td><c:out value="${row['discipline'].practicalHours}" /></td>
                    <td><c:out value="${row['discipline'].labHours}" /></td>
                    <td>
                        <c:choose>
                            <c:when test="${row['discipline'].exam}">Да</c:when>
                            <c:otherwise>Нет</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${row['discipline'].test}">Да</c:when>
                            <c:otherwise>Нет</c:otherwise>
                        </c:choose>
                    </td>
                    <c:if test="${sessionScope.userInfo.role.name() == 'ADMIN'}">
                        <td>
                            <form action="${pageContext.request.contextPath}/delete-discipline" method="post" style="margin:0;">
                                <input type="hidden" name="disciplineId" value="${row['discipline'].id}" />
                                <button type="submit" onclick="return confirm('Вы уверены, что хотите удалить дисциплину?')">
                                    Удалить
                                </button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <h3>Общее количество аудиторных часов по кафедре</h3>
        <ul>
            <li>Лекции: <c:out value="${hours['lectureHours']}" /></li>
            <li>Практика: <c:out value="${hours['practicalHours']}" /></li>
            <li>Лабораторные: <c:out value="${hours['labHours']}" /></li>
            <li>Всего: <c:out value="${hours['totalHours']}" /></li>
        </ul>
    </c:if>

    <c:if test="${empty schedule}">
        <div class="message">Расписание отсутствует.</div>
    </c:if>

</div>
</body>
</html>
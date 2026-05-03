<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, by.may.department.connection.ConnectionPool" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Department app</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>

<body>

    <div class="header">
        <div class="titles">
            <h1>Кафедра</h1>
            <h2>Список дисциплин</h2>
        </div>
    </div>

<div class="container">

    <h1>Добро пожаловать в Department App</h1>
    <p>
        Это веб-приложение для управления дисциплинами кафедры.
        Здесь вы можете просматривать список дисциплин, информацию о преподавателях и группах,
        а также вести учет часов лекций, практических и лабораторных занятий.
        Доступ к функционалу ограничен в зависимости от вашей роли.
    </p>
    <a class="button" href="${pageContext.request.contextPath}/app?command=login">Войти в систему</a>
</div>
</body>

</html>
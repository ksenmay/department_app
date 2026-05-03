<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Добавление группы</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="header">
    <div class="titles">
        <h1>Кафедра</h1>
        <h2>Добавление группы</h2>
    </div>
    <div class="auth">
        <form action="${pageContext.request.contextPath}//app?command=logout" method="post">
            <button type="submit">Выйти</button>
        </form>
        Пользователь:
        <c:out value="${sessionScope.userInfo.surname}"/>
        <c:out value="${sessionScope.userInfo.name}"/>
        <c:out value="${sessionScope.userInfo.patronymic}"/>
    </div>
</div>

<div class="container">

    <h2 class="section-title">Добавление новой группы</h2>

    <!-- Сообщения -->
    <c:if test="${not empty message}">
        <div class="message" style="background-color: #e0f7e9; color: #2f662e;">
            ${message}
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message" style="background-color: var(--error-bg); color: #b00020;">
            ${error}
        </div>
    </c:if>

    <!-- Форма добавления группы -->
    <form action="${pageContext.request.contextPath}//app?command=addGroup" method="post" class="admin-form">
        <div class="form-item">
            <label for="groupNumber">Номер группы:</label>
            <input type="number" name="groupNumber" id="groupNumber" required min="1" placeholder="Введите номер группы">
        </div>
        <div class="form-item">
            <label for="quantityOfStudents">Количество студентов:</label>
            <input type="number" name="quantityOfStudents" id="quantityOfStudents" required min="1" placeholder="Введите количество студентов">
        </div>
        <div class="form-item">
            <button type="submit">Добавить группу</button>
        </div>
    </form>

    <div class="actions">
        <a href="${pageContext.request.contextPath}//app?command=showDisciplines">Вернуться к кафедре</a>
    </div>

</div>
</body>
</html>
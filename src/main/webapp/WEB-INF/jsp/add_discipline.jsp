<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Добавление дисциплины</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="header">
    <div class="titles">
        <h1>Кафедра</h1>
        <h2>Добавление дисциплины</h2>
    </div>
    <div class="auth">
        <form action="${pageContext.request.contextPath}/app?command=logout" method="post">
            <button type="submit">Выйти</button>
        </form>
        Пользователь:
        <c:out value="${sessionScope.userInfo.surname}"/>
        <c:out value="${sessionScope.userInfo.name}"/>
        <c:out value="${sessionScope.userInfo.patronymic}"/>
    </div>
</div>

<div class="container">

    <h2 class="section-title">Добавление новой дисциплины</h2>

    <!-- Flash сообщения -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="message" style="background-color: #e0f7e9; color: #2f662e;">
            ${sessionScope.successMessage}
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message" style="background-color: var(--error-bg); color: #b00020;">
            ${error}
        </div>
    </c:if>

    <!-- Форма добавления дисциплины -->
        <form action="${pageContext.request.contextPath}/app?command=addDiscipline" method="post">        <div class="form-item">
            <label for="name">Название дисциплины:</label>
            <input type="text" name="name" id="name" required placeholder="Введите название">
        </div>
        <div class="form-item">
            <label for="lectureHours">Лекции:</label>
            <input type="number" name="lectureHours" id="lectureHours" min="0" value="0">
        </div>
        <div class="form-item">
            <label for="practicalHours">Практика:</label>
            <input type="number" name="practicalHours" id="practicalHours" min="0" value="0">
        </div>
        <div class="form-item">
            <label for="labHours">Лабораторные:</label>
            <input type="number" name="labHours" id="labHours" min="0" value="0">
        </div>
        <div class="form-item">
            <label><input type="checkbox" name="exam"> Экзамен</label>
        </div>
        <div class="form-item">
            <label><input type="checkbox" name="test"> Зачёт</label>
        </div>

        <!-- Выбор групп -->
        <div class="form-item">
            <label>Выберите группы:</label>
            <c:forEach var="group" items="${groups}">
                <div>
                    <label>
                        <input type="checkbox" name="groupIds" value="${group.groupId}">
                        ${group.groupNumber}
                    </label>
                </div>
            </c:forEach>
        </div>

        <!-- Выбор преподавателей -->
        <div class="form-item">
            <label>Выберите преподавателей:</label>
            <c:forEach var="teacher" items="${teachers}">
                <div>
                    <label>
                        <input type="checkbox" name="teacherIds" value="${teacher.id}">
                        ${teacher.userInfo.surname} ${teacher.userInfo.name} ${teacher.userInfo.patronymic} <!-- ФИО преподавателя -->
                    </label>
                </div>
            </c:forEach>
        </div>

        <div class="form-item">
            <button type="submit">Добавить дисциплину</button>
        </div>
    </form>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/app?command=showDisciplines">Вернуться к кафедре</a>
    </div>

</div>
</body>
</html>
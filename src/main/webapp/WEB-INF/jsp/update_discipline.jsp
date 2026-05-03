<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Редактирование дисциплины</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="header">
    <div class="titles">
        <h1>Кафедра</h1>
        <h2>Редактирование дисциплины</h2>
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

    <h2 class="section-title">Редактирование дисциплины</h2>

    <c:if test="${not empty error}">
        <div class="message" style="background-color: var(--error-bg); color: #b00020;">
            ${error}
        </div>
    </c:if>

    <!-- UPDATE FORM -->
    <form action="${pageContext.request.contextPath}/update-discipline" method="post" class="admin-form">

        <!-- ID -->
        <input type="hidden" name="id" value="${discipline.id}">

        <div class="form-item">
            <label>Название дисциплины:</label>
            <input type="text" name="name" value="${discipline.name}" required>
        </div>

        <div class="form-item">
            <label>Лекции:</label>
            <input type="number" name="lectureHours" min="0" value="${discipline.lectureHours}">
        </div>

        <div class="form-item">
            <label>Практика:</label>
            <input type="number" name="practicalHours" min="0" value="${discipline.practicalHours}">
        </div>

        <div class="form-item">
            <label>Лабораторные:</label>
            <input type="number" name="labHours" min="0" value="${discipline.labHours}">
        </div>

        <div class="form-item">
            <label>
                <input type="checkbox" name="exam"
                       <c:if test="${discipline.exam}">checked</c:if>>
                Экзамен
            </label>
        </div>

        <div class="form-item">
            <label>
                <input type="checkbox" name="test"
                       <c:if test="${discipline.test}">checked</c:if>>
                Зачёт
            </label>
        </div>

        <!-- ГРУППЫ -->
        <div class="form-item">
            <label>Выберите группы:</label>

            <c:forEach var="group" items="${groups}">
                <div>
                    <label>
                        <input type="checkbox"
                               name="groupIds"
                               value="${group.groupId}"
                               <c:if test="${disciplineGroups.contains(group.groupId)}">checked</c:if>>
                        ${group.groupNumber}
                    </label>
                </div>
            </c:forEach>
        </div>

        <!-- ПРЕПОДАВАТЕЛИ -->
        <div class="form-item">
            <label>Выберите преподавателей:</label>

            <c:forEach var="teacher" items="${teachers}">
                <div>
                    <label>
                        <input type="checkbox"
                               name="teacherIds"
                               value="${teacher.id}"
                               <c:if test="${disciplineTeachers.contains(teacher.id)}">checked</c:if>>
                        ${teacher.userInfo.surname}
                        ${teacher.userInfo.name}
                        ${teacher.userInfo.patronymic}
                    </label>
                </div>
            </c:forEach>
        </div>

        <div class="form-item">
            <button type="submit">Сохранить изменения</button>
        </div>

    </form>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/disciplines">Назад</a>
    </div>

</div>

</body>
</html>
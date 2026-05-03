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

    <h2 class="section-title">Редактирование дисциплины</h2>

    <c:if test="${not empty error}">
        <div class="message" style="background-color: var(--error-bg); color: #b00020;">
            ${error}
        </div>
    </c:if>

    <!-- FORM -->
    <form action="${pageContext.request.contextPath}/app?command=updateDiscipline"
          method="post"
          class="admin-form">

        <input type="hidden" name="id" value="${discipline.id}">

        <!-- NAME -->
        <div class="form-item">
            <label>Название дисциплины:</label>
            <input type="text" name="name" value="${discipline.name}" required>
        </div>

        <!-- HOURS -->
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

/        <div class="form-item checkbox-wrapper">
            <label>
                <div class="checkbox-wrapper">
                    <input type="checkbox" name="exam" id="exam"
                           <c:if test="${discipline.exam}">checked</c:if>>
                    <label for="exam">Экзамен</label>
                </div>
            </label>
        </div>

        <div class="form-item checkbox-wrapper">
            <label>
                <input type="checkbox" name="test"
                       <c:if test="${discipline.test}">checked</c:if>>
                Зачёт
            </label>
        </div>

        <!-- GROUPS -->
        <div class="form-item">
            <label>Выберите группы:</label>

            <<c:forEach var="group" items="${groups}">
                 <div class="checkbox-wrapper">
                     <input type="checkbox"
                            id="group-${group.groupId}"
                            name="groupIds"
                            value="${group.groupId}"
                            <c:if test="${disciplineGroups.contains(group.groupId)}">checked</c:if>>

                     <label for="group-${group.groupId}">
                             ${group.groupNumber}
                     </label>
                 </div>
             </c:forEach>
        </div>

        <!-- TEACHERS -->
        <div class="form-item">
            <label>Выберите преподавателей:</label>

            <c:forEach var="teacher" items="${teachers}">
                <div class="checkbox-wrapper">
                    <input type="checkbox"
                           id="teacher-${teacher.id}"
                           name="teacherIds"
                           value="${teacher.id}"
                           <c:if test="${disciplineTeachers.contains(teacher.id)}">checked</c:if>>

                    <label for="teacher-${teacher.id}">
                        ${teacher.userInfo.surname}
                        ${teacher.userInfo.name}
                        ${teacher.userInfo.patronymic}
                    </label>
                </div>
            </c:forEach>
        </div>

        <!-- SUBMIT -->
        <div class="form-item">
            <button type="submit">Сохранить изменения</button>
        </div>

    </form>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/app?command=showDisciplines">
            Назад
        </a>
    </div>

</div>

</body>
</html>
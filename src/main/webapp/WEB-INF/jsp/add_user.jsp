<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head>
    <title>Добавить пользователя</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<div class="container">

    <h2 class="section-title">Добавление пользователя</h2>

    <!-- сообщение об ошибке -->
    <c:if test="${not empty error}">
        <div class="message" style="background-color: #ffd6e0;">
            <c:out value="${error}" />
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/app?command=addUser" method="post" class="admin-form">

        <div class="form-item">
            <label>Логин</label>
            <input type="text" name="username" required>
        </div>

        <div class="form-item">
            <label>Пароль</label>
            <input type="text" name="password" required>
        </div>

        <div class="form-item">
            <label>Фамилия</label>
            <input type="text" name="surname" required>
        </div>

        <div class="form-item">
            <label>Имя</label>
            <input type="text" name="name" required>
        </div>

        <div class="form-item">
            <label>Отчество</label>
            <input type="text" name="patronymic" required>
        </div>

        <div class="form-item">
            <label>Роль</label>
            <select name="role" id="roleSelect" required>
                <option value="admin">Администратор</option>
                <option value="teacher">Преподаватель</option>
                <option value="student">Студент</option>
            </select>
        </div>

        <div class="form-item" id="groupBlock" style="display: none;">
            <label>Группа</label>
            <select name="groupId">
                <option value="">выберите группу</option>
                <c:forEach var="group" items="${groups}">
                    <option value="${group.groupId}">
                        <c:out value="${group.groupNumber}" />
                    </option>
                </c:forEach>
            </select>
        </div>

        <button type="submit">Создать пользователя</button>
    </form>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/app?command=showDisciplines">Назад</a>
    </div>

</div>

<script>
    const roleSelect = document.getElementById("roleSelect");
    const groupBlock = document.getElementById("groupBlock");

    function toggleGroup() {
        if (roleSelect.value === "student") {
            groupBlock.style.display = "flex";
        } else {
            groupBlock.style.display = "none";
        }
    }

    roleSelect.addEventListener("change", toggleGroup);

    toggleGroup();
</script>

</body>
</html>
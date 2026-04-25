<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Вход</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    </head>
<body>

<div class="header">
    <div class="titles">
        <h1>Кафедра</h1>
        <h2>Список дисциплин</h2>
    </div>
</div>

<div class="login-container">
    <h2>Вход в систему</h2>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div>
            <label>Логин:</label>
            <input type="text" name="username" required>
        </div>
        <div>
            <label>Пароль:</label>
            <input type="password" name="password" required>
        </div>
        <div>
            <button type="submit">Войти</button>
        </div>
    </form>

    <c:if test="${not empty error}">
            <div class="message">${error}</div>
    </c:if>

</div>

</body>
</html>>
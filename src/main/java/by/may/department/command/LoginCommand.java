package by.may.department.command;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.User;
import by.may.department.model.interfaces.IUserInfo;
import by.may.department.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginCommand implements Command {

    private final AuthService authService = ServiceFactory.getInstance().getAuthService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if ("GET".equalsIgnoreCase(request.getMethod())) {

            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                    .forward(request, response);
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = authService.login(username, password);

        if (user == null) {
            request.setAttribute("error", "Неверный логин или пароль");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                    .forward(request, response);
            return;
        }

        IUserInfo info = user.getUserInfo();

        if (info == null) {
            request.setAttribute("error", "Нет данных о пользователе");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", info.getRole());
        session.setAttribute("userInfo", info);

        response.sendRedirect(request.getContextPath() + "/app?command=showDisciplines");
    }
}
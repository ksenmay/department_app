package by.may.department.servlet;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.User;
import by.may.department.model.UserInfo;
import by.may.department.model.enums.Role;
import by.may.department.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class AddUserServlet extends HttpServlet {

    private final UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/add_user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            String username = req.getParameter("username");
            String password = req.getParameter("password");

            User user = User.builder()
                    .username(username)
                    .password(password)
                    .build();

            String roleStr = req.getParameter("role");
            Role role = Role.valueOf(roleStr.toUpperCase());

            String name = req.getParameter("name");
            String surname = req.getParameter("surname");
            String patronymic = req.getParameter("patronymic");

            String groupIdStr = req.getParameter("groupId");
            int groupId = 0;

            if (role == Role.STUDENT) {
                if (groupIdStr == null || groupIdStr.isBlank()) {
                    throw new IllegalArgumentException("Для студента необходимо указать группу");
                }
                groupId = Integer.parseInt(groupIdStr);
            }

            UserInfo userInfo = UserInfo.builder()
                    .role(role)
                    .name(name)
                    .surname(surname)
                    .patronymic(patronymic)
                    .groupId(groupId)
                    .build();

            userService.createUser(user, userInfo);

            resp.sendRedirect(req.getContextPath() + "/disciplines");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/add_user.jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Ошибка при создании пользователя");
            req.getRequestDispatcher("/WEB-INF/jsp/add_user.jsp").forward(req, resp);
        }
    }
}
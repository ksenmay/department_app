package by.may.department.servlet;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.Group;
import by.may.department.service.GroupService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddGroupServlet extends HttpServlet {

    private final GroupService groupService = ServiceFactory.getInstance().getGroupService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/add_group.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int groupNumber = Integer.parseInt(req.getParameter("groupNumber"));
            int quantity = Integer.parseInt(req.getParameter("quantityOfStudents"));

            Group group = Group.builder()
                    .groupNumber(groupNumber)
                    .quantityOfStudent(quantity)
                    .build();
            groupService.createGroup(group);
            req.setAttribute("message", "Группа добавлена успешно");
        }   catch (Exception e) {
            req.setAttribute("error", "Ошибка добавления группы " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/jsp/add_group.jsp").forward(req, resp);
    }

}

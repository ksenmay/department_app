package by.may.department.command;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.Group;
import by.may.department.service.GroupService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddGroupCommand implements Command {

    private final GroupService groupService =
            ServiceFactory.getInstance().getGroupService();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(req.getMethod())) {

            req.getRequestDispatcher("/WEB-INF/jsp/add_group.jsp")
                    .forward(req, resp);
            return;
        }

        //POST
        try {
            int groupNumber = Integer.parseInt(req.getParameter("groupNumber"));
            int quantity = Integer.parseInt(req.getParameter("quantityOfStudents"));

            Group group = Group.builder()
                    .groupNumber(groupNumber)
                    .quantityOfStudent(quantity)
                    .build();

            groupService.createGroup(group);

            req.setAttribute("message", "Группа добавлена успешно");

        } catch (Exception e) {
            req.setAttribute("error", "Ошибка добавления группы " + e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/add_group.jsp")
                .forward(req, resp);
    }
}
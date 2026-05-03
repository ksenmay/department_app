package by.may.department.command;

import by.may.department.factory.ServiceFactory;
import by.may.department.service.DisciplineService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteDisciplineCommand implements Command {

    private final DisciplineService disciplineService =
            ServiceFactory.getInstance().getDisciplineService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idParam = request.getParameter("disciplineId");

            if (idParam != null) {
                int disciplineId = Integer.parseInt(idParam);
                disciplineService.deleteDiscipline(disciplineId);
            }

        } catch (NumberFormatException ignored) {}

        response.sendRedirect(request.getContextPath() + "/app?command=showDisciplines");
    }
}
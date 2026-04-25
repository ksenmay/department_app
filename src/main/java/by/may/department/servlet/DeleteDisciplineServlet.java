package by.may.department.servlet;

import by.may.department.factory.ServiceFactory;
import by.may.department.service.DisciplineService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteDisciplineServlet extends HttpServlet {

    private final DisciplineService disciplineService = ServiceFactory.getInstance().getDisciplineService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("disciplineId");
        if (idParam != null) {
            try {
                int disciplineId = Integer.parseInt(idParam);
                disciplineService.deleteDiscipline(disciplineId);
            } catch (NumberFormatException e) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/disciplines");
    }
}
package by.may.department.command;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.Discipline;
import by.may.department.model.User;
import by.may.department.model.enums.Role;
import by.may.department.service.DisciplineService;
import by.may.department.service.TimetableService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DisciplineCommand implements Command {

    private final DisciplineService disciplineService =
            ServiceFactory.getInstance().getDisciplineService();

    private final TimetableService timetableService =
            ServiceFactory.getInstance().getTimetableService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("user");

        if (user == null || user.getUserInfo() == null) {
            response.sendRedirect(request.getContextPath() + "/app?command=login");
            return;
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {

            String showAll = request.getParameter("showAll");

            List<Map<String, Object>> schedule;

            if ("true".equals(showAll)) {
                schedule = timetableService.buildFullSchedule();
            } else {
                schedule = timetableService.getScheduleForUser(user);
            }

            for (Map<String, Object> row : schedule) {
                System.out.println("Discipline: " + row.get("discipline")
                        + ", Teacher: " + row.get("teacher")
                        + ", Group: " + row.get("group"));
            }

            request.setAttribute("schedule", schedule);

            Map<String, Integer> hours = timetableService.calculateHours();
            request.setAttribute("hours", hours);

            request.setAttribute("role", user.getUserInfo().getRole());

            request.getRequestDispatcher("/WEB-INF/jsp/disciplines.jsp")
                    .forward(request, response);

            return;
        }

        //POST
        try {

            String action = request.getParameter("action");

            if ("create".equals(action)) {

                if (user.getUserInfo().getRole() != Role.ADMIN) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Нет прав для создания дисциплины");
                    return;
                }

                String name = request.getParameter("name");
                int lecture = Integer.parseInt(request.getParameter("lectureHours"));
                int practical = Integer.parseInt(request.getParameter("practicalHours"));
                int lab = Integer.parseInt(request.getParameter("labHours"));
                boolean exam = "on".equals(request.getParameter("exam"));
                boolean test = "on".equals(request.getParameter("test"));

                Discipline discipline = Discipline.builder()
                        .name(name)
                        .lectureHours(lecture)
                        .practicalHours(practical)
                        .labHours(lab)
                        .exam(exam)
                        .test(test)
                        .build();

                disciplineService.createDiscipline(discipline);
            }

            else if ("delete".equals(action)) {

                int disciplineId = Integer.parseInt(request.getParameter("disciplineId"));

                if (!timetableService.canDelete(user, disciplineId)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "Нет прав для удаления дисциплины");
                    return;
                }

                disciplineService.deleteDiscipline(disciplineId);
            }

        } catch (Exception e) {
            System.out.println("Error in DisciplineCommand: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/app?command=showDisciplines?showAll=true");
    }
}
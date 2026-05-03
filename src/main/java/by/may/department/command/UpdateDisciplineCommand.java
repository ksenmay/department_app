package by.may.department.command;

import by.may.department.factory.ServiceFactory;
import by.may.department.model.Discipline;
import by.may.department.model.Group;
import by.may.department.model.User;
import by.may.department.service.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class UpdateDisciplineCommand implements Command {

    private final DisciplineService disciplineService =
            ServiceFactory.getInstance().getDisciplineService();

    private final DisciplineGroupService disciplineGroupService =
            ServiceFactory.getInstance().getDisciplineGroupService();

    private final DisciplineTeacherService disciplineTeacherService =
            ServiceFactory.getInstance().getDisciplineTeacherService();

    private final GroupService groupService =
            ServiceFactory.getInstance().getGroupService();

    private final UserService userService =
            ServiceFactory.getInstance().getUserService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();

        if ("GET".equalsIgnoreCase(request.getMethod())) {

            int id = Integer.parseInt(request.getParameter("id"));

            Discipline discipline = disciplineService.getDisciplineById(id);
            List<Group> allGroups = groupService.getAllGroups();
            List<User> teachers = userService.getAllTeachers();

            request.setAttribute("groups", allGroups);
            request.setAttribute("teachers", teachers);
            request.setAttribute("discipline", discipline);

            request.getRequestDispatcher("/WEB-INF/jsp/update_discipline.jsp")
                    .forward(request, response);

            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String lectureStr = request.getParameter("lectureHours");
        String practicalStr = request.getParameter("practicalHours");
        String labStr = request.getParameter("labHours");
        String examStr = request.getParameter("exam");
        String testStr = request.getParameter("test");

        String[] groupIds = request.getParameterValues("groupIds");
        String[] teacherIds = request.getParameterValues("teacherIds");

        String error = null;

        if (name == null || name.isBlank()) {
            error = "Название дисциплины обязательно";
        }

        if ((groupIds == null || groupIds.length == 0)
                || (teacherIds == null || teacherIds.length == 0)) {
            error = "Необходимо выбрать хотя бы одну группу и одного преподавателя";
        }

        int lecture = parseOrDefault(lectureStr, 0);
        int practical = parseOrDefault(practicalStr, 0);
        int lab = parseOrDefault(labStr, 0);
        boolean exam = "on".equalsIgnoreCase(examStr);
        boolean test = "on".equalsIgnoreCase(testStr);

        if (error != null) {
            request.setAttribute("error", error);

            request.getRequestDispatcher("/WEB-INF/jsp/update_discipline.jsp")
                    .forward(request, response);

            return;
        }

        Discipline discipline = Discipline.builder()
                .id(id)
                .name(name)
                .lectureHours(lecture)
                .practicalHours(practical)
                .labHours(lab)
                .exam(exam)
                .test(test)
                .build();

        disciplineService.updateDiscipline(discipline);

        disciplineGroupService.removeAllGroupsFromDiscipline(id);
        disciplineTeacherService.removeAllTeachersFromDiscipline(id);

        for (String gId : groupIds) {
            try {
                int groupId = Integer.parseInt(gId);
                disciplineGroupService.assignDisciplineToGroup(discipline.getId(), groupId);
            } catch (NumberFormatException ignored) {}
        }

        for (String tId : teacherIds) {
            try {
                int teacherId = Integer.parseInt(tId);
                disciplineTeacherService.assignTeacherToDiscipline(discipline.getId(), teacherId);
            } catch (NumberFormatException ignored) {}
        }

        session.setAttribute("successMessage",
                "Дисциплина " + name + " успешно обновлена!");

        response.sendRedirect(request.getContextPath() + "/app?command=showDisciplines");
    }

    private int parseOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
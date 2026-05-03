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

public class AddDisciplineCommand implements Command {

    private final DisciplineService disciplineService = ServiceFactory.getInstance().getDisciplineService();
    private final DisciplineGroupService disciplineGroupService = ServiceFactory.getInstance().getDisciplineGroupService();
    private final DisciplineTeacherService disciplineTeacherService = ServiceFactory.getInstance().getDisciplineTeacherService();
    private final GroupService groupService = ServiceFactory.getInstance().getGroupService();
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(req.getMethod())) {

            List<Group> allGroups = groupService.getAllGroups();
            List<User> teachers = userService.getAllTeachers();

            req.setAttribute("groups", allGroups);
            req.setAttribute("teachers", teachers);

            req.getRequestDispatcher("/WEB-INF/jsp/add_discipline.jsp").forward(req, resp);
            return;
        }

        //POST
        try {
            req.setCharacterEncoding("UTF-8");
            HttpSession session = req.getSession();

            String name = req.getParameter("name");
            String lectureStr = req.getParameter("lectureHours");
            String practicalStr = req.getParameter("practicalHours");
            String labStr = req.getParameter("labHours");
            String examStr = req.getParameter("exam");
            String testStr = req.getParameter("test");

            String[] groupIds = req.getParameterValues("groupIds");
            String[] teacherIds = req.getParameterValues("teacherIds");

            String error = null;
            if (name == null || name.isBlank()) {
                error = "Название дисциплины обязательно";
            }
            if ((groupIds == null || groupIds.length == 0) || (teacherIds == null || teacherIds.length == 0)) {
                error = "Необходимо выбрать хотя бы одну группу и одного преподавателя";
            }

            int lecture = parseOrDefault(lectureStr, 0);
            int practical = parseOrDefault(practicalStr, 0);
            int lab = parseOrDefault(labStr, 0);
            boolean exam = "on".equalsIgnoreCase(examStr);
            boolean test = "on".equalsIgnoreCase(testStr);

            if (error != null) {
                req.setAttribute("error", error);
                req.getRequestDispatcher("/WEB-INF/jsp/add_discipline.jsp").forward(req, resp);
                return;
            }

            Discipline discipline = Discipline.builder()
                    .name(name)
                    .lectureHours(lecture)
                    .practicalHours(practical)
                    .labHours(lab)
                    .exam(exam)
                    .test(test)
                    .build();
            disciplineService.createDiscipline(discipline);

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

            session.setAttribute("successMessage", "Дисциплина \"" + name + "\" успешно добавлена!");
            resp.sendRedirect(req.getContextPath() + "/app?command=showDisciplines");
        } catch (Exception e) {
            req.setAttribute("error", "Ошибка при создании дисциплины");

            req.getRequestDispatcher("/WEB-INF/jsp/add_discipline.jsp")
                    .forward(req, resp);
        }
    }

    private int parseOrDefault(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
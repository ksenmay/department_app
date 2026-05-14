package by.may.department.service;

import by.may.department.dao.GroupDAO;
import by.may.department.dao.UserDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.*;
import by.may.department.model.enums.Role;
import by.may.department.model.interfaces.IUserInfo;

import java.util.*;

public class TimetableService {

    private final DisciplineService disciplineService;
    private final DisciplineGroupService dgService;
    private final DisciplineTeacherService dtService;
    private final GroupDAO groupDAO;
    private final UserDAO userDAO;

    public TimetableService(DisciplineGroupService dgService,
                            DisciplineService disciplineService,
                            DisciplineTeacherService dtService) {

        this.dgService = dgService;
        this.dtService = dtService;
        this.disciplineService = disciplineService;
        DAOFactory daoFactory = new DAOFactory();
        this.groupDAO = daoFactory.getGroupDAO();
        this.userDAO = daoFactory.getUserDAO();
    }

    public List<Map<String, Object>> getScheduleForUser(User user) {
        if (user == null) return Collections.emptyList();

        IUserInfo userInfo = user.getUserInfo();
        if (userInfo == null) return Collections.emptyList();

        List<Map<String, Object>> fullSchedule = buildFullSchedule();

        if (Objects.requireNonNull(userInfo.getRole()) == Role.STUDENT) {

            return fullSchedule.stream()
                    .filter(r ->
                            ((Group) r.get("group")).getGroupId() == userInfo.getGroupId()
                    )
                    .toList();

        } else if (userInfo.getRole() == Role.TEACHER) {

            return fullSchedule.stream()
                    .filter(r ->
                            ((IUserInfo) r.get("teacher")).getUserId() == user.getId()
                    )
                    .toList();

        } else if (userInfo.getRole() == Role.ADMIN) {
            return fullSchedule;
        }

        return Collections.emptyList();
    }

    public List<Map<String, Object>> buildFullSchedule() {

        List<Discipline> disciplines = disciplineService.getAllDisciplines();
        List<DisciplineGroup> dgList = dgService.getAll();
        List<DisciplineTeacher> dtList = dtService.getAll();

        List<Map<String, Object>> schedule = new ArrayList<>();

        for (Discipline d : disciplines) {

            List<Group> groups = new ArrayList<>();

            for (DisciplineGroup dg : dgList) {
                if (dg.getDisciplineId() == d.getId()) {

                    Group group = groupDAO.findById(dg.getGroupId());
                    if (group != null) {
                        groups.add(group);
                    }
                }
            }

            for (DisciplineTeacher dt : dtList) {
                if (dt.getDisciplineId() == d.getId()) {

                    // ✔ Lazy Load через Proxy (User → UserInfo)
                    User teacherUser = userDAO.findById(dt.getTeacherId());

                    IUserInfo teacher = teacherUser.getUserInfo();

                    for (Group group : groups) {

                        schedule.add(Map.of(
                                "discipline", d,
                                "teacher", teacher,
                                "group", group
                        ));
                    }
                }
            }
        }

        return schedule;
    }

    public Map<String, Integer> calculateHours() {

        List<Discipline> disciplines = disciplineService.getAllDisciplines();

        int lecture = disciplines.stream().mapToInt(Discipline::getLectureHours).sum();
        int practical = disciplines.stream().mapToInt(Discipline::getPracticalHours).sum();
        int lab = disciplines.stream().mapToInt(Discipline::getLabHours).sum();
        int total = lecture + practical + lab;

        return Map.of(
                "lectureHours", lecture,
                "practicalHours", practical,
                "labHours", lab,
                "totalHours", total
        );
    }

    public boolean canDelete(User user, int teacherId) {

        if (user == null) return false;

        IUserInfo userInfo = user.getUserInfo();
        if (userInfo == null) return false;

        Role role = userInfo.getRole();

        return role == Role.ADMIN ||
                (role == Role.TEACHER && user.getId() == teacherId);
    }
}
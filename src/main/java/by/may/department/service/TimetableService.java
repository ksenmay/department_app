package by.may.department.service;

import by.may.department.dao.GroupDAO;
import by.may.department.dao.UserInfoDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.*;
import by.may.department.model.enums.Role;

import java.util.*;

public class TimetableService {

    private final DisciplineService disciplineService;
    private final DisciplineGroupService dgService;
    private final DisciplineTeacherService dtService;
    private final UserInfoDAO userInfoDAO;
    private final GroupDAO groupDAO;

    public TimetableService(DisciplineGroupService dgService,
                             DisciplineService disciplineService,
                             DisciplineTeacherService dtService) {
        this.dgService = dgService;
        this.dtService = dtService;
        this.disciplineService = disciplineService;
        this.userInfoDAO = DAOFactory.getInstance().getUserInfoDAO();
        this.groupDAO = DAOFactory.getInstance().getGroupDAO();
    }

    public List<Map<String, Object>> getScheduleForUser(User user) {
        if (user == null) return Collections.emptyList();
        UserInfo userInfo = user.getUserInfo();
        if (userInfo == null) return Collections.emptyList();

        List<Map<String, Object>> fullSchedule = buildFullSchedule();

        if (Objects.requireNonNull(userInfo.getRole()) == Role.STUDENT) {
            return fullSchedule.stream()
                    .filter(r -> ((Group) r.get("group")).getGroupId() == userInfo.getGroupId())
                    .toList();
        } else if (userInfo.getRole() == Role.TEACHER) {
            return fullSchedule.stream()
                    .filter(r -> ((UserInfo) r.get("teacher")).getUserId() == user.getId())
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

            List<UserInfo> teachers = new ArrayList<>();
            for (DisciplineTeacher dt : dtList) {
                if (dt.getDisciplineId() == d.getId()) {
                    UserInfo teacher = userInfoDAO.findByUserId(dt.getTeacherId());
                    if (teacher != null) {
                        teachers.add(teacher);
                    }
                }
            }

            for (UserInfo teacher : teachers) {
                for (Group group : groups) {
                    schedule.add(Map.of(
                            "discipline", d,
                            "teacher", teacher,
                            "group", group
                    ));
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

        UserInfo userInfo = userInfoDAO.findByUserId(user.getId());
        if (userInfo == null) return false;

        Role role = userInfo.getRole();

        return role == Role.ADMIN || (role == Role.TEACHER && user.getId() == teacherId);
    }
}
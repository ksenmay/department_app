package by.may.department.factory;

import by.may.department.service.*;
import lombok.Getter;

@Getter
public class ServiceFactory {

    private static final ServiceFactory INSTANCE = new ServiceFactory();

    private final AuthService authService = new AuthService();
    private final DisciplineGroupService disciplineGroupService = new DisciplineGroupService();
    private final GroupService groupService = new GroupService();
    private final DisciplineTeacherService disciplineTeacherService = new DisciplineTeacherService();
    private final DisciplineService disciplineService = new DisciplineService(disciplineGroupService, disciplineTeacherService);

    private TimetableService timetableService;
    private final UserService userService = new UserService();

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public TimetableService getTimetableService() {
        if (timetableService == null) {
            timetableService = new TimetableService(disciplineGroupService, disciplineService, disciplineTeacherService);
        }
        return timetableService;
    }
}

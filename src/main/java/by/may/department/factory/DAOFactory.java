package by.may.department.factory;

import by.may.department.dao.*;
import lombok.Getter;

@Getter
public class DAOFactory {

    //private static final DAOFactory INSTANCE = new DAOFactory();

    private final DisciplineDAO disciplineDAO = new DisciplineDAO();
    private final DisciplineGroupDAO disciplineGroupDAO = new DisciplineGroupDAO();
    private final DisciplineTeacherDAO disciplineTeacherDAO = new DisciplineTeacherDAO();
    private final GroupDAO groupDAO = new GroupDAO();
    private final UserInfoDAO userInfoDAO = new UserInfoDAO();
    private final UserDAO userDAO = new UserDAO();

    public DAOFactory() {}

//    public static DAOFactory getInstance() {
//        return INSTANCE;
//    }
}

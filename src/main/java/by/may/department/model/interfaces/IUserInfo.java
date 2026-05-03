package by.may.department.model.interfaces;

import by.may.department.model.enums.Role;

public interface IUserInfo {

    int getUserId();
    void setUserId(int userId);

    Role getRole();
    void setRole(Role role);

    String getName();
    void setName(String name);

    String getPatronymic();
    void setPatronymic(String patronymic);

    String getSurname();
    void setSurname(String surname);

    int getGroupId();
    void setGroupId(int groupId);

}

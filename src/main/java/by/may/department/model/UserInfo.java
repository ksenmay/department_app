package by.may.department.model;

import by.may.department.model.enums.Role;
import by.may.department.model.interfaces.IUserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfo implements IUserInfo {

    private int userId;
    private Role role;
    private String name;
    private String patronymic;
    private String surname;
    private int groupId;

}


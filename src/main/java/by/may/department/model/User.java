package by.may.department.model;

import by.may.department.model.interfaces.IUserInfo;
import lombok.*;
@Getter
@Setter
@Builder
public class User {

    private int id;
    private String username;
    private String password;
    private IUserInfo userInfo;

}

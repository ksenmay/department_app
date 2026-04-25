package by.may.department.model;

import lombok.*;
@Getter
@Setter
@Builder
public class User {

    private int id;
    private String username;
    private String password;
    private UserInfo userInfo;

}

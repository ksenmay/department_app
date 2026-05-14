package by.may.department.model.proxy;

import by.may.department.dao.UserInfoDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.UserInfo;
import by.may.department.model.enums.Role;
import by.may.department.model.interfaces.IUserInfo;

public class UserInfoProxy implements IUserInfo {

    private int userId;

    private UserInfo realUserInfo;
    private UserInfo userInfo;
    private final UserInfoDAO userInfoDAO;


    public UserInfoProxy(int userId, UserInfoDAO userInfoDAO) {
        this.userId = userId;
        this.userInfoDAO = userInfoDAO;
    }

    private UserInfo getReal() {
        if (realUserInfo == null) {
            realUserInfo = userInfoDAO.findByUserId(userId);
        }
        return realUserInfo;
    }


    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public Role getRole() {
        return getReal().getRole();
    }

    @Override
    public void setRole(Role role) {
        getReal().setRole(role);
    }

    @Override
    public String getName() {
        return getReal().getName();
    }

    @Override
    public void setName(String name) {
        getReal().setName(name);
    }

    @Override
    public String getPatronymic() {
        return getReal().getPatronymic();
    }

    @Override
    public void setPatronymic(String patronymic) {
        getReal().setPatronymic(patronymic);
    }

    @Override
    public String getSurname() {
        return getReal().getSurname();
    }

    @Override
    public void setSurname(String surname) {
        getReal().setSurname(surname);
    }

    @Override
    public int getGroupId() {
        return getReal().getGroupId();
    }

    @Override
    public void setGroupId(int groupId) {
        getReal().setGroupId(groupId);
    }
}
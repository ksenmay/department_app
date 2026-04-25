package by.may.department.service;

import by.may.department.dao.UserDAO;
import by.may.department.dao.UserInfoDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.User;
import by.may.department.model.UserInfo;
import by.may.department.model.enums.Role;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;

    public UserService() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
        this.userInfoDAO = DAOFactory.getInstance().getUserInfoDAO();
    }

    public void createUser(User user, UserInfo userInfo) {

        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        try {
            userDAO.save(user);
            userInfo.setUserId(user.getId());
            userInfoDAO.save(userInfo);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании пользователя", e);
        }
    }

    public User getUserById(int id) {
        User user = userDAO.findById(id);
        if (user != null) {
            user.setUserInfo(userInfoDAO.findByUserId(id));
        }
        return user;
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = userDAO.findAll();
        for (User user : users) {
            user.setUserInfo(userInfoDAO.findByUserId(user.getId()));
        }
        return users;
    }

    public List<User> getAllTeachers() {
        List<User> allUsers = getAllUsers();
        List<User> teachers = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getUserInfo() != null && user.getUserInfo().getRole() == Role.TEACHER) {
                teachers.add(user);
            }
        }
        return teachers;
    }


    public void updateUser(User user) {
        try {
            userDAO.update(user);
            userInfoDAO.update(user.getUserInfo());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    public void deleteUser(int userId) {
        try {
            userInfoDAO.delete(userId);
            userDAO.delete(userId);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }
}
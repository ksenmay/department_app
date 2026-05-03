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
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public List<User> getAllTeachers() {
        List<User> users = userDAO.findAll();
        List<User> teachers = new ArrayList<>();

        for (User user : users) {
            if (user.getUserInfo().getRole() == Role.TEACHER) {
                teachers.add(user);
            }
        }

        return teachers;
    }

    public void updateUser(User user) {
        try {
            userDAO.update(user);

            if (user.getUserInfo() instanceof UserInfo) {
                userInfoDAO.update((UserInfo) user.getUserInfo());
            }

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
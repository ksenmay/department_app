package by.may.department.service;

import by.may.department.dao.UserDAO;
import by.may.department.dao.UserInfoDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.User;

public class AuthService {

    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;

    public AuthService() {
        this.userDAO = DAOFactory.getInstance().getUserDAO();
        this.userInfoDAO = DAOFactory.getInstance().getUserInfoDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;
        if (!user.getPassword().equals(password)) return null;
        user.setUserInfo(userInfoDAO.findByUserId(user.getId()));

        return user;
    }
}
package by.may.department.service;

import by.may.department.dao.UserDAO;
import by.may.department.dao.UserInfoDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.User;

public class AuthService {

    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;

    public AuthService() {
        DAOFactory factory = new DAOFactory();
        this.userDAO = factory.getUserDAO();
        this.userInfoDAO = factory.getUserInfoDAO();
    }

    public User login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null) return null;

        if (!user.getPassword().equals(password)) return null;

        return user;
    }
}
package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.User;
import by.may.department.model.proxy.UserInfoProxy;

import java.sql.*;

public class UserDAO extends AbstractDAO<User, Integer> {

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO users (username, password) VALUES (?, ?)";
    }

    @Override
    protected void fillInsertStatement(PreparedStatement ps, User entity) throws SQLException {
        ps.setString(1, entity.getUsername());
        ps.setString(2, entity.getPassword());
    }

    @Override
    protected void setGeneratedId(User entity, ResultSet rs) throws SQLException {
        entity.setId(rs.getInt(1));
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE users SET username=?, password=? WHERE id=?";
    }

    @Override
    protected void setUpdateStatement(PreparedStatement ps, User entity) throws SQLException {
        ps.setString(1, entity.getUsername());
        ps.setString(2, entity.getPassword());
        ps.setInt(3, entity.getId());
    }

    @Override
    protected String getSelectByIdSQL() {
        return "SELECT id, username, password FROM users WHERE id=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT id, username, password FROM users";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM users WHERE id=?";
    }

    @Override
    protected User mapRow(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .userInfo(new UserInfoProxy(rs.getInt("id")))
                .build();
    }

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password FROM users WHERE username=?";
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить пользователя по username", e);
        }
        return null;
    }
}
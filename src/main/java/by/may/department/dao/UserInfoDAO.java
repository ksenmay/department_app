package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.UserInfo;
import by.may.department.model.enums.Role;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserInfoDAO extends AbstractDAO<UserInfo, Integer> {

    private final Map<Integer, UserInfo> identityMap = new HashMap<>();

    @Override
    public UserInfo findById(Integer id) {
        if (identityMap.containsKey(id)) {
            return identityMap.get(id);
        }

        UserInfo userInfo = super.findById(id);

        if (userInfo != null) {
            identityMap.put(id, userInfo);
        }

        return userInfo;
    }

    public UserInfo findByUserId(int userId) {
        if (identityMap.containsKey(userId)) {
            return identityMap.get(userId);
        }

        String sql = "SELECT * FROM user_info WHERE user_id=?";
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserInfo userInfo = mapRow(rs);

                    identityMap.put(userId, userInfo);
                    return userInfo;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить UserInfo", e);
        }

        return null;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO user_info (user_id, role, name, patronymic, surname, group_id) VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void fillInsertStatement(PreparedStatement ps, UserInfo entity) throws SQLException {
        ps.setInt(1, entity.getUserId());
        ps.setString(2, entity.getRole().name());
        ps.setString(3, entity.getName());
        ps.setString(4, entity.getPatronymic());
        ps.setString(5, entity.getSurname());
        ps.setInt(6, entity.getGroupId());
    }

    @Override
    protected void setGeneratedId(UserInfo entity, ResultSet rs) throws SQLException {}

    @Override
    protected String getUpdateSQL() {
        return "UPDATE user_info SET role=?, name=?, patronymic=?, surname=?, group_id=? WHERE user_id=?";
    }

    @Override
    protected void setUpdateStatement(PreparedStatement ps, UserInfo entity) throws SQLException {
        ps.setString(1, entity.getRole().name());
        ps.setString(2, entity.getName());
        ps.setString(3, entity.getPatronymic());
        ps.setString(4, entity.getSurname());
        ps.setInt(5, entity.getGroupId());
        ps.setInt(6, entity.getUserId());
    }

    @Override
    protected String getSelectByIdSQL() {
        return "SELECT * FROM user_info WHERE user_id=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM user_info";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM user_info WHERE user_id=?";
    }

    @Override
    protected UserInfo mapRow(ResultSet rs) throws SQLException {
        String roleStr = rs.getString("role");
        Role role = null;
        if (roleStr != null && !roleStr.isBlank()) {
            role = Role.valueOf(roleStr.trim().toUpperCase());
        }

        return UserInfo.builder()
                .userId(rs.getInt("user_id"))
                .role(role)
                .name(rs.getString("name"))
                .patronymic(rs.getString("patronymic"))
                .surname(rs.getString("surname"))
                .groupId(rs.getInt("group_id"))
                .build();
    }

}
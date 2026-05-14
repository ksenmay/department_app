package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GroupDAO extends AbstractDAO<Group, Integer> {

    private final Map<Integer, Group> identityMap = new HashMap<>();

    @Override
    public Group findById(Integer id) {
        if (identityMap.containsKey(id)) {
            return identityMap.get(id);
        }

        Group group = super.findById(id);

        if (group != null) {
            identityMap.put(id, group);
        }

        return group;
    }


    @Override
    protected String getInsertQuery() {
        return "INSERT INTO student_groups (group_number, quantity_of_students) VALUES (?, ?)";
    }

    @Override
    protected void fillInsertStatement(PreparedStatement ps, Group entity) throws SQLException {
        ps.setInt(1, entity.getGroupNumber());
        ps.setInt(2, entity.getQuantityOfStudent());
    }

    @Override
    protected void setGeneratedId(Group entity, ResultSet rs) throws SQLException {
        if (rs.next()) {
            entity.setGroupId(rs.getInt(1));
        }
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE student_groups SET group_number = ?, quantity_of_students = ? WHERE id = ?";
    }

    @Override
    protected void setUpdateStatement(PreparedStatement ps, Group entity) throws SQLException {
        ps.setInt(1, entity.getGroupNumber());
        ps.setInt(2, entity.getQuantityOfStudent());
        ps.setInt(3, entity.getGroupId());
    }

    @Override
    protected String getSelectByIdSQL() {
        return "SELECT id, group_number, quantity_of_students FROM student_groups WHERE id = ?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT id, group_number, quantity_of_students FROM student_groups";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM student_groups WHERE id = ?";
    }

//    public Group findById(int id) {
//        String sql = "SELECT id, group_number, quantity_of_students FROM student_groups WHERE id = ?";
//        try (Connection c = ConnectionPool.getInstance().getConnection();
//             PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    return mapRow(rs);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Не удалось получить группу по id", e);
//        }
//        return null;
//    }

    @Override
    protected Group mapRow(ResultSet rs) throws SQLException {
        return Group.builder()
                .groupId(rs.getInt("id"))
                .groupNumber(rs.getInt("group_number"))
                .quantityOfStudent(rs.getInt("quantity_of_students"))
                .build();
    }
}
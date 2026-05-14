package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.DisciplineGroup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisciplineGroupDAO {

    private final Map<String, DisciplineGroup> identityMap = new HashMap<>();

    private String makeKey(int disciplineId, int groupId) {
        return disciplineId + "_" + groupId;
    }

    public DisciplineGroup save(DisciplineGroup entity) {
        String sql = "INSERT INTO discipline_vs_groups (discipline_id, group_id) VALUES (?, ?)";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getDisciplineId());
            ps.setInt(2, entity.getGroupId());
            ps.executeUpdate();

            String key = makeKey(entity.getDisciplineId(), entity.getGroupId());
            identityMap.put(key, entity);

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить связь дисциплина-группа", e);
        }
        return entity;
    }

    public boolean delete(int disciplineId, int groupId) {
        String sql = "DELETE FROM discipline_vs_groups WHERE discipline_id = ? AND group_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, groupId);
            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                identityMap.remove(makeKey(disciplineId, groupId));
            }
            return deleted;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить связь дисциплина-группа", e);
        }
    }

    public boolean deleteAll(int disciplineId) {
        String sql = "DELETE FROM discipline_vs_groups WHERE discipline_id = ?";

        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.executeUpdate();

            identityMap.entrySet().removeIf(entry -> entry.getKey().startsWith(disciplineId + "_"));

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить связи дисциплины с группами", e);
        }
    }

    public DisciplineGroup find(int disciplineId, int groupId) {
        String key = makeKey(disciplineId, groupId);

        if (identityMap.containsKey(key)) {
            return identityMap.get(key);
        }

        String sql = "SELECT discipline_id, group_id FROM discipline_vs_groups WHERE discipline_id = ? AND group_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, groupId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DisciplineGroup entity = mapRow(rs);
                    identityMap.put(key, entity);
                    return entity;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить связь дисциплина-группа", e);
        }
        return null;
    }

    public List<DisciplineGroup> findAll() {
        List<DisciplineGroup> list = new ArrayList<>();
        String sql = "SELECT discipline_id, group_id FROM discipline_vs_groups";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int dId = rs.getInt("discipline_id");
                int gId = rs.getInt("group_id");

                list.add(find(dId, gId));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить все связи дисциплина-группа", e);
        }
        return list;
    }

    public List<DisciplineGroup> findByDisciplineId(int disciplineId) {
        List<DisciplineGroup> list = new ArrayList<>();
        String sql = "SELECT discipline_id, group_id FROM discipline_vs_groups WHERE discipline_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gId = rs.getInt("group_id");
                    list.add(find(disciplineId, gId));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить группы по дисциплине", e);
        }
        return list;
    }

    public List<DisciplineGroup> findByGroupId(int groupId) {
        List<DisciplineGroup> list = new ArrayList<>();
        String sql = "SELECT discipline_id, group_id FROM discipline_vs_groups WHERE group_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int dId = rs.getInt("discipline_id");
                    list.add(find(dId, groupId));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить дисциплины по группе", e);
        }
        return list;
    }

    private DisciplineGroup mapRow(ResultSet rs) throws SQLException {
        return DisciplineGroup.builder()
                .disciplineId(rs.getInt("discipline_id"))
                .groupId(rs.getInt("group_id"))
                .build();
    }
}
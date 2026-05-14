package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.DisciplineTeacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisciplineTeacherDAO {

    private final Map<String, DisciplineTeacher> identityMap = new HashMap<>();

    public DisciplineTeacher save(DisciplineTeacher entity) {
        String sql = "INSERT INTO disciplines_vs_teachers (discipline_id, teacher_id) VALUES (?, ?)";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getDisciplineId());
            ps.setInt(2, entity.getTeacherId());
            ps.executeUpdate();

            String key = generateKey(entity.getDisciplineId(), entity.getTeacherId());
            identityMap.put(key, entity);

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить связь дисциплина-преподаватель", e);
        }
        return entity;
    }

    public boolean deleteAll(int disciplineId) {
        String sql = "DELETE FROM disciplines_vs_teachers WHERE discipline_id = ?";

        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.executeUpdate();

            List<String> keysToRemove = new ArrayList<>();
            for (String key : identityMap.keySet()) {
                if (key.startsWith(disciplineId + "_")) {
                    keysToRemove.add(key);
                }
            }
            keysToRemove.forEach(identityMap::remove);

            return true;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить связи дисциплины с преподавателями", e);
        }
    }

    public boolean delete(int disciplineId, int teacherId) {
        String sql = "DELETE FROM disciplines_vs_teachers WHERE discipline_id = ? AND teacher_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, teacherId);
            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                identityMap.remove(generateKey(disciplineId, teacherId));
            }
            return deleted;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить связь дисциплина-преподаватель", e);
        }
    }

    public DisciplineTeacher find(int disciplineId, int teacherId) {
        String key = generateKey(disciplineId, teacherId);

        if (identityMap.containsKey(key)) {
            return identityMap.get(key);
        }

        String sql = "SELECT discipline_id, teacher_id FROM disciplines_vs_teachers WHERE discipline_id = ? AND teacher_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, teacherId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DisciplineTeacher entity = mapRow(rs);
                    identityMap.put(key, entity);
                    return entity;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить связь дисциплина-преподаватель", e);
        }
        return null;
    }

    public List<DisciplineTeacher> findAll() {
        List<DisciplineTeacher> list = new ArrayList<>();
        String sql = "SELECT discipline_id, teacher_id FROM disciplines_vs_teachers";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(getOrCacheFromRs(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить все связи дисциплина-преподаватель", e);
        }
        return list;
    }

    public List<DisciplineTeacher> findByDisciplineId(int disciplineId) {
        List<DisciplineTeacher> list = new ArrayList<>();
        String sql = "SELECT discipline_id, teacher_id FROM disciplines_vs_teachers WHERE discipline_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getOrCacheFromRs(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить преподавателей по дисциплине", e);
        }
        return list;
    }

    public List<DisciplineTeacher> findByTeacherId(int teacherId) {
        List<DisciplineTeacher> list = new ArrayList<>();
        String sql = "SELECT discipline_id, teacher_id FROM disciplines_vs_teachers WHERE teacher_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(getOrCacheFromRs(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить дисциплины по преподавателю", e);
        }
        return list;
    }

    private DisciplineTeacher getOrCacheFromRs(ResultSet rs) throws SQLException {
        int dId = rs.getInt("discipline_id");
        int tId = rs.getInt("teacher_id");
        String key = generateKey(dId, tId);

        if (identityMap.containsKey(key)) {
            return identityMap.get(key);
        }

        DisciplineTeacher entity = mapRow(rs);
        identityMap.put(key, entity);
        return entity;
    }

    private DisciplineTeacher mapRow(ResultSet rs) throws SQLException {
        return DisciplineTeacher.builder()
                .disciplineId(rs.getInt("discipline_id"))
                .teacherId(rs.getInt("teacher_id"))
                .build();
    }

    private String generateKey(int disciplineId, int teacherId) {
        return disciplineId + "_" + teacherId;
    }
}
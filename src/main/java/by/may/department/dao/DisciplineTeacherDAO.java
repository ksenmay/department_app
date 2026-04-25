package by.may.department.dao;

import by.may.department.connection.ConnectionPool;
import by.may.department.model.DisciplineTeacher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisciplineTeacherDAO {

    public DisciplineTeacher save(DisciplineTeacher entity) {
        String sql = "INSERT INTO disciplines_vs_teachers (discipline_id, teacher_id) VALUES (?, ?)";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entity.getDisciplineId());
            ps.setInt(2, entity.getTeacherId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить связь дисциплина-преподаватель", e);
        }
        return entity;
    }

    public boolean delete(int disciplineId, int teacherId) {
        String sql = "DELETE FROM disciplines_vs_teachers WHERE discipline_id = ? AND teacher_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, teacherId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить связь дисциплина-преподаватель", e);
        }
    }

    public DisciplineTeacher find(int disciplineId, int teacherId) {
        String sql = "SELECT discipline_id, teacher_id FROM disciplines_vs_teachers WHERE discipline_id = ? AND teacher_id = ?";
        try (var conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, disciplineId);
            ps.setInt(2, teacherId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
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
                list.add(mapRow(rs));
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
                    list.add(mapRow(rs));
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
                    list.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить дисциплины по преподавателю", e);
        }
        return list;
    }

    private DisciplineTeacher mapRow(ResultSet rs) throws SQLException {
        return DisciplineTeacher.builder()
                .disciplineId(rs.getInt("discipline_id"))
                .teacherId(rs.getInt("teacher_id"))
                .build();
    }
}
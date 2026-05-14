package by.may.department.dao;

import by.may.department.model.Discipline;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DisciplineDAO extends AbstractDAO<Discipline, Integer> {

    private final Map<Integer, Discipline> identityMap = new HashMap<>();

    @Override
    public Discipline findById(Integer id) {
        if (identityMap.containsKey(id)) {
            return identityMap.get(id);
        }

        Discipline discipline = super.findById(id);

        if (discipline != null) {
            identityMap.put(id, discipline);
        }

        return discipline;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO disciplines " +
                "(discipline_name, quantity_of_lecture, quantity_of_practical_training, quantity_of_laboratory_work, exam, test) "+
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void fillInsertStatement(PreparedStatement ps, Discipline discipline) throws SQLException {
        ps.setString(1, discipline.getName());
        ps.setInt(2, discipline.getLectureHours());
        ps.setInt(3, discipline.getPracticalHours());
        ps.setInt(4, discipline.getLabHours());
        ps.setBoolean(5, discipline.isExam());
        ps.setBoolean(6, discipline.isTest());
    }

    @Override
    protected void setGeneratedId(Discipline discipline, ResultSet rs) throws SQLException {
        discipline.setId(rs.getInt(1));
    }

    @Override
    protected String getUpdateSQL() {
        return "UPDATE disciplines SET discipline_name=?, quantity_of_lecture=?, quantity_of_practical_training=?, " +
                "quantity_of_laboratory_work=?, exam=?, test=? WHERE id=?";
    }

    @Override
    protected void setUpdateStatement(PreparedStatement ps, Discipline entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getLectureHours());
        ps.setInt(3, entity.getPracticalHours());
        ps.setInt(4, entity.getLabHours());
        ps.setBoolean(5, entity.isExam());
        ps.setBoolean(6, entity.isTest());
        ps.setInt(7, entity.getId());
    }

    @Override
    protected String getSelectByIdSQL() {
        return "SELECT * FROM disciplines WHERE id=?";
    }

    @Override
    protected String getSelectAllSQL() {
        return "SELECT * FROM disciplines";
    }

    @Override
    protected String getDeleteSQL() {
        return "DELETE FROM disciplines WHERE id=?";
    }

    @Override
    protected Discipline mapRow(ResultSet rs) throws SQLException {
        return Discipline.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("discipline_name"))
                .lectureHours(rs.getInt("quantity_of_lecture"))
                .practicalHours(rs.getInt("quantity_of_practical_training"))
                .labHours(rs.getInt("quantity_of_laboratory_work"))
                .exam(rs.getBoolean("exam"))
                .test(rs.getBoolean("test"))
                .build();
    }
}
package by.may.department.dao;

import by.may.department.connection.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T, ID> implements DAO<T, ID> {

    protected abstract String getInsertQuery();
    protected abstract void fillInsertStatement(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setGeneratedId(T entity, ResultSet rs) throws SQLException;

    protected abstract String getUpdateSQL();
    protected abstract void setUpdateStatement(PreparedStatement ps, T entity) throws SQLException;

    protected abstract String getSelectByIdSQL();
    protected abstract String getSelectAllSQL();
    protected abstract String getDeleteSQL();
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    @Override
    public T save(T entity) {
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(getInsertQuery(), PreparedStatement.RETURN_GENERATED_KEYS)) {

            fillInsertStatement(ps, entity);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    setGeneratedId(entity, rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось сохранить сущность", e);
        }
        return entity;
    }

    @Override
    public T findById(ID id) {
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(getSelectByIdSQL())) {

            ps.setObject(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить сущность по id", e);
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(getSelectAllSQL());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить список сущностей", e);
        }

        return list;
    }

    @Override
    public boolean update(T entity) {
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(getUpdateSQL())) {

            setUpdateStatement(ps, entity);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось обновить сущность", e);
        }
    }

    @Override
    public boolean delete(ID id) {
        try (Connection c = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(getDeleteSQL())) {

            ps.setObject(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось удалить сущность", e);
        }
    }
}
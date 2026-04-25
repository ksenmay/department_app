package by.may.department.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {

    T save(T entity);
    T findById(ID id);
    List<T> findAll();
    boolean update(T entity);
    boolean delete(ID id);
}

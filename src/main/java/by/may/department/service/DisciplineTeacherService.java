package by.may.department.service;

import by.may.department.dao.DisciplineTeacherDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.DisciplineTeacher;

import java.util.List;

public class DisciplineTeacherService {

    private final DisciplineTeacherDAO dao;

    public DisciplineTeacherService() {
        this.dao = DAOFactory.getInstance().getDisciplineTeacherDAO();
    }

    public DisciplineTeacher assignTeacherToDiscipline(int disciplineId, int teacherId) {
        DisciplineTeacher dt = DisciplineTeacher.builder()
                .disciplineId(disciplineId)
                .teacherId(teacherId)
                .build();
        return dao.save(dt);
    }

    public boolean removeTeacherFromDiscipline(int disciplineId, int teacherId) {
        return dao.delete(disciplineId, teacherId);
    }

    public List<DisciplineTeacher> getTeachersByDiscipline(int disciplineId) {
        return dao.findByDisciplineId(disciplineId);
    }

    public List<DisciplineTeacher> getDisciplinesByTeacher(int teacherId) {
        return dao.findByTeacherId(teacherId);
    }

    public List<DisciplineTeacher> getAll() {
        return dao.findAll();
    }
}
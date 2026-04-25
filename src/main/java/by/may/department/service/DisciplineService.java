package by.may.department.service;

import by.may.department.dao.DisciplineDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.Discipline;
import by.may.department.model.DisciplineGroup;
import by.may.department.model.DisciplineTeacher;

import java.util.List;

public class DisciplineService {

    private final DisciplineDAO disciplineDAO;
    private final DisciplineGroupService disciplineGroupService;
    private final DisciplineTeacherService disciplineTeacherService;


    public DisciplineService(DisciplineGroupService disciplineGroupService, DisciplineTeacherService disciplineTeacherService) {
        this.disciplineDAO = DAOFactory.getInstance().getDisciplineDAO();
        this.disciplineGroupService = disciplineGroupService;
        this.disciplineTeacherService = disciplineTeacherService;
    }

    public List<Discipline> getAllDisciplines() {
        return disciplineDAO.findAll();
    }

    public Discipline getDisciplineById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID дисциплины должен быть больше 0");
        }
        return disciplineDAO.findById(id);
    }

    public Discipline createDiscipline(Discipline discipline) {
        if (discipline == null) {
            throw new IllegalArgumentException("Discipline не может быть null");
        }
        return disciplineDAO.save(discipline);
    }

    public boolean updateDiscipline(Discipline discipline) {
        if (discipline == null || discipline.getId() <= 0) {
            throw new IllegalArgumentException("Некорректная дисциплина для обновления");
        }
        return disciplineDAO.update(discipline);
    }

    public boolean deleteDiscipline(int disciplineId) {
        if (disciplineId <= 0) {
            throw new IllegalArgumentException("Некорректный ID дисциплины для удаления");
        }

        try {
            List<DisciplineGroup> groups = disciplineGroupService.getGroupsByDiscipline(disciplineId);
            for (DisciplineGroup dg : groups) {
                disciplineGroupService.removeDisciplineFromGroup(disciplineId, dg.getGroupId());
            }

            List<DisciplineTeacher> teachers = disciplineTeacherService.getTeachersByDiscipline(disciplineId);
            for (DisciplineTeacher dt : teachers) {
                disciplineTeacherService.removeTeacherFromDiscipline(disciplineId, dt.getTeacherId());
            }

            return disciplineDAO.delete(disciplineId);

        } catch (Exception e) {
            throw new RuntimeException("Не удалось безопасно удалить дисциплину", e);
        }
    }
    public int calculateTotalHours(Discipline discipline) {
        if (discipline == null) return 0;
        return discipline.getLectureHours() + discipline.getPracticalHours() + discipline.getLabHours();
    }
}
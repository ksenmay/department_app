package by.may.department.service;

import by.may.department.dao.DisciplineGroupDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.DisciplineGroup;

import java.util.List;

public class DisciplineGroupService {

    private final DisciplineGroupDAO dao;

    public DisciplineGroupService() {
        this.dao = DAOFactory.getInstance().getDisciplineGroupDAO();
    }

    public DisciplineGroup assignDisciplineToGroup(int disciplineId, int groupId) {
        DisciplineGroup dg = DisciplineGroup.builder()
                .disciplineId(disciplineId)
                .groupId(groupId)
                .build();
        return dao.save(dg);
    }

    public boolean removeDisciplineFromGroup(int disciplineId, int groupId) {
        return dao.delete(disciplineId, groupId);
    }

    public List<DisciplineGroup> getGroupsByDiscipline(int disciplineId) {
        return dao.findByDisciplineId(disciplineId);
    }

    public List<DisciplineGroup> getDisciplinesByGroup(int groupId) {
        return dao.findByGroupId(groupId);
    }

    public List<DisciplineGroup> getAll() {
        return dao.findAll();
    }
}
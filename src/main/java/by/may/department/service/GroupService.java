package by.may.department.service;

import by.may.department.dao.GroupDAO;
import by.may.department.factory.DAOFactory;
import by.may.department.model.Group;

import java.util.List;

public class GroupService {

    private final GroupDAO groupDAO;

    public GroupService() {
        DAOFactory daoFactory = new DAOFactory();
        this.groupDAO = daoFactory.getGroupDAO();
    }

    public List<Group> getAllGroups() {
        return groupDAO.findAll();
    }

    public Group getGroupById(int id) {
        if (id <= 0) throw new IllegalArgumentException("Некорректный ID группы");
        if (groupDAO.findById(id) == null) throw new IllegalArgumentException("Group не может быть null");
        return groupDAO.findById(id);
    }

    public Group createGroup(Group group) {
        if (group == null) throw new IllegalArgumentException("Group не может быть null");
        return groupDAO.save(group);
    }

    public boolean updateGroup(Group group) {
        if (group == null || group.getGroupId() <= 0)
            throw new IllegalArgumentException("Некорректная группа для обновления");
        return groupDAO.update(group);
    }

    public boolean deleteGroup(int id) {
        if (id <= 0) throw new IllegalArgumentException("Некорректный ID группы для удаления");
        return groupDAO.delete(id);
    }
}
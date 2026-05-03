package by.may.department.factory;

import by.may.department.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("addUser", new AddUserCommand());
        commands.put("addGroup", new AddGroupCommand());
        commands.put("addDiscipline", new AddDisciplineCommand());
        commands.put("updateDiscipline", new UpdateDisciplineCommand());
        commands.put("deleteDiscipline", new DeleteDisciplineCommand());
        commands.put("showDisciplines", new DisciplineCommand());
        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
    }

    public static Command getCommand(String name) {
        return commands.get(name);
    }

}

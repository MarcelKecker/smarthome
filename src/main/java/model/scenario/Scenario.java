package model.scenario;

import java.util.ArrayList;
import java.util.List;

public class Scenario {

    private String name;
    private String description;
    private List<Command> commands = new ArrayList<>();

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}

package model.scenario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Scenario {

    private String name;
    private String description;
    private ObservableList<DeviceCommand> commands;

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
        this.commands = FXCollections.observableArrayList();
    }

    public void addCommand(DeviceCommand command) {
        commands.add(command);
    }

    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }

    public ObservableList<DeviceCommand> getCommands() {
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

    public void setCommands(ObservableList<DeviceCommand> commands) {
        this.commands = commands;
    }
}

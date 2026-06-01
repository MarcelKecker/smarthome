package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.room.Raum;
import model.scenario.DeviceCommand;

public class DeviceCommandService {
    private ObservableList<DeviceCommand> deviceCommands = FXCollections.observableArrayList();

    public ObservableList<DeviceCommand> getDeviceCommands() {
        return deviceCommands;
    }

    public void addDeviceCommand(DeviceCommand deviceCommand) {
        deviceCommands.add(deviceCommand);
    }

    public void deleteDeviceCommand(DeviceCommand deviceCommand) {
        deviceCommands.remove(deviceCommand);
    }

    public int getDeviceCommandCount() {
        return deviceCommands.size();
    }
}


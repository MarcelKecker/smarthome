package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.device.Device;

public class DeviceService {
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    public ObservableList<Device> getDevices() {
        return devices;
    }

    public ObservableList<String> getDeviceNames() {
        ObservableList<String> deviceNames = FXCollections.observableArrayList();
        for (Device device : devices) {
            deviceNames.add(device.getName());
        }
        return deviceNames;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

}


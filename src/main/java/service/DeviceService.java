package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.device.Device;

public class DeviceService {
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    public ObservableList<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

}


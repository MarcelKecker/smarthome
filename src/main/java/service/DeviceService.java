package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.device.Device;

import java.util.List;

public class DeviceService {
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    public ObservableList<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public void setDevices(List<Device> devices) {
        this.clear();
        for (Device device : devices) {
            this.addDevice(device);
        }
    }

    public void clear() {
        this.devices.clear();
    }
}


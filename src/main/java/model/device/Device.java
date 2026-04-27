package model.device;

public interface Device {
    String getName();
    String getId();
    String getType();

    void executeAction(DeviceAction action);

    String getState();
}

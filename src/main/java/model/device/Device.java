package model.device;

import model.room.Raum;

public interface Device {
    String getName();
    String getId();
    String getType();

    void executeAction(DeviceAction action);

    String getState();

    Raum getRoom();
}

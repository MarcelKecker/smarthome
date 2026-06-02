package model.device;

import model.room.Raum;

public interface Device {
    String getName();
    String getId();
    String getType();

    void executeAction(DeviceAction action);

    String getState();
    void setState(String state);

    Raum getRoom();

    void setName(String name);

    void setRoom(Raum room);

    @Override
    public String toString();

}

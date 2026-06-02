package model.device;

import model.DeviceType;
import model.State;
import model.room.Raum;
import model.action.Command;

public interface Device {
    String getName();
    String getId();
    DeviceType getType();

    void executeAction(Command command);

    State getState();
    void setState(State state);

    Raum getRoom();

    void setName(String name);

    void setRoom(Raum room);

    @Override
    public String toString();

}

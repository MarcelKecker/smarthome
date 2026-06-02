package model.action;

import model.ActionType;
import model.device.Device;

public interface Command {
    void execute();
    Device getDevice();
    ActionType getActionType();
    String getID();
    @Override
    String toString();
    int getOrderIndex();
    void setOrderIndex(int i);
}

package model.action;

import model.ActionType;
import model.action.impl.RollDownShutterCommand;
import model.device.Device;
import com.fasterxml.jackson.annotation.*;

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

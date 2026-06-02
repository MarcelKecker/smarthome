package model.action;

import model.ActionType;
import model.device.Device;
import model.device.impl.Shutter;

import java.util.UUID;

public abstract class BaseCommand implements Command {
    private final String id;
    private final Device device;
    private final ActionType actionType;
    private int orderIndex;

    public BaseCommand (Device device, ActionType actionType,  int orderIndex) {
        this.device = device;
        this.actionType = actionType;
        this.orderIndex = orderIndex;
        this.id =  UUID.randomUUID().toString();
    }

    @Override
    public abstract void execute();

    @Override
    public Device getDevice() {
        return device;
    }

    @Override
    public ActionType getActionType(){
        return actionType;
    };

    @Override
    public String getID() {
        return id;
    }
    @Override
    public String toString() {
        String roomPart = device.getRoom() != null
                ? " in Raum " + device.getRoom().getName()
                : "";

        return device.getType() + " " + device.getName() + roomPart + " " + actionType;
    }
    @Override
    public int getOrderIndex() {
        return orderIndex;
    }
    @Override
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}

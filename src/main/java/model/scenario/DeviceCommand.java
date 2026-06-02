package model.scenario;

import model.device.Device;
import model.device.DeviceAction;

public class DeviceCommand  implements Command {

    private Device device;
    private DeviceAction action;
    private int orderIndex;

    public DeviceCommand(Device device, DeviceAction action,  int orderIndex) {
        this.device = device;
        this.action = action;
        this.orderIndex = orderIndex;
    }

    @Override
    public void execute() {
        device.executeAction(action);
    }
    @Override
    public String toString() {
        String output = device.getType() + " " + device.getName();
        if (device.getRoom() != null) {
            output += " in Raum " + device.getRoom();
        }
        output += " → " + action.getActionType();
        if (action.getValue() != null) {
            output += " " + action.getValue();
        }
        return output;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Device getDevice() {
        return device;
    }

    public DeviceAction getAction() {
        return action;
    }
}

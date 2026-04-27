package model.scenario;

import model.device.Device;
import model.device.DeviceAction;

public class DeviceCommand  implements Command {

    private Device device;
    private DeviceAction action;

    public DeviceCommand(Device device, DeviceAction action) {
        this.device = device;
        this.action = action;
    }

    @Override
    public void execute() {
        device.executeAction(action);
    }
}

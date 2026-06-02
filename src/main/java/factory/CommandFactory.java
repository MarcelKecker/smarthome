package factory;

import model.ActionType;
import model.DeviceType;
import model.action.impl.*;
import model.device.Device;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.action.Command;

public class CommandFactory {

    public static Command create(Device device, ActionType actionType, Object value, int orderIndex) {
        if (device.getType() == DeviceType.LAMP && actionType == ActionType.TURN_ON) {
            return new TurnOnLampCommand((Lamp)device, orderIndex);
        } else if (device.getType() == DeviceType.LAMP && actionType == ActionType.TURN_OFF) {
            return new TurnOFfLampCommand((Lamp)device, orderIndex);
        } else if (device.getType() == DeviceType.LAMP && actionType == ActionType.SET_BRIGHTNESS) {
            return new SetBrightnessLampCommand((Lamp)device, Integer.parseInt(value.toString()), orderIndex);
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.TURN_ON) {
            return new TurnOnHeatingCommand((Heating) device, orderIndex);
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.TURN_OFF) {
            return new TurnOffHeatingCommand((Heating) device, orderIndex);
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.SET_TEMPERATURE) {
            return new SetTemperatureHeatingCommand((Heating) device, Double.parseDouble(value.toString()), orderIndex);
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.ROLL_UP) {
            return new RollUpShutterCommand((Shutter) device, orderIndex);
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.ROLL_DOWN) {
            return new RollDownShutterCommand((Shutter) device, orderIndex);
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.SET_POSITION) {
            return new SetPositionShutterCommand((Shutter) device, Integer.parseInt(value.toString()), orderIndex);
        }
        throw new IllegalArgumentException("Invalid action type: " + actionType + " or illegal device type: "  + device.getType());
    }
}

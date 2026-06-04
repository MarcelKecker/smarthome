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
            return new TurnOnLampCommand(null, (Lamp)device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.LAMP && actionType == ActionType.TURN_OFF) {
            return new TurnOFfLampCommand(null, (Lamp)device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.LAMP && actionType == ActionType.SET_BRIGHTNESS) {
            return new SetBrightnessLampCommand(null, (Lamp)device, actionType, orderIndex, Integer.parseInt(value.toString()));
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.TURN_ON) {
            return new TurnOnHeatingCommand(null, (Heating) device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.TURN_OFF) {
            return new TurnOffHeatingCommand(null, (Heating) device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.HEATING && actionType == ActionType.SET_TEMPERATURE) {
            return new SetTemperatureHeatingCommand(null, (Heating) device, actionType, orderIndex, Double.parseDouble(value.toString()));
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.ROLL_UP) {
            return new RollUpShutterCommand(null, (Shutter) device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.ROLL_DOWN) {
            return new RollDownShutterCommand(null, (Shutter) device, actionType, orderIndex);
        } else if (device.getType() == DeviceType.SHUTTER && actionType == ActionType.SET_POSITION) {
            return new SetPositionShutterCommand(null, (Shutter) device, actionType, orderIndex, Integer.parseInt(value.toString()));
        }
        throw new IllegalArgumentException("Invalid action type: " + actionType + " or illegal device type: "  + device.getType());
    }
}
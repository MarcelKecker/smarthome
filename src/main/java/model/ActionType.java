package model;

import model.device.Device;

import java.util.ArrayList;
import java.util.List;

public enum ActionType {
    ROLL_UP,
    ROLL_DOWN,
    TURN_OFF,
    TURN_ON,
    SET_BRIGHTNESS,
    SET_POSITION,
    SET_TEMPERATURE;

    public static ActionType[] forDevice(Device device) {
        switch (device.getType()) {
            case LAMP:
                return new ActionType[]{TURN_ON, TURN_OFF, SET_BRIGHTNESS};
            case HEATING:
                return new ActionType[]{TURN_ON, TURN_OFF, SET_TEMPERATURE};
            case SHUTTER:
                return new ActionType[]{ROLL_DOWN, ROLL_UP, SET_POSITION};
        }
        return null;
    }
}

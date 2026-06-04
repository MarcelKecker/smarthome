package model;

import model.device.Device;

public enum ActionType {
    ROLL_UP("Hochfahren"),
    ROLL_DOWN("Herunterfahren"),
    TURN_OFF("Ausschalten"),
    TURN_ON("Einschalten"),
    SET_BRIGHTNESS("Helligkeit setzen"),
    SET_POSITION("Position setzen"),
    SET_TEMPERATURE("Temperatur setzen");

    private final String label;

    ActionType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static ActionType getValue(String label) {
        for (ActionType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ActionType label: " + label);
    }

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
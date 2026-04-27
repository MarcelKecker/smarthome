package model.device.impl;

import model.device.Device;
import model.device.DeviceAction;

public class Lamp implements Device {

    private String id;
    private String name;
    private boolean isOn;
    private int brightness;

    public Lamp(String id, String name) {
        this.id = id;
        this.name = name;
        this.isOn = false;
        this.brightness = 0;
    }

    @Override
    public void executeAction(DeviceAction action) {
        switch (action.getActionType()) {
            case "TURN_ON" -> isOn = true;
            case "TURN_OFF" -> isOn = false;
            case "SET_BRIGHTNESS" -> brightness = (int) action.getValue();
        }
    }

    @Override
    public String getState() {
        return isOn ? "An (" + brightness + "%)" : "Aus";
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getType() { return "Lamp"; }
}


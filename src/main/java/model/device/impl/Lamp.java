package model.device.impl;

import model.device.Device;
import model.device.DeviceAction;
import model.room.Raum;

public class Lamp implements Device {
    private String id;
    private String name;
    private Raum room;
    private boolean isOn;
    private int brightness;

    public Lamp(String id,String name, Raum room) {
        this.id = id;
        this.name = name;
        this.room = room;
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

    @Override
    public Raum getRoom() {
        return room;
    }

    @Override
    public void setRoom(Raum room){
        this.room = room;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void setState(String state) {
        if ("Aus".equals(state)) {
            isOn = false;
        } else if ("An".equals(state)) {
            isOn = true;
        }
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getType() { return "Lampe"; }
    @Override public String toString() {
        if (room == null) {
            return name + " | Lampe";
        }
        return name + " | Lampe in Raum " + room;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}


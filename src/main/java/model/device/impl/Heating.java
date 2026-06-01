package model.device.impl;

import model.device.Device;
import model.device.DeviceAction;
import model.room.Raum;

public class Heating implements Device {
    private String id;
    private String name;
    private Raum room;
    private boolean isOn;
    private int temperature;

    public Heating(String id, String name, Raum room) {
        this.id = id;
        this.name = name;
        this.room = room;
        isOn = false;
        temperature = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "Heizung";
    }

    @Override
    public void executeAction(DeviceAction action) {
        switch (action.getActionType()) {
            case "Ausschalten" -> isOn = false;
            case "Anschalten" -> isOn = true;
            case "Temperatur setzen" -> temperature = Integer.parseInt(action.getValue().toString());
            default -> throw new IllegalArgumentException("Unknown action type " + action.getActionType());
        }
    }

    @Override
    public String getState() {
        return isOn ? "An und auf " + this.getTemperature() + "°" : "Aus";
    }

    @Override
    public void setState(String state) {
        if ("An".equals(state)) {
            isOn = true;
        }else if ("Aus".equals(state)) {
            isOn = false;
        }
    }

    @Override
    public Raum getRoom() {
        return room;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRoom(Raum room) {
        this.room = room;
    }
    @Override public String toString() {
        if (room == null) {
            return name + " | Heizung";
        }
        return name + " | Heizung in Raum " + room;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }
}

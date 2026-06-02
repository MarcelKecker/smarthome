package model.device.impl;

import model.device.Device;
import model.device.DeviceAction;
import model.room.Raum;

public class Shutter implements Device {
    private String id;
    private String name;
    private Raum room;
    private boolean isRolledDown;
    private int position;

    public Shutter(String id, String name, Raum room) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.isRolledDown = false;
        this.position = 0;
    }

    @Override
    public void executeAction(DeviceAction action) {
        switch (action.getActionType()) {
            case "Hochfahren" -> isRolledDown = false;
            case "Herunterfahren" -> isRolledDown = true;
            case "Position setzen" -> position = Integer.parseInt(action.getValue().toString());
            default -> throw new IllegalArgumentException("Unknown action type " + action.getActionType());
        }
    }

    @Override
    public String getState() {
        return isRolledDown ? "Unten (" + position + "%)" : "Oben";
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
        if ("Oben".equals(state)) {
            isRolledDown = false;
        } else if ("Unten".equals(state)) {
            isRolledDown = true;
        }
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getType() { return "Rollladen"; }
    @Override public String toString() {
        if (room == null) {
            return name + " | Rollladen";
        }
        return name + " | Rollladen in Raum " + room;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}


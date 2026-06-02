package model.device.impl;

import model.DeviceType;
import model.device.Device;
import model.State;
import model.room.Raum;
import model.action.Command;

public class Shutter implements Device {
    private String id;
    private String name;
    private Raum room;
    private State state;
    private int position;

    public Shutter(String id, String name, Raum room) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.state = State.ROLLED_UP;
        this.position = 0;
    }

    @Override
    public void executeAction(Command command) {
        command.execute();
    }

    @Override
    public State getState() {
        return this.state;
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
    public void setState(State state) {
        this.state = state;
    }

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public DeviceType getType() { return DeviceType.SHUTTER; }
    @Override public String toString() {
        if (room == null) {
            return name + " | " + getType();
        }
        return name + " | " + getType() + " in Raum " + room;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}


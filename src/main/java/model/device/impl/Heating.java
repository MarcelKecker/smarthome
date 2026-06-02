package model.device.impl;

import model.DeviceType;
import model.device.Device;
import model.State;
import model.room.Raum;
import model.action.Command;

public class Heating implements Device {
    private String id;
    private String name;
    private Raum room;
    private State state;
    private double temperature;

    public Heating(String id, String name, Raum room) {
        this.id = id;
        this.name = name;
        this.room = room;
        state = State.TURNED_OFF;
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
    public DeviceType getType() {
        return DeviceType.HEATING;
    }

    @Override
    public void executeAction(Command command) {
        command.execute();
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
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
            return name + " | " + getType();
        }
        return name + " | " + getType() + " in Raum " + room;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }
}

package model.device.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.DeviceType;
import model.device.Device;
import model.State;
import model.room.Raum;
import model.action.Command;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Lamp implements Device {
    private String id;
    private String name;
    private Raum room;
    private State state;
    private int brightness;


    @JsonCreator
    public Lamp(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("room") Raum room
    ) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.state = State.TURNED_OFF;
        this.brightness = 0;
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
    @Override public DeviceType getType() { return DeviceType.LAMP; }

    @Override public String toString() {
        if (room == null) {
            return name + " | " + getType();
        }
        return name + " | " + getType() + " in Raum " + room;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}
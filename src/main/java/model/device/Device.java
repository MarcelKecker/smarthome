package model.device;

import model.DeviceType;
import model.State;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;
import model.action.Command;
import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Heating.class, name = "HEATING"),
        @JsonSubTypes.Type(value = Lamp.class, name = "LAMP"),
        @JsonSubTypes.Type(value = Shutter.class, name = "SHUTTER")
})
public interface Device {
    String getName();
    String getId();
    DeviceType getType();

    void executeAction(Command command);

    State getState();
    void setState(State state);

    Raum getRoom();

    void setName(String name);

    void setRoom(Raum room);

    @Override
    public String toString();
}
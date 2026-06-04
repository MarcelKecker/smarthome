package model.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import model.ActionType;
import model.device.Device;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseCommand implements Command {
    private final String id;
    private final Device device;
    private final ActionType actionType;
    private int orderIndex;

    public BaseCommand(Device device, ActionType actionType, int orderIndex) {
        this.device = device;
        this.actionType = actionType;
        this.orderIndex = orderIndex;
        this.id = UUID.randomUUID().toString();
    }

    @JsonCreator
    public BaseCommand(
            @JsonProperty("id") String id,
            @JsonProperty("device") Device device,
            @JsonProperty("actionType") ActionType actionType,
            @JsonProperty("orderIndex") int orderIndex
    ) {
        this.id = (id != null) ? id : UUID.randomUUID().toString();
        this.device = device;
        this.actionType = actionType;
        this.orderIndex = orderIndex;
    }

    @Override
    public abstract void execute();

    @Override
    public Device getDevice() {
        return device;
    }

    @Override
    public ActionType getActionType(){
        return actionType;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        String roomPart = device.getRoom() != null
                ? " in Raum " + device.getRoom().getName()
                : "";
        return device.getType() + " " + device.getName() + roomPart + " " + actionType;
    }

    @Override
    public int getOrderIndex() {
        return orderIndex;
    }

    @Override
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
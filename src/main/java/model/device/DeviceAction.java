package model.device;

public class DeviceAction {
    private String actionType; // z.B. "SET_BRIGHTNESS"
    private Object value;

    public DeviceAction(String actionType, Object value) {
        this.actionType = actionType;
        this.value = value;
    }

    public String getActionType() { return actionType; }
    public Object getValue() { return value; }
}

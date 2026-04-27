package factory;

import model.device.Device;
import model.device.impl.Lamp;

public class DeviceFactory {
    public static Device create(String type, String id, String name) {
        return switch (type) {
            case "LAMP" -> new Lamp(id, name);
            default -> throw new IllegalArgumentException("Unknown device type");
        };
    }
}

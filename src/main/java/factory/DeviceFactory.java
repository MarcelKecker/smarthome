package factory;

import model.device.Device;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;

public class DeviceFactory {
    public static Device create(String type, String id, String name, Raum room) {
        return switch (type) {
            case "Lampe" -> new Lamp(id, name,room);
            case "Heizung" -> new Heating(id, name,room);
            case "Rollladen" -> new Shutter(id, name,room);
            default -> throw new IllegalArgumentException("Unknown device type");
        };
    }
}

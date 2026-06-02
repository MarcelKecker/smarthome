package factory;

import model.DeviceType;
import model.device.Device;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;

public class DeviceFactory {
    public static Device create(DeviceType type, String id, String name, Raum room) {
        return switch (type) {
            case LAMP -> new Lamp(id, name,room);
            case HEATING -> new Heating(id, name,room);
            case SHUTTER -> new Shutter(id, name,room);
        };
    }
}

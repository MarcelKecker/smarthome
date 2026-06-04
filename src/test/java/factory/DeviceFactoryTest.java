package factory;

import junit.framework.TestCase;
import model.DeviceType;
import model.State;
import model.device.Device;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;

public class DeviceFactoryTest extends TestCase {

    public void testCreateLamp() {
        Raum raum = new Raum("Wohnzimmer");

        Device device = DeviceFactory.create(DeviceType.LAMP, "L1", "Stehlampe", raum);

        assertNotNull(device);
        assertTrue(device instanceof Lamp);
        assertEquals("L1", device.getId());
        assertEquals("Stehlampe", device.getName());
        assertEquals(raum, device.getRoom());
        assertEquals(DeviceType.LAMP, device.getType());
        assertEquals(State.TURNED_OFF, device.getState());
    }

    public void testCreateHeating() {
        Raum raum = new Raum("Bad");

        Device device = DeviceFactory.create(DeviceType.HEATING, "H1", "Badheizung", raum);

        assertNotNull(device);
        assertTrue(device instanceof Heating);
        assertEquals("H1", device.getId());
        assertEquals("Badheizung", device.getName());
        assertEquals(raum, device.getRoom());
        assertEquals(DeviceType.HEATING, device.getType());
        assertEquals(State.TURNED_OFF, device.getState());
    }

    public void testCreateShutter() {
        Raum raum = new Raum("Schlafzimmer");

        Device device = DeviceFactory.create(DeviceType.SHUTTER, "S1", "Rollladen", raum);

        assertNotNull(device);
        assertTrue(device instanceof Shutter);
        assertEquals("S1", device.getId());
        assertEquals("Rollladen", device.getName());
        assertEquals(raum, device.getRoom());
        assertEquals(DeviceType.SHUTTER, device.getType());
        assertEquals(State.ROLLED_UP, device.getState());
    }

    public void testCreateDeviceWithoutRoom() {
        Device device = DeviceFactory.create(DeviceType.LAMP, "L2", "Deckenlampe", null);

        assertNotNull(device);
        assertNull(device.getRoom());
    }
}

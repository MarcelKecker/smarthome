package service;

import junit.framework.TestCase;
import model.device.Device;
import model.device.impl.Lamp;
import model.device.impl.Heating;
import model.device.impl.Shutter;
import model.room.Raum;

public class DeviceServiceTest extends TestCase {

    public void testAddDevice() {
        DeviceService service = new DeviceService();
        Lamp lamp = new Lamp("L1", "Lampe", null);

        service.addDevice(lamp);

        assertEquals(1, service.getDevices().size());
        assertSame(lamp, service.getDevices().get(0));
    }

    public void testAddMultipleDevices() {
        DeviceService service = new DeviceService();
        Raum raum = new Raum("Wohnzimmer");
        Lamp lamp = new Lamp("L1", "Lampe", raum);
        Heating heating = new Heating("H1", "Heizung", raum);
        Shutter shutter = new Shutter("S1", "Rollladen", raum);

        service.addDevice(lamp);
        service.addDevice(heating);
        service.addDevice(shutter);

        assertEquals(3, service.getDevices().size());
    }

    public void testGetDevicesInitiallyEmpty() {
        DeviceService service = new DeviceService();

        assertNotNull(service.getDevices());
        assertEquals(0, service.getDevices().size());
    }

    public void testGetDevicesContainsAddedDevice() {
        DeviceService service = new DeviceService();
        Heating heating = new Heating("H1", "Heizung", null);

        service.addDevice(heating);

        assertTrue(service.getDevices().contains(heating));
    }

    public void testClearRemovesAllDevices() {
        DeviceService service = new DeviceService();
        service.addDevice(new Lamp("L1", "Lampe", null));
        service.addDevice(new Heating("H1", "Heizung", null));

        service.clear();

        assertEquals(0, service.getDevices().size());
    }

    public void testSetDevicesReplacesExistingList() {
        DeviceService service = new DeviceService();
        service.addDevice(new Lamp("L1", "Alt", null));

        Lamp newLamp = new Lamp("L2", "Neu", null);
        service.setDevices(java.util.List.of(newLamp));

        assertEquals(1, service.getDevices().size());
        assertSame(newLamp, service.getDevices().get(0));
    }
}

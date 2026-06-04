package model;

import junit.framework.TestCase;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;

import java.util.Arrays;
import java.util.List;

public class ActionTypeTest extends TestCase {

    public void testForDeviceLamp() {
        Lamp lamp = new Lamp("L1", "Lampe", null);

        ActionType[] types = ActionType.forDevice(lamp);

        assertNotNull(types);
        List<ActionType> list = Arrays.asList(types);
        assertTrue(list.contains(ActionType.TURN_ON));
        assertTrue(list.contains(ActionType.TURN_OFF));
        assertTrue(list.contains(ActionType.SET_BRIGHTNESS));
        assertEquals(3, types.length);
    }

    public void testForDeviceHeating() {
        Heating heating = new Heating("H1", "Heizung", null);

        ActionType[] types = ActionType.forDevice(heating);

        assertNotNull(types);
        List<ActionType> list = Arrays.asList(types);
        assertTrue(list.contains(ActionType.TURN_ON));
        assertTrue(list.contains(ActionType.TURN_OFF));
        assertTrue(list.contains(ActionType.SET_TEMPERATURE));
        assertEquals(3, types.length);
    }

    public void testForDeviceShutter() {
        Shutter shutter = new Shutter("S1", "Rollladen", null);

        ActionType[] types = ActionType.forDevice(shutter);

        assertNotNull(types);
        List<ActionType> list = Arrays.asList(types);
        assertTrue(list.contains(ActionType.ROLL_DOWN));
        assertTrue(list.contains(ActionType.ROLL_UP));
        assertTrue(list.contains(ActionType.SET_POSITION));
        assertEquals(3, types.length);
    }

    public void testForDeviceLampDoesNotContainShutterActions() {
        Lamp lamp = new Lamp("L1", "Lampe", null);

        List<ActionType> list = Arrays.asList(ActionType.forDevice(lamp));

        assertFalse(list.contains(ActionType.ROLL_UP));
        assertFalse(list.contains(ActionType.ROLL_DOWN));
        assertFalse(list.contains(ActionType.SET_TEMPERATURE));
    }

    public void testDeviceTypeToStringLamp() {
        assertEquals("Lampe", DeviceType.LAMP.toString());
    }

    public void testDeviceTypeToStringHeating() {
        assertEquals("Heizung", DeviceType.HEATING.toString());
    }

    public void testDeviceTypeToStringShutter() {
        assertEquals("Rollladen", DeviceType.SHUTTER.toString());
    }
}

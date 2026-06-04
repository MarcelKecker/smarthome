package model.device.impl;

import junit.framework.TestCase;
import model.State;
import model.room.Raum;

public class LampTest extends TestCase {
    public void testCreateLamp(){
        Raum raum = new Raum("Wohnzimmer");
        Lamp lampe = new Lamp("L1", "Stehlampe", raum);
        assertEquals("L1", lampe.getId());
        assertEquals("Stehlampe", lampe.getName());
        assertEquals(raum, lampe.getRoom());
        assertEquals(State.TURNED_OFF, lampe.getState());
        assertEquals(0, lampe.getBrightness());
        assertEquals("Stehlampe | Lampe in Raum Wohnzimmer", lampe.toString());
    }

    public void testSetName() {
        Lamp lampe = new Lamp("L1", "Stehlampe", null);

        lampe.setName("Deckenlampe");

        assertEquals("Deckenlampe", lampe.getName());
        assertEquals("Deckenlampe | Lampe", lampe.toString());
    }

    public void testSetRoom() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        Raum raum = new Raum("Küche");

        lampe.setRoom(raum);

        assertEquals(raum, lampe.getRoom());
        assertEquals("Lampe | Lampe in Raum Küche", lampe.toString());
    }

    public void testSetState() {
        Lamp lampe = new Lamp("L1", "Lampe", null);

        lampe.setState(State.TURNED_ON);

        assertEquals(State.TURNED_ON, lampe.getState());
    }

    public void testSetBrightness() {
        Lamp lampe = new Lamp("L1", "Lampe", null);

        lampe.setBrightness(50);

        assertEquals(50, lampe.getBrightness());
    }
}
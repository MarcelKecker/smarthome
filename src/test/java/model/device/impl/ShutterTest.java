package model.device.impl;

import junit.framework.TestCase;
import model.room.Raum;
import model.State;

public class ShutterTest extends TestCase {
    public void testCreateShutter() {
        Raum raum = new Raum("Schlafzimmer");
        Shutter shutter = new Shutter("S1", "Rollladen", raum);

        assertEquals("S1", shutter.getId());
        assertEquals("Rollladen", shutter.getName());
        assertEquals(raum, shutter.getRoom());
        assertEquals(State.ROLLED_UP, shutter.getState());
        assertEquals(0, shutter.getPosition());
        assertEquals("Rollladen | Rollladen in Raum Schlafzimmer", shutter.toString());
    }

    public void testSetNameAndToStringWithoutRoom() {
        Shutter shutter = new Shutter("S2", "Fensterladen", null);

        shutter.setName("Terrassenladen");

        assertEquals("Terrassenladen", shutter.getName());
        assertEquals("Terrassenladen | Rollladen", shutter.toString());
    }

    public void testSetRoom() {
        Shutter shutter = new Shutter("S3", "Rollladen", null);
        Raum raum = new Raum("Büro");

        shutter.setRoom(raum);

        assertEquals(raum, shutter.getRoom());
        assertEquals("Rollladen | Rollladen in Raum Büro", shutter.toString());
    }

    public void testSetState() {
        Shutter shutter = new Shutter("S4", "Rollladen", null);

        shutter.setState(State.ROLLED_DOWN);

        assertEquals(State.ROLLED_DOWN, shutter.getState());
    }

    public void testSetPosition() {
        Shutter shutter = new Shutter("S5", "Rollladen", null);

        shutter.setPosition(75);

        assertEquals(75, shutter.getPosition());
    }
}
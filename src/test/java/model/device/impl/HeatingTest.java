package model.device.impl;

import junit.framework.TestCase;
import model.State;
import model.room.Raum;

public class HeatingTest extends TestCase {

    public void testCreateHeating() {
        Raum raum = new Raum("Wohnzimmer");
        Heating heating = new Heating("H1", "Heizung", raum);

        assertEquals("H1", heating.getId());
        assertEquals("Heizung", heating.getName());
        assertEquals(raum, heating.getRoom());
        assertEquals(State.TURNED_OFF, heating.getState());
        assertEquals(0.0, heating.getTemperature());
        assertEquals("Heizung | Heizung in Raum Wohnzimmer", heating.toString());
    }

    public void testSetNameAndToStringWithoutRoom() {
        Heating heating = new Heating("H2", "Radiator", null);

        heating.setName("Badheizung");

        assertEquals("Badheizung", heating.getName());
        assertEquals("Badheizung | Heizung", heating.toString());
    }

    public void testSetRoom() {
        Heating heating = new Heating("H3", "Heizung", null);
        Raum raum = new Raum("Büro");

        heating.setRoom(raum);

        assertEquals(raum, heating.getRoom());
        assertEquals("Heizung | Heizung in Raum Büro", heating.toString());
    }

    public void testSetState() {
        Heating heating = new Heating("H4", "Heizung", null);

        heating.setState(State.TURNED_ON);

        assertEquals(State.TURNED_ON, heating.getState());
    }

    public void testSetTemperature() {
        Heating heating = new Heating("H5", "Heizung", null);

        heating.setTemperature(22.5);

        assertEquals(22.5, heating.getTemperature());
    }
}
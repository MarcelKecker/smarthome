package model.action.impl;

import junit.framework.TestCase;
import model.State;
import model.device.impl.Heating;
import model.room.Raum;

public class HeatingCommandTest extends TestCase {

    public void testTurnOnHeatingCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Heating heating = new Heating("H1", "Heizung", raum);

        assertEquals(State.TURNED_OFF, heating.getState());

        TurnOnHeatingCommand command = new TurnOnHeatingCommand(heating, 0);
        command.execute();

        assertEquals(State.TURNED_ON, heating.getState());
    }

    public void testTurnOffHeatingCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Heating heating = new Heating("H1", "Heizung", raum);
        heating.setState(State.TURNED_ON);

        assertEquals(State.TURNED_ON, heating.getState());

        TurnOffHeatingCommand command = new TurnOffHeatingCommand(heating, 0);
        command.execute();

        assertEquals(State.TURNED_OFF, heating.getState());
    }

    public void testSetTemperatureHeatingCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Heating heating = new Heating("H1", "Heizung", raum);

        SetTemperatureHeatingCommand command = new SetTemperatureHeatingCommand(heating, 22.5, 0);
        command.execute();

        assertEquals(22.5, heating.getTemperature());
    }
}

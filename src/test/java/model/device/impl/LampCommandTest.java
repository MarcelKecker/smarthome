package model.action.impl;

import junit.framework.TestCase;
import model.State;
import model.device.impl.Lamp;
import model.room.Raum;

public class LampCommandTest extends TestCase {

    public void testTurnOnLampCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Lamp lampe = new Lamp("L1", "Stehlampe", raum);

        assertEquals(State.TURNED_OFF, lampe.getState());

        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);
        command.execute();

        assertEquals(State.TURNED_ON, lampe.getState());
    }

    public void testTurnOffLampCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Lamp lampe = new Lamp("L1", "Stehlampe", raum);
        lampe.setState(State.TURNED_ON);

        assertEquals(State.TURNED_ON, lampe.getState());

        TurnOFfLampCommand command = new TurnOFfLampCommand(lampe, 0);
        command.execute();

        assertEquals(State.TURNED_OFF, lampe.getState());
    }

    public void testSetBrightnessLampCommand() {
        Raum raum = new Raum("Wohnzimmer");
        Lamp lampe = new Lamp("L1", "Stehlampe", raum);

        SetBrightnessLampCommand command = new SetBrightnessLampCommand(lampe, 50, 0);
        command.execute();

        assertEquals(50, lampe.getBrightness());
    }
}
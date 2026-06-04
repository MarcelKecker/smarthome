package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
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

    public void testSetBrightnessCommandGetBrightness() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        SetBrightnessLampCommand command = new SetBrightnessLampCommand(lampe, 80, 0);

        assertEquals(80, command.getBrightness());
    }

    // --- BaseCommand-Eigenschaften ---

    public void testBaseCommandGetDevice() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        assertSame(lampe, command.getDevice());
    }

    public void testBaseCommandGetActionType() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        assertEquals(ActionType.TURN_ON, command.getActionType());
    }

    public void testBaseCommandGetID() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        assertNotNull(command.getID());
        assertFalse(command.getID().isEmpty());
    }

    public void testBaseCommandGetOrderIndex() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 3);

        assertEquals(3, command.getOrderIndex());
    }

    public void testBaseCommandSetOrderIndex() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        command.setOrderIndex(5);

        assertEquals(5, command.getOrderIndex());
    }

    public void testBaseCommandToStringWithRoom() {
        Raum raum = new Raum("Wohnzimmer");
        Lamp lampe = new Lamp("L1", "Stehlampe", raum);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        String result = command.toString();

        assertTrue(result.contains("Stehlampe"));
        assertTrue(result.contains("Wohnzimmer"));
        assertTrue(result.contains("TURN_ON"));
    }

    public void testBaseCommandToStringWithoutRoom() {
        Lamp lampe = new Lamp("L1", "Stehlampe", null);
        TurnOnLampCommand command = new TurnOnLampCommand(lampe, 0);

        String result = command.toString();

        assertTrue(result.contains("Stehlampe"));
        assertTrue(result.contains("TURN_ON"));
        assertFalse(result.contains("Raum"));
    }

    public void testTwoDifferentCommandsHaveDifferentIDs() {
        Lamp lampe = new Lamp("L1", "Lampe", null);
        TurnOnLampCommand cmd1 = new TurnOnLampCommand(lampe, 0);
        TurnOnLampCommand cmd2 = new TurnOnLampCommand(lampe, 1);

        assertFalse(cmd1.getID().equals(cmd2.getID()));
    }
}
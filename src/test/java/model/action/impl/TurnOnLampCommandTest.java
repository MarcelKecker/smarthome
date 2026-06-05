package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Lamp;
import model.room.Raum;

public class TurnOnLampCommandTest extends TestCase {

    private Raum raum;
    private Lamp lamp;

    @Override
    protected void setUp() {
        raum = new Raum("Wohnzimmer");
        lamp = new Lamp("L-TEST-ON", "Wohnzimmerlampe", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-LAMP-ON-123";
        int orderIndex = 0;

        TurnOnLampCommand cmd = new TurnOnLampCommand(
                customId,
                lamp,
                ActionType.TURN_ON,
                orderIndex
        );

        // Überprüfung, ob die Parameter korrekt an BaseCommand weitergegeben wurden
        assertEquals(customId, cmd.getID());
        assertEquals(lamp, cmd.getDevice());
        assertEquals(ActionType.TURN_ON, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
    }

    public void testExecuteChangesLampStateToTurnedOn() {
        // Ausgangszustand explizit auf TURNED_OFF setzen, um die Änderung zu prüfen
        lamp.setState(State.TURNED_OFF);

        TurnOnLampCommand cmd = new TurnOnLampCommand(
                "CMD-1",
                lamp,
                ActionType.TURN_ON,
                0
        );

        // Befehl ausführen
        cmd.execute();

        // Fachliche Zusicherung prüfen
        assertEquals(State.TURNED_ON, lamp.getState());
    }

    public void testExecuteWithNullLampDoesNotThrowException() {
        TurnOnLampCommand cmd = new TurnOnLampCommand(
                "CMD-NULL",
                null,
                ActionType.TURN_ON,
                0
        );

        try {
            // Durch den Null-Check "if (lamp != null)" in deiner execute()-Methode
            // darf hier keine NullPointerException fliegen.
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einer null-Lampe robust sein und keine NullPointerException werfen.");
        }
    }
}
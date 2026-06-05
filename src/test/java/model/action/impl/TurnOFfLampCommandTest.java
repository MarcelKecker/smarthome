package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Lamp;
import model.room.Raum;

public class TurnOFfLampCommandTest extends TestCase {

    private Raum raum;
    private Lamp lamp;

    @Override
    protected void setUp() {
        raum = new Raum("Wohnzimmer");
        lamp = new Lamp("L-TEST-OFF", "Deckenleuchte", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-LAMP-OFF-123";
        int orderIndex = 1;

        TurnOFfLampCommand cmd = new TurnOFfLampCommand(
                customId,
                lamp,
                ActionType.TURN_OFF,
                orderIndex
        );

        // Überprüfung, ob die Parameter korrekt an BaseCommand weitergegeben wurden
        assertEquals(customId, cmd.getID());
        assertEquals(lamp, cmd.getDevice());
        assertEquals(ActionType.TURN_OFF, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
    }

    public void testExecuteChangesLampStateToTurnedOff() {
        // Ausgangszustand explizit auf TURNED_ON setzen, um die Änderung zu überprüfen
        lamp.setState(State.TURNED_ON);

        TurnOFfLampCommand cmd = new TurnOFfLampCommand(
                "CMD-1",
                lamp,
                ActionType.TURN_OFF,
                0
        );

        // Befehl ausführen
        cmd.execute();

        // Fachliche Zusicherung prüfen
        assertEquals(State.TURNED_OFF, lamp.getState());
    }

    public void testExecuteWithNullLampDoesNotThrowException() {
        TurnOFfLampCommand cmd = new TurnOFfLampCommand(
                "CMD-NULL",
                null,
                ActionType.TURN_OFF,
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
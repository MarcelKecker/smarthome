package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Heating;
import model.room.Raum;

public class TurnOnHeatingCommandTest extends TestCase {

    private Raum raum;
    private Heating heating;

    @Override
    protected void setUp() {
        raum = new Raum("Wohnzimmer");
        heating = new Heating("H-TEST-ON", "Fußbodenheizung", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-HEAT-ON-999";
        int orderIndex = 1;

        TurnOnHeatingCommand cmd = new TurnOnHeatingCommand(
                customId,
                heating,
                ActionType.TURN_ON,
                orderIndex
        );

        // Überprüfung, ob die Parameter korrekt an BaseCommand weitergegeben wurden
        assertEquals(customId, cmd.getID());
        assertEquals(heating, cmd.getDevice());
        assertEquals(ActionType.TURN_ON, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
    }

    public void testExecuteChangesHeatingStateToTurnedOn() {
        // Ausgangszustand explizit auf TURNED_OFF setzen, um die Änderung zu überprüfen
        heating.setState(State.TURNED_OFF);

        TurnOnHeatingCommand cmd = new TurnOnHeatingCommand(
                "CMD-1",
                heating,
                ActionType.TURN_ON,
                0
        );

        // Befehl ausführen
        cmd.execute();

        // Fachliche Zusicherung prüfen
        assertEquals(State.TURNED_ON, heating.getState());
    }

    public void testExecuteWithNullHeatingDoesNotThrowException() {
        TurnOnHeatingCommand cmd = new TurnOnHeatingCommand(
                "CMD-NULL",
                null,
                ActionType.TURN_ON,
                0
        );

        try {
            // Durch den Null-Check "if (heating != null)" in deiner execute()-Methode
            // darf hier keine NullPointerException fliegen.
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einer null-Heizung robust sein und keine NullPointerException werfen.");
        }
    }
}
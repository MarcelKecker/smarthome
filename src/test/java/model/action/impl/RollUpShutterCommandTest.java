package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Shutter;
import model.room.Raum;

public class RollUpShutterCommandTest extends TestCase {

    private Raum raum;
    private Shutter shutter;

    @Override
    protected void setUp() {
        raum = new Raum("Wohnzimmer");
        shutter = new Shutter("S-TEST-UP", "Rollladen Süd", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-SHUTTER-456";
        int orderIndex = 2;

        RollUpShutterCommand cmd = new RollUpShutterCommand(
                customId,
                shutter,
                ActionType.ROLL_UP,
                orderIndex
        );

        // Überprüfung, ob die Parameter korrekt über super() an BaseCommand weitergegeben wurden
        assertEquals(customId, cmd.getID());
        assertEquals(shutter, cmd.getDevice());
        assertEquals(ActionType.ROLL_UP, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
    }

    public void testExecuteChangesShutterStateAndPosition() {
        // Ausgangszustand geschlossen (unten) setzen, um die Änderung nach oben zu testen
        shutter.setState(State.ROLLED_DOWN);
        shutter.setPosition(100);

        RollUpShutterCommand cmd = new RollUpShutterCommand(
                "CMD-2",
                shutter,
                ActionType.ROLL_UP,
                0
        );

        // Befehl ausführen
        cmd.execute();

        // Fachliche Zusicherungen prüfen (oben ist Zustand ROLLED_UP und Position 0)
        assertEquals(State.ROLLED_UP, shutter.getState());
        assertEquals(0, shutter.getPosition());
    }

    public void testExecuteWithNullShutterDoesNotThrowException() {
        RollUpShutterCommand cmd = new RollUpShutterCommand(
                "CMD-NULL-UP",
                null,
                ActionType.ROLL_UP,
                0
        );

        try {
            // Durch den Null-Check "if (shutter != null)" in deiner execute()-Methode
            // darf hier keine NullPointerException fliegen.
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einem null-Shutter robust sein und keine NullPointerException werfen.");
        }
    }
}
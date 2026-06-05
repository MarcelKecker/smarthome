package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Shutter;
import model.room.Raum;

public class RollDownShutterCommandTest extends TestCase {

    private Raum raum;
    private Shutter shutter;

    @Override
    protected void setUp() {
        raum = new Raum("Schlafzimmer");
        shutter = new Shutter("S-TEST", "Rollladen Ost", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-SHUTTER-123";
        int orderIndex = 4;

        RollDownShutterCommand cmd = new RollDownShutterCommand(
                customId,
                shutter,
                ActionType.ROLL_DOWN,
                orderIndex
        );

        // Überprüfung, ob die Parameter korrekt über super() an BaseCommand weitergegeben wurden
        assertEquals(customId, cmd.getID());
        assertEquals(shutter, cmd.getDevice());
        assertEquals(ActionType.ROLL_DOWN, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
    }

    public void testExecuteChangesShutterStateAndPosition() {
        // Ausgangszustand explizit anders setzen, um die Änderung sicher zu überprüfen
        shutter.setState(State.ROLLED_UP);
        shutter.setPosition(0);

        RollDownShutterCommand cmd = new RollDownShutterCommand(
                "CMD-1",
                shutter,
                ActionType.ROLL_DOWN,
                0
        );

        // Befehl ausführen
        cmd.execute();

        // Fachliche Zusicherungen prüfen
        assertEquals(State.ROLLED_DOWN, shutter.getState());
        assertEquals(100, shutter.getPosition());
    }

    public void testExecuteWithNullShutterDoesNotThrowException() {
        RollDownShutterCommand cmd = new RollDownShutterCommand(
                "CMD-NULL",
                null,
                ActionType.ROLL_DOWN,
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
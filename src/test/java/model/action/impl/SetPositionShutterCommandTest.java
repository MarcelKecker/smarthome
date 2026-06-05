package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Shutter;
import model.room.Raum;

public class SetPositionShutterCommandTest extends TestCase {

    private Raum raum;
    private Shutter shutter;

    @Override
    protected void setUp() {
        raum = new Raum("Schlafzimmer");
        shutter = new Shutter("S-TEST-POS", "Rollladen Ost", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-SHUTTER-POS-1";
        int orderIndex = 3;
        int targetPosition = 40;

        SetPositionShutterCommand cmd = new SetPositionShutterCommand(
                customId,
                shutter,
                ActionType.SET_POSITION,
                orderIndex,
                targetPosition
        );

        // Überprüfung der Vererbung und des eigenen Feldes
        assertEquals(customId, cmd.getID());
        assertEquals(shutter, cmd.getDevice());
        assertEquals(ActionType.SET_POSITION, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
        assertEquals(targetPosition, cmd.getPosition());
    }

    public void testExecuteChangesPositionWhenShutterIsRolledDown() {
        // Zustand auf ROLLED_DOWN setzen, um die Bedingung im Code zu erfüllen
        shutter.setState(State.ROLLED_DOWN);
        shutter.setPosition(100); // Alter Wert/Standardwert

        SetPositionShutterCommand cmd = new SetPositionShutterCommand(
                "CMD-1",
                shutter,
                ActionType.SET_POSITION,
                0,
                40
        );

        cmd.execute();

        // Die Position muss nun erfolgreich auf 40 geändert worden sein
        assertEquals(40, shutter.getPosition());
    }

    public void testExecuteDoesNotChangePositionWhenShutterIsNotRolledDown() {
        // Zustand auf ROLLED_UP setzen -> Bedingung im Code schlägt fehl
        shutter.setState(State.ROLLED_UP);
        shutter.setPosition(0); // Alter Wert

        SetPositionShutterCommand cmd = new SetPositionShutterCommand(
                "CMD-2",
                shutter,
                ActionType.SET_POSITION,
                0,
                40
        );

        cmd.execute();

        // Da die Bedingung nicht erfüllt war, muss die Position unverändert bei 0 bleiben
        assertEquals(0, shutter.getPosition());
    }

    public void testExecuteWithNullShutterDoesNotThrowException() {
        SetPositionShutterCommand cmd = new SetPositionShutterCommand(
                "CMD-NULL",
                null,
                ActionType.SET_POSITION,
                0,
                50
        );

        try {
            // Durch den Null-Check "if (shutter != null ...)" darf hier keine NullPointerException geworfen werden
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einem null-Shutter robust sein und keine NullPointerException werfen.");
        }
    }
}
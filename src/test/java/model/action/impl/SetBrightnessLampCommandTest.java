package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Lamp;
import model.room.Raum;

public class SetBrightnessLampCommandTest extends TestCase {

    private Raum raum;
    private Lamp lamp;

    @Override
    protected void setUp() {
        raum = new Raum("Wohnzimmer");
        lamp = new Lamp("L-TEST", "Dimmbare Stehlampe", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-LAMP-BRIGHT";
        int orderIndex = 1;
        int targetBrightness = 75;

        SetBrightnessLampCommand cmd = new SetBrightnessLampCommand(
                customId,
                lamp,
                ActionType.SET_BRIGHTNESS,
                orderIndex,
                targetBrightness
        );

        // Überprüfung der Delegierung an BaseCommand & des eigenen Feldes
        assertEquals(customId, cmd.getID());
        assertEquals(lamp, cmd.getDevice());
        assertEquals(ActionType.SET_BRIGHTNESS, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
        assertEquals(targetBrightness, cmd.getBrightness());
    }

    public void testExecuteChangesBrightnessWhenLampIsTurnedOn() {
        // Zustand auf TURNED_ON setzen, damit die Bedingung im Code erfüllt ist
        lamp.setState(State.TURNED_ON);
        lamp.setBrightness(10); // Alter Wert

        SetBrightnessLampCommand cmd = new SetBrightnessLampCommand(
                "CMD-1",
                lamp,
                ActionType.SET_BRIGHTNESS,
                0,
                85
        );

        cmd.execute();

        // Der Wert muss sich geändert haben
        assertEquals(85, lamp.getBrightness());
    }

    public void testExecuteDoesNotChangeBrightnessWhenLampIsTurnedOff() {
        // Zustand ist standardmäßig TURNED_OFF oder explizit setzen
        lamp.setState(State.TURNED_OFF);
        lamp.setBrightness(10); // Alter Wert

        SetBrightnessLampCommand cmd = new SetBrightnessLampCommand(
                "CMD-2",
                lamp,
                ActionType.SET_BRIGHTNESS,
                0,
                85
        );

        cmd.execute();

        // Da die Lampe aus ist, muss die Helligkeit unverändert bei 10 bleiben!
        assertEquals(10, lamp.getBrightness());
    }

    public void testExecuteWithNullLampDoesNotThrowException() {
        SetBrightnessLampCommand cmd = new SetBrightnessLampCommand(
                "CMD-NULL",
                null,
                ActionType.SET_BRIGHTNESS,
                0,
                50
        );

        try {
            // Durch deinen Null-Check "if (lamp != null ...)" darf hier keine Exception fliegen
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einer null-Lampe robust sein und keine NullPointerException werfen.");
        }
    }
}
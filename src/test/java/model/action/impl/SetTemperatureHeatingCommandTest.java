package model.action.impl;

import junit.framework.TestCase;
import model.ActionType;
import model.State;
import model.device.impl.Heating;
import model.room.Raum;

public class SetTemperatureHeatingCommandTest extends TestCase {

    private Raum raum;
    private Heating heating;

    @Override
    protected void setUp() {
        raum = new Raum("Badezimmer");
        heating = new Heating("H-TEST-TEMP", "Handtuchheizung", raum);
    }

    public void testConstructorAndBaseCommandDelegation() {
        String customId = "CMD-HEAT-TEMP-1";
        int orderIndex = 2;
        double targetTemperature = 22.5;

        SetTemperatureHeatingCommand cmd = new SetTemperatureHeatingCommand(
                customId,
                heating,
                ActionType.SET_TEMPERATURE,
                orderIndex,
                targetTemperature
        );

        // Überprüfung der Vererbung und des eigenen double-Feldes
        assertEquals(customId, cmd.getID());
        assertEquals(heating, cmd.getDevice());
        assertEquals(ActionType.SET_TEMPERATURE, cmd.getActionType());
        assertEquals(orderIndex, cmd.getOrderIndex());
        assertEquals(targetTemperature, cmd.getTemperature(), 0.001); // Delta für double-Vergleich
    }

    public void testExecuteChangesTemperatureWhenHeatingIsTurnedOn() {
        // Zustand auf TURNED_ON setzen, damit die Bedingung im Code erfüllt ist
        heating.setState(State.TURNED_ON);
        heating.setTemperature(18.0); // Alter Ausgangswert

        SetTemperatureHeatingCommand cmd = new SetTemperatureHeatingCommand(
                "CMD-1",
                heating,
                ActionType.SET_TEMPERATURE,
                0,
                21.5
        );

        cmd.execute();

        // Die Temperatur muss sich auf 21.5 geändert haben
        assertEquals(21.5, heating.getTemperature(), 0.001);
    }

    public void testExecuteDoesNotChangeTemperatureWhenHeatingIsTurnedOff() {
        // Zustand auf TURNED_OFF setzen -> Bedingung im Code greift nicht
        heating.setState(State.TURNED_OFF);
        heating.setTemperature(18.0); // Alter Ausgangswert

        SetTemperatureHeatingCommand cmd = new SetTemperatureHeatingCommand(
                "CMD-2",
                heating,
                ActionType.SET_TEMPERATURE,
                0,
                21.5
        );

        cmd.execute();

        // Da die Heizung aus war, muss die Temperatur unverändert bei 18.0 bleiben
        assertEquals(18.0, heating.getTemperature(), 0.001);
    }

    public void testExecuteWithNullHeatingDoesNotThrowException() {
        SetTemperatureHeatingCommand cmd = new SetTemperatureHeatingCommand(
                "CMD-NULL",
                null,
                ActionType.SET_TEMPERATURE,
                0,
                20.0
        );

        try {
            // Durch den Null-Check "if (heating != null ...)" darf keine NullPointerException geworfen werden
            cmd.execute();
        } catch (NullPointerException e) {
            fail("execute() sollte bei einer null-Heizung robust sein und keine NullPointerException werfen.");
        }
    }
}
package model.scenario;

import junit.framework.TestCase;
import model.State;
import model.action.Command;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.action.impl.*;
import model.room.Raum;

public class ScenarioTest extends TestCase {

    // --- Anlegen und Bearbeiten von Szenarien ---

    public void testCreateScenario() {
        Scenario scenario = new Scenario("Abend", "Abendmodus aktivieren");

        assertEquals("Abend", scenario.getName());
        assertEquals("Abendmodus aktivieren", scenario.getDescription());
        assertNotNull(scenario.getCommands());
        assertEquals(0, scenario.getCommands().size());
    }

    public void testSetName() {
        Scenario scenario = new Scenario("Alt", "Beschreibung");

        scenario.setName("Nacht");

        assertEquals("Nacht", scenario.getName());
    }

    public void testSetDescription() {
        Scenario scenario = new Scenario("Morgen", "Alt");

        scenario.setDescription("Neuer Morgen-Modus");

        assertEquals("Neuer Morgen-Modus", scenario.getDescription());
    }

    // --- Aktionen hinzufügen ---

    public void testAddCommand() {
        Scenario scenario = new Scenario("Test", "");
        TestCommand cmd = new TestCommand("cmd1");

        scenario.addCommand(cmd);

        assertEquals(1, scenario.getCommands().size());
        assertSame(cmd, scenario.getCommands().get(0));
    }

    public void testAddMultipleCommands() {
        Scenario scenario = new Scenario("Abend", "");
        TestCommand cmd1 = new TestCommand("cmd1");
        TestCommand cmd2 = new TestCommand("cmd2");
        TestCommand cmd3 = new TestCommand("cmd3");

        scenario.addCommand(cmd1);
        scenario.addCommand(cmd2);
        scenario.addCommand(cmd3);

        assertEquals(3, scenario.getCommands().size());
    }

    // --- Ausführung von Szenarien ---

    public void testExecuteEmptyScenario() {
        Scenario scenario = new Scenario("Leer", "");

        // Kein Fehler beim Ausführen eines leeren Szenarios
        scenario.execute();

        assertEquals(0, scenario.getCommands().size());
    }

    public void testExecuteScenarioRunsAllCommands() {
        Scenario scenario = new Scenario("Test", "");
        TestCommand cmd1 = new TestCommand("cmd1");
        TestCommand cmd2 = new TestCommand("cmd2");
        scenario.addCommand(cmd1);
        scenario.addCommand(cmd2);

        scenario.execute();

        assertTrue(cmd1.isExecuted());
        assertTrue(cmd2.isExecuted());
    }

    // --- Zustandsänderungen von Geräten nach Ausführung ---

    public void testExecuteScenarioTurnsOnLamp() {
        Raum raum = new Raum("Wohnzimmer");
        Lamp lamp = new Lamp("L1", "Stehlampe", raum);
        Scenario scenario = new Scenario("Abend", "");
        scenario.addCommand(new TurnOnLampCommand(lamp, 0));

        assertEquals(State.TURNED_OFF, lamp.getState());

        scenario.execute();

        assertEquals(State.TURNED_ON, lamp.getState());
    }

    public void testExecuteScenarioSetsHeatingTemperature() {
        Raum raum = new Raum("Bad");
        Heating heating = new Heating("H1", "Badheizung", raum);
        Scenario scenario = new Scenario("Abend", "");
        scenario.addCommand(new SetTemperatureHeatingCommand(heating, 22.0, 0));

        assertEquals(0.0, heating.getTemperature());

        scenario.execute();

        assertEquals(22.0, heating.getTemperature());
    }

    public void testExecuteScenarioClosesShutter() {
        Raum raum = new Raum("Schlafzimmer");
        Shutter shutter = new Shutter("S1", "Rollladen", raum);
        Scenario scenario = new Scenario("Nacht", "");
        scenario.addCommand(new RollDownShutterCommand(shutter, 0));

        assertEquals(State.ROLLED_UP, shutter.getState());

        scenario.execute();

        assertEquals(State.ROLLED_DOWN, shutter.getState());
        assertEquals(100, shutter.getPosition());
    }

    public void testExecuteScenarioMultipleDevices() {
        Raum wohnzimmer = new Raum("Wohnzimmer");
        Lamp lamp = new Lamp("L1", "Lampe", wohnzimmer);
        Heating heating = new Heating("H1", "Heizung", wohnzimmer);
        Shutter shutter = new Shutter("S1", "Rollladen", wohnzimmer);

        Scenario scenario = new Scenario("Abend", "Abend-Szenario");
        scenario.addCommand(new TurnOnLampCommand(lamp, 0));
        scenario.addCommand(new SetTemperatureHeatingCommand(heating, 20.0, 1));
        scenario.addCommand(new SetPositionShutterCommand(shutter, 50, 2));

        scenario.execute();

        assertEquals(State.TURNED_ON, lamp.getState());
        assertEquals(20.0, heating.getTemperature());
        assertEquals(50, shutter.getPosition());
    }

    // --- replaceCommand ---

    public void testReplaceCommand() {
        Scenario scenario = new Scenario("Test", "");
        TestCommand cmd1 = new TestCommand("cmd1");
        TestCommand cmd2 = new TestCommand("cmd2");
        scenario.addCommand(cmd1);

        scenario.replaceCommand(cmd1, cmd2);

        assertEquals(1, scenario.getCommands().size());
        assertSame(cmd2, scenario.getCommands().get(0));
    }

    public void testReplaceCommandDoesNotReplaceWrongId() {
        Scenario scenario = new Scenario("Test", "");
        TestCommand cmd1 = new TestCommand("cmd1");
        TestCommand cmdOther = new TestCommand("other");
        TestCommand cmdNew = new TestCommand("new");
        scenario.addCommand(cmd1);

        scenario.replaceCommand(cmdOther, cmdNew);

        assertSame(cmd1, scenario.getCommands().get(0));
    }

    // --- Reihenfolge der Aktionen ---

    public void testCommandOrderPreserved() {
        Scenario scenario = new Scenario("Test", "");
        TestCommand cmd1 = new TestCommand("first");
        TestCommand cmd2 = new TestCommand("second");
        TestCommand cmd3 = new TestCommand("third");
        scenario.addCommand(cmd1);
        scenario.addCommand(cmd2);
        scenario.addCommand(cmd3);

        assertEquals("first", scenario.getCommands().get(0).getID());
        assertEquals("second", scenario.getCommands().get(1).getID());
        assertEquals("third", scenario.getCommands().get(2).getID());
    }
}

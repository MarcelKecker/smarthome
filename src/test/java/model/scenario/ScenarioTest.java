package model.scenario;

import junit.framework.TestCase;
import model.ActionType;
import model.DeviceType;
import model.State;
import model.action.Command;
import model.device.Device;
import model.device.impl.Lamp;
import model.room.Raum;

import java.util.ArrayList;
import java.util.List;

public class ScenarioTest extends TestCase {

    // --- Stub Command implementation for testing ---

    private static class StubCommand implements Command {
        private final String id;
        private final Device device;
        private final ActionType actionType;
        private int orderIndex;
        private boolean executed = false;

        StubCommand(String id, Device device, ActionType actionType, int orderIndex) {
            this.id = id;
            this.device = device;
            this.actionType = actionType;
            this.orderIndex = orderIndex;
        }

        @Override
        public void execute() {
            executed = true;
        }

        @Override
        public Device getDevice() {
            return device;
        }

        @Override
        public ActionType getActionType() {
            return actionType;
        }

        @Override
        public String getID() {
            return id;
        }

        @Override
        public int getOrderIndex() {
            return orderIndex;
        }

        @Override
        public void setOrderIndex(int i) {
            this.orderIndex = i;
        }

        boolean wasExecuted() {
            return executed;
        }
    }

    // --- Helper ---

    private Lamp createLamp(String name) {
        Raum room = new Raum("Wohnzimmer");
        return new Lamp("lamp-1", name, room);
    }

    // --- Tests: Szenario anlegen ---

    public void testDefaultConstructorCreatesEmptyScenario() {
        Scenario scenario = new Scenario();
        assertNotNull(scenario);
        assertTrue(scenario.getCommands().isEmpty());
    }

    public void testConstructorWithNameAndDescription() {
        Scenario scenario = new Scenario("Abend", "Abendmodus aktivieren");
        assertEquals("Abend", scenario.getName());
        assertEquals("Abendmodus aktivieren", scenario.getDescription());
    }

    public void testConstructorSetsEmptyCommandList() {
        Scenario scenario = new Scenario("Morgen", "Morgenmodus");
        assertNotNull(scenario.getCommands());
        assertEquals(0, scenario.getCommands().size());
    }

    // --- Tests: Name und Beschreibung bearbeiten ---

    public void testSetName() {
        Scenario scenario = new Scenario();
        scenario.setName("Nacht");
        assertEquals("Nacht", scenario.getName());
    }

    public void testSetDescription() {
        Scenario scenario = new Scenario();
        scenario.setDescription("Nachtmodus: Lichter aus, Rollläden zu");
        assertEquals("Nachtmodus: Lichter aus, Rollläden zu", scenario.getDescription());
    }

    public void testUpdateNameAndDescription() {
        Scenario scenario = new Scenario("Alt", "Alte Beschreibung");
        scenario.setName("Neu");
        scenario.setDescription("Neue Beschreibung");
        assertEquals("Neu", scenario.getName());
        assertEquals("Neue Beschreibung", scenario.getDescription());
    }

    // --- Tests: Aktionen hinzufügen und abrufen ---

    public void testAddCommandIncreasesCommandCount() {
        Scenario scenario = new Scenario("Test", "");
        StubCommand cmd = new StubCommand("cmd-1", createLamp("Lampe"), ActionType.TURN_ON, 0);
        scenario.addCommand(cmd);
        assertEquals(1, scenario.getCommands().size());
    }

    public void testAddMultipleCommands() {
        Scenario scenario = new Scenario("Abend", "");
        Lamp lamp = createLamp("Wohnzimmerlampe");
        scenario.addCommand(new StubCommand("c1", lamp, ActionType.TURN_ON, 0));
        scenario.addCommand(new StubCommand("c2", lamp, ActionType.SET_BRIGHTNESS, 1));
        scenario.addCommand(new StubCommand("c3", lamp, ActionType.TURN_OFF, 2));
        assertEquals(3, scenario.getCommands().size());
    }

    public void testGetCommandsContainsAddedCommand() {
        Scenario scenario = new Scenario("Test", "");
        StubCommand cmd = new StubCommand("cmd-42", createLamp("Lampe"), ActionType.TURN_ON, 0);
        scenario.addCommand(cmd);
        assertTrue(scenario.getCommands().contains(cmd));
    }

    public void testSetCommands() {
        Scenario scenario = new Scenario("Test", "");
        List<Command> commands = new ArrayList<>();
        commands.add(new StubCommand("c1", createLamp("L1"), ActionType.TURN_ON, 0));
        commands.add(new StubCommand("c2", createLamp("L2"), ActionType.TURN_OFF, 1));
        scenario.setCommands(commands);
        assertEquals(2, scenario.getCommands().size());
    }

    // --- Tests: Ausführung von Szenarien ---

    public void testExecuteCallsAllCommands() {
        Scenario scenario = new Scenario("Abend", "");
        Lamp lamp = createLamp("Lampe");
        StubCommand cmd1 = new StubCommand("c1", lamp, ActionType.TURN_ON, 0);
        StubCommand cmd2 = new StubCommand("c2", lamp, ActionType.SET_BRIGHTNESS, 1);
        scenario.addCommand(cmd1);
        scenario.addCommand(cmd2);

        scenario.execute();

        assertTrue("cmd1 sollte ausgeführt worden sein", cmd1.wasExecuted());
        assertTrue("cmd2 sollte ausgeführt worden sein", cmd2.wasExecuted());
    }

    public void testExecuteEmptyScenarioDoesNotThrow() {
        Scenario scenario = new Scenario("Leer", "Keine Aktionen");
        try {
            scenario.execute();
        } catch (Exception e) {
            fail("execute() auf leerem Szenario darf keine Exception werfen: " + e.getMessage());
        }
    }

    public void testExecuteSingleCommand() {
        Scenario scenario = new Scenario("Einzel", "");
        StubCommand cmd = new StubCommand("c1", createLamp("Lampe"), ActionType.TURN_ON, 0);
        scenario.addCommand(cmd);
        scenario.execute();
        assertTrue(cmd.wasExecuted());
    }

    // --- Tests: Aktion ersetzen ---

    public void testReplaceCommandSwapsCorrectly() {
        Scenario scenario = new Scenario("Test", "");
        Lamp lamp = createLamp("Lampe");
        StubCommand oldCmd = new StubCommand("id-1", lamp, ActionType.TURN_ON, 0);
        StubCommand newCmd = new StubCommand("id-1", lamp, ActionType.TURN_OFF, 0);
        scenario.addCommand(oldCmd);

        scenario.replaceCommand(oldCmd, newCmd);

        assertFalse(scenario.getCommands().contains(oldCmd));
        assertTrue(scenario.getCommands().contains(newCmd));
    }

    public void testReplaceCommandKeepsListSize() {
        Scenario scenario = new Scenario("Test", "");
        Lamp lamp = createLamp("Lampe");
        StubCommand cmd1 = new StubCommand("id-1", lamp, ActionType.TURN_ON, 0);
        StubCommand cmd2 = new StubCommand("id-2", lamp, ActionType.SET_BRIGHTNESS, 1);
        StubCommand replacement = new StubCommand("id-1", lamp, ActionType.TURN_OFF, 0);
        scenario.addCommand(cmd1);
        scenario.addCommand(cmd2);

        scenario.replaceCommand(cmd1, replacement);

        assertEquals(2, scenario.getCommands().size());
    }

    public void testReplaceCommandWithUnknownIdLeavesListUnchanged() {
        Scenario scenario = new Scenario("Test", "");
        StubCommand existing = new StubCommand("known-id", createLamp("L"), ActionType.TURN_ON, 0);
        StubCommand unknown = new StubCommand("unknown-id", createLamp("L"), ActionType.TURN_OFF, 0);
        StubCommand replacement = new StubCommand("unknown-id", createLamp("L"), ActionType.TURN_ON, 0);
        scenario.addCommand(existing);

        scenario.replaceCommand(unknown, replacement);

        assertEquals(1, scenario.getCommands().size());
        assertTrue(scenario.getCommands().contains(existing));
    }

    // --- Tests: Reihenfolge der Aktionen ---

    public void testCommandOrderIndexIsPreserved() {
        Scenario scenario = new Scenario("Reihenfolge", "");
        Lamp lamp = createLamp("Lampe");
        StubCommand first = new StubCommand("c1", lamp, ActionType.TURN_ON, 0);
        StubCommand second = new StubCommand("c2", lamp, ActionType.SET_BRIGHTNESS, 1);
        StubCommand third = new StubCommand("c3", lamp, ActionType.TURN_OFF, 2);
        scenario.addCommand(first);
        scenario.addCommand(second);
        scenario.addCommand(third);

        List<Command> commands = scenario.getCommands();
        assertEquals(0, commands.get(0).getOrderIndex());
        assertEquals(1, commands.get(1).getOrderIndex());
        assertEquals(2, commands.get(2).getOrderIndex());
    }
}

package service;

import junit.framework.TestCase;
import model.scenario.Scenario;

public class ScenarioServiceTest extends TestCase {

    public void testGetScenariosInitiallyEmpty() {
        ScenarioService service = new ScenarioService();

        assertNotNull(service.getScenarios());
        assertEquals(0, service.getScenarios().size());
    }

    public void testAddScenario() {
        ScenarioService service = new ScenarioService();
        Scenario scenario = new Scenario("Abend", "Abend-Modus");

        service.addScenario(scenario);

        assertEquals(1, service.getScenarios().size());
        assertSame(scenario, service.getScenarios().get(0));
    }

    public void testAddMultipleScenarios() {
        ScenarioService service = new ScenarioService();
        service.addScenario(new Scenario("Abend", ""));
        service.addScenario(new Scenario("Morgen", ""));
        service.addScenario(new Scenario("Nacht", ""));

        assertEquals(3, service.getScenarios().size());
    }

    public void testDeleteScenario() {
        ScenarioService service = new ScenarioService();
        Scenario scenario = new Scenario("Abend", "");
        service.addScenario(scenario);

        service.deleteScenario(scenario);

        assertEquals(0, service.getScenarios().size());
    }

    public void testDeleteScenarioOnlyRemovesCorrectOne() {
        ScenarioService service = new ScenarioService();
        Scenario s1 = new Scenario("Abend", "");
        Scenario s2 = new Scenario("Morgen", "");
        service.addScenario(s1);
        service.addScenario(s2);

        service.deleteScenario(s1);

        assertEquals(1, service.getScenarios().size());
        assertSame(s2, service.getScenarios().get(0));
    }

    public void testDeleteNonExistentScenarioDoesNotThrow() {
        ScenarioService service = new ScenarioService();
        Scenario scenario = new Scenario("Test", "");

        service.deleteScenario(scenario);

        assertEquals(0, service.getScenarios().size());
    }
}

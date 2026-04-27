package controller;

import model.scenario.Scenario;

public class ScenarioController {
    public void executeScenario(Scenario scenario) {
        scenario.execute();
    }
}

package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.room.Raum;
import model.scenario.Scenario;

public class ScenarioService {
    private ObservableList<Scenario> scenarios = FXCollections.observableArrayList();

    public ObservableList<Scenario> getScenarios() {
        return scenarios;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public void deleteScenario(Scenario scenario) {
        scenarios.remove(scenario);
    }

}


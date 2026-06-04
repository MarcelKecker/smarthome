package service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.room.Raum;
import model.scenario.Scenario;

import java.util.List;

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

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = (ObservableList<Scenario>) scenarios;
    }

    public void clear() {
        this.scenarios.clear();
    }
}


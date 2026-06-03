package config;

import model.State;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeviceConfig {
    public String label;
    public double min;
    public double max;
    public double value;
    public State activeState;
    public State inactiveState;
    public Consumer<Integer> saveAction;
    public Supplier<String> logMessage;

    public DeviceConfig(
            String label,
            double min,
            double max,
            double value,
            State activeState,
            State inactiveState,
            Consumer<Integer> saveAction,
            Supplier<String> logMessage) {

        this.label = label;
        this.min = min;
        this.max = max;
        this.value = value;
        this.activeState = activeState;
        this.inactiveState = inactiveState;
        this.saveAction = saveAction;
        this.logMessage = logMessage;
    }
}

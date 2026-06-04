package model.action.impl;

import model.ActionType;
import model.State;
import model.action.BaseCommand;
import model.device.Device;
import model.device.impl.Heating;

import java.util.UUID;

public class SetTemperatureHeatingCommand extends BaseCommand {

    private final Heating heating;
    private final double temperature;

    public SetTemperatureHeatingCommand(Heating heating, double temperature, int orderIndex) {
        super(heating,ActionType.SET_TEMPERATURE, orderIndex);
        this.heating = heating;
        this.temperature = temperature;
    }

    @Override
    public void execute() {
        if (heating.getState() == State.TURNED_ON){
            heating.setTemperature(temperature);
        }
    }

    public double getTemperature() {
        return temperature;
    }
}
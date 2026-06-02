package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.State;
import model.device.Device;
import model.device.impl.Heating;

import java.util.UUID;

public class TurnOffHeatingCommand extends BaseCommand {

    private final Heating heating;

    public TurnOffHeatingCommand(Heating heating, int orderIndex) {
        super(heating, ActionType.TURN_OFF, orderIndex);
        this.heating = heating;
    }

    @Override
    public void execute() {
        heating.setState(State.TURNED_OFF);
    }
}
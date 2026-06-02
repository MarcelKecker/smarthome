package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.State;
import model.device.Device;
import model.device.impl.Heating;

import java.util.UUID;

public class TurnOnHeatingCommand extends BaseCommand {

    private final Heating heating;

    public TurnOnHeatingCommand(Heating heating, int orderIndex) {
        super(heating, ActionType.TURN_ON, orderIndex);
        this.heating = heating;
    }

    @Override
    public void execute() {
        heating.setState(State.TURNED_ON);
    }
}
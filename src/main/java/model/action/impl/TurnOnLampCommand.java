package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.State;
import model.device.Device;
import model.device.impl.Lamp;

import java.util.UUID;

public class TurnOnLampCommand extends BaseCommand {

    private final Lamp lamp;

    public TurnOnLampCommand(Lamp lamp, int orderIndex) {
        super(lamp, ActionType.TURN_ON, orderIndex);
        this.lamp = lamp;
    }

    @Override
    public void execute() {
        lamp.setState(State.TURNED_ON);
    }
}

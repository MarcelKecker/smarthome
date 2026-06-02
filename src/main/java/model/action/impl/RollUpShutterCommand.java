package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.State;
import model.device.Device;
import model.device.impl.Shutter;


public class RollUpShutterCommand extends BaseCommand {

    private final Shutter shutter;

    public RollUpShutterCommand(Shutter shutter, int orderIndex) {
        super(shutter, ActionType.ROLL_UP, orderIndex);
        this.shutter = shutter;
    }

    @Override
    public void execute() {
        shutter.setState(State.ROLLED_UP);
        shutter.setPosition(0);
    }
}
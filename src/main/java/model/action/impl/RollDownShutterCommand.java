package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.State;
import model.device.impl.Shutter;


public class RollDownShutterCommand extends BaseCommand {

    private final Shutter shutter;

    public RollDownShutterCommand(Shutter shutter, int orderIndex) {
        super(shutter, ActionType.ROLL_DOWN, orderIndex);
        this.shutter = shutter;
    }

    @Override
    public void execute() {
        shutter.setState(State.ROLLED_DOWN);
        shutter.setPosition(100);
    }
}
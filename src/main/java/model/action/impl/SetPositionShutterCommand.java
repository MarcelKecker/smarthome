package model.action.impl;

import model.ActionType;
import model.action.BaseCommand;
import model.device.Device;
import model.device.impl.Shutter;

import java.util.UUID;

public class SetPositionShutterCommand extends BaseCommand {

    private final Shutter shutter;
    private final int position;

    public SetPositionShutterCommand(Shutter shutter, int position, int orderIndex) {
        super(shutter, ActionType.SET_POSITION, orderIndex);
        this.shutter = shutter;
        this.position = position;
    }

    @Override
    public void execute() {
        shutter.setPosition(position);
    }

    public int getPosition() {
        return position;
    }
}
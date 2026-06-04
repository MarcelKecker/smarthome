package model.action.impl;

import model.ActionType;
import model.State;
import model.action.BaseCommand;
import model.device.Device;
import model.device.impl.Lamp;

import java.util.UUID;

public class SetBrightnessLampCommand extends BaseCommand {

    private final Lamp lamp;
    private final int brightness;

    public SetBrightnessLampCommand(Lamp lamp, int brightness, int orderIndex) {
        super(lamp, ActionType.SET_BRIGHTNESS, orderIndex);
        this.lamp = lamp;
        this.brightness = brightness;
    }

    @Override
    public void execute() {
        if (lamp.getState() == State.TURNED_ON) {
            lamp.setBrightness(brightness);
        }
    }

    public int getBrightness() {
        return brightness;
    }
}

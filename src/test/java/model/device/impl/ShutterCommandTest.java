package model.action.impl;

import junit.framework.TestCase;
import model.State;
import model.device.impl.Shutter;
import model.room.Raum;

public class ShutterCommandTest extends TestCase {

    public void testRollUpShutterCommand() {
        Raum raum = new Raum("Schlafzimmer");
        Shutter shutter = new Shutter("S1", "Rollladen", raum);
        shutter.setState(State.ROLLED_DOWN);
        shutter.setPosition(100);

        assertEquals(State.ROLLED_DOWN, shutter.getState());
        assertEquals(100, shutter.getPosition());

        RollUpShutterCommand command = new RollUpShutterCommand(shutter, 0);
        command.execute();

        assertEquals(State.ROLLED_UP, shutter.getState());
        assertEquals(0, shutter.getPosition());
    }

    public void testRollDownShutterCommand() {
        Raum raum = new Raum("Schlafzimmer");
        Shutter shutter = new Shutter("S1", "Rollladen", raum);
        shutter.setState(State.ROLLED_UP);
        shutter.setPosition(0);

        assertEquals(State.ROLLED_UP, shutter.getState());
        assertEquals(0, shutter.getPosition());

        RollDownShutterCommand command = new RollDownShutterCommand(shutter, 0);
        command.execute();

        assertEquals(State.ROLLED_DOWN, shutter.getState());
        assertEquals(100, shutter.getPosition());
    }

    public void testSetPositionShutterCommand() {
        Raum raum = new Raum("Schlafzimmer");
        Shutter shutter = new Shutter("S1", "Rollladen", raum);

        SetPositionShutterCommand command = new SetPositionShutterCommand(shutter, 75, 0);
        command.execute();

        assertEquals(75, shutter.getPosition());
    }
}

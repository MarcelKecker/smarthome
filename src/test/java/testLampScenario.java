import model.device.DeviceAction;
import model.device.impl.Lamp;
import model.scenario.DeviceCommand;
import model.scenario.Scenario;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testLampScenario {
    @Test
    public void testLampScenario() {
        Lamp lamp = new Lamp("1", "Wohnzimmer");

        DeviceAction action = new DeviceAction("SET_BRIGHTNESS", 30);
        DeviceCommand command = new DeviceCommand(lamp, action);

        Scenario scenario = new Scenario("Abend", "Test");
        scenario.addCommand(command);

        scenario.execute();

        assertEquals("An (30%)", lamp.getState());
    }
}

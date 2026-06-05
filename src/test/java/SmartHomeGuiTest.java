import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.ActionType;
import model.State;
import model.action.impl.TurnOnLampCommand;
import model.device.impl.Lamp;
import model.room.Raum;
import model.scenario.Scenario;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

public class SmartHomeGuiTest extends ApplicationTest {

    private SmartHomeApp app;
    private Lamp testLamp;
    private Scenario testScenario;

    @Override
    public void start(Stage stage) throws Exception {
        app = new SmartHomeApp();
        app.start(stage);

        // Daten-Setup [cite: 46, 47, 48]
        Raum room = new Raum("Testzimmer");
        app.getRoomService().addRoom(room);

        testLamp = new Lamp(UUID.randomUUID().toString(), "Testlampe", room);
        app.getDeviceService().addDevice(testLamp);

        testScenario = new Scenario("Abend-Test", "Automatisch erstelltes Testszenario");

        // ANGEPASST: Konstruktor-Signatur an die Struktur der CommandFactory angepasst
        // Parameter: context (null), device (testLamp), actionType (TURN_ON), orderIndex (0)
        testScenario.getCommands().add(new TurnOnLampCommand(null, testLamp, ActionType.TURN_ON, 0));

        app.getScenarioService().addScenario(testScenario);
    }

    @Test
    public void testSzenarioAnlegenViaGui() {
        // 1. Klicke auf den Navigationsbutton
        clickOn("🎬 Szenarien");

        // Warte kurz, bis die Tabelle sichtbar und geladen ist
        verifyThat(".table-view", isVisible());

        TableView<?> table = lookup(".table-view").queryAs(TableView.class);
        int initialCount = table.getItems().size();

        // 2. Formular öffnen [cite: 90]
        clickOn("Neu");

        // 3. Textfeld fokussieren und beschreiben [cite: 98]
        clickOn(".text-field").write("GUI-Testszenario");

        // 4. Erstellen bestätigen
        clickOn("Erstellen");

        // 5. Dem UI-Thread einen Moment Zeit geben, die Tabelle zu aktualisieren
        WaitForAsyncUtils.waitForFxEvents();

        // Überprüfung
        assertEquals(initialCount + 1, table.getItems().size());
    }

    @Test
    public void testSzenarioAusfuehrenAendertGeraetezustand() {
        assertEquals(State.TURNED_OFF, testLamp.getState());

        clickOn("🎬 Szenarien");

        // Sicherstellen, dass der Eintrag in der Tabelle geklickt werden kann [cite: 89]
        clickOn("Abend-Test");
        clickOn("Ausführen"); // [cite: 93]

        // Warten, da Command-Ausführungen oft in separaten Threads oder leicht verzögert laufen
        WaitForAsyncUtils.sleep(200, TimeUnit.MILLISECONDS);
        WaitForAsyncUtils.waitForFxEvents();

        // Überprüft die funktionale Anforderung der Zustandsänderung in der GUI [cite: 51, 115]
        assertEquals(State.TURNED_ON, testLamp.getState());
    }
}
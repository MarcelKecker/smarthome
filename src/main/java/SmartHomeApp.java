import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.device.Device;

public class SmartHomeApp extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // Top Toolbar
        ToolBar toolBar = new ToolBar(
                new Button("Neu"),
                new Button("Speichern"),
                new Button("Laden"),
                new Separator(),
                new Button("Szenario ausführen")
        );
        root.setTop(toolBar);

        // Left Navigation
        TreeItem<String> rootItem = new TreeItem<>("Smart Home");
        TreeItem<String> rooms = new TreeItem<>("Räume");
        TreeItem<String> devices = new TreeItem<>("Geräte");
        TreeItem<String> scenarios = new TreeItem<>("Szenarien");

        rootItem.getChildren().addAll(rooms, devices, scenarios);
        TreeView<String> navigation = new TreeView<>(rootItem);
        navigation.setPrefWidth(200);
        root.setLeft(navigation);

        // Center Content
        TabPane mainTabs = new TabPane();

        // Device Table
        TableView<Device> deviceTable = new TableView<>();
        TableColumn<Device, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );

        deviceTable.getColumns().add(nameCol);
        
        deviceTable.getColumns().add(new TableColumn<>("Name"));
        deviceTable.getColumns().add(new TableColumn<>("Typ"));
        deviceTable.getColumns().add(new TableColumn<>("Raum"));
        deviceTable.getColumns().add(new TableColumn<>("Zustand"));

        Tab deviceTab = new Tab("Geräte", deviceTable);

        // Scenario Table
        TableView<String> scenarioTable = new TableView<>();
        scenarioTable.getColumns().add(new TableColumn<>("Name"));
        scenarioTable.getColumns().add(new TableColumn<>("Beschreibung"));
        scenarioTable.getColumns().add(new TableColumn<>("Aktionen"));

        Tab scenarioTab = new Tab("Szenarien", scenarioTable);

        mainTabs.getTabs().addAll(deviceTab, scenarioTab);
        root.setCenter(mainTabs);

        // Right Log Panel
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefWidth(250);
        logArea.setPromptText("Protokoll...");
        root.setRight(logArea);

        // Bottom Status Bar
        Label statusLabel = new Label("Bereit");
        HBox statusBar = new HBox(statusLabel);
        statusBar.setPadding(new Insets(5));
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Smart Home Szenario Editor");
        stage.setScene(scene);
        stage.show();

        navigation.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.getValue().equals("Geräte")) {
                mainTabs.getSelectionModel().select(deviceTab);
            }
        });
        /*
        executeButton.setOnAction(e -> {
            scenarioController.executeScenario(selectedScenario);
            logArea.appendText("Szenario ausgeführt\\n");
        });

         */
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }
}


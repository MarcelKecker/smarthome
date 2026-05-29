
import atlantafx.base.theme.PrimerLight;
import factory.DeviceFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.device.Device;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;
import model.scenario.Command;
import model.scenario.DeviceCommand;
import model.scenario.Scenario;
import service.DeviceService;
import service.RoomService;
import service.ScenarioService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SmartHomeApp extends Application {

    private BorderPane root;

    private RoomService roomService = new RoomService();

    private DeviceService deviceService = new DeviceService();

    private ScenarioService scenarioService = new ScenarioService();

    @Override
    public void start(Stage stage) {
        root = new BorderPane();

        // ===== Sidebar =====
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");

        Label title = new Label("Smart Home");
        title.getStyleClass().add("title");

        Button roomsBtn = new Button("🏠 Räume");
        roomsBtn.setOnAction(e -> {
            openRooms();
        });

        Button devicesBtn = new Button("🔌 Geräte");
        devicesBtn.setOnAction(e -> {
            openDevices();
        });
        Button scenariosBtn = new Button("🎬 Szenarien");
        scenariosBtn.setOnAction(e -> {
            openScenarios();
        });

        roomsBtn.setMaxWidth(Double.MAX_VALUE);
        devicesBtn.setMaxWidth(Double.MAX_VALUE);
        scenariosBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(title, roomsBtn, devicesBtn, scenariosBtn);

        // ===== Top Bar =====
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("topBar");

        Label header = new Label("Dashboard");
        header.getStyleClass().add("header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button neuBtn = new Button("\uD83D\uDDD2 Neu");

        neuBtn.setOnAction(e -> {
            System.out.println("Neu geklickt");
        });

        Button openBtn = new Button("📂 Öffnen");

        openBtn.setOnAction(e -> {
            System.out.println("Öffnen geklickt");
        });

        Button saveBtn = new Button("💾 Speichern");

        saveBtn.setOnAction(e -> {
            System.out.println("Speichern geklickt");
        });
        //Dropdown für Szenarien?
        Button runScenario = new Button("▶ Szenario ausführen");

        runScenario.setOnAction(e -> {
            System.out.println("Szenario geklickt");
        });

        topBar.getChildren().addAll(header, neuBtn, openBtn, saveBtn, spacer, runScenario);

        // ===== Center Dashboard =====
        GridPane dashboard = new GridPane();
        dashboard.setPadding(new Insets(20));
        dashboard.setHgap(20);
        dashboard.setVgap(20);

        //Beipsiele:
        dashboard.add(createCard("Licht", "Wohnzimmer", "30%"), 0, 0);
        dashboard.add(createCard("Heizung", "Bad", "22°C"), 1, 0);
        dashboard.add(createCard("Rollladen", "Schlafzimmer", "0%"), 2, 0);

        // ===== Log Panel =====
        VBox logPanel = new VBox(10);
        logPanel.setPadding(new Insets(15));
        logPanel.setPrefWidth(250);

        Label logTitle = new Label("Aktivität");
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        logPanel.getChildren().addAll(logTitle, logArea);

        root.setLeft(sidebar);
        root.setTop(topBar);
        root.setCenter(dashboard);
        root.setRight(logPanel);

        Scene scene = new Scene(root, 1200, 700);

        // Atlantafx Theme
        scene.getStylesheets().add(new PrimerLight().getUserAgentStylesheet());

        // Custom styling
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Smart Home");
        stage.setScene(scene);
        stage.show();
    }

    private void openDevices() {
        VBox devicesView = new VBox(10);
        devicesView.setPadding(new Insets(20));

        Label title = new Label("Geräte");
        title.getStyleClass().add("header");
        TableView<Device> table = new TableView<>();
        TableColumn<Device, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );

        TableColumn<Device, String> typeCol = new TableColumn<>("Typ");
        typeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType())
        );

        TableColumn<Device, String> roomCol = new TableColumn<>("Raum");
        roomCol.setCellValueFactory(data -> {
                    Raum room = data.getValue().getRoom();

                    String roomName = (room != null)
                            ? room.getName()
                            : "Nicht zugeordnet";
                    return new SimpleStringProperty(roomName);
                }
        );

        TableColumn<Device, String> stateCol = new TableColumn<>("Zustand");
        stateCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getState())
        );

        table.getColumns().addAll(nameCol, typeCol, roomCol, stateCol);
        table.setItems(deviceService.getDevices());

        Button addDevice = new Button("Neu");
        addDevice.setOnAction(e -> {
            openAddDevice();
        });

        Button viewDevice = new Button("Anzeigen");
        viewDevice.setOnAction(e -> {
            Device gerät = table.getSelectionModel().getSelectedItem();
            if (null != gerät){
                openDeviceEditor(false, gerät);
            }

        });


        Button changeDevice = new Button("Bearbeiten");
        changeDevice.setOnAction(e -> {
            Device gerät = table.getSelectionModel().getSelectedItem();
            if (null != gerät){
                openDeviceEditor(true, gerät);
            }
        });

        Button deleteDevice = new Button("Löschen");
        deleteDevice.setOnAction(e -> {});

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        buttonBar.getChildren().addAll(addDevice, viewDevice, changeDevice, deleteDevice);

        devicesView.getChildren().addAll(title, table, buttonBar);

        root.setCenter(devicesView);
    }

    private void openDeviceEditor(boolean edit, Device device) {

        VBox deviceEditor = new VBox(15);
        deviceEditor.setPadding(new Insets(20));

        Label title = new Label("Gerät");
        title.getStyleClass().add("header");

        final boolean[] isEditing = {edit};

        Label idLabel = new Label("ID: " + device.getId());

        Label nameLabel = new Label("Name:");

        TextField nameField = new TextField(device.getName());
        nameField.setEditable(isEditing[0]);

        HBox nameBar = new HBox(10, nameLabel, nameField);

        Label typeLabel = new Label("Typ: " + device.getType());

        Label roomLabel = new Label("Raum:");

        ComboBox<Raum> roomBox = new ComboBox<>();
        roomBox.getItems().addAll(roomService.getAllRooms());

        roomBox.setValue(device.getRoom());
        roomBox.setDisable(!isEditing[0]);

        HBox roomBar = new HBox(10, roomLabel, roomBox);

        // ===== Zustand =====
        Label stateLabel = new Label("Zustand:");

        ToggleButton stateToggle = new ToggleButton();
        if (Objects.equals(device.getState(), "Oben")){
            stateToggle.setText("Oben");
        } else if (device.getState().startsWith("Unten")){
            stateToggle.setText("Unten");
        } else if (Objects.equals(device.getState(), "Aus")){
            stateToggle.setText("Aus");
        } else if (device.getState().startsWith("An")){
            stateToggle.setText("An");
        } else {
            throw new RuntimeException("State nicht bekannt: " +  device.getState());
        }

        stateToggle.setDisable(!isEditing[0]);
        HBox stateBar = new HBox(10, stateLabel, stateToggle);

        deviceEditor.getChildren().addAll(
                title,
                idLabel,
                typeLabel,
                nameBar,
                roomBar,
                stateBar );


            // ===== Buttons =====
        Button backBtn = new Button("Zurück");
        backBtn.setOnAction(e -> openDevices());

        Button editBtn = new Button();

        editBtn.setText(isEditing[0]
                ? "Speichern"
                : "Bearbeiten"
        );

        //gerät- spezifisch
        if (device instanceof Lamp lamp) {

            Label brightnessLabel = new Label("Helligkeit:");

            Slider brightnessSlider =
                    new Slider(0, 100, lamp.getBrightness());
            brightnessSlider.setShowTickLabels(true);
            brightnessSlider.setDisable(true);

            HBox brightnessBar = new HBox(
                    10,
                    brightnessLabel,
                    brightnessSlider
            );
            deviceEditor.getChildren().addAll(brightnessBar);
            stateToggle.setOnAction(e -> {
                if (stateToggle.getText().equals("Aus")) {
                    stateToggle.setText("An");
                    brightnessSlider.setDisable(false);
                } else {
                    stateToggle.setText("Aus");
                    brightnessSlider.setDisable(true);
                }
            });
            editBtn.setOnAction(e -> {
                if (isEditing[0]) {

                    device.setName(nameField.getText());
                    device.setRoom(roomBox.getValue());
                    device.setState(stateToggle.getText());
                    lamp.setBrightness((int) brightnessSlider.getValue());

                    isEditing[0] = false;

                    nameField.setEditable(false);
                    roomBox.setDisable(true);
                    stateToggle.setDisable(true);
                    brightnessSlider.setDisable(true);

                    editBtn.setText("Bearbeiten");

                } else {

                    isEditing[0] = true;

                    nameField.setEditable(true);
                    roomBox.setDisable(false);
                    stateToggle.setDisable(false);
                    if (stateToggle.getText().equals("An")){
                        brightnessSlider.setDisable(false);
                    }

                    editBtn.setText("Speichern");
                }
            });
        } else if (device instanceof Heating heating) {

            Label brightnessLabel = new Label("Temperatur: ");

            TextField temperaturField = new TextField();
            temperaturField.setText(String.valueOf(heating.getTemperature()));
            temperaturField.setEditable(false);

            HBox temperatureBar = new HBox(
                    10,
                    brightnessLabel,
                    temperaturField
            );
            deviceEditor.getChildren().addAll(temperatureBar);
            stateToggle.setOnAction(e -> {
                if (stateToggle.getText().equals("Aus")) {
                    stateToggle.setText("An");
                    temperaturField.setDisable(false);
                } else {
                    stateToggle.setText("Aus");
                    temperaturField.setDisable(true);
                }
            });
            editBtn.setOnAction(e -> {
                        if (isEditing[0]) {

                            device.setName(nameField.getText());
                            device.setRoom(roomBox.getValue());
                            device.setState(stateToggle.getText());

                            isEditing[0] = false;

                            nameField.setEditable(false);
                            roomBox.setDisable(true);
                            stateToggle.setDisable(true);
                            temperaturField.setDisable(true);

                            editBtn.setText("Bearbeiten");

                        } else {

                            isEditing[0] = true;

                            nameField.setEditable(true);
                            roomBox.setDisable(false);
                            stateToggle.setDisable(false);
                            if (stateToggle.getText().equals("An")){
                                temperaturField.setDisable(false);
                            }

                            editBtn.setText("Speichern");
                        }
            });
        } else if (device instanceof Shutter shutter) {

            Label positionLabel = new Label("Positon:");

            Slider positionSlider =
                    new Slider(0, 100, shutter.getRolledDownPercent());
            positionSlider.setShowTickLabels(true);
            positionSlider.setDisable(true);

            HBox brightnessBar = new HBox(
                    10,
                    positionLabel,
                    positionSlider
            );
            deviceEditor.getChildren().addAll(brightnessBar);
            stateToggle.setOnAction(e -> {
                if (stateToggle.getText().equals("Oben")) {
                    stateToggle.setText("Unten");
                    positionSlider.setDisable(false);
                } else {
                    stateToggle.setText("Oben");
                    positionSlider.setDisable(true);
                }
            });
            editBtn.setOnAction(e -> {
                if (isEditing[0]) {

                    device.setName(nameField.getText());
                    device.setRoom(roomBox.getValue());
                    device.setState(stateToggle.getText());
                    shutter.setRolledDownPercent((int) positionSlider.getValue());

                    isEditing[0] = false;

                    nameField.setEditable(false);
                    roomBox.setDisable(true);
                    stateToggle.setDisable(true);
                    positionSlider.setDisable(true);

                    editBtn.setText("Bearbeiten");

                } else {

                    isEditing[0] = true;

                    nameField.setEditable(true);
                    roomBox.setDisable(false);
                    stateToggle.setDisable(false);
                    if (stateToggle.getText().equals("Unten")){
                        positionSlider.setDisable(false);
                    }

                    editBtn.setText("Speichern");
                }
            });
        }


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(
                10,
                backBtn,
                spacer,
                editBtn
        );
        deviceEditor.getChildren().addAll(buttonBar);
        root.setCenter(deviceEditor);
    }
    private void openAddDevice() {
        Dialog<Device> dialog = new Dialog<>();
        dialog.setTitle("Neues Gerät");

        ButtonType saveButtonType = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);


        // Form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));



        TextField nameField = new TextField();
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Lampe", "Heizung", "Rollladen");

        Runnable validate = () -> {
            boolean invalid =
                    nameField.getText().trim().isEmpty()
                            || typeBox.getValue() == null;

            saveButton.setDisable(invalid);
        };
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validate.run());

        typeBox.valueProperty().addListener((obs, oldVal, newVal) -> validate.run());

        ComboBox<Raum> roomBox = new ComboBox<>();
        roomBox.getItems().addAll(roomService.getAllRooms());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Typ:"), 0, 1);
        grid.add(typeBox, 1, 1);
        grid.add(new Label("Raum:"), 0, 2);
        grid.add(roomBox, 1, 2);


        dialog.getDialogPane().setContent(grid);

        // Ergebnis erzeugen
        dialog.setResultConverter(button -> {
            if (button == saveButtonType) {
                String name = nameField.getText();
                String type = typeBox.getValue();
                Raum room = roomBox.getValue();

                return DeviceFactory.create(type, UUID.randomUUID().toString(), name, room);
            }
            return null;
        });

        Optional<Device> result = dialog.showAndWait();

        result.ifPresent(device -> {
            deviceService.addDevice(device);
        });
    }

    private void openRooms() {
        VBox roomsView = new VBox(10);
        roomsView.setPadding(new Insets(20));

        Label title = new Label("Räume");
        title.getStyleClass().add("header");

        ListView<Raum> roomList = new ListView<>();
        roomList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Raum room, boolean empty) {
                super.updateItem(room, empty);

                if (empty || room == null) {
                    setText(null);
                } else {
                    setText(room.getName());
                }
            }
        });
        roomList.getItems().addAll(roomService.getAllRooms());

        Button addRoom = new Button("Neu");
        addRoom.setOnAction(e -> {
            openAddRoom();
        });

        Button viewRoom = new Button("Anzeigen");
        viewRoom.setOnAction(e -> {
            Raum room = roomList.getSelectionModel().getSelectedItem();
            if (room != null) {
                openRoomEditor(false, room);
            }
           //todo log
        });

        Button changeRoom = new Button("Bearbeiten");
        changeRoom.setOnAction(e -> {
            Raum room = roomList.getSelectionModel().getSelectedItem();
            if (room != null) {
                openRoomEditor(true, room);
            }
            //log
        });

        Button deleteRoom = new Button("Löschen");
        deleteRoom.setOnAction(e -> {
            Raum room = roomList.getSelectionModel().getSelectedItem();
            if (room != null) {
                roomService.deleteRoom(room);
                openRooms();
            }
        });

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        buttonBar.getChildren().addAll(addRoom, viewRoom, changeRoom, deleteRoom);

        roomsView.getChildren().addAll(title, roomList, buttonBar);

        root.setCenter(roomsView);
    }

    private void openAddRoom() {
        VBox newRoom = new VBox(10);
        newRoom.setPadding(new Insets(20));

        Label title = new Label("Räume");
        title.getStyleClass().add("header");

        TextField roomName = new TextField();
        roomName.setPromptText("Name");

        Button addRoomBtn = new Button("Erstellen");
        addRoomBtn.setOnAction(f -> {
            if (roomName.getText().isEmpty()) {
                TextField noRoomName = new TextField("Bitte einen Namen eingeben");
                newRoom.getChildren().add(noRoomName);
                noRoomName.setEditable(false);
                roomName.requestFocus();
                //log
            }
            else{
                roomService.addRoom(new Raum(roomName.getText()));
                openRooms();
            }
            //else if ob der name bereits verwendet wird?
        });
        Button backtoView = new Button("Zurück");
        backtoView.setOnAction(f -> {
            openRooms();
        });

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonBar.getChildren().addAll(backtoView, spacer, addRoomBtn);

        newRoom.getChildren().addAll(title, roomName, buttonBar);
        root.setCenter(newRoom);
    }

    private void openRoomEditor(boolean edit, Raum room) {
        VBox roomEditor = new VBox(10);
        roomEditor.setPadding(new Insets(20));

        Label title = new Label("Raum");
        title.getStyleClass().add("header");

        Label nameLabel = new Label("Raumname:");
        TextField nameField = new TextField(room.getName());

        // Zustand
        final boolean[] isEditing = {edit};

        nameField.setEditable(isEditing[0]);

        HBox raumnamebar = new HBox(10);
        raumnamebar.setPadding(new Insets(20));
        raumnamebar.getChildren().addAll(nameLabel, nameField);

        Label idLabel = new Label("Id: " + room.getId());
        idLabel.setPadding(new Insets(20));

        Button backtoView = new Button("Zurück");
        backtoView.setOnAction(f -> openRooms());

        Button editBtn = new Button();

        editBtn.setText(isEditing[0] ? "Speichern" : "Bearbeiten");

        editBtn.setOnAction(e -> {
            if (isEditing[0]) {
                room.setName(nameField.getText());

                isEditing[0] = false;
                nameField.setEditable(false);
                editBtn.setText("Bearbeiten");

            } else {
                isEditing[0] = true;
                nameField.setEditable(true);
                editBtn.setText("Speichern");
            }
        });

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBar.getChildren().addAll(backtoView, spacer, editBtn);

        roomEditor.getChildren().addAll(title, idLabel, raumnamebar, buttonBar);

        root.setCenter(roomEditor);
    }

    private void openScenarios(){
        VBox scenariosView = new VBox(10);
        scenariosView.setPadding(new Insets(20));

        Label title = new Label("Szenarien");
        title.getStyleClass().add("header");

        TableView<Scenario> table = new TableView<>();
        TableColumn<Scenario, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName())
        );

        TableColumn<Scenario, String> descCol = new TableColumn<>("Kurzbeschreibung");
        descCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescription())
        );

        TableColumn<Scenario, String> actionsCol = new TableColumn<>("Anzahl enthaltener Aktionen");
        actionsCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCommands().size()))
        );

        table.getColumns().addAll(nameCol, descCol, actionsCol);
        table.setItems(scenarioService.getScenarios());

        Button addScenario = new Button("Neu");
        addScenario.setOnAction(e -> {openAddScenario();});

        Button viewScenario = new Button("Anzeigen");
        viewScenario.setOnAction(e -> {
            Scenario scenario = table.getSelectionModel().getSelectedItem();
            if (null != scenario){
                openScenarioEditor(false, scenario);
            }
            //log
        });

        Button changeScenario = new Button("Bearbeiten");
        changeScenario.setOnAction(e -> {
            Scenario scenario = table.getSelectionModel().getSelectedItem();
            if (null != scenario){
                openScenarioEditor(true, scenario);
            }
            //log
        });

        Button deleteScenario = new Button("Löschen");
        deleteScenario.setOnAction(e -> {
            Scenario scenario = table.getSelectionModel().getSelectedItem();
            if (null != scenario){
                scenarioService.deleteScenario(scenario);
                openScenarios();
            }
            //log
        });

        Button runScenario = new Button("Ausführen");
        runScenario.setOnAction(e -> {
            Scenario scenario = table.getSelectionModel().getSelectedItem();
            if (null != scenario){
                scenario.execute();
                openScenarios();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        buttonBar.getChildren().addAll(addScenario, viewScenario, changeScenario, deleteScenario, spacer, runScenario);

        scenariosView.getChildren().addAll(title, table, buttonBar);
        root.setCenter(scenariosView);

    }

    private void openAddScenario() {
        VBox newScenario = new VBox(10);
        newScenario.setPadding(new Insets(20));

        Label title = new Label("Szenarien");
        title.getStyleClass().add("header");

        TextField scenarioName = new TextField();
        scenarioName.setPromptText("Name");

        TextField scenarioDescription = new TextField();
        scenarioDescription.setPromptText("Beschreibung");

        Button addScenarioBtn = new Button("Erstellen");
        addScenarioBtn.setOnAction(f -> {
            if (scenarioName.getText().isEmpty()) {
                TextField errorField = new TextField("Bitte einen Namen eingeben");
                newScenario.getChildren().add(errorField);
                errorField.setEditable(false);
                scenarioName.requestFocus();
                //log
            }
            else{
                scenarioService.addScenario(new Scenario(scenarioName.getText(), scenarioDescription.getText()));
                openScenarios();
            }
            //else if ob der name bereits verwendet wird?
        });
        Button backtoView = new Button("Zurück");
        backtoView.setOnAction(f -> {
            openScenarios();
        });

        HBox buttonBar = new HBox(10);
        buttonBar.setPadding(new Insets(10));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonBar.getChildren().addAll(backtoView, spacer, addScenarioBtn);

        newScenario.getChildren().addAll(title, scenarioName, scenarioDescription, buttonBar);
        root.setCenter(newScenario);
    }

    private void openScenarioEditor(boolean edit, Scenario scenario) {

        VBox scenarioEditor = new VBox(15);
        scenarioEditor.setPadding(new Insets(20));

        Label title = new Label("Szenario");
        title.getStyleClass().add("header");

        final boolean[] isEditing = {edit};

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(scenario.getName());
        nameField.setEditable(isEditing[0]);
        HBox nameBar = new HBox(10, nameLabel, nameField);

        Label descriptionLabel = new Label("Beschreibung:");
        TextField descriptionField = new TextField(scenario.getDescription());
        descriptionField.setEditable(isEditing[0]);
        HBox descriptionBar = new HBox(10, descriptionLabel, descriptionField);
        // ===== Liste Aktionen =====

        ListView<Command> commandList = new ListView<>();
        commandList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Command command, boolean empty) {
                super.updateItem(command, empty);

                if (empty || command == null) {
                    setText(null);
                } else {
                    setText(command.toString());
                }
            }
        });
        commandList.getItems().addAll(scenario.getCommands());


        scenarioEditor.getChildren().addAll(
                title,
                nameBar,
                descriptionBar,
                commandList);
        // ===== Buttons =====
        Button backBtn = new Button("Zurück");
        backBtn.setOnAction(e -> openScenarios());

        Button addCommandBtn = new Button("Aktion hinzufügen");
        addCommandBtn.setOnAction(e -> {
            //scenario.addCommand();
            openScenarioEditor(edit, scenario);
        });

        Button changeCommandBtn = new Button("Aktion ändern");
        changeCommandBtn.setOnAction(e -> {

            openScenarioEditor(edit, scenario);
        });

        Button deleteCommandBtn = new Button("Aktion löschen");
        deleteCommandBtn.setOnAction(e -> {

            openScenarioEditor(edit, scenario);
        });

        Button editBtn = new Button();

        editBtn.setText(isEditing[0]
                ? "Speichern"
                : "Bearbeiten"
        );

        editBtn.setOnAction(e -> {
            if (isEditing[0]) {
                scenario.setName(nameField.getText());
                scenario.setDescription(descriptionField.getText());

                isEditing[0] = false;
                nameField.setEditable(false);
                descriptionField.setEditable(false);
                editBtn.setText("Bearbeiten");

            } else {
                isEditing[0] = true;
                nameField.setEditable(true);
                descriptionField.setEditable(true);
                editBtn.setText("Speichern");
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(
                10,
                backBtn,
                spacer,
                editBtn
        );
        scenarioEditor.getChildren().addAll(buttonBar);
        root.setCenter(scenarioEditor);
    }


    private VBox createCard(String type, String room, String value) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setPrefSize(200, 120);
        card.getStyleClass().add("card");

        Label typeLabel = new Label(type);
        typeLabel.getStyleClass().add("card-title");

        Label roomLabel = new Label(room);
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");

        card.getChildren().addAll(typeLabel, roomLabel, valueLabel);
        return card;
    }

    public static void main(String[] args) {
        launch();
    }
}

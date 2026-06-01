
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
import model.device.DeviceAction;
import model.device.impl.Heating;
import model.device.impl.Lamp;
import model.device.impl.Shutter;
import model.room.Raum;
import model.scenario.DeviceCommand;
import model.scenario.Scenario;
import service.DeviceCommandService;
import service.DeviceService;
import service.RoomService;
import service.ScenarioService;

import java.util.*;

public class SmartHomeApp extends Application {

    private BorderPane root;

    private RoomService roomService = new RoomService();

    private DeviceService deviceService = new DeviceService();

    private DeviceCommandService deviceCommandService = new DeviceCommandService();


    private ScenarioService scenarioService = new ScenarioService();

    private Runnable currentRefreshAction;

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

        ComboBox<Scenario> scenarioSelect = new ComboBox<>();
        scenarioSelect.setPrefWidth(200);

// LIVE-BINDING (wichtig!)
        scenarioSelect.setItems(scenarioService.getScenarios());

// Anzeige schön machen
        scenarioSelect.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Scenario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        scenarioSelect.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Scenario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Szenario auswählen" : item.getName());
            }
        });

// BUTTON wieder hinzufügen
        Button runScenario = new Button("▶ Ausführen");

        runScenario.setOnAction(e -> {

            Scenario selected = scenarioSelect.getValue();
            if (selected != null) {
                selected.execute();
                refreshCurrentView();
            }

        });

        topBar.getChildren().addAll(
                header,
                neuBtn,
                openBtn,
                saveBtn,
                spacer,
                scenarioSelect,
                runScenario
        );
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

    private void refreshCurrentView() {
        if (currentRefreshAction != null) {
            currentRefreshAction.run();
        }
    }

    private void openDevices() {
        currentRefreshAction = this::openDevices;
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
        currentRefreshAction = () -> openDeviceEditor(edit, device);
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
                    new Slider(0, 100, shutter.getPosition());
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
                    shutter.setPosition((int) positionSlider.getValue());

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
        currentRefreshAction = this::openAddDevice;
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
        currentRefreshAction = this::openRooms;
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
        currentRefreshAction = this::openAddRoom;
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
        currentRefreshAction = () -> openRoomEditor(edit, room);
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
        currentRefreshAction = this::openScenarios;
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
                if (scenario.getCommands().get(0).getDevice().getType().equals("Heizung")){
                    Heating heating = (Heating) scenario.getCommands().get(0).getDevice();
                    System.out.println(heating.toString());
                    System.out.println(heating.getState());
                    System.out.println(heating.getTemperature());
                    scenario.execute();
                    System.out.println(heating.toString());
                    System.out.println(heating.getState());
                    System.out.println(heating.getTemperature());
                } else if (scenario.getCommands().get(0).getDevice().getType().equals("Rollladen")){
                    Shutter shutter = (Shutter) scenario.getCommands().get(0).getDevice();
                    System.out.println(shutter.toString());
                    System.out.println(shutter.getState());
                    System.out.println(shutter.getPosition());
                    scenario.execute();
                    System.out.println(shutter.toString());
                    System.out.println(shutter.getState());
                    System.out.println(shutter.getPosition());
                } else if (scenario.getCommands().get(0).getDevice().getType().equals("Lampe")){
                    Lamp lamp = (Lamp) scenario.getCommands().get(0).getDevice();
                    System.out.println(lamp.toString());
                    System.out.println(lamp.getState());
                    System.out.println(lamp.getBrightness());
                    scenario.execute();
                    System.out.println(lamp.toString());
                    System.out.println(lamp.getState());
                    System.out.println(lamp.getBrightness());
                }

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
        currentRefreshAction = this::openAddScenario;
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
        currentRefreshAction = () -> openScenarioEditor(edit, scenario);
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

        TableView<DeviceCommand> tableDeviceCommands = new TableView<>();
        TableColumn<DeviceCommand, String> orderCol = new TableColumn<>("Reihenfolge");
        orderCol.setCellValueFactory(data ->
                new SimpleStringProperty(Integer.toString(data.getValue().getOrderIndex()))
        );

        TableColumn<DeviceCommand, String> deviceCommandCol = new TableColumn<>("Aktion");
        deviceCommandCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().toString())
        );

        tableDeviceCommands.getColumns().addAll(orderCol, deviceCommandCol);
        tableDeviceCommands.setItems(scenario.getCommands());


        scenarioEditor.getChildren().addAll(
                title,
                nameBar,
                descriptionBar,
                tableDeviceCommands);
        // ===== Buttons =====
        Button backBtn = new Button("Zurück");
        backBtn.setOnAction(e -> openScenarios());

        Button addCommandBtn = new Button("Aktion hinzufügen");
        addCommandBtn.setOnAction(e -> {
            openAddCommand(scenario);
            openScenarioEditor(isEditing[0], scenario);
        });
        addCommandBtn.setDisable(!isEditing[0]);

        Button viewCommandBtn = new Button("Aktion anzeigen");
        viewCommandBtn.setOnAction(e -> {
            DeviceCommand deviceCommand = tableDeviceCommands.getSelectionModel().getSelectedItem();
            if (null != deviceCommand){
                openCommandEditor(deviceCommand, false, scenario, edit);
            }
        });
        viewCommandBtn.setDisable(!isEditing[0]);

        Button changeCommandBtn = new Button("Aktion ändern");
        changeCommandBtn.setOnAction(e -> {
            DeviceCommand deviceCommand = tableDeviceCommands.getSelectionModel().getSelectedItem();
            if (null != deviceCommand){
                openCommandEditor(deviceCommand, true, scenario, edit);
            }
        });
        changeCommandBtn.setDisable(!isEditing[0]);

        Button deleteCommandBtn = new Button("Aktion löschen");
        deleteCommandBtn.setOnAction(e -> {
            DeviceCommand deviceCommand = tableDeviceCommands.getSelectionModel().getSelectedItem();
            if (deviceCommand != null) {
                for (DeviceCommand deviceCommand2 : tableDeviceCommands.getItems()) {
                    if (deviceCommand2.getOrderIndex() > deviceCommand.getOrderIndex()) {
                        deviceCommand2.setOrderIndex(deviceCommand2.getOrderIndex() - 1);
                    }
                }
                scenario.getCommands().remove(deviceCommand);
                openScenarioEditor(isEditing[0], scenario);
            }
        });
        deleteCommandBtn.setDisable(!isEditing[0]);

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
                addCommandBtn.setDisable(true);
                changeCommandBtn.setDisable(true);
                viewCommandBtn.setDisable(true);
                deleteCommandBtn.setDisable(true);
                editBtn.setText("Bearbeiten");

            } else {
                isEditing[0] = true;
                nameField.setEditable(true);
                descriptionField.setEditable(true);
                addCommandBtn.setDisable(false);
                changeCommandBtn.setDisable(false);
                viewCommandBtn.setDisable(false);
                deleteCommandBtn.setDisable(false);
                editBtn.setText("Speichern");
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(
                10,
                backBtn,
                addCommandBtn,
                viewCommandBtn,
                changeCommandBtn,
                deleteCommandBtn,
                spacer,
                editBtn
        );
        scenarioEditor.getChildren().addAll(buttonBar);
        root.setCenter(scenarioEditor);
    }

    private void openAddCommand(Scenario scenario) {
        currentRefreshAction = () -> openAddCommand(scenario);

        Dialog<DeviceCommand> dialog = new Dialog<>();
        dialog.setTitle("Aktion hinzufügen");

        ButtonType saveButtonType = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // ===== FORM =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // ===== DEVICE =====
        ComboBox<Device> deviceBox = new ComboBox<>();
        deviceBox.getItems().addAll(deviceService.getDevices());

        // ===== ACTION TYPE =====
        ComboBox<String> actionTypeBox = new ComboBox<>();

        deviceBox.valueProperty().addListener((obs, oldDevice, newDevice) -> {

            actionTypeBox.getItems().clear();

            if (newDevice == null) return;

            switch (newDevice.getType()) {

                case "Heizung" -> actionTypeBox.getItems().addAll(
                        "Ausschalten",
                        "Anschalten",
                        "Temperatur setzen"
                );

                case "Lampe" -> actionTypeBox.getItems().addAll(
                        "Ausschalten",
                        "Anschalten",
                        "Helligkeit setzen"
                );

                case "Rollladen" -> actionTypeBox.getItems().addAll(
                        "Herunterfahren",
                        "Hochfahren",
                        "Position setzen"
                );
            }
        });

        // ===== VALUE =====
        TextField valueBox = new TextField();

        // ===== VALUE TYPE LOGIC =====
        enum ValueType { NONE, INT, DOUBLE }

        java.util.function.Function<String, ValueType> getValueType = (actionType) -> {
            if (actionType == null) return ValueType.NONE;

            return switch (actionType) {
                case "Temperatur setzen" -> ValueType.DOUBLE;
                case "Helligkeit setzen",
                     "Position setzen" -> ValueType.INT;
                default -> ValueType.NONE;
            };
        };

        Runnable applyValueState = () -> {

            ValueType type = getValueType.apply(actionTypeBox.getValue());
            boolean requiresValue = type != ValueType.NONE;

            valueBox.setDisable(!requiresValue);

            if (!requiresValue) {
                valueBox.clear();
            }
        };

        // ===== VALIDATION (FIXED INT/DOUBLE) =====
        Runnable validate = () -> {

            Device device = deviceBox.getValue();
            String actionType = actionTypeBox.getValue();

            ValueType type = getValueType.apply(actionType);

            String text = valueBox.getText() == null ? "" : valueBox.getText().trim();

            boolean valueValid = switch (type) {

                case NONE -> true;

                case INT -> text.matches("^\\d+$");

                case DOUBLE -> text.matches("^\\d+(\\.\\d+)?$");
            };

            boolean invalid =
                    device == null
                            || actionType == null
                            || !valueValid;

            saveButton.setDisable(invalid);
        };

        // ===== LISTENERS =====
        actionTypeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            applyValueState.run();
            validate.run();
        });

        deviceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            validate.run();
        });

        valueBox.textProperty().addListener((obs, oldVal, newVal) -> {
            validate.run();
        });

        // ===== UI =====
        grid.add(new Label("Gerät:"), 0, 0);
        grid.add(deviceBox, 1, 0);

        grid.add(new Label("Aktionstyp:"), 0, 1);
        grid.add(actionTypeBox, 1, 1);

        grid.add(new Label("Wert:"), 0, 2);
        grid.add(valueBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // ===== RESULT =====
        dialog.setResultConverter(button -> {

            if (button == saveButtonType) {

                Device device = deviceBox.getValue();
                String actionType = actionTypeBox.getValue();

                DeviceAction deviceAction = new DeviceAction(
                        actionType,
                        valueBox.getText()
                );

                int orderIndex = scenario.getCommands().size();

                DeviceCommand command = new DeviceCommand(
                        device,
                        deviceAction,
                        orderIndex
                );

                scenario.getCommands().add(command);
            }

            return null;
        });

        Optional<DeviceCommand> result = dialog.showAndWait();

        result.ifPresent(deviceCommand -> {
            scenario.getCommands().add(deviceCommand);
        });
    }

    private boolean needsValue(String actionType) {
        if (actionType == null) {
            return false;
        }
        return switch (actionType) {
            case "Temperatur setzen",
                 "Helligkeit setzen",
                 "Position setzen" -> true;
            default -> false;
        };
    }

    private List<String> getActionsForDevice(Device device) {
        return switch (device.getType()) {
            case "Heizung" -> List.of(
                    "Ausschalten",
                    "Anschalten",
                    "Temperatur setzen"
            );

            case "Lampe" -> List.of(
                    "Ausschalten",
                    "Anschalten",
                    "Helligkeit setzen"
            );

            case "Rollladen" -> List.of(
                    "Hochfahren",
                    "Herunterfahren",
                    "Position setzen"
            );

            default -> List.of();
        };
    }

    private void openCommandEditor(DeviceCommand deviceCommand,
                                   boolean edit,
                                   Scenario scenario,
                                   boolean editScenario) {
        currentRefreshAction = () -> openCommandEditor(
                deviceCommand,
                edit,
                scenario,
                editScenario
        );

        VBox editor = new VBox(15);
        editor.setPadding(new Insets(20));

        Label title = new Label("Aktion");
        title.getStyleClass().add("header");

        final boolean[] isEditing = {edit};

        Label deviceLabel = new Label(
                "Gerät: " + deviceCommand.getDevice().getName()
        );

        // ===== VALUE TYPES =====
        enum ValueType { NONE, INT, DOUBLE }

        Runnable validate;

        // ===== ACTION TYPE =====
        Label actionTypeLabel = new Label("Aktionstyp:");

        ComboBox<String> actionTypeBox = new ComboBox<>();
        actionTypeBox.getItems().addAll(
                getActionsForDevice(deviceCommand.getDevice())
        );

        actionTypeBox.setValue(deviceCommand.getAction().getActionType());
        actionTypeBox.setDisable(!isEditing[0]);

        HBox actionTypeBar = new HBox(10, actionTypeLabel, actionTypeBox);

        // ===== VALUE =====
        Label valueLabel = new Label("Wert:");

        TextField valueField = new TextField(
                deviceCommand.getAction().getValue() == null
                        ? ""
                        : String.valueOf(deviceCommand.getAction().getValue())
        );

        valueField.setDisable(!isEditing[0]);

        HBox valueBar = new HBox(10, valueLabel, valueField);

        editor.getChildren().addAll(
                title,
                deviceLabel,
                actionTypeBar,
                valueBar
        );

        // ===== BACK BUTTON =====
        Button backBtn = new Button("Zurück");
        backBtn.setOnAction(e -> openScenarioEditor(editScenario, scenario));

        // ===== VALUE TYPE MAPPING =====
        java.util.function.Function<String, ValueType> getValueType = (actionType) -> {
            if (actionType == null) return ValueType.NONE;

            return switch (actionType) {
                case "Temperatur setzen" -> ValueType.DOUBLE;
                case "Helligkeit setzen",
                     "Position setzen" -> ValueType.INT;
                default -> ValueType.NONE;
            };
        };

        // ===== APPLY VALUE STATE =====
        Runnable applyValueState = () -> {
            ValueType type = getValueType.apply(actionTypeBox.getValue());
            boolean requiresValue = type != ValueType.NONE;

            valueField.setDisable(!isEditing[0] || !requiresValue);

            if (!requiresValue && isEditing[0]) {
                valueField.clear();
            }
        };

        // ===== EDIT BUTTON =====
        Button editBtn = new Button(isEditing[0] ? "Speichern" : "Bearbeiten");

        editBtn.setOnAction(e -> {

            if (isEditing[0]) {

                // ===== SAVE =====
                deviceCommand.getAction().setActionType(actionTypeBox.getValue());
                deviceCommand.getAction().setValue(valueField.getText());

                isEditing[0] = false;

                actionTypeBox.setDisable(true);
                valueField.setDisable(true);

                editBtn.setText("Bearbeiten");

            } else {

                // ===== EDIT MODE =====
                isEditing[0] = true;

                actionTypeBox.setDisable(false);
                applyValueState.run();

                editBtn.setText("Speichern");
            }
        });

        // ===== VALIDATION =====
        validate = () -> {

            String actionType = actionTypeBox.getValue();
            ValueType type = getValueType.apply(actionType);

            String text = valueField.getText() == null ? "" : valueField.getText().trim();

            boolean valueValid = switch (type) {

                case NONE -> true;

                case INT -> text.matches("^\\d+$");

                case DOUBLE -> text.matches("^\\d+(\\.\\d+)?$");
            };

            boolean invalid =
                    actionType == null
                            || !valueValid;
            editBtn.setDisable(invalid);
        };

        // ===== LISTENERS =====
        actionTypeBox.valueProperty().addListener((obs, o, n) -> {
            applyValueState.run();
            validate.run();
        });

        valueField.textProperty().addListener((obs, o, n) -> {
            validate.run();
        });


        // ===== INITIAL STATE =====
        applyValueState.run();
        validate.run();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBar = new HBox(
                10,
                backBtn,
                spacer,
                editBtn
        );

        editor.getChildren().add(buttonBar);

        root.setCenter(editor);
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

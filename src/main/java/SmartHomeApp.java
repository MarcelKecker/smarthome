
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SmartHomeApp extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        // ===== Sidebar =====
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(220);
        sidebar.getStyleClass().add("sidebar");

        Label title = new Label("Smart Home");
        title.getStyleClass().add("title");

        Button roomsBtn = new Button("🏠 Räume");
        Button devicesBtn = new Button("🔌 Geräte");
        Button scenariosBtn = new Button("🎬 Szenarien");

        roomsBtn.setMaxWidth(Double.MAX_VALUE);
        devicesBtn.setMaxWidth(Double.MAX_VALUE);
        scenariosBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(title, roomsBtn, devicesBtn, scenariosBtn);

        // ===== Top Bar =====
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label header = new Label("Dashboard");
        header.getStyleClass().add("header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button runScenario = new Button("▶ Szenario ausführen");

        topBar.getChildren().addAll(header, spacer, runScenario);

        // ===== Center Dashboard =====
        GridPane dashboard = new GridPane();
        dashboard.setPadding(new Insets(20));
        dashboard.setHgap(20);
        dashboard.setVgap(20);

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

        stage.setTitle("Smart Home Pro");
        stage.setScene(scene);
        stage.show();
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

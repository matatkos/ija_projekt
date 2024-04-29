package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.logging.Logger;

import model.RobotParams;
import util.Vector2D;

public class EditorDialog extends Application {
    private static final Logger LOGGER = Logger.getLogger(EditorDialog.class.getName());

    private TextField robotDirectionInput, detectionRangeInput, turningAngleInput;
    private ComboBox<String> modeBox, turningDirectionInput;
    private EditorMapView mapView;
    private Button btnSave, btnUpdateRobotParams;
    private RadioButton btnAddObstacle, btnAddRobot;
    private ToggleGroup placementToggle;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Map Editor");

        mapView = new EditorMapView();

        btnSave = new Button("Save");
        btnAddObstacle = new RadioButton("Add Obstacle");
        btnAddRobot = new RadioButton("Add Robot");
        btnAddRobot.setSelected(true);

        modeBox = new ComboBox<>();
        modeBox.getItems().addAll("Placement Mode", "Editing Mode");
        modeBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mapView.setMode(newVal);
        });

        turningDirectionInput = new ComboBox<>();
        turningDirectionInput.getItems().addAll("Left", "Right");

        robotDirectionInput = new TextField();
        detectionRangeInput = new TextField();
        turningAngleInput = new TextField();

        setToolTips();

        btnUpdateRobotParams = new Button("Update Robot Parameters");
        btnUpdateRobotParams.setOnAction(event -> updateRobotParams());

        btnSave.setOnAction(event -> exportMapToFile());

        placementToggle = new ToggleGroup();
        btnAddRobot.setToggleGroup(placementToggle);
        btnAddObstacle.setToggleGroup(placementToggle);
        btnAddRobot.setUserData("Robot");
        btnAddObstacle.setUserData("Obstacle");

        placementToggle.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                mapView.setPlacementMode(newToggle.getUserData().toString());
            }
        });

        GridPane robotParamsLayout = createRobotParamsLayout();

        VBox modeLayout = new VBox(10, new Label("Select Mode:"), modeBox, btnAddRobot, btnAddObstacle);
        modeLayout.setPadding(new Insets(10));

        VBox rightPanel = new VBox(10, robotParamsLayout, btnSave);
        rightPanel.setPadding(new Insets(10));

        HBox layout = new HBox(10, modeLayout, mapView, rightPanel);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 1200, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setToolTips() {
        robotDirectionInput.setTooltip(new Tooltip("Enter the direction of the robot here (degrees)"));
        detectionRangeInput.setTooltip(new Tooltip("Enter the detection range of the robot here (meters)"));
        turningAngleInput.setTooltip(new Tooltip("Enter the turning angle of the robot here (degrees)"));
    }

    private GridPane createRobotParamsLayout() {
        GridPane robotParamsLayout = new GridPane();
        robotParamsLayout.setVgap(10);
        robotParamsLayout.setHgap(10);
        robotParamsLayout.add(new Label("Direction:"), 0, 0);
        robotParamsLayout.add(robotDirectionInput, 1, 0);
        robotParamsLayout.add(new Label("Detection Range:"), 0, 1);
        robotParamsLayout.add(detectionRangeInput, 1, 1);
        robotParamsLayout.add(new Label("Turning Direction:"), 0, 2);
        robotParamsLayout.add(turningDirectionInput, 1, 2);
        robotParamsLayout.add(new Label("Turning Angle:"), 0, 3);
        robotParamsLayout.add(turningAngleInput, 1, 3);
        robotParamsLayout.add(btnUpdateRobotParams, 1, 4);
        return robotParamsLayout;
    }

    private void exportMapToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Map data saved."); // Placeholder for actual map data saving
                LOGGER.info("Map data exported to file.");
            } catch (IOException e) {
                LOGGER.severe("Failed to save map data: " + e.getMessage());
            }
        }
    }

    private void updateRobotParams() {
        if (mapView.getSelectedRobot() != null) {
            double directionDegrees = Double.parseDouble(robotDirectionInput.getText());
            Vector2D direction = new Vector2D(Math.cos(Math.toRadians(directionDegrees)),
                    Math.sin(Math.toRadians(directionDegrees)));
            double detectionRange = Double.parseDouble(detectionRangeInput.getText());
            double turningAngle = Double.parseDouble(turningAngleInput.getText());
            boolean turningDirection = turningDirectionInput.getValue().equals("Right");

            RobotParams params = new RobotParams(direction, detectionRange, turningAngle, turningDirection);
            mapView.getSelectedRobot().updateParameters(params);
            LOGGER.info("Robot parameters updated: Direction = " + directionDegrees + " degrees, Detection Range = " +
                    detectionRange + ", Turning Angle = " + turningAngle + ", Turning Direction = " + (turningDirection ? "Right" : "Left"));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

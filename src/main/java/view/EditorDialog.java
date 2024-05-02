package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.*;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import model.Obstacle;
import model.Robot;
import model.RobotParams;
import util.Vector2D;

public class EditorDialog extends Application {
    private static final Logger LOGGER = Logger.getLogger(EditorDialog.class.getName());
    DecimalFormat f = new DecimalFormat("##.00");

    private TextField robotDirectionInput, detectionRangeInput, turningAngleInput;
    private ComboBox<String> turningDirectionInput, modeBox;
    private EditorMapView mapView;
    private Button btnSave, btnUpdateRobotParams;
    private RadioButton btnAddObstacle, btnAddRobot;
    private ToggleGroup placementToggle;
    private Robot currentSelectedRobot; // Currently selected robot

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Map Editor");

        mapView = new EditorMapView();  // Assume this class is properly implemented

        btnSave = new Button("Save");
        btnAddObstacle = new RadioButton("Add Obstacle");
        btnAddRobot = new RadioButton("Add Robot");
        btnAddRobot.setSelected(true);

        modeBox = new ComboBox<>();
        modeBox.getItems().addAll("Placement Mode", "Editing Mode");
        modeBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mapView.setMode(newVal);
            if(newVal.equals("Placement Mode")) {
                btnAddRobot.setSelected(true);
                btnAddObstacle.setSelected(false);
            } else {
                btnAddObstacle.setSelected(true);
                btnAddRobot.setSelected(false);
            }
        });
        modeBox.setValue("Placement Mode");

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

        mapView.setOnRobotClickListener(this::selectRobot);

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

        // Set up robot click listener
        mapView.setOnRobotClickListener(this::selectRobot);
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
        robotParamsLayout.add(btnUpdateRobotParams, 0, 4, 2, 1);
        return robotParamsLayout;
    }

    private void exportMapToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Export robots
                writer.write("Robots:\n");
                for (Circle circle : mapView.robots.keySet()) {
                    Robot robot = mapView.robots.get(circle);
                    Vector2D position = robot.getPos();
                    RobotParams params = new RobotParams(robot.getDir(), robot.getDetectionRange(), robot.getRotationAngle(), robot.getTurningDirection());
                    String turningDirectionStr = robot.getTurningDirection() ? "right" : "left";
                    writer.write(f.format(position.getX()) + ";" + f.format(position.getY())
                            + ";" + params.direction + ";" + params.rotationAngle + ";" + params.detectionRange + ";" + turningDirectionStr);
                    writer.newLine();
                }

                // Export obstacles
                writer.write("\nObstacles:\n");
                for (Rectangle rectangle : mapView.obstacles.keySet()) {
                    Obstacle obstacle = mapView.obstacles.get(rectangle);
                    Vector2D position = obstacle.getPos();
                    writer.write(f.format(position.getX()) + ";" + f.format(position.getY()));
                    writer.newLine();
                }

                LOGGER.info("Map data exported to file.");
            } catch (IOException e) {
                LOGGER.severe("Failed to save map data: " + e.getMessage());
            }
        }
    }

    private void updateRobotParams() {
        if (currentSelectedRobot != null) {
            double direction = Double.parseDouble(robotDirectionInput.getText());
            double detectionRange = Double.parseDouble(detectionRangeInput.getText());
            double turningAngle = Double.parseDouble(turningAngleInput.getText());
            boolean turningDirection = turningDirectionInput.getValue().equals("Right");

            currentSelectedRobot.setDirection(direction);
            currentSelectedRobot.setDetectionRange(detectionRange);
            currentSelectedRobot.setRotationAngle(turningAngle);
            currentSelectedRobot.setTurningDirection(turningDirection);

            LOGGER.info("Robot parameters updated: Direction = " + direction + " degrees, Detection Range = " +
                    detectionRange + ", Turning Angle = " + turningAngle + ", Turning Direction = " + (turningDirection ? "Right" : "Left"));
        }
    }

    private void selectRobot(Robot robot) {
        currentSelectedRobot = robot;
        updateInputFields();
    }

    private void updateInputFields() {
        if (currentSelectedRobot != null) {
            robotDirectionInput.setText(String.format("%.2f", currentSelectedRobot.getDir()));
            detectionRangeInput.setText(String.format("%.2f", currentSelectedRobot.getDetectionRange()));
            turningAngleInput.setText(String.format("%.2f", currentSelectedRobot.getRotationAngle()));
            turningDirectionInput.setValue(currentSelectedRobot.getTurningDirection() ? "Right" : "Left");
        } else {
            robotDirectionInput.clear();
            detectionRangeInput.clear();
            turningAngleInput.clear();
            turningDirectionInput.getSelectionModel().clearSelection();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

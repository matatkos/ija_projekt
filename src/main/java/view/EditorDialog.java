package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.*;

import model.RobotParams;
import util.Vector2D;

public class EditorDialog extends Application {
    private TextField robotDirectionInput, detectionRangeInput, turningAngleInput;
    private ComboBox<String> modeBox, turningDirectionInput;
    private EditorMapView mapView; // Placeholder for your custom component
    private Button btnSave, btnUpdateRobotParams;
    private RadioButton btnAddObstacle, btnAddRobot;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Map Editor");

        mapView = new EditorMapView(); // Placeholder for your custom component

        btnSave = new Button("Save");
        btnAddObstacle = new RadioButton("Add Obstacle");
        btnAddRobot = new RadioButton("Add Robot");
        btnAddRobot.setSelected(true);

        modeBox = new ComboBox<>();
        modeBox.getItems().addAll("Placement Mode", "Editing Mode");

        turningDirectionInput = new ComboBox<>();
        turningDirectionInput.getItems().addAll("Left", "Right");

        robotDirectionInput = new TextField();
        detectionRangeInput = new TextField();
        turningAngleInput = new TextField();

        robotDirectionInput.setTooltip(new Tooltip("Enter the direction of the robot here(degrees)"));
        detectionRangeInput.setTooltip(new Tooltip("Enter the detection range of the robot here"));
        turningAngleInput.setTooltip(new Tooltip("Enter the turning angle of the robot here(degrees)"));

        btnUpdateRobotParams = new Button("Update Robot Parameters");
        btnUpdateRobotParams.setOnAction(event -> updateRobotParams());

        btnSave.setOnAction(event -> exportMapToFile());

        // Robot parameters layout
        GridPane robotParamsLayout = new GridPane();
        robotParamsLayout.setVgap(10);
        robotParamsLayout.setHgap(10);
        robotParamsLayout.add(new Label("Direction"), 0, 0);
        robotParamsLayout.add(robotDirectionInput, 1, 0);
        robotParamsLayout.add(new Label("Detection Range"), 0, 1);
        robotParamsLayout.add(detectionRangeInput, 1, 1);
        robotParamsLayout.add(new Label("Turning Direction"), 0, 2);
        robotParamsLayout.add(turningDirectionInput, 1, 2);
        robotParamsLayout.add(new Label("Turning Angle"), 0, 3);
        robotParamsLayout.add(turningAngleInput, 1, 3);
        robotParamsLayout.add(btnUpdateRobotParams, 0, 4, 2, 1);

        VBox modeLayout = new VBox(10, btnAddRobot, btnAddObstacle, modeBox);
        modeLayout.setPadding(new Insets(10));

        HBox layout = new HBox(10, modeLayout, mapView, btnSave);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 1200, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void exportMapToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Map File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Example format for saving data, customize as needed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRobotParams() {
        if (mapView.getSelectedRobot() != null) {
            Vector2D direction = new Vector2D(Math.cos(Math.toRadians(Double.parseDouble(robotDirectionInput.getText()))),
                    Math.sin(Math.toRadians(Double.parseDouble(robotDirectionInput.getText()))));
            double detectionRange = Double.parseDouble(detectionRangeInput.getText());
            double turningAngle = Double.parseDouble(turningAngleInput.getText());
            boolean turningDirection = turningDirectionInput.getValue().equals("Right");

            RobotParams params = new RobotParams(direction, detectionRange, turningAngle, turningDirection);
            mapView.getSelectedRobot().updateParameters(params);
            System.out.println("Robot parameters updated");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


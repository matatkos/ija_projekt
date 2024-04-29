package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10); // 10 is the spacing between elements in the VBox

        Button btnSimulation = new Button("Start Simulation");
        Button btnEditor = new Button("Open Map Editor");

        btnSimulation.setOnAction(event -> onSimulationClicked(primaryStage));
        btnEditor.setOnAction(event -> onEditorClicked(primaryStage));

        layout.getChildren().addAll(btnSimulation, btnEditor);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onSimulationClicked(Stage parentStage) {
        SimulationDialog simulationDialog = new SimulationDialog();
        Stage stage = new Stage();
        try {
            simulationDialog.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onEditorClicked(Stage parentStage) {
        EditorDialog editorDialog = new EditorDialog();
        Stage stage = new Stage();
        try {
            editorDialog.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

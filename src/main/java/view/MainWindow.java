package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button btnSimulation = new Button("Start Simulation");
        btnSimulation.setPrefSize(120, 60);
        Button btnEditor = new Button("Open Map Editor");
        btnEditor.setPrefSize(120, 60);

        layout.getChildren().addAll(btnSimulation, btnEditor);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Connections
        btnSimulation.setOnAction(event -> onSimulationClicked());
        btnEditor.setOnAction(event -> onEditorClicked());
    }

    private void onSimulationClicked() {
        SimulationDialog dialog = new SimulationDialog();
        dialog.show();
    }

    private void onEditorClicked() {
        EditorDialog dialog = new EditorDialog();
        dialog.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

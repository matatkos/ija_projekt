package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50, 20, 50, 20));

        Button btnSimulation = new Button("Start Simulation");
        Button btnEditor = new Button("Open Map Editor");


        btnSimulation.setMinWidth(200);
        btnEditor.setMinWidth(200);

        btnSimulation.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        btnEditor.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");

        btnSimulation.setOnAction(event -> onSimulationClicked(primaryStage));
        btnEditor.setOnAction(event -> onEditorClicked(primaryStage));

        layout.getChildren().addAll(btnSimulation, btnEditor);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onSimulationClicked(Stage parentStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Map File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"), "data"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Map files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(parentStage);
        if (file != null) {
            String mapFilePath = file.getAbsolutePath();
            SimulationDialog simulationDialog = new SimulationDialog(mapFilePath);
            Stage stage = new Stage();
            try {
                simulationDialog.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

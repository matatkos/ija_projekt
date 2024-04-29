import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import view.MainWindow;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainWindow mainWindow = new MainWindow(); // Assume MainWindow is properly set up as a JavaFX Application
        mainWindow.start(new Stage()); // This assumes MainWindow extends Application and overrides the start method.

        // If you have a SimulationController, you would typically initialize it here
        // SimulationController simulationController = new SimulationController();
    }
}

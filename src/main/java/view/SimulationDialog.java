package view;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Dialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.DialogPane;

public class SimulationDialog extends Dialog<Void> {

    public SimulationDialog() {
        setTitle("Simulation");
        setResizable(false);

        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(createContent());
    }

    private Pane createContent() {
        Pane contentPane = new Pane();
        contentPane.setPrefSize(800, 600);

        javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(50, 50, 50, 50);
        rect.setFill(Color.BLUE);

        contentPane.getChildren().add(rect);

        return contentPane;
    }
}
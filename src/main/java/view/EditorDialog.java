package view;

import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EditorDialog extends Dialog<Void> {

    public EditorDialog() {
        setTitle("Map Editor");
        setResizable(false);

        VBox layout = new VBox();
        TextArea textArea = new TextArea();
        Button btnSave = new Button("Save Map");

        layout.getChildren().addAll(textArea, btnSave);
        getDialogPane().setContent(layout); // Set the content to the VBox layout

        btnSave.setOnAction(event -> saveMap(textArea));
    }

    private void saveMap(TextArea textArea) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("map.txt"))) {
            writer.println(textArea.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



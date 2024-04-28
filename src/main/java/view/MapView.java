package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapView extends Pane {

    public MapView() {
        drawMap();
    }

    private void drawMap() {
        // Define the rectangle (box) dimensions and position
        double x = 50.0;
        double y = 50.0;
        double width = 100.0;
        double height = 100.0;

        // Create a rectangle shape
        Rectangle rectangle = new Rectangle(x, y, width, height);

        // Set the stroke (pen) color
        rectangle.setStroke(Color.BLACK);

        // Set the fill (brush) color
        rectangle.setFill(Color.BLUE);

        // Add the rectangle to the MapView (this Pane)
        getChildren().add(rectangle);
    }
}

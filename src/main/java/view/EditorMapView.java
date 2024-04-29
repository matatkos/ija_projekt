package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import model.Robot;
import model.Obstacle;
import util.Vector2D;

public class EditorMapView extends Pane {
    private static final Logger LOGGER = Logger.getLogger(EditorMapView.class.getName());
    private String placementMode = "Robot";
    private String mode = "Placement Mode";
    private Map<Circle, Robot> robots = new HashMap<>();
    private Map<Rectangle, Obstacle> obstacles = new HashMap<>();
    private Object selectedItem;
    private double initialMouseX, initialMouseY;

    public EditorMapView() {
        setPrefSize(800, 600); // This sets the preferred size of the editor map view
        this.setOnMousePressed(this::handleMousePressed);
        this.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (mode.equals("Placement Mode")) {
            if (placementMode.equals("Robot")) {
                LOGGER.info("Added a robot to position " + x + " " + y);
                addRobotAtPosition(x, y);
            } else if (placementMode.equals("Obstacle")) {
                LOGGER.info("Added an obstacle to position " + x + " " + y);
                addObstacleAtPosition(x, y);
            }
        } else if (mode.equals("Editing Mode")) {
            selectItem(event.getTarget());
            initialMouseX = event.getX();
            initialMouseY = event.getY();
        }
    }

    public void setPlacementMode(String placementMode) {
        this.placementMode = placementMode;
    }

    private void handleMouseReleased(MouseEvent event) {
        if (selectedItem != null && mode.equals("Editing Mode")) {
            double dx = event.getX() - initialMouseX;
            double dy = event.getY() - initialMouseY;
            moveItem(dx, dy);
        }
    }

    private void addRobotAtPosition(double x, double y) {
        if (!checkOverlap(x, y)) {
            Circle circle = new Circle(10); // Radius of 10
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setFill(Color.BLUE);
            getChildren().add(circle);
            robots.put(circle, new Robot(new Vector2D(x, y))); // Assuming a constructor accepting a Vector2D
        }
    }

    private void addObstacleAtPosition(double x, double y) {
        if (!checkOverlap(x, y)) {
            Rectangle rectangle = new Rectangle(x - 10, y - 10, 20, 20); // Width and height of 20
            rectangle.setFill(Color.GRAY);
            getChildren().add(rectangle);
            obstacles.put(rectangle, new Obstacle(new Vector2D(x, y))); // Assuming a constructor accepting a Vector2D
        }
    }

    private boolean checkOverlap(double x, double y) {
        // This method checks for overlap with existing robots or obstacles
        for (Circle circle : robots.keySet()) {
            if (circle.contains(x, y)) {
                return true;
            }
        }
        for (Rectangle rectangle : obstacles.keySet()) {
            if (rectangle.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    private void selectItem(Object item) {
        if (item instanceof Circle || item instanceof Rectangle) {
            if (selectedItem != null) {
                setColorBack(selectedItem);
            }
            selectedItem = item;
            setItemColor(item, Color.GREEN);
        }
    }

    private void moveItem(double dx, double dy) {
        if (selectedItem instanceof Circle) {
            Circle circle = (Circle) selectedItem;
            circle.setCenterX(circle.getCenterX() + dx);
            circle.setCenterY(circle.getCenterY() + dy);
            LOGGER.info("Moved robot to new position: x=" + circle.getCenterX() + ", y=" + circle.getCenterY());
        } else if (selectedItem instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) selectedItem;
            rectangle.setX(rectangle.getX() + dx);
            rectangle.setY(rectangle.getY() + dy);
            LOGGER.info("Moved obstacle to new position: x=" + rectangle.getX() + ", y=" + rectangle.getY());
        }
    }

    private void setItemColor(Object item, Color color) {
        if (item instanceof Circle) {
            ((Circle) item).setFill(color);
        } else if (item instanceof Rectangle) {
            ((Rectangle) item).setFill(color);
        }
    }

    private void setColorBack(Object item) {
        if (item instanceof Circle) {
            Circle circle = (Circle) item;
            circle.setFill(Color.BLUE);
        } else if (item instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) item;
            rectangle.setFill(Color.GRAY);
        }
    }

    public void setEditMode(String mode) {
        this.placementMode = mode;
    }

    public void setMode(String m) {
        this.mode = m;
    }

    public Robot getSelectedRobot() {
        if (selectedItem instanceof Circle) {
            return robots.get(selectedItem);
        }
        return null;
    }
}

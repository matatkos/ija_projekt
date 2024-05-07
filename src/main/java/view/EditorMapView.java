package view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.function.Consumer;

import model.Robot;
import model.Obstacle;
import model.RobotParams;
import util.Vector2D;

public class EditorMapView extends Pane {
    private static final Logger LOGGER = Logger.getLogger(EditorMapView.class.getName());
    private DecimalFormat f = new DecimalFormat("##.00");

    private String placementMode = "Robot";
    private String mode = "Placement Mode";

    private EditorDialog editorDialog;
    Map<Circle, Robot> robots = new HashMap<>();
    Map<Rectangle, Obstacle> obstacles = new HashMap<>();
    private Object selectedItem;
    private double initialMouseX, initialMouseY;
    private Consumer<Robot> robotClickListener; // Listener for robot clicks

    public void setEditorDialog(EditorDialog editorDialog) {
        this.editorDialog = editorDialog;
    }


    public EditorMapView() {
        setPrefSize(800, 800);
        this.setOnMousePressed(this::handleMousePressed);
        this.setOnMouseReleased(this::handleMouseReleased);
    }

    private void handleMousePressed(MouseEvent event) {
        double x = event.getX();
        double y = event.getY();

        if (mode.equals("Placement Mode")) {
            if (placementMode.equals("Robot")) {
                LOGGER.info("Attempting to add a robot at position (" + f.format(x) + "; " + f.format(y) + ")");
                addRobotAtPosition(x, y);
            } else if (placementMode.equals("Obstacle")) {
                LOGGER.info("Attempting to add an obstacle at position (" + f.format(x) + "; " + f.format(y) + ")");
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
        if (!checkOverlap(x, y, 10)) {
            RobotParams params = editorDialog.getCurrentRobotParams();
            Robot robot = new Robot(params, new Vector2D(x, y));
            getChildren().addAll(robot.getCirle(), robot.getLine());
            robots.put(robot.getCirle(), robot);
            robot.getCirle().setOnMouseClicked(e -> {
                if (robotClickListener != null) {
                    robotClickListener.accept(robot);
                }
                e.consume();
            });
        } else {
            LOGGER.warning("Failed to add robot due to overlap at position (" + x + ", " + y + ")");
        }
    }

    private void addObstacleAtPosition(double x, double y) {
        if (!checkOverlap(x, y, 50)) {
            Rectangle rectangle = new Rectangle(x - 10, y - 10, 50, 50);
            rectangle.setFill(Color.GRAY);
            getChildren().add(rectangle);
            Obstacle obstacle = new Obstacle(new Vector2D(x, y));
            obstacles.put(rectangle, obstacle);
        } else {
            LOGGER.warning("Failed to add obstacle due to overlap at position (" + x + ", " + y + ")");
        }
    }

    private boolean checkOverlap(double x, double y, double size) {
        // Check for overlap with existing robots
        for (Circle circle : robots.keySet()) {
            if (circle.getBoundsInParent().intersects(x - size, y - size, size * 2, size * 2)) {
                return true;
            }
        }
        // Check for overlap with existing obstacles
        for (Rectangle rectangle : obstacles.keySet()) {
            if (rectangle.getBoundsInParent().intersects(x - size, y - size, size * 2, size * 2)) {
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
            double newX = circle.getCenterX() + dx;
            double newY = circle.getCenterY() + dy;
            if (!checkOverlap(newX, newY, circle.getRadius())) {
                circle.setCenterX(newX);
                circle.setCenterY(newY);
                LOGGER.info("Moved robot to new position: (" + f.format(newX) + ", " + f.format(newY) + ")");
            }
        } else if (selectedItem instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) selectedItem;
            double newX = rectangle.getX() + dx;
            double newY = rectangle.getY() + dy;
            if (!checkOverlap(newX, newY, rectangle.getWidth() / 2)) {
                rectangle.setX(newX);
                rectangle.setY(newY);
                LOGGER.info("Moved obstacle to new position: (" + f.format(newX) + ", " + f.format(newY) + ")");
            }
        }
    }

    private void setItemColor(Object item, Color color) {
        if (item instanceof Circle) {
            ((Circle) item).setFill(color);
        } else if (item instanceof Rectangle) {
            ((Rectangle) item).setFill(color);
        }
    }

    public void clear(){
        setColorBack(selectedItem);
    }
    private void setColorBack(Object item) {
        if (item instanceof Circle) {
            ((Circle) item).setFill(Color.BLUE);
        } else if (item instanceof Rectangle) {
            ((Rectangle) item).setFill(Color.GRAY);
        }
    }

    public void setMode(String m) {
        this.mode = m;
    }

    public void setOnRobotClickListener(Consumer<Robot> listener) {
        this.robotClickListener = listener;
    }
}

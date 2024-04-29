package view;

import model.Map;
import model.Robot;
import util.Vector2D;
import model.Obstacle;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class SimulationMapView extends Pane {
    private Map map;

    public SimulationMapView(Map simulationMap) {
        this.map = simulationMap;
        resizeView();
        populateScene();
    }

    private void populateScene() {
        getChildren().clear();

        // Add robots
        for (Robot robot : map.getRobots()) {
            Vector2D pos = robot.getPos();
            Circle robotCircle = new Circle(pos.x, pos.y, 10, Color.BLUE);
            getChildren().add(robotCircle);
        }

        // Add obstacles
        for (Obstacle obstacle : map.getObstacles()) {
            Vector2D pos = obstacle.getPos();
            Rectangle obstacleRect = new Rectangle(pos.x - 10, pos.y - 10, 30, 30);
            obstacleRect.setFill(Color.RED);
            getChildren().add(obstacleRect);
        }
    }

    public void updateRobotPositions() {
        populateScene();
    }

    public void resizeView() {
        Vector2D size = map.getSize();
        setPrefSize(size.x, size.y);
    }
}

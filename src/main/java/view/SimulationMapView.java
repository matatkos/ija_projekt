package view;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import model.Map;
import model.Robot;
import util.Vector2D;
import model.Obstacle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.HashMap;



public class wSimulationMapView extends Pane {
    private java.util.Map<Robot, Circle> robotGraphics = new HashMap<>();
    private java.util.Map<Circle, Robot> circleRobotHashMap = new HashMap<>();

    private java.util.Map<Robot, Line> detectionLines = new HashMap<>();
    private Map map;

    public Robot selectedRobot;


    public SimulationMapView(Map simulationMap) {
        this.map = simulationMap;
        initializeScene();
        resizeView();
        populateScene();
        setOnMousePressed(this::handleMousePressed);
    }

    private void initializeScene() {
        for (Robot robot : map.getRobots()) {
            createRobotGraphics(robot);
        }
        for (Obstacle obstacle : map.getObstacles()) {
            createObstacleGraphics(obstacle);
        }
    }

    private void handleMousePressed(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        Vector2D clickedPosition = new Vector2D(mouseX, mouseY);

        Object target = event.getTarget();
        if (target instanceof Circle) {
            System.out.println("Circle clicked");
            selectRobot(clickedPosition);
        }
    }


    private void selectRobot(Vector2D clickedPosition) {
        for (Robot robot : map.getRobots()) {
            Vector2D robotPosition = robot.getPos();
            double distance = clickedPosition.distanceTo(robotPosition);
            if (distance <= 15) {
                if (selectedRobot != null) {
                    selectedRobot.deselect();
                }
                selectedRobot = robot;
                selectedRobot.select();
                return;
            }
        }
    }

    private void populateScene() {
        getChildren().clear();

        // Add robots
        for (Robot robot : map.getRobots()) {
            Vector2D pos = robot.getPos();
            Circle robotCircle = new Circle(pos.x, pos.y, 10, Color.BLUE);
            getChildren().add(robotCircle);

            double endX = pos.getX() + Math.cos(Math.toRadians(robot.getDir())) * robot.getDetectionRange();
            double endY = pos.getY() + Math.sin(Math.toRadians(robot.getDir())) * robot.getDetectionRange();

            Line detectionLine = new Line(pos.getX(), pos.getY(), endX, endY);
            detectionLine.setStroke(Color.RED);
            detectionLine.setStrokeWidth(2); // Make the line visible enough
            getChildren().add(detectionLine);
        }

        // Add obstacles
        for (Obstacle obstacle : map.getObstacles()) {
            Vector2D pos = obstacle.getPos();
            Rectangle obstacleRect = new Rectangle(pos.x - 10, pos.y - 10, 50, 50);
            obstacleRect.setFill(Color.GREY);
            getChildren().add(obstacleRect);
        }
    }

    private void createRobotGraphics(Robot robot) {
        Vector2D pos = robot.getPos();
        Circle robotCircle = new Circle(pos.getX(), pos.getY(), 10, Color.BLUE);
        Line detectionLine = new Line();
        updateRobotGraphics(robot, robotCircle, detectionLine);

        robotGraphics.put(robot, robotCircle);
        circleRobotHashMap.put(robotCircle, robot);
        detectionLines.put(robot, detectionLine);

        getChildren().addAll(robotCircle, detectionLine);
    }

    private void updateRobotGraphics(Robot robot, Circle circle, Line line) {
        Vector2D pos = robot.getPos();
        circle.setCenterX(pos.getX());
        circle.setCenterY(pos.getY());

        double endX = pos.getX() + Math.cos(Math.toRadians(robot.getDir()) *( robot.getDetectionRange() + 10));
        double endY = pos.getY() + Math.sin(Math.toRadians(robot.getDir()) * (robot.getDetectionRange() + 10));

        line.setStartX(pos.getX());
        line.setStartY(pos.getY());
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(1);
    }

    private void createObstacleGraphics(Obstacle obstacle) {
        Vector2D pos = obstacle.getPos();
        Rectangle obstacleRect = new Rectangle(pos.getX() - 15, pos.getY() - 15, 30, 30);
        obstacleRect.setFill(Color.GRAY);
        getChildren().add(obstacleRect);
    }

    public void updateRobotPositions() {
        populateScene();
    }



    public void resizeView() {
        Vector2D size = map.getSize();
        setPrefSize(size.x, size.y);
    }
}

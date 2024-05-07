package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import util.Vector2D;

import java.util.List;
import java.util.logging.Logger;


public class Robot {

    private static final Logger LOGGER = Logger.getLogger(Robot.class.getName());
    private Vector2D pos;
    private double dir;
    private double rotationAngle;
    private double detectionRange;
    private Circle graphic;
    private Line directionLine;
    private boolean turningDirection;
    private double speed = 0.3; // Default speed value



    public Robot(RobotParams params, Vector2D pos) {

        this.pos = pos;
        this.dir = params.direction;
        this.rotationAngle = params.rotationAngle;
        this.detectionRange = params.detectionRange;
        this.turningDirection = params.turningDirection;
        this.graphic = new Circle(pos.getX(), pos.getY(), 10);
        this.graphic.setFill(Color.BLUE);
        this.directionLine = new Line();
        updateGraphics();
    }

    public Circle getCirle() {
        return graphic;
    }
    public Line getLine() {
        return directionLine;
    }

    public void updateGraphics() {
        graphic.setCenterX(pos.getX());
        graphic.setCenterY(pos.getY());
        directionLine.setStartX(pos.getX());
        directionLine.setStartY(pos.getY());
        directionLine.setEndX(pos.getX() + Math.cos(Math.toRadians(dir)) * (detectionRange+ 10));
        directionLine.setEndY(pos.getY() + Math.sin(Math.toRadians(dir)) * (detectionRange+ 10));
        directionLine.setStroke(Color.RED);
        directionLine.setStrokeWidth(1);
    }

    public Vector2D getPos() {
        return pos;
    }

    public double getDir() {
        return dir;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public double getDetectionRange() {
        return detectionRange;
    }

    public boolean getTurningDirection() {
        return turningDirection;
    }

    public void setPos(Vector2D newPos) {
        pos = newPos;
    }

    public void setDirection(double newDir) {
        dir = newDir;
    }




    private void avoidObstacle() {
        LOGGER.info("Avoiding obstacle. Current direction: " + dir);
        this.dir += (turningDirection ? 1 : -1) * rotationAngle / 100; // Smoother turning
        normalizeDirection();
    }

    private void normalizeDirection() {
        if (dir < 0) dir += 360;
        else if (dir >= 360) dir -= 360;
    }


    public void update(List<Obstacle> obstacles, Vector2D mapSize) {
        Vector2D newPos = calculateNewPosition();

        boolean obstacleDetected = detectObstacleApproach(newPos, obstacles);
        boolean boundaryApproach = detectBoundaryApproach(newPos, mapSize);

        if (detectCollision(newPos, obstacles, mapSize)) {
            LOGGER.warning("Collision detected at position " + newPos + ". Engaging avoidance maneuvers.");
            avoidObstacle();
            if (!obstacleDetected && !boundaryApproach){
                LOGGER.info("Approaching an obstacle or boundary. Engaging avoidance maneuvers.");
                newPos = calculateNewPosition();
            }
        } else {
            if (obstacleDetected || boundaryApproach) {
                avoidObstacle();
                newPos = calculateNewPosition();
            }
        }

        newPos = keepWithinBoundaries(newPos, mapSize);


        this.pos = newPos;

        updateGraphics();
    }

    private Vector2D keepWithinBoundaries(Vector2D newPos, Vector2D mapSize) {
        double newX = Math.min(Math.max(newPos.getX(), 0), mapSize.getX());
        double newY = Math.min(Math.max(newPos.getY(), 0), mapSize.getY());
        return new Vector2D(newX, newY);
    }



    private boolean detectObstacleApproach(Vector2D newPos, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            Vector2D obstaclePos = obstacle.getPos();
            double halfWidth = 25;
            double halfHeight = 25;

            Vector2D[] corners = {
                    new Vector2D(obstaclePos.getX() - halfWidth, obstaclePos.getY() - halfHeight),
                    new Vector2D(obstaclePos.getX() + halfWidth, obstaclePos.getY() - halfHeight),
                    new Vector2D(obstaclePos.getX() - halfWidth, obstaclePos.getY() + halfHeight),
                    new Vector2D(obstaclePos.getX() + halfWidth, obstaclePos.getY() + halfHeight)
            };

            for (Vector2D corner : corners) {
                double distanceToCorner = newPos.distanceTo(corner);
                if (distanceToCorner < detectionRange) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean detectBoundaryApproach(Vector2D newPos, Vector2D mapSize) {
        double margin = detectionRange;

        if (newPos.getX() < margin || newPos.getX() > mapSize.getX() - margin ||
                newPos.getY() < margin || newPos.getY() > mapSize.getY() - margin) {
            return true;
        }
        return false;
    }

    private boolean detectCollision(Vector2D newPos, List<Obstacle> obstacles, Vector2D mapSize) {
        if (newPos.getX() < 0 || newPos.getX() > mapSize.getX() || newPos.getY() < 0 || newPos.getY() > mapSize.getY()) {
            return true;
        }
        for (Obstacle obstacle : obstacles) {
            Vector2D obstaclePos = obstacle.getPos();
            double halfWidth = 25;
            double halfHeight = 25;

            Vector2D[] corners = {
                    new Vector2D(obstaclePos.getX() - halfWidth, obstaclePos.getY() - halfHeight),
                    new Vector2D(obstaclePos.getX() + halfWidth, obstaclePos.getY() - halfHeight),
                    new Vector2D(obstaclePos.getX() - halfWidth, obstaclePos.getY() + halfHeight),
                    new Vector2D(obstaclePos.getX() + halfWidth, obstaclePos.getY() + halfHeight)
            };

            for (Vector2D corner : corners) {
                double robotRadius = 10;

                double distance = newPos.distanceTo(corner);

                if (distance < robotRadius) {

                    return true;
                }
            }

            if (newPos.getX() > obstaclePos.getX() - halfWidth && newPos.getX() < obstaclePos.getX() + halfWidth &&
                    newPos.getY() > obstaclePos.getY() - halfHeight && newPos.getY() < obstaclePos.getY() + halfHeight) {
                return true;
            }
        }

        return false;
    }

    public void moveForward() {
        speed = 0.3;
        LOGGER.info("Controlled robot started moving.");
    }

    public void stop() {
        speed = 0;
        LOGGER.info("Controlled robot stopped.");
    }


    public void select() {
        graphic.setFill(Color.GREEN);
        graphic.setStrokeWidth(3);
        speed = 0;
    }
    public void deselect() {
        graphic.setFill(Color.BLUE);
        graphic.setStrokeWidth(1);
        speed = 0.5;
    }

    public void turnLeft() {
        dir -= 2;
        normalizeDirection();
        LOGGER.info(String.format("Controlled robot changing direction %s", dir));
    }

    public void turnRight() {
        dir += 2;
        normalizeDirection();
        LOGGER.info(String.format("Controlled robot changing direction %s", dir));
    }

    public Vector2D calculateNewPosition() {
        double radians = Math.toRadians(dir);
        double dx = Math.cos(radians) * speed;
        double dy = Math.sin(radians) * speed;
        return new Vector2D(pos.x + dx, pos.y + dy);
    }

    public void setRotationAngle(double newRotationAngle) {
        rotationAngle = newRotationAngle;
    }

    public void setDetectionRange(double newDetectionRange) {
        detectionRange = newDetectionRange;
    }

    public void setTurningDirection(boolean newTurningDirection) {
        turningDirection = newTurningDirection;
    }
}

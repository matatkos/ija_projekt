package model;

import util.Vector2D;

public class Robot {
    private Vector2D pos;
    private double dir;
    private double rotationAngle;
    private double detectionRange;
    private boolean turningDirection;
    private double speed = 10; // Default speed value

    public Robot(Vector2D pos) {
        this.pos = pos;
        this.dir = 0;
    }

    public Robot(RobotParams params, Vector2D pos) {
        this.pos = pos;
        this.dir = params.direction;
        this.rotationAngle = params.rotationAngle;
        this.detectionRange = params.detectionRange;
        this.turningDirection = params.turningDirection;
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

    public void updateParameters(RobotParams params) {
        rotationAngle = params.rotationAngle;
        detectionRange = params.detectionRange;
        turningDirection = params.turningDirection;
        dir = params.direction;
        System.out.println("Robot parameters updated");
    }

    public Vector2D updatePos() {
        // Convert direction angle to radians
        double radians = Math.toRadians(dir);
        // Calculate new x and y components
        double dx = Math.cos(radians) * speed;
        double dy = Math.sin(radians) * speed;
        // Update position
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

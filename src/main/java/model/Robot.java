package model;

import util.Vector2D;

public class Robot {
    private Vector2D pos;
    private Vector2D dir;
    private double rotationAngle;
    private double detectionRange;
    private boolean turningDirection;
    private double speed = 10; // Default speed value

    public Robot(Vector2D pos) {
        this.pos = pos;
        this.dir = new Vector2D();
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

    public Vector2D getDir() {
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

    public void setDirection(Vector2D newDir) {
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
        return pos.add(dir.multiply(speed));
    }

    private void setRotationAngle(double newRotationAngle) {
        rotationAngle = newRotationAngle;
    }

    private void setDetectionRange(double newDetectionRange) {
        detectionRange = newDetectionRange;
    }

    private void setTurningDirection(boolean newTurningDirection) {
        turningDirection = newTurningDirection;
    }
}

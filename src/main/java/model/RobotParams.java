package model;

import util.Vector2D;

public class RobotParams {
    public Vector2D direction;
    public double detectionRange;
    public double rotationAngle;
    public boolean turningDirection;

    // Constructor
    public RobotParams(Vector2D dir, double range, double angle, boolean direction) {
        this.direction = dir;
        this.detectionRange = range;
        this.rotationAngle = angle;
        this.turningDirection = direction;
    }
}


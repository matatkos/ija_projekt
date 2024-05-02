package model;

import util.Vector2D;

public class RobotParams {
    public double direction;
    public double detectionRange;
    public double rotationAngle;
    public boolean turningDirection;

    // Constructor
    public RobotParams(double dir, double range, double angle, boolean direction) {
        this.direction = dir;
        this.detectionRange = range;
        this.rotationAngle = angle;
        this.turningDirection = direction;
    }

    // Override the toString() method to return a meaningful string representation
    @Override
    public String toString() {
        return "Direction=" + direction + ", " +
                "Detection Range=" + detectionRange + ", " +
                "Rotation Angle=" + rotationAngle + ", " +
                "Turning Direction=" + (turningDirection ? "Right" : "Left");
    }
}


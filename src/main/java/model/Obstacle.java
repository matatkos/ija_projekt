package model;

import util.Vector2D;

public class Obstacle {
    private Vector2D pos;

    public Obstacle(Vector2D pos) {
        this.pos = pos;
    }

    public Obstacle(double x, double y) {
        this.pos = new Vector2D(x, y);
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D newPos) {
        this.pos = newPos;
    }
}


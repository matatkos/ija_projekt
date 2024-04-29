package model;

import java.util.ArrayList;
import java.util.List;

import observer.Observable;
import util.Vector2D;

public class Map {
    private Vector2D size;
    private List<Robot> robots;
    private List<Obstacle> obstacles;

    public Map() {
        this.size = new Vector2D(0, 0);
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }

    public Map(double x, double y) {
        this.size = new Vector2D(x, y);
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }

    public Map(Vector2D size) {
        this.size = size;
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }

    public void setSize(Vector2D size) {
        this.size = size;
    }

    public void setSize(double x, double y) {
        this.size = new Vector2D(x, y);
    }

    public void setRobots(List<Robot> newRobots) {
        this.robots = newRobots;
    }

    public void setObstacles(List<Obstacle> newObstacles) {
        this.obstacles = newObstacles;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addRobot(Robot newRobot) {
        robots.add(newRobot);
    }

    public void addObstacle(Obstacle newObstacle) {
        obstacles.add(newObstacle);
    }

    public Vector2D getSize() {
        return size;
    }

    public void moveRobot(Vector2D oldPos, Vector2D newPos) {
        for (Robot robot : robots) {
            if (robot.getPos().equals(oldPos)) {
                robot.setPos(newPos);
                return;
            }
        }
    }

    public void moveObstacle(Vector2D oldPos, Vector2D newPos) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPos().equals(oldPos)) {
                obstacle.setPos(newPos);
                return;
            }
        }
    }
}

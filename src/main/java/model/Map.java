package model;

import observer.Observable;
import observer.Observer;
import util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class Map implements Observable {
    private Vector2D size;
    private List<Robot> robots;
    private List<Obstacle> obstacles;



    public Map(double x, double y) {
        this.size = new Vector2D(x, y);
        this.robots = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }


    public void setSize(Vector2D size) {
        this.size = size;
    }



    public void setRobots(List<Robot> newRobots) {
        this.robots = newRobots;
        notifyObservers(); // Notify observers when robots are updated
    }

    public void setObstacles(List<Obstacle> newObstacles) {
        this.obstacles = newObstacles;
        notifyObservers(); // Notify observers when obstacles are updated
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void addRobot(Robot newRobot) {
        robots.add(newRobot);
        notifyObservers(); // Notify observers when a new robot is added
    }

    public void addObstacle(Obstacle newObstacle) {
        obstacles.add(newObstacle);
        notifyObservers(); // Notify observers when a new obstacle is added
    }

    public Vector2D getSize() {
        return size;
    }

    public void moveRobot(Vector2D oldPos, Vector2D newPos) {
        for (Robot robot : robots) {
            if (robot.getPos().equals(oldPos)) {
                robot.setPos(newPos);
                notifyObservers(); // Notify observers when a robot is moved
                return;
            }
        }
    }

    public void moveObstacle(Vector2D oldPos, Vector2D newPos) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPos().equals(oldPos)) {
                obstacle.setPos(newPos);
                notifyObservers(); // Notify observers when an obstacle is moved
                return;
            }
        }
    }


}

package model;

import java.util.ArrayList;
import java.util.List;

import observer.Observable;

public class Map {
    private List<Obstacle> obstacles;
    private float width;
    private float height;
    private Observable observable; // Composition

    public Map() {
        obstacles = new ArrayList<>();
        observable = new Observable(); // Initialize Observable
        // Initialize width and height as needed
        width = 0;  // Placeholder value
        height = 0; // Placeholder value
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        // Notify observers about the change in the map
        notifyObservers();
    }

    public void clearObstacles() {
        obstacles.clear();
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    // Trigger notification when changes occur
    private void notifyObservers() {
        observable.notifyObservers();
    }
}

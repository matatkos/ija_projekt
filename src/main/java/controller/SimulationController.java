package controller;
import javafx.animation.AnimationTimer;
import model.Map;
import model.Obstacle;
import view.SimulationMapView;
import model.Robot;
import util.Vector2D;
import java.util.List;


public class SimulationController {
    private Map map;

    private SimulationMapView mapView;

    private AnimationTimer animationTimer;
    private boolean isRunning;

    public SimulationController(Map simulationMap, SimulationMapView simulationView) {
        this.map = simulationMap;
        this.mapView = simulationView;
        this.animationTimer = createAnimationTimer();

    }

    public void setView(SimulationMapView view) {
        this.mapView = view;
    }

    private AnimationTimer createAnimationTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSimulation();
            }
        };
    }



    public void startSimulation() {

        isRunning = true;
        animationTimer.start();
        System.out.println("Simulation started");
        System.out.println("Map size: " + map.getSize().toString());

        updateSimulation();
    }

    public void pauseSimulation() {
        isRunning = false;
        animationTimer.stop();
        System.out.println("Simulation paused");
    }



    private void updateSimulation() {
        List<Robot> robots = map.getRobots();
        List<Obstacle> obstacles = map.getObstacles();
        Vector2D mapSize = map.getSize();

        if (robots.isEmpty()) {

            return;
        }

        for (Robot robot : robots) {
            robot.update(obstacles, mapSize);
        }

        // Update positions on the map view
        mapView.updateRobotPositions();
    }
}

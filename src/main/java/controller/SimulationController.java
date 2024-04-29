package controller;

import model.Map;
import view.SimulationMapView;
import model.Robot;
import util.Vector2D;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class SimulationController {
    private Map map;
    private SimulationMapView mapView;
    private boolean isRunning;

    public SimulationController(Map simulationMap, SimulationMapView simulationView) {
        this.map = simulationMap;
        this.mapView = simulationView;
    }

    public void startSimulation() {
        isRunning = true;
        System.out.println("Simulation started");

        updateRobots();
    }

    public void pauseSimulation() {
        isRunning = false;
        System.out.println("Simulation paused");
    }

    public void resumeSimulation() {
        isRunning = true;
        System.out.println("Simulation resumed");
        updateRobots();
    }

    private void updateRobots() {
        if (!isRunning) {
            return;
        }

        System.out.println("Updating robots...");
        List<Robot> robots = map.getRobots();

        if (robots.isEmpty()) {
            System.out.println("Robots list is empty!");
            return;
        }

        for (Robot robot : robots) {
            Vector2D newPos = robot.updatePos();
            double mapWidth = map.getSize().getX();
            double mapHeight = map.getSize().getY();

            if (newPos.getX() < 0) {
                newPos.setX(0);
            }
            if (newPos.getY() < 0) {
                newPos.setY(0);
            }
            if (newPos.getX() > mapWidth) {
                newPos.setX(mapWidth);
            }
            if (newPos.getY() > mapHeight) {
                newPos.setY(mapHeight);
            }

            robot.setPos(newPos);
        }

        mapView.updateRobotPositions();

        Timer timer = new Timer(33, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRobots();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}

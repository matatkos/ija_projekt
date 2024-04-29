package view;

import model.Map;
import view.SimulationMapView;
import controller.SimulationController;
import model.Obstacle;
import model.Robot;
import util.Vector2D;
import model.RobotParams;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationDialog extends Application {
    private Map map;
    private SimulationMapView view;
    private SimulationController controller;
    private Button playButton;
    private Button stopButton;
    private Button menuButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulation");

        map = new Map(1000.0, 1000.0);
        view = new SimulationMapView(map);
        controller = new SimulationController(map, view);

        playButton = new Button("Play");
        stopButton = new Button("Stop");
        menuButton = new Button("Menu");

        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startSimulation();
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopSimulation();
            }
        });

        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeWindow();
            }
        });

        HBox buttonLayout = new HBox();
        buttonLayout.getChildren().addAll(playButton, stopButton, menuButton);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(view);
        mainLayout.setBottom(buttonLayout);

        Scene scene = new Scene(mainLayout, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startSimulation() {
        controller.startSimulation();
    }

    public void stopSimulation() {
        controller.pauseSimulation();
    }

    public void closeWindow() {
        controller.pauseSimulation();
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.close();
    }

    public void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Vector2D size = new Vector2D();
                List<Robot> robots = new ArrayList<>();
                List<Obstacle> obstacles = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("robots:")) {
                        while ((line = reader.readLine()) != null && !line.startsWith("obstacles:")) {
                            String[] parts = line.split("x");
                            double posX = Double.parseDouble(parts[0].trim());
                            double posY = Double.parseDouble(parts[1].trim());
                            double dirX = Double.parseDouble(parts[2].trim());
                            double dirY = Double.parseDouble(parts[3].trim());
                            double angle = Double.parseDouble(parts[4].trim());
                            double range = Double.parseDouble(parts[5].trim());
                            boolean turningDirection = parts[6].trim().equals("Right");

                            Vector2D pos = new Vector2D(posX, posY);
                            Vector2D dir = new Vector2D(dirX, dirY);
                            RobotParams params = new RobotParams(dir, range, angle, turningDirection);
                            Robot robot = new Robot(params, pos);
                            robots.add(robot);
                            System.out.println("x: " + pos.x + " | y: " + pos.y);
                        }
                    } else if (line.startsWith("obstacles:")) {
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split("x");
                            double posX = Double.parseDouble(parts[0].trim());
                            double posY = Double.parseDouble(parts[1].trim());
                            Obstacle obstacle = new Obstacle(posX, posY);
                            obstacles.add(obstacle);
                            System.out.println("obstacle");
                        }
                    } else {
                        String[] sizeParts = line.split("x");
                        size.x = Double.parseDouble(sizeParts[0].trim());
                        size.y = Double.parseDouble(sizeParts[1].trim());
                    }
                }

                map.setSize(size);
                map.setRobots(robots);
                map.setObstacles(obstacles);

                System.out.println("map x:" + map.getSize().x + " y:" + map.getSize().y);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

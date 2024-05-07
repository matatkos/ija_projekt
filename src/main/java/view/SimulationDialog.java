package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationDialog extends Application {
    private Map map;
    private String mapFilePath;
    private SimulationMapView view;
    private SimulationController controller;
    private Button playButton;
    private Button stopButton;
    private Button menuButton;

    private Robot selectedRobot;

    public SimulationDialog(String mapFilePath) {
        this.mapFilePath = mapFilePath;

    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulation");


        map = new Map(800, 800);
        controller = new SimulationController(map, null);

        view = new SimulationMapView(map);
        view.setBackground(new Background(new BackgroundFill(Color.web("#a3c9a8"), CornerRadii.EMPTY, Insets.EMPTY)));
        controller.setView(view);

        HBox layout = new HBox(20);
        layout.setAlignment(Pos.CENTER);

        //Simulation buttons
        playButton = new Button("Play");
        stopButton = new Button("Stop");
        menuButton = new Button("Menu");

        layout.getChildren().addAll(playButton, stopButton, menuButton);

        playButton.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        menuButton.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        stopButton.setStyle("-fx-background-color: #2a9df4; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
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

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(view);
        mainLayout.setBottom(layout);

        Scene scene = new Scene(mainLayout, 800, 830);
        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        if (mapFilePath != null && !mapFilePath.isEmpty()) {
            File mapFile = new File(mapFilePath);
            loadFile(mapFile);
            view.updateRobotPositions();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        if (view.selectedRobot == null) return;

        switch (event.getCode()) {
            case W:
                view.selectedRobot.moveForward();
                break;
            case S:
                view.selectedRobot.stop();
                break;
            case A:
                view.selectedRobot.turnLeft();
                break;
            case D:
                view.selectedRobot.turnRight();
                break;
        }
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

    public void loadFile(File file) {
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean isReadingRobots = false;
                boolean isReadingObstacles = false;

                List<Robot> robots = new ArrayList<>();
                List<Obstacle> obstacles = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    if (line.equals("Robots:")) {
                        isReadingRobots = true;
                        isReadingObstacles = false;
                        continue;
                    } else if (line.equals("Obstacles:")) {
                        isReadingObstacles = true;
                        isReadingRobots = false;
                        continue;
                    }

                    if (isReadingRobots) {
                        String[] parts = line.split(";");
                        if (parts.length < 6) continue;
                        try {
                            double posX = Double.parseDouble(parts[0].trim().replace(",", "."));
                            double posY = Double.parseDouble(parts[1].trim().replace(",", "."));
                            double dir = Double.parseDouble(parts[2].trim());
                            double angle = Double.parseDouble(parts[3].trim());
                            double range = Double.parseDouble(parts[4].trim());
                            boolean turningDirection = parts[5].trim().equalsIgnoreCase("right");

                            Vector2D pos = new Vector2D(posX, posY);
                            RobotParams params = new RobotParams(dir, range, angle, turningDirection);
                            Robot robot = new Robot(params, pos);
                            robots.add(robot);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing robot data: " + e.getMessage());
                        }
                    } else if (isReadingObstacles) {
                        String[] parts = line.split(";");
                        if (parts.length < 2) continue;
                        try {
                            double posX = Double.parseDouble(parts[0].trim().replace(",", "."));
                            double posY = Double.parseDouble(parts[1].trim().replace(",", "."));
                            Obstacle obstacle = new Obstacle(posX, posY);
                            obstacles.add(obstacle);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing obstacle data: " + e.getMessage());
                        }
                    }
                }

                map.setRobots(robots);
                map.setObstacles(obstacles);
                System.out.println("Map loaded successfully with " + robots.size() + " robots and " + obstacles.size() + " obstacles.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}

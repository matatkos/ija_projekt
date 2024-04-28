import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Square Animation");

        // Create a rectangle
        Rectangle square = new Rectangle(100, 100, Color.BLUE);
        square.setLayoutX(0);
        square.setLayoutY(100);

        // Create translation animation
        TranslateTransition animation = new TranslateTransition(Duration.seconds(2), square);
        animation.setFromX(0);
        animation.setToX(300);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
        animation.setInterpolator(Interpolator.EASE_BOTH);
        animation.play();

        root.getChildren().add(square);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

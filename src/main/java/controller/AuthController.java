package controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AuthController extends GuiController {

    protected void fadeInFromBelowAnimation(Node node) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), node);
        translateTransition.setFromY(100);
        translateTransition.setToY(0);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(translateTransition, fadeTransition);

        parallelTransition.play();
    }

    protected ParallelTransition fadeInToAboveAnimation(Node node) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(600), node);
        translateTransition.setToY(-100);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(600), node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        return new ParallelTransition(translateTransition, fadeTransition);
    }

}

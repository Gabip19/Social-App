package controller;

import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Animations;

import java.io.IOException;

public class AuthController extends GuiController {

    public static void switchToMainPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                GuiController.class.getResource("/gui/main-window.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());

        currentStage = new Stage();
        currentStage.setScene(scene);
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.show();
    }

    protected void switchAuthenticationScene(Node oldRootNode, Parent newRootNode) {
        Transition animation = Animations.fadeInToAboveAnimation(oldRootNode);
        animation.setOnFinished( e -> {
            currentStage.setScene(new Scene(newRootNode));
            currentStage.show();
        });
        animation.play();
    }

}

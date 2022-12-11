package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignInController extends AuthController {
    @FXML
    public VBox signInVBox;

    public void initialize() {
        fadeInFromBelowAnimation(signInVBox);
    }

    public void switchToSignUpScene(ActionEvent event) throws IOException {
        Parent signUpRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/signup.fxml")));

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var animation = fadeInToAboveAnimation(signInVBox);
        animation.setOnFinished( e -> {
            currentStage.setScene(new Scene(signUpRoot));
            currentStage.show();
        });
        animation.play();
    }
}

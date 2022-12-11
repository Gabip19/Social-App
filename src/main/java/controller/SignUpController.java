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

public class SignUpController extends AuthController {
    @FXML
    public VBox signUpVBox;

    public void initialize() {
        fadeInFromBelowAnimation(signUpVBox);
    }

    public void switchToSignInScene(ActionEvent event) throws IOException {
        Parent signInRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/gui/signin.fxml")));

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var animation = fadeInToAboveAnimation(signUpVBox);
        animation.setOnFinished( e -> {
            currentStage.setScene(new Scene(signInRoot));
            currentStage.show();
        });
        animation.play();
    }
}

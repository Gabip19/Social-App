package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Network;

import java.io.IOException;
import java.util.Objects;

public class SignUpController extends AuthController {
    private Network srv;

    @FXML
    public VBox signUpVBox;

    public void setSrv(Network srv) {
        this.srv = srv;
    }

    public void initialize() {
        fadeInFromBelowAnimation(signUpVBox);
    }

    public void switchToSignInScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/signin.fxml")));

        Parent signInRoot = loader.load();
        SignInController signInController = loader.getController();
        signInController.setSrv(srv);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var animation = fadeInToAboveAnimation(signUpVBox);
        animation.setOnFinished( e -> {
            currentStage.setScene(new Scene(signInRoot));
            currentStage.show();
        });
        animation.play();
    }
}

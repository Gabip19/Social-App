package controller;

import domain.validators.exceptions.SignInException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Network;

import java.io.IOException;
import java.util.Objects;

public class SignInController extends AuthController {
    private Network srv;

    @FXML
    public VBox signInVBox;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label errorLabel;
    @FXML
    public Button signInButton;

    public void setSrv(Network srv) {
        this.srv = srv;
    }

    public void initialize() {
        fadeInFromBelowAnimation(signInVBox);
        signInVBox.requestFocus();
    }

    private void clearFields() {
        emailField.setText("");
        passwordField.setText("");
    }

    public void signIn() {
        System.out.println("\nTrying to sign in.\n");
        try {
            srv.signIn(emailField.getText(), passwordField.getText());
            System.out.println("\nSigned in successfully.\n");
        } catch (SignInException e) {
            errorLabel.setText(e.getMessage());
            clearFields();
        }
        finally {
            signInVBox.requestFocus();
        }
    }

    public void switchToSignUpScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/gui/signup.fxml")));

        Parent signUpRoot = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.setSrv(srv);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        var animation = fadeInToAboveAnimation(signInVBox);
        animation.setOnFinished( e -> {
            currentStage.setScene(new Scene(signUpRoot));
            currentStage.show();
        });
        animation.play();
    }
}

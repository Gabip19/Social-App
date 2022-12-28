package controller;

import domain.validators.exceptions.SignInException;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Animations;

import java.io.IOException;
import java.util.Objects;

public class SignInController extends AuthController {
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
    public HBox topHBox;

    public void initialize() {
        defineDraggableNode(topHBox);
        playIntroAnimation();
        signInVBox.requestFocus();
    }

    private void playIntroAnimation() {
        Transition animation = Animations.fadeInFromBelowAnimation(signInVBox);
        animation.play();
    }

    private void clearFields() {
        emailField.clear();
        passwordField.clear();
    }

    public void signIn() {
        try {
            srv.signIn(emailField.getText(), passwordField.getText());
            currentStage.close();
            switchToMainPage();

        } catch (SignInException e) {
            errorLabel.setText(e.getMessage());
            clearFields();

        } catch (IOException e) {
            throw new RuntimeException(e);

        } finally {
            signInVBox.requestFocus();
        }
    }

    public void switchToSignUpScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/gui/signup.fxml"))
        );
        Parent signUpRoot = loader.load();
        switchAuthenticationScene(signInVBox, signUpRoot);
    }
}

package controller;

import domain.validators.exceptions.ValidationException;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import utils.Animations;

import java.io.IOException;
import java.util.Objects;

public class SignUpController extends AuthController {
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public DatePicker birthdateField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField rePasswordField;
    @FXML
    public Label errorLabel;
    @FXML
    public Button signUpButton;
    @FXML
    public VBox signUpVBox;
    public HBox topHBox;

    public void initialize() {
        defineDraggableNode(topHBox);
        playIntroAnimation();
        signUpVBox.requestFocus();
        errorLabel.setMinHeight(Region.USE_PREF_SIZE);
        errorLabel.setTextAlignment(TextAlignment.JUSTIFY);
    }

    private void playIntroAnimation() {
        Transition animation = Animations.fadeInFromBelowAnimation(signUpVBox);
        animation.play();
    }

    public void signUp() {
        if (hasEmptyFields()) {
            errorLabel.setText("Please complete every field.");
        } else if (passwordField.getText().equals(rePasswordField.getText())) {
            try {
                srv.addUser(
                        lastNameField.getText(),
                        firstNameField.getText(),
                        emailField.getText(),
                        String.valueOf(birthdateField.getValue()),
                        passwordField.getText()
                );
                currentStage.close();
                switchToMainPage();
            }
            catch (ValidationException e) {
                errorLabel.setText(e.getMessage());
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            passwordField.clear();
            rePasswordField.clear();
            errorLabel.setText("Password does not match.");
        }
        signUpVBox.requestFocus();
    }

    private boolean hasEmptyFields() {
        return lastNameField.getText().equals("") ||
                firstNameField.getText().equals("") ||
                emailField.getText().equals("") ||
                passwordField.getText().equals("") ||
                birthdateField.getEditor().getText().equals("");
    }

    public void switchToSignInScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("/gui/signin.fxml"))
        );
        Parent signInRoot = loader.load();
        switchAuthenticationScene(signUpVBox, signInRoot);
    }

}

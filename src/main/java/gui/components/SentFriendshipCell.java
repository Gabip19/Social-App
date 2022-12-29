package gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;

public class SentFriendshipCell extends HBox {
    @FXML
    public Button cancelButton;
    @FXML
    public Label dateLabel;
    @FXML
    public Circle profileCircle;
    @FXML
    public Label userNameLabel;
    @FXML
    public AnchorPane anchorRoot;

    public static final URL fxmlLocation = FriendshipCell.class.getResource("/gui/sent-request-view.fxml");

    public SentFriendshipCell() {
        super();
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getAnchorRoot() {
        return anchorRoot;
    }

    public void setNameLabelText(String text) {
        userNameLabel.setText(text);
    }

    public void setDateLabelText(String text) {
        dateLabel.setText(text);
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Circle getProfileCircle() {
        return profileCircle;
    }
}

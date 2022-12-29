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

public class UserCell extends HBox {
    @FXML
    public AnchorPane anchorRoot;
    @FXML
    public Button addFriendButton;
    @FXML
    public Circle profileCircle;
    @FXML
    public Label nameLabel;

    private static final URL fxmlLocation = UserCell.class.getResource("/gui/user-cell-view.fxml");

    public UserCell() {
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
        nameLabel.setText(text);
    }

    public Button getAddFriendButton() {
        return addFriendButton;
    }

    public Circle getProfileCircle() {
        return profileCircle;
    }
}

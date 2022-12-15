package gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class UserCell extends HBox {
    @FXML
    public AnchorPane anchorRoot;
    @FXML
    public Button addFriendButton;
    @FXML
    public Label nameLabel;

    public UserCell() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/user-cell-view.fxml"));
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
}

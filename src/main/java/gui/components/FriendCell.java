package gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;

public class FriendCell extends HBox {
    @FXML
    public Button removeFriendButton;
    @FXML
    public Label nameLabel;
    @FXML
    public AnchorPane anchorRoot;
    @FXML
    public Button openChatButton;

    public static final URL fxmlLocation = FriendCell.class.getResource("/gui/friend-cell-view.fxml");

    public FriendCell() {
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

    public Button getRemoveFriendButton() {
        return removeFriendButton;
    }
}

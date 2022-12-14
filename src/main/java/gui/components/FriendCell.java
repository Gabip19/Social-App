package gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class FriendCell {
    @FXML
    public Button removeFriendButton;
    @FXML
    public Label nameLabel;
    @FXML
    public AnchorPane anchorRoot;
    @FXML
    public Button openChatButton;

    public FriendCell() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/friend-cell-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
            openChatButton.setOnAction(param -> System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA"));
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

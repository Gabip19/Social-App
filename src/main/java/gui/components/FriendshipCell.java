package gui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class FriendshipCell extends HBox {
    @FXML
    public Button rejectButton;
    @FXML
    public Button acceptButton;
    @FXML
    public Label dateLabel;
    @FXML
    public Label statusLabel;
    @FXML
    public Label userNameLabel;
    @FXML
    public AnchorPane anchorRoot;

    public FriendshipCell() {
        super();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/friend-requests-cell-view.fxml"));
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

    public void setStatusLabelText(String text) {
        statusLabel.setText(text);
    }

    public Button getAcceptButton() {
        return acceptButton;
    }

    public Button getRejectButton() {
        return rejectButton;
    }
}

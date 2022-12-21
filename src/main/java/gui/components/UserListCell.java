package gui.components;

import domain.User;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import service.Network;

public class UserListCell extends ListCell<User> {
    private final Network srv;

    public UserListCell(Network srv) {
        super();
        this.srv = srv;
        setStyle("-fx-padding: 0px; -fx-background-color: rgba(0, 0, 0, 0);");
        setPrefWidth(0);
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            UserCell userCell = new UserCell();
            userCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());

            Button addFriendButton = userCell.getAddFriendButton();

            initAddFriendButtonFor(item, addFriendButton);

            setText(null);
            setGraphic(userCell.getAnchorRoot());
        }
    }

    private void initAddFriendButtonFor(User item, Button addFriendButton) {
        if (!srv.usersAreFriends(srv.getCurrentUser(), item)) {
            addFriendButton.setOnAction(param -> {
                srv.sendFriendRequest(item);
                addFriendButton.setDisable(true);
                addFriendButton.setText("SENT");
                addFriendButton.setGraphic(null);
            });
        } else {
            addFriendButton.setDisable(true);
            addFriendButton.setText(null);
            FontIcon blockIcon = new FontIcon("mdal-block");
            blockIcon.setIconColor(Color.RED);
            blockIcon.setIconSize(25);
            addFriendButton.setGraphic(blockIcon);
        }
    }
}

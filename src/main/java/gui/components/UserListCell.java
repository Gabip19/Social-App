package gui.components;

import domain.Friendship;
import domain.User;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import service.Network;

public class UserListCell extends ListCell<User> {
    private final Network srv;

    private final ObservableList<Friendship> sentRequests;


    public UserListCell(Network srv, ObservableList<Friendship> sentRequests) {
        super();
        this.srv = srv;
        this.sentRequests = sentRequests;
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
            userCell.getProfileCircle().setFill(Color.valueOf(item.getHexProfileColor()));

            Button addFriendButton = userCell.getAddFriendButton();

            initAddFriendButtonFor(item, addFriendButton);

            setText(null);
            setGraphic(userCell.getAnchorRoot());
        }
    }

    private void initAddFriendButtonFor(User item, Button addFriendButton) {
        if (!srv.usersAreFriends(srv.getCurrentUser(), item)) {
            addFriendButton.setOnAction(param -> {
                Friendship sentRequest = srv.sendFriendRequest(item);
                sentRequests.add(sentRequest);
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

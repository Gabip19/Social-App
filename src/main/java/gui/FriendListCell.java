package gui;

import domain.User;
import javafx.scene.control.ListCell;

public class FriendListCell extends ListCell<User> {
    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {

            FriendCell friendCell = new FriendCell();
            friendCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());

            setGraphic(friendCell.getHBoxRoot());
        }
    }
}

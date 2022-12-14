package gui.components;

import domain.User;
import javafx.scene.control.ListCell;

public class FriendListCell extends ListCell<User> {

    public FriendListCell() {
        super();
        setPrefWidth(0);
        setStyle("-fx-padding: 0px; -fx-background-color: rgba(255,255,255,0.6);"); // rgb(194, 194, 194)
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {

            FriendCell friendCell = new FriendCell();
            friendCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());
            setGraphic(friendCell.getAnchorRoot());
        }
    }
}

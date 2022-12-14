package gui.components;

import domain.User;
import javafx.scene.control.ListCell;
import service.Network;

public class FriendListCell extends ListCell<User> {

    private Network srv;

    public FriendListCell(Network srv) {
        super();
        this.srv = srv;
        setPrefWidth(0);
        setStyle("-fx-background-color: rgba(255,255,255,0.6);"); // rgb(194, 194, 194)
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            FriendCell friendCell = new FriendCell();
            friendCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());

            friendCell.getRemoveFriendButton().setOnAction(param ->
                    srv.removeFriend(item)
            );

            setText(null);
            setGraphic(friendCell.getAnchorRoot());
        }
    }
}

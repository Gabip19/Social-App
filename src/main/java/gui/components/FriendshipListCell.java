package gui.components;

import domain.Friendship;
import domain.User;
import javafx.scene.control.ListCell;
import service.Network;

import java.time.format.DateTimeFormatter;

public class FriendshipListCell extends ListCell<Friendship> {

    private Network srv;

    public FriendshipListCell(Network srv) {
        super();
        this.srv = srv;
        setPrefWidth(0);
    }

    @Override
    protected void updateItem(Friendship item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) { // FIXME: 12/14/22
            FriendshipCell friendshipCell = new FriendshipCell();

            User senderUser = srv.findOneUser(item.getSenderID());
            friendshipCell.setNameLabelText(senderUser.getFirstName() + " " + senderUser.getLastName());
            friendshipCell.setStatusLabelText(String.valueOf(item.getFriendshipStatus()));
            friendshipCell.setDateLabelText(item.getFriendshipDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            setGraphic(friendshipCell.getAnchorRoot());
        }
    }
}

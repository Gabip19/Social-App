package gui.components;

import domain.Friendship;
import domain.User;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import service.Network;

import java.time.format.DateTimeFormatter;

public class SentFriendshipListCell extends ListCell<Friendship> {

    private final Network srv;

    private final ObservableList<Friendship> sentRequests;

    public SentFriendshipListCell(Network srv, ObservableList<Friendship> sentRequests) {
        super();
        this.srv = srv;
        this.sentRequests = sentRequests;
        setPrefWidth(0);
        setStyle("-fx-padding: 0px 10px 10px 10px; -fx-background-color: rgba(0, 0, 0, 0);");
    }

    @Override
    protected void updateItem(Friendship item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            SentFriendshipCell friendshipCell = new SentFriendshipCell();

            User receiver = srv.findOneUser(item.getReceiverID());
            friendshipCell.setNameLabelText(receiver.getFirstName() + " " + receiver.getLastName());
            friendshipCell.setDateLabelText(item.getFriendshipDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

            friendshipCell.getCancelButton().setOnAction(param -> {
                srv.cancelFriendRequest(item);
                sentRequests.remove(item);
            });

            setText(null);
            setGraphic(friendshipCell.getAnchorRoot());
        }
    }
}

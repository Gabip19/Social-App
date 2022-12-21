package gui.components;

import domain.Friendship;
import domain.FriendshipStatus;
import domain.User;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import service.Network;

import java.time.format.DateTimeFormatter;

public class FriendshipListCell extends ListCell<Friendship> {

    private final Network srv;

    private final ObservableList<Friendship> friendships;

    public FriendshipListCell(Network srv, ObservableList<Friendship> friendships) {
        super();
        this.srv = srv;
        this.friendships = friendships;
        setPrefWidth(0);
        setStyle("-fx-padding: 0px; -fx-background-color: rgba(255,255,255,0.6);");
    }

    @Override
    protected void updateItem(Friendship item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            FriendshipCell friendshipCell = new FriendshipCell();

            User senderUser = srv.findOneUser(item.getSenderID());

            initLabels(item, friendshipCell, senderUser);
            initButtons(item, friendshipCell);

            setText(null);
            setGraphic(friendshipCell.getAnchorRoot());
        }
    }

    private void initButtons(Friendship item, FriendshipCell friendshipCell) {
        if (item.getFriendshipStatus().equals(FriendshipStatus.PENDING)) {
            initButtonsForPending(item, friendshipCell);
        } else {
            disableFriendshipButtons(item, friendshipCell);
        }
    }

    private static void disableFriendshipButtons(Friendship item, FriendshipCell friendshipCell) {
        friendshipCell.getAcceptButton().setDisable(true);
        friendshipCell.getRejectButton().setDisable(true);

        if (item.getFriendshipStatus().equals(FriendshipStatus.REJECTED)) {
            friendshipCell.getAnchorRoot().getStylesheets().clear();
            friendshipCell.getAnchorRoot().getStylesheets().add("gui/styles/friend-req-rejectedCSS.css");
        } else {
            friendshipCell.getAnchorRoot().getStylesheets().clear();
            friendshipCell.getAnchorRoot().getStylesheets().add("gui/styles/friend-req-acceptedCSS.css");
        }
    }

    private void initButtonsForPending(Friendship item, FriendshipCell friendshipCell) {
        friendshipCell.getAcceptButton().setOnAction(param -> {
            srv.acceptFriendRequest(item);
            friendships.setAll(srv.getUserFriendships(srv.getCurrentUser()));
        });

        friendshipCell.getRejectButton().setOnAction(param -> {
            srv.rejectFriendRequest(item);
            friendships.setAll(srv.getFriendRequestsForUser(srv.getCurrentUser()));
        });
    }

    private static void initLabels(Friendship item, FriendshipCell friendshipCell, User senderUser) {
        friendshipCell.setNameLabelText(senderUser.getFirstName() + " " + senderUser.getLastName());
        friendshipCell.setStatusLabelText(String.valueOf(item.getFriendshipStatus()));
        friendshipCell.setDateLabelText(item.getFriendshipDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}

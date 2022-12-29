package gui.components;

import domain.User;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import service.Network;
import utils.Animations;

public class FriendListCell extends ListCell<User> {

    private final Network srv;
    private final ObservableList<User> friends;
    private final BorderPane borderPane;

    public FriendListCell(Network srv, ObservableList<User> friends, BorderPane borderPane) {
        super();
        this.srv = srv;
        this.friends = friends;
        this.borderPane = borderPane;
        setPrefWidth(0);
        setStyle("-fx-padding: 0px 10px 12px 10px; -fx-background-color: rgba(0, 0, 0, 0);");
    }

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            FriendCell friendCell = new FriendCell();
            friendCell.setNameLabelText(item.getFirstName() + " " + item.getLastName());
            friendCell.getProfileCircle().setFill(Color.valueOf(item.getHexProfileColor()));

            initRemoveFriendButtonFor(item, friendCell);
            initOpenChatButton(item, friendCell);

            setText(null);
            setGraphic(friendCell.getAnchorRoot());
        }
    }

    private void initOpenChatButton(User item, FriendCell friendCell) {
        friendCell.getOpenChatButton().setOnAction(param -> {
            ChatWindow chat = new ChatWindow(srv, item);

            if (borderPane.getCenter() == null) {
                slideInChat(chat);
            }
            else {
                fadeInChat(chat);
            }
        });
    }

    private void fadeInChat(ChatWindow chat) {
        Transition transition1 = Animations.fadeIn(
                chat.getMessagesScrollPane(),
                400d
        );
        Transition transition2 = Animations.fadeIn(
                chat.getUserNameLabel(),
                400d
        );
        ParallelTransition transition = new ParallelTransition(transition1, transition2);
        borderPane.setCenter(chat.getMainAnchor());
        transition.play();
    }

    private void slideInChat(ChatWindow chat) {
        Transition transition = Animations.verticalSlideAnimation(
                chat.getMainAnchor(),
                600d,
                -600d,
                300d
        );
        borderPane.setCenter(chat.getMainAnchor());
        transition.play();
    }

    private void initRemoveFriendButtonFor(User item, FriendCell friendCell) {
        friendCell.getRemoveFriendButton().setOnAction(param -> {
            srv.removeFriend(item);
            friends.remove(item);
        });
    }
}

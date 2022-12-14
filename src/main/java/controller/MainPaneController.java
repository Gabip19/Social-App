package controller;

import domain.Friendship;
import domain.User;
import gui.components.FriendListCell;
import gui.components.FriendshipListCell;
import gui.components.UserListCell;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class MainPaneController extends GuiController {
    public Button logOutButton;
    public Label userNameLabel;
    public Button messagesButton;
    public Button requestsButton;
    public TextField searchBar;
    public Button homeButton;
    public Button friendsButton;
    public AnchorPane midWindow;
    public HBox topHBox;
    public AnchorPane rootAnchor;
    public BorderPane borderPane;
    public ListView<User> searchUsersListView = new ListView<>();

    // FRIEND REQUESTS LIST
    public ListView<Friendship> requestsListView;
    private final ObservableList<Friendship> friendRequests = FXCollections.observableArrayList();

    // FRIENDS LIST
    public ListView<User> friendsListView;
    private final ObservableList<User> currentUserFriends = FXCollections.observableArrayList();

    public void initialize() {
        defineDraggableNode(topHBox);

        srv.signIn("test@test.test", "test");

//        srv.getUsers().forEach(srv::sendFriendRequest);

        // TODO: 12/13/22 init function for searchUserListView
        rootAnchor.getChildren().add(searchUsersListView);
        searchUsersListView.setCellFactory(param -> new UserListCell());
        searchUsersListView.prefWidthProperty().bind(Bindings.add(0, searchBar.widthProperty()));
        searchBar.layoutXProperty().addListener(param -> {
            searchUsersListView.setLayoutX(searchBar.getLayoutX());
        });

        initializeFriendRequestsListView();
        initializeFriendListView();

        userNameLabel.setText(srv.getCurrentUser().getFirstName());

        searchBar.onInputMethodTextChangedProperty().addListener(param -> searchForUsersAction());
    }

    private void initializeFriendRequestsListView() {
        requestsListView.setCellFactory(param -> new FriendshipListCell(srv, friendRequests));
        friendRequests.setAll(srv.getFriendRequestsForUser(srv.getCurrentUser()));
        requestsListView.setItems(friendRequests);
        requestsListView.setPrefWidth(400);
        borderPane.setRight(requestsListView);
    }

    private void initializeFriendListView() {
        friendsListView.setCellFactory(param -> new FriendListCell(srv, currentUserFriends));
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));
        friendsListView.setItems(currentUserFriends);
        borderPane.setLeft(friendsListView);
    }

    public void searchForUsersAction() {
        if (!searchBar.getText().equals("")) {
            searchUsersListView.setLayoutY(searchBar.getLayoutY() + 60);
            var resultSet = srv.getUsersWithName(searchBar.getText());
            searchUsersListView.setItems(FXCollections.observableArrayList(resultSet));
            searchUsersListView.setPrefHeight(resultSet.size() * 30 + 20);
            searchUsersListView.setVisible(true);
        } else {
            searchUsersListView.setVisible(false);
        }
    }

    public void showRequests() {
        if (borderPane.getRight() == null) {
            showRequestsPanel();
        } else {
            hideRequestsPanel();
        }
    }

    private void hideRequestsPanel() {

    }

    private void showRequestsPanel() {

    }

    public void showFriends() {
        System.out.println("\n\n");
        var friends = srv.getFriendsForUser(srv.getCurrentUser());
        friends.forEach(System.out::println);
        System.out.println("\n");
        currentUserFriends.setAll(friends);
//        currentUserFriends.forEach(System.out::println);
        if (borderPane.getLeft() == null) {
            showFriendsPanel();
        } else {
            hideFriendsPanel();
        }
    }

    private void hideFriendsPanel() {
        TranslateTransition tr = getXTransition(
                friendsListView,
                0d,
                -1 * friendsListView.getWidth(),
                700d
        );
        tr.setOnFinished(param -> {
            borderPane.setLeft(null);
        });
        tr.play();
    }

    private void showFriendsPanel() {
        TranslateTransition tr = getXTransition(
                friendsListView,
                -1 * friendsListView.getWidth(),
                friendsListView.getWidth(),
                700d
        );
        borderPane.setLeft(friendsListView);
        tr.play();
    }

    private TranslateTransition getXTransition(Node node, Double fromX, Double ByX, Double duration) {
        TranslateTransition tr = new TranslateTransition();
        tr.setFromX(fromX);
        tr.setByX(ByX);
        tr.setDuration(Duration.millis(duration));
        tr.setNode(node);
        return tr;
    }
}

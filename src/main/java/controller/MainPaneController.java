package controller;

import domain.Friendship;
import domain.User;
import gui.components.FriendListCell;
import gui.components.FriendshipListCell;
import gui.components.UserListCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

// TODO: 12/13/22 refactor friend cells
public class MainPaneController extends GuiController {
    public Button logOutButton;
    public Label userNameLabel;
    public Button messagesButton;
    public Button requestsButton;
    public TextField searchBar;
    public Button homeButton;
    public Button friendsButton;
    public AnchorPane midWindow;
    public ListView<User> friendsListView;
    public HBox topHBox;
    public AnchorPane rootAnchor;
    public BorderPane borderPane;
    public ListView<Friendship> requestsListView;

    public ListView<User> searchUsersListView = new ListView<>();
    private final ObservableList<User> currentUserFriends = FXCollections.observableArrayList();

    public void initialize() {

        srv.signIn("beni_eug@gmail.com", "beniamin");

        // TODO: 12/13/22 init function for searchUserListView
        rootAnchor.getChildren().add(searchUsersListView);
        searchUsersListView.setCellFactory(param -> new UserListCell());
        searchUsersListView.prefWidthProperty().bind(Bindings.add(0, searchBar.widthProperty()));
        searchBar.layoutXProperty().addListener(param -> {
            searchUsersListView.setLayoutX(searchBar.getLayoutX());
        });

        requestsListView.setCellFactory(param -> new FriendshipListCell(srv));
        requestsListView.setItems(FXCollections.observableArrayList(srv.getFriendRequestsForUser(srv.getCurrentUser())));
        requestsListView.setPrefWidth(500);
        borderPane.setRight(requestsListView);

        friendsListView.setCellFactory(param -> new FriendListCell());
        currentUserFriends.addAll(srv.getFriendsForUser(srv.getCurrentUser()));

        friendsListView.setItems(currentUserFriends);
        userNameLabel.setText(srv.getCurrentUser().getFirstName());

        searchBar.onInputMethodTextChangedProperty().addListener(param -> searchForUsersAction());
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

    public void showFriends() {
        if (borderPane.getLeft() == null) {
            borderPane.setLeft(friendsListView);
        } else {
            borderPane.setLeft(null);
        }
    }
}

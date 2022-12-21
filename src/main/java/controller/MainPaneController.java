package controller;

import domain.Friendship;
import domain.User;
import gui.components.FriendListCell;
import gui.components.FriendshipListCell;
import gui.components.UserListCell;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.Animations;

import java.io.IOException;
import java.util.ArrayList;

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

        initializeUserSearchListView();
        initializeFriendRequestsListView();
        initializeFriendListView();

        userNameLabel.setText(srv.getCurrentUser().getFirstName());

//        srv.getUsers().forEach(srv::sendFriendRequest);
    }

    private void initializeUserSearchListView() {
        searchBar.layoutXProperty().addListener(
                param -> searchUsersListView.setLayoutX(searchBar.getLayoutX()));
        rootAnchor.getChildren().add(searchUsersListView);
        searchUsersListView.setCellFactory(param -> new UserListCell(srv));
        searchUsersListView.prefWidthProperty().bind(Bindings.add(0, searchBar.widthProperty()));
        searchUsersListView.setVisible(false);
        searchUsersListView.setStyle("-fx-padding: 0px; -fx-background-radius: 0 0 30 30;");
    }

    private void initializeFriendRequestsListView() {
        reloadRequestsList();
        friendRequests.addListener((ListChangeListener<? super Friendship>) param -> reloadFriendsList());

        requestsListView.setCellFactory(param -> new FriendshipListCell(srv, friendRequests));
        requestsListView.setItems(friendRequests);
        requestsListView.setPrefWidth(400);

        borderPane.setRight(requestsListView);
    }

    private void initializeFriendListView() {
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));

        friendsListView.setCellFactory(param -> new FriendListCell(srv, currentUserFriends));
        friendsListView.setItems(currentUserFriends);

        borderPane.setLeft(friendsListView);
    }

    public void searchForUsersAction() {
        if (!searchBar.getText().equals("")) {
            showSearchView();
        } else {
            hideSearchView();
        }
    }

    private void showSearchView() {
        searchUsersListView.setLayoutY(searchBar.getLayoutY() + 60);
        var resultSet = srv.getUsersWithName(searchBar.getText());

        handleSearchUsersResult(resultSet);
    }

    private void hideSearchView() {
        searchUsersListView.setVisible(false);
        searchBar.setStyle("-fx-background-radius: 50");
    }

    private void handleSearchUsersResult(ArrayList<User> resultSet) {
        resultSet.remove(srv.getCurrentUser());
        if (!resultSet.isEmpty()) {
            searchUsersListView.setItems(FXCollections.observableArrayList(resultSet));
            searchUsersListView.setPrefHeight(resultSet.size() * 50 + 30);
            searchBar.setStyle("-fx-background-radius: 50 50 0 0");
            searchUsersListView.setVisible(true);
        } else {
            searchUsersListView.setVisible(false);
            searchBar.setStyle("-fx-background-radius: 50");
        }
    }

    public void toggleRequestsPanel() {
        if (borderPane.getRight() == null) {
            reloadRequestsList();
            showRequestsPanel();
        } else {
            hideRequestsPanel();
        }
    }

    private void showRequestsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                requestsListView,
                requestsListView.getWidth(),
                -1* requestsListView.getWidth(),
                700d
        );
        borderPane.setRight(requestsListView);
        transition.play();
    }

    private void hideRequestsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                requestsListView,
                0d,
                requestsListView.getWidth(),
                700d
        );
        transition.setOnFinished(param -> borderPane.setRight(null));
        transition.play();
    }

    private void reloadFriendsList() {
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));
    }

    private void reloadRequestsList() {
        friendRequests.setAll(srv.getFriendRequestsForUser(srv.getCurrentUser()));
    }

    public void toggleFriendsPanel() {
        if (borderPane.getLeft() == null) {
            reloadFriendsList();
            showFriendsPanel();
        } else {
            hideFriendsPanel();
        }
    }

    private void showFriendsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                friendsListView,
                -1 * friendsListView.getWidth(),
                friendsListView.getWidth(),
                700d
        );
        borderPane.setLeft(friendsListView);
        transition.play();
    }

    private void hideFriendsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                friendsListView,
                0d,
                -1 * friendsListView.getWidth(),
                700d
        );
        transition.setOnFinished(param -> borderPane.setLeft(null));
        transition.play();
    }

    public void logOut() {
        try {
            currentStage.close();
            switchToSignInScene();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void switchToSignInScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1146, 810);
        currentStage = new Stage();

        currentStage.setResizable(false);
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.setScene(scene);
        currentStage.show();
    }
}

package controller;

import domain.Friendship;
import domain.User;
import gui.components.FriendListCell;
import gui.components.FriendshipListCell;
import gui.components.UserListCell;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.util.Duration;

import java.io.IOException;

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


//        srv.getUsers().forEach(srv::sendFriendRequest);

        initializeUserSearchListView();
        initializeFriendRequestsListView();
        initializeFriendListView();

        userNameLabel.setText(srv.getCurrentUser().getFirstName());

        searchBar.onInputMethodTextChangedProperty().addListener(param -> searchForUsersAction());
    }

    private void initializeUserSearchListView() {
        rootAnchor.getChildren().add(searchUsersListView);
        searchUsersListView.setCellFactory(param -> new UserListCell(srv));
        searchUsersListView.prefWidthProperty().bind(Bindings.add(0, searchBar.widthProperty()));
        searchBar.layoutXProperty().addListener(param -> {
            searchUsersListView.setLayoutX(searchBar.getLayoutX());
        });
        searchUsersListView.setStyle("-fx-padding: 0px; -fx-background-radius: 0 0 30 30;");
    }

    private void initializeFriendRequestsListView() {
        friendRequests.setAll(srv.getFriendRequestsForUser(srv.getCurrentUser()));
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
            searchUsersListView.setLayoutY(searchBar.getLayoutY() + 60);
            var resultSet = srv.getUsersWithName(searchBar.getText());
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
        } else {
            searchUsersListView.setVisible(false);
            searchBar.setStyle("-fx-background-radius: 50");
        }
    }

    public void toggleRequestsPanel() {
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

    private void reloadFriendsList() {
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));
    }

    public void toggleFriendsPanel() {
        if (borderPane.getLeft() == null) {
            reloadFriendsList();
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
        Stage stage = new Stage();

        GuiController.setCurrentStage(stage);

        stage.setResizable(false);
        stage.setTitle("Colors App");
//        stage.getIcons().add(new Image("gui/testgui/styles/images/colorslogo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}

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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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


    /* ***********************************
     *                                   *
     *          FRIEND REQUESTS          *
     *                                   *
     *************************************/
    /**
     * Main Vbox holding all components regarding friend requests
     */
    public VBox friendRequestsVbox;
    /**
     * Button to switch between sent and incoming friend requests
     */
    public Button switchRequestsBtn;
    /**
     * Vbox holding the current displayed list view
     */
    public VBox requestsViewVBox;
    /**
     * Flag for knowing which list view is currently displayed
     */
    public boolean showingSentRequests = false;
    /**
     * Incoming friend requests list view
     */
    public ListView<Friendship> inFriendReqListView;
    /**
     * Incoming friend requests list
     */
    private final ObservableList<Friendship> incomingFriendRequests = FXCollections.observableArrayList();
    /**
     * Sent friend requests list view
     */
    public ListView<Friendship> sentFriendReqListView;
    /**
     * Sent friend request list
     */
    public final ObservableList<Friendship> sentFriendRequests = FXCollections.observableArrayList();


    /* ***********************************
     *                                   *
     *           FRIENDS LIST            *
     *                                   *
     *************************************/
    public ListView<User> friendsListView;
    private final ObservableList<User> currentUserFriends = FXCollections.observableArrayList();
    public VBox friendsVbox;

    public void initialize() {
        defineDraggableNode(topHBox);

        srv.signIn("test@test.test", "test");

        initializeUserSearchListView();
        initializeFriendRequestsListViews();
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

    private void initializeFriendRequestsListViews() {
        requestsViewVBox.getChildren().clear();
        requestsViewVBox.getChildren().add(inFriendReqListView);

        initializeIncomingReqListView();
        initializeSentReqListView();

        borderPane.setRight(friendRequestsVbox);
    }

    private void initializeSentReqListView() {
        reloadSentRequestsList();

        sentFriendReqListView.setCellFactory(param -> new FriendshipListCell(srv, sentFriendRequests));
        sentFriendReqListView.setItems(sentFriendRequests);
        sentFriendReqListView.setPrefWidth(400);
    }

    private void initializeIncomingReqListView() {
        reloadIncomingRequestsList();
        incomingFriendRequests.addListener((ListChangeListener<? super Friendship>) param -> reloadFriendsList());

        inFriendReqListView.setCellFactory(param -> new FriendshipListCell(srv, incomingFriendRequests));
        inFriendReqListView.setItems(incomingFriendRequests);
        inFriendReqListView.setPrefWidth(400);
    }

    private void initializeFriendListView() {
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));

        friendsListView.setCellFactory(param -> new FriendListCell(srv, currentUserFriends));
        friendsListView.setItems(currentUserFriends);

        borderPane.setLeft(friendsVbox);
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
            reloadRequestsPanelCurrentList();
            showRequestsPanel();
        } else {
            hideRequestsPanel();
        }
    }

    private void reloadRequestsPanelCurrentList() {
        if (showingSentRequests)
            reloadSentRequestsList();
        else reloadIncomingRequestsList();
    }

    private void showRequestsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                friendRequestsVbox,
                friendRequestsVbox.getWidth(),
                -1* friendRequestsVbox.getWidth(),
                700d
        );
        borderPane.setRight(friendRequestsVbox);
        transition.play();
    }

    private void hideRequestsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                friendRequestsVbox,
                0d,
                friendRequestsVbox.getWidth(),
                700d
        );
        transition.setOnFinished(param -> borderPane.setRight(null));
        transition.play();
    }

    private void reloadFriendsList() {
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));
    }

    private void reloadIncomingRequestsList() {
        incomingFriendRequests.setAll(srv.getFriendRequestsForUser(srv.getCurrentUser()));
    }

    private void reloadSentRequestsList() {
        sentFriendRequests.setAll(srv.getFriendRequestsFromUser(srv.getCurrentUser()));
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
                friendsVbox,
                -1 * friendsVbox.getWidth(),
                friendsVbox.getWidth(),
                700d
        );
        borderPane.setLeft(friendsVbox);
        transition.play();
    }

    private void hideFriendsPanel() {
        Transition transition = Animations.horizontalSlideAnimation(
                friendsVbox,
                0d,
                -1 * friendsVbox.getWidth(),
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

    public void switchRequestsPanel() {
        if (showingSentRequests) {
            reloadIncomingRequestsList();
            switchRequestsBtn.setText("Sent");
            hideSentFriendRequests();
        } else {
            reloadSentRequestsList();
            switchRequestsBtn.setText("Incoming");
            hideIncomingFriendRequests();
        }
        showingSentRequests = !showingSentRequests;
    }

    private void showIncomingFriendRequests() {
        Transition transition = Animations.fadeIn(inFriendReqListView, 600d);
        requestsViewVBox.getChildren().add(inFriendReqListView);
        transition.play();
    }

    private void hideIncomingFriendRequests() {
        Transition transition = Animations.fadeOut(inFriendReqListView, 600d);
        transition.setOnFinished(param -> {
            requestsViewVBox.getChildren().clear();
            showSentFriendRequests();
        });
        transition.play();
    }

    private void showSentFriendRequests() {
        Transition transition = Animations.fadeIn(sentFriendReqListView, 600d);
        requestsViewVBox.getChildren().add(sentFriendReqListView);
        transition.play();
    }

    private void hideSentFriendRequests() {
        Transition transition = Animations.fadeOut(sentFriendReqListView, 600d);
        transition.setOnFinished(param -> {
            requestsViewVBox.getChildren().clear();
            showIncomingFriendRequests();
        });
        transition.play();
    }
}

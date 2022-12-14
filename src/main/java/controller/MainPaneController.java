package controller;

import domain.Friendship;
import domain.User;
import gui.components.FriendCell;
import gui.components.FriendListCell;
import gui.components.FriendshipListCell;
import gui.components.UserListCell;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.List;

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


    public ListView<Friendship> requestsListView;

    public ListView<User> searchUsersListView = new ListView<>();


    public ListView<User> friendsListView;
    private ObservableList<User> currentUserFriends;


    public void initialize() {
        defineDraggableNode(topHBox);

//        srv.signIn("beni_eug@gmail.com", "beniamin");
        srv.signIn("test@test.test", "test");

//        srv.getUsers().forEach(friendToAdd -> {
//            if (!friendToAdd.equals(srv.getCurrentUser())) {
//                srv.sendFriendRequest(friendToAdd);
//            }
//        });

//        initializeSearchListView();
//        initializeFriendsListView();


        List<User> friends = srv.getFriendsForUser(srv.getCurrentUser());
        currentUserFriends = FXCollections.observableArrayList(friends);
        friendsListView = new ListView<>();
        friendsListView.setPrefHeight(746.0);
        friendsListView.setPrefWidth(356.0);

        friendsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
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
        });


        friendsListView.setItems(currentUserFriends);
        borderPane.setLeft(friendsListView);





        requestsListView.setCellFactory(param -> new FriendshipListCell(srv));
        requestsListView.setItems(FXCollections.observableArrayList(srv.getFriendRequestsForUser(srv.getCurrentUser())));
        requestsListView.setPrefWidth(500);
        borderPane.setRight(requestsListView);


        searchBar.onInputMethodTextChangedProperty().addListener(param -> searchForUsersAction());
    }

    private void initializeFriendsListView() {
        friendsListView.setCellFactory(param -> new FriendListCell(srv));
        currentUserFriends.addAll(srv.getFriendsForUser(srv.getCurrentUser()));
        friendsListView.setItems(currentUserFriends);
        userNameLabel.setText(srv.getCurrentUser().getFirstName());
    }

    private void initializeSearchListView() {
        rootAnchor.getChildren().add(searchUsersListView);
        searchUsersListView.setCellFactory(param -> new UserListCell());
        searchUsersListView.setLayoutY(searchBar.getLayoutY() + 60);

        searchUsersListView.prefWidthProperty().bind(Bindings.add(0, searchBar.widthProperty()));
        searchBar.layoutXProperty().addListener(param -> searchUsersListView.setLayoutX(searchBar.getLayoutX()));
    }

    public void searchForUsersAction() {
        if (!searchBar.getText().equals("")) {
            var resultSet = srv.getUsersWithName(searchBar.getText());
            searchUsersListView.setItems(FXCollections.observableArrayList(resultSet));
            searchUsersListView.setPrefHeight(resultSet.size() * 30 + 20);
            searchUsersListView.setVisible(true);
        } else {
            searchUsersListView.setVisible(false);
        }
    }

    public void showFriends() {
//        currentUserFriends.add(new User("AAA", "BBB", "CCC", LocalDate.now()));
        System.out.println("\n\n\n");
        srv.getFriendsForUser(srv.getCurrentUser()).forEach(System.out::println);
        currentUserFriends.setAll(srv.getFriendsForUser(srv.getCurrentUser()));
    }
}

package controller;

import domain.User;
import gui.FriendListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

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

    public void initialize() {
        friendsListView.setCellFactory(param -> new FriendListCell());
        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(
                new User("Nume1", "Nume2", "email@e.com", LocalDate.of(2000, 12, 12)),
                new User("Nume 12341234", "Nume2 6432", "email1@e.com", LocalDate.of(2000, 12, 12)),
                new User("Nume 6 443433", "Nume2 6342222", "email2@e.com", LocalDate.of(2000, 12, 12)),
                new User("Nume1 52125", "Nume2 2512", "email3@e.com", LocalDate.of(2000, 12, 12))
        );
        friendsListView.setItems(users);
    }
}

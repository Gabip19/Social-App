package controller;

import domain.User;
import gui.FriendListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;

public class FriendListViewController {
    @FXML
    public ListView<User> mainView;

    public void initialize() {
        mainView.setCellFactory(param -> new FriendListCell());

        ObservableList<User> users = FXCollections.observableArrayList();
        users.addAll(
            new User("Nume1", "Nume2", "email@e.com", LocalDate.of(2000, 12, 12)),
            new User("Nume 12341234", "Nume2 6432", "email1@e.com", LocalDate.of(2000, 12, 12)),
            new User("Nume 6 443433", "Nume2 6342222", "email2@e.com", LocalDate.of(2000, 12, 12)),
            new User("Nume1 52125", "Nume2 2512", "email3@e.com", LocalDate.of(2000, 12, 12))
        );
        mainView.setItems(users);
    }
}

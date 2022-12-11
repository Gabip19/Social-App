package gui;

import config.ApplicationContext;
import controller.SignInController;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repository.database.DatabaseTables;
import repository.database.FriendshipDatabaseRepo;
import repository.database.UserDatabaseRepo;
import service.FriendshipService;
import service.Network;
import service.UserService;

import java.io.IOException;

// TODO: 12/11/22 set background and icon
public class GUI extends Application {
    private Network service;

    @Override
    public void start(Stage stage) throws IOException {
        createNetwork();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1146, 810);

        SignInController signInController = fxmlLoader.getController();
        signInController.setSrv(service);

        stage.setResizable(false);
        stage.setTitle("Colors App");
//        stage.getIcons().add(new Image("gui/testgui/styles/images/colorslogo.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void createNetwork() {
        this.service = new Network(
                new UserService(new UserDatabaseRepo(
                        DatabaseTables.users.toString(),
                        ApplicationContext.DATABASE_URL,
                        ApplicationContext.DB_USERNAME,
                        ApplicationContext.DB_PASSWORD,
                        new UserValidator()
                )),
                new FriendshipService(new FriendshipDatabaseRepo(
                        DatabaseTables.friendships.toString(),
                        ApplicationContext.DATABASE_URL,
                        ApplicationContext.DB_USERNAME,
                        ApplicationContext.DB_PASSWORD,
                        new FriendshipValidator()
                ))
        );
    }
}
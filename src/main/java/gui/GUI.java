package gui;

import config.ApplicationContext;
import controller.GuiController;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import repository.database.DatabaseTables;
import repository.database.FriendshipDatabaseRepo;
import repository.database.TextMessageDatabaseRepo;
import repository.database.UserDatabaseRepo;
import service.FriendshipService;
import service.Network;
import service.TextMessageService;
import service.UserService;

import java.io.IOException;

// TODO: 12/11/22 set background and icon
public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        GuiController.setSrv(createNetwork());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1330, 810);

        GuiController.setCurrentStage(stage);

        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("gui/styles/images/colorslogo.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Network createNetwork() {
        return new Network(
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
                )),
                new TextMessageService(new TextMessageDatabaseRepo(
                        DatabaseTables.messages.toString(),
                        ApplicationContext.DATABASE_URL,
                        ApplicationContext.DB_USERNAME,
                        ApplicationContext.DB_PASSWORD
                ))
        );
    }
}
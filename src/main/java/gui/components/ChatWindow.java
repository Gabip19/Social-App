package gui.components;

import domain.TextMessage;
import domain.User;
import gui.components.message.MessageBoxFactory;
import javafx.animation.Transition;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import service.Network;
import utils.Animations;

import java.io.IOException;
import java.net.URL;

public class ChatWindow {

    public AnchorPane mainAnchor;
    public Label userNameLabel;
    public Button closeButton;
    public ScrollPane messagesScrollPane;
    public VBox messagesVBox;
    public TextField inputField;
    public Button sendButton;
    public static final URL fxmlLocation = FriendCell.class.getResource("/gui/chat-view.fxml");
    private final Network srv;
    private final User user;

    public ChatWindow(Network srv, User user) {
        this.srv = srv;
        this.user = user;

        loadChatView();
        initSendActions();
        initCloseAction();
        loadMessages();

        messagesVBox.heightProperty().addListener(param ->
                messagesScrollPane.setVvalue(messagesVBox.getHeight())
        );

        userNameLabel.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void initSendActions() {
        sendButton.setOnAction(param -> sendMessage());
        inputField.setOnAction(param -> sendMessage());
    }

    private void initCloseAction() {
        closeButton.setOnAction(param -> {
            Transition transition = Animations.verticalSlideAnimation(
                    mainAnchor,
                    0d,
                    600d,
                    300d
            );
            transition.setOnFinished(param2 -> {
                ((BorderPane) mainAnchor.getParent()).setCenter(null);
            });
            transition.play();
        });
    }

    private void sendMessage() {
        String messageContent = inputField.getText();
        messageContent = messageContent.trim();
        if (!messageContent.isEmpty()) {
            displayMessage(srv.sendMessage(user, messageContent));
            inputField.clear();
        }
    }

    private void loadMessages() {
        srv.getMessagesWithUsers(user).forEach(this::displayMessage);
    }

    private void displayMessage(TextMessage messageToDisplay) {
        if (messageToDisplay.getSenderID().equals(user.getId())) {
            messagesVBox.getChildren().add(
                    MessageBoxFactory.newReceiverMessageBox(messageToDisplay)
            );
        } else {
            messagesVBox.getChildren().add(
                    MessageBoxFactory.newSenderMessageBox(messageToDisplay)
            );
        }
    }

    private void loadChatView() {
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AnchorPane getMainAnchor() {
        return mainAnchor;
    }

    public ScrollPane getMessagesScrollPane() {
        return messagesScrollPane;
    }

    public Label getUserNameLabel() {
        return userNameLabel;
    }
}

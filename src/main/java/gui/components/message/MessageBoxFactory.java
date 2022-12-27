package gui.components.message;

import domain.TextMessage;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;

public class MessageBoxFactory {
    private static MessageBox createMessageBox(TextMessage messageContent) {
        return new MessageBox(messageContent);
    }

    public static MessageBox newSenderMessageBox(TextMessage textMessage) {
        MessageBox messageBox = createMessageBox(textMessage);
        messageBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        messageBox.getTextFlow().setStyle("-fx-background-radius: 20px; -fx-background-color: #d747cd"); // #be4eb7 #c241b9
        return messageBox;
    }

    public static MessageBox newReceiverMessageBox(TextMessage textMessage) {
        MessageBox messageBox = createMessageBox(textMessage);
        messageBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        messageBox.getTextFlow().setStyle("-fx-background-radius: 20px; -fx-background-color: #7384F7FF");
        messageBox.getTextFlow().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        return messageBox;
    }
}

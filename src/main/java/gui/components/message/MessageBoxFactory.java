package gui.components.message;

import javafx.geometry.Pos;

public class MessageBoxFactory {
    private static MessageBox createMessageBox(String messageContent) {
        MessageBox messageBox = new MessageBox(messageContent);
        return messageBox;
    }

    public static MessageBox newSenderMessageBox(String messageContent) {
        MessageBox messageBox = createMessageBox(messageContent);
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        return messageBox;
    }

    public static MessageBox newReceiverMessageBox(String messageContent) {
        MessageBox messageBox = createMessageBox(messageContent);
        messageBox.setAlignment(Pos.CENTER_LEFT);
        return messageBox;
    }
}

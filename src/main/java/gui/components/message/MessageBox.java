package gui.components.message;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessageBox extends HBox {
    public MessageBox(String messageContent) {
        super(); // 44.16
        Text text = new Text(messageContent);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5, 10, 5, 10));

        getChildren().add(textFlow);
    }
}

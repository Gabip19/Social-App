package gui.components.message;

import domain.TextMessage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;

public class MessageBox extends HBox {

    private final TextFlow textFlow;
    private final Label sentDateLabel;

    public MessageBox(TextMessage textMessage) {
        super();

        initHBox();
        textFlow = initText(textMessage);
        sentDateLabel = initDateLabel(textMessage);
        Region region = initRegion();

        getChildren().add(sentDateLabel);
        getChildren().add(region);
        getChildren().add(textFlow);
    }

    private static Region initRegion() {
        Region region = new Region();
        region.setMaxWidth(Double.MAX_VALUE);
//        region.setMinWidth(100);
        setHgrow(region, Priority.ALWAYS);
        return region;
    }

    private Label initDateLabel(TextMessage textMessage) {
        final Label sentDateLabel;
        String date = DateTimeFormatter.ofPattern("EEE HH:mm").format(textMessage.getSentDate());
        sentDateLabel = new Label(date);
        sentDateLabel.setMinWidth(USE_COMPUTED_SIZE);
        sentDateLabel.getStyleClass().add("msg-date-label");
        return sentDateLabel;
    }

    private TextFlow initText(TextMessage textMessage) {
        final TextFlow textFlow;
        Text text = new Text(textMessage.getText());
        text.setFill(Color.WHITE);
        textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(7, 10, 7, 10));
        textFlow.setMaxWidth(220);
        return textFlow;
    }

    private void initHBox() {
        setAlignment(Pos.CENTER_LEFT);
        getStyleClass().add("message-hbox");
        getStylesheets().add("gui/styles/chatCSS.css");
        setPadding(new Insets(5, 3, 5, 3));
    }

    public TextFlow getTextFlow() {
        return textFlow;
    }
}

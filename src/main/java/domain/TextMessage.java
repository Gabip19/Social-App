package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class TextMessage extends Entity<UUID> {
    private UUID senderID;
    private UUID receiverID;
    private String text;
    private LocalDateTime sentDate;

    public TextMessage(UUID senderID, UUID receiverID, String text, LocalDateTime date) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.text = text;
        this.sentDate = date;
        setId(UUID.randomUUID());
    }

    public TextMessage(UUID id, UUID senderID, UUID receiverID, String text, LocalDateTime date) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.text = text;
        this.sentDate = date;
        setId(id);
    }

    public UUID getSenderID() {
        return senderID;
    }

    public void setSenderID(UUID senderID) {
        this.senderID = senderID;
    }

    public UUID getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(UUID receiverID) {
        this.receiverID = receiverID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextMessage that = (TextMessage) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

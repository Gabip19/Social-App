package service;

import domain.TextMessage;
import domain.User;
import repository.database.TextMessageDatabaseRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class TextMessageService {
    private final TextMessageDatabaseRepo messageRepo;

    public TextMessageService(TextMessageDatabaseRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public TextMessage addMessage(UUID senderId, UUID receiverId, String text, LocalDateTime sentDate) {
        TextMessage textMessage = new TextMessage(senderId, receiverId, text, sentDate);
        return messageRepo.save(textMessage);
    }

    ArrayList<TextMessage> getMessagesBetweenUsers(User user1, User user2) {
        return new ArrayList<>((Collection<TextMessage>) messageRepo.getMessagesBetweenUsers(user1.getId(), user2.getId()));
    }
}

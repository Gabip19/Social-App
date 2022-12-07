package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Friendship extends Entity<UUID> {
    private UUID senderID;
    private UUID receiverID;
    private LocalDateTime friendshipDate;
    private FriendshipStatus friendshipStatus;

    /**
     * Creates a friendship with the given IDs.
     * <p> The lower UUID will be always stored first </p>
     * @param senderID first user's id
     * @param receiverID first user's id
     */
    public Friendship(UUID senderID, UUID receiverID, FriendshipStatus status) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.setId(UUID.randomUUID());
        this.friendshipDate = LocalDateTime.now();
        this.friendshipStatus = status;
    }

    public Friendship(UUID friendshipID, UUID senderID, UUID receiverID, String friendsFromDate, FriendshipStatus friendshipStatus) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.setId(friendshipID);
        friendshipDate = LocalDateTime.parse(friendsFromDate);
        this.friendshipStatus = friendshipStatus;
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

    public LocalDateTime getFriendshipDate() {
        return friendshipDate;
    }

    public void setFriendshipDate(LocalDateTime friendshipDate) {
        this.friendshipDate = friendshipDate;
    }

    public FriendshipStatus getFriendshipStatus() {
        return friendshipStatus;
    }

    public void setFriendshipStatus(FriendshipStatus friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }

    public boolean containsID(UUID id) {
        return this.senderID.equals(id) || this.receiverID.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return that.containsID(senderID) && that.containsID(receiverID);
    }

    @Override
    public int hashCode() {
        if (senderID.compareTo(receiverID) < 0)
            return Objects.hash(senderID, receiverID);
        else return Objects.hash(receiverID, senderID);
    }

    @Override
    public String toString() {
        return "FriendShip: " +
                "User1_ID= " + senderID +
                ", User2_ID= " + receiverID +
                ", ID= " + getId() +
                ", FriendsFrom= " + getFriendshipDate() +
                ", FriendshipStatus= " + getFriendshipStatus();
    }
}

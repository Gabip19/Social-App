package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Friendship extends Entity<UUID> {
    private UUID user1ID;
    private UUID user2ID;

    private LocalDateTime friendsFrom;

    /**
     * Creates a friendship with the given IDs.
     * <p> The lower UUID will be always stored first </p>
     * @param user1ID first user's id
     * @param user2ID first user's id
     */
    public Friendship(UUID user1ID, UUID user2ID) {
        if (user1ID.compareTo(user2ID) < 0) {
            this.user1ID = user1ID;
            this.user2ID = user2ID;
        }
        else {
            this.user1ID = user2ID;
            this.user2ID = user1ID;
        }
        this.setId(UUID.randomUUID());
        this.friendsFrom = LocalDateTime.now();
    }

    public Friendship(UUID friendshipID, UUID user1ID, UUID user2ID, String date) {
        if (user1ID.compareTo(user2ID) < 0) {
            this.user1ID = user1ID;
            this.user2ID = user2ID;
        }
        else {
            this.user1ID = user2ID;
            this.user2ID = user1ID;
        }
        this.setId(friendshipID);
        friendsFrom = LocalDateTime.parse(date);
    }

    public UUID getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(UUID user1ID) {
        this.user1ID = user1ID;
    }

    public UUID getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(UUID user2ID) {
        this.user2ID = user2ID;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    public boolean containsID(UUID id) {
        return this.user1ID.equals(id) || this.user2ID.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1ID, that.user1ID) && Objects.equals(user2ID, that.user2ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1ID, user2ID);
    }

    @Override
    public String toString() {
        return "FriendShip: " +
                "User1_ID= " + user1ID +
                ", User2_ID= " + user2ID +
                ", ID= " + getId() +
                ", FriendsFrom= " + getFriendsFrom();
    }
}

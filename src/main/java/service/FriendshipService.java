package service;

import domain.Friendship;
import domain.FriendshipStatus;
import domain.User;
import domain.validators.exceptions.FriendshipException;
import repository.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class FriendshipService {
    private final Repository<UUID, Friendship> friendRepo;

    public FriendshipService(Repository<UUID, Friendship> friendRepo) {
        this.friendRepo = friendRepo;
    }

    public List<Friendship> getFriendships() {
        return new ArrayList<>( (Collection<Friendship>) friendRepo.findAll());
    }

    /**
     * Loads into the user's friends list the IDs of his current friends.
     * The load occurs only if the list is empty
     * @param user the user for which to determin friends' IDs
     */
    public void loadFriends(User user) {
        if (user.getFriendIDs().isEmpty()) {
            UUID userId = user.getId();
            friendRepo.findAll().forEach(
                    (Friendship x) -> {
                        if (x.getFriendshipStatus().equals(FriendshipStatus.ACCEPTED)) {
                            if (x.getUser1ID().equals(userId))
                                user.getFriendIDs().add(x.getUser2ID());
                            else if (x.getUser2ID().equals(userId))
                                user.getFriendIDs().add(x.getUser1ID());
                        }
                    }
            );
        }
    }
    
    public void addFriendship(Friendship friendshipToAdd) throws FriendshipException {
        friendRepo.findAll().forEach((Friendship x) -> {
            if (x.equals(friendshipToAdd)) {
                throwExistingFriendshipException(x);
            }
        });

        friendRepo.save(friendshipToAdd);
    }

    private static void throwExistingFriendshipException(Friendship x) throws FriendshipException {
        if (x.getFriendshipStatus().equals(FriendshipStatus.ACCEPTED))
            throw new FriendshipException("Already friend with that user.\n");
        if (x.getFriendshipStatus().equals(FriendshipStatus.PENDING))
            throw new FriendshipException("A pending friend request already exists.\n");
        if (x.getFriendshipStatus().equals(FriendshipStatus.REJECTED))
            throw new FriendshipException("User already rejected your friend request.\n");
    }

    public void removeFriendship(Friendship friendshipToRemove) throws FriendshipException {
        for (Friendship friendship : friendRepo.findAll()) {
            if (friendship.equals(friendshipToRemove)) {
                friendRepo.delete(friendship.getId());
                return;
            }
        }
        throw new FriendshipException();
    }

    public Friendship delete(UUID id) {
        return friendRepo.delete(id);
    }
}

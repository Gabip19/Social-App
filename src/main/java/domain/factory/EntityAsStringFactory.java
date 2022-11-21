package domain.factory;

import domain.Entity;
import domain.Friendship;
import domain.User;

import java.util.UUID;

public class EntityAsStringFactory {
    public static <E extends Entity<UUID>> String getStringFromEntity(E entity) {
        if (entity instanceof User entityUser) {
            return entityUser.getId() + ";"
                    + entityUser.getFirstName() + ";"
                    + entityUser.getLastName() + ";"
                    + entityUser.getEmail() + ";"
                    + entityUser.getBirthdate();
        } else if (entity instanceof Friendship entityFriendship) {
            return entityFriendship.getId() + ";"
                    + entityFriendship.getUser1ID() + ";"
                    + entityFriendship.getUser2ID() + ";"
                    + entityFriendship.getFriendsFrom();
        }
        throw new RuntimeException("Unknown entity type.\n");
    }
}

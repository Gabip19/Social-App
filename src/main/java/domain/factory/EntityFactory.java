package domain.factory;

import domain.Entity;
import domain.Friendship;
import domain.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//TODO maybe refactor generic method into separated methods
public class EntityFactory {
    public @SuppressWarnings("unchecked") static <E extends Entity<UUID>> E createEntity(EntityTypeEnum entityType, List<String> attr) {
        if (entityType == EntityTypeEnum.USER) {
            User user = new User(attr.get(1), attr.get(2), attr.get(3), LocalDate.parse(attr.get(4)));
            user.setId(UUID.fromString(attr.get(0)));

            return (E) user;
        }
        else if (entityType == EntityTypeEnum.FRIENDSHIP) {
            return (E) new Friendship(
                    UUID.fromString(attr.get(0)),
                    UUID.fromString(attr.get(1)),
                    UUID.fromString(attr.get(2)),
                    attr.get(3)
            );
        }
        return null;
    }
}

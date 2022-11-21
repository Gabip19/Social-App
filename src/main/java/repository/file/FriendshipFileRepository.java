package repository.file;

import domain.Friendship;
import domain.validators.Validator;
import domain.factory.EntityAsStringFactory;
import domain.factory.EntityFactory;
import domain.factory.EntityTypeEnum;

import java.util.List;
import java.util.UUID;

public class FriendshipFileRepository extends AbstractFileRepository<UUID, Friendship> {

    public FriendshipFileRepository(Validator<Friendship> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Friendship extractEntity(List<String> attr) {
        return EntityFactory.createEntity(EntityTypeEnum.FRIENDSHIP, attr);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return EntityAsStringFactory.getStringFromEntity(entity);
    }
}

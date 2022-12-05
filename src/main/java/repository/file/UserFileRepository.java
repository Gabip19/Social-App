package repository.file;

import domain.User;
import domain.validators.Validator;
import domain.factory.EntityAsStringFactory;
import domain.factory.EntityFactory;
import domain.factory.EntityTypes;

import java.util.List;
import java.util.UUID;

public class UserFileRepository extends AbstractFileRepository<UUID, User> {

    public UserFileRepository(Validator<User> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected User extractEntity(List<String> attr) {
        return EntityFactory.createEntity(EntityTypes.USER, attr);
    }

    @Override
    protected String createEntityAsString(User entity) {
        return EntityAsStringFactory.getStringFromEntity(entity);
    }
}

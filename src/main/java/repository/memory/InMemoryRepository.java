package repository.memory;

import domain.Entity;
import domain.validators.Validator;
import repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    private final Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id){
        if (id == null)
            throw new IllegalArgumentException("Id must not be null.\n");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");

        validator.validate(entity);

        if (entities.get(entity.getId()) != null) {
            throw new IllegalArgumentException("An entity with the given ID already exists.\n");
        }
        else entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E delete(ID id) {
        E entity = entities.get(id);
        if (entity != null) {
            entities.remove(id);
            return entity;
        }
        throw new IllegalArgumentException("Entity with the given ID does not exist.\n");
    }

    @Override
    public E update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must not be null.\n");
        validator.validate(entity);

        if (entities.get(entity.getId()) != null) {
            entities.put(entity.getId(), entity);
            return entity;
        }
        throw new IllegalArgumentException("The entity to be updated does not exist.\n");
    }

}

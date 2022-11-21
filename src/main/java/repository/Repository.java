package repository;

import domain.Entity;
import domain.validators.exceptions.UserValidationException;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> -  type of entities saved in repository
 */
public interface Repository<ID, E extends Entity<ID>> {

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     *          or null - if there is no entity with the given id
     * @throws IllegalArgumentException
     *                  if id is null.
     */
    E findOne(ID id);

    /**
     *
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     *
     * @param entity
     *         entity must be not null
     * @return the entity if it was saved properly
     * @throws UserValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    E save(E entity);

    /**
     *  removes the entity with the specified id
     * @param id
     *      id must be not null
     * @return the removed entity
     * @throws IllegalArgumentException
     *                   if the given id is null.
     */
    E delete(ID id);

    /**
     *
     * @param entity
     *          entity must not be null
     * @return the entity - if the entity is updated
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws UserValidationException
     *             if the entity is not valid.
     */
    E update(E entity);

}
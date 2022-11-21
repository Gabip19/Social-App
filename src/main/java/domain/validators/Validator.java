package domain.validators;

import domain.validators.exceptions.ValidationException;

public interface Validator<T> {
    /**
     * Validates an entity of type T.
     * Should be implemented specifically for the type
     * @param entity entity that will be validated
     * @throws ValidationException if entity fails validation
     */
    void validate(T entity) throws ValidationException;
}

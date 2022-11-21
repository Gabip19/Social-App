package domain.validators;

import domain.User;
import domain.validators.exceptions.UserValidationException;

import java.time.LocalDate;

/**
 * User entity validator
 */
public class UserValidator implements Validator<User> {
    /**
     * Validates a user
     * <p>A user is valid if:</p>
     * <p>- his names start with a letter</p>
     * <p>- his names length are between 2 and 60 characters</p>
     * <p>- his names contain only letters, numbers and "`" and "-" characters</p>
     * <p>- his email has a valid format</p>
     * @param user user that will be validated
     * @throws UserValidationException if the user does not meet the requirements
     */
    @Override
    public void validate(User user) throws UserValidationException {
        String errors = "";
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        LocalDate date = user.getBirthdate();

        if (lastName.length() < 2 || lastName.length() > 60 ||
            firstName.length() < 2 || firstName.length() > 60) {
            errors += "Name must have between 2 and 60 characters.\n";
        }
        if (!lastName.matches("^[a-zA-Z].*") || !firstName.matches("^[a-zA-Z].*")) {
            errors += "Name must start with a letter.\n";
        }
        if (!lastName.matches("^[a-zA-Z0-9-' ]+$") || !firstName.matches("^[a-zA-Z0-9-' ]+$")) {
            errors += "Unexpected symbol in name.\n";
        }
        if (!email.matches("^[a-z]+[a-z0-9._-]+@[a-z]+[.][a-z]+$")) {
            errors += "Invalid email format.\n";
        }
        if (date.isBefore(LocalDate.of(1800, 1,1)) || date.isAfter(LocalDate.now().minusYears(3))) {
            errors += "User must be at least 3 years old and no older than 200 years.\n";
        }

        // StringUtils.join(errors, "\n");
        if (!(errors.equals(""))) {
            throw new UserValidationException(errors);
        }
    }
}

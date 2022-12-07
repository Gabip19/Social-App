package runner;

import domain.Friendship;
import domain.FriendshipStatus;
import domain.User;
import domain.validators.UserValidator;
import domain.validators.Validator;
import domain.validators.exceptions.UserValidationException;

import java.time.LocalDate;

public class TestRunner {
    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        validatorTests();
    }

    private static void validatorTests() {
        Validator<User> validator = new UserValidator();
        User user1 = new User("A-SD F", "aaA SDF", "email@gmail.com", LocalDate.parse("2000-12-15"));
        try {
            validator.validate(user1);
            assert (true);
        } catch (UserValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}

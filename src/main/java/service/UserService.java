package service;

import domain.HashedPasswordDTO;
import domain.User;
import repository.database.UserDatabaseRepo;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class UserService {
//    private final Repository<UUID, User> userRepo;
    private final UserDatabaseRepo userRepo;

    public UserService(UserDatabaseRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>( (Collection<User>) userRepo.findAll());
    }

    public void addUser(String lastName, String firstName, String email, String birthdate, HashedPasswordDTO passwordInfo) throws RuntimeException {
        LocalDate date;
        try {
            date = LocalDate.parse(birthdate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Try: yyyy-MM-dd.\n");
        }

        userRepo.findAll().forEach(
                x -> {
                    if (x.getEmail().equals(email))
                        throw new RuntimeException("An user with the given email already exists.");
                }
        );

        User user = new User(firstName, lastName, email, date);
        userRepo.save(user);
        userRepo.updateUserPassword(user.getEmail(), passwordInfo);
    }

    public void removeUser(User user) {
        UUID id = user.getId();
        userRepo.delete(id);
    }

    public ArrayList<User> getUsersWithName(String string) {
        Collection<User> users = (Collection<User>) userRepo.findAll();
        final String stringToMatch = string.toLowerCase();
        return new ArrayList<>( users.stream()
                .filter(
                        (User x) -> {
                            String fullName = x.getLastName().toLowerCase() + " " + x.getFirstName().toLowerCase();
                            return fullName.contains(stringToMatch);
                        }
                )
                .toList()
        );
    }

    public User findOneUser(UUID userID) {
        return userRepo.findOne(userID);
    }

    public void updateUser(User userToUpdate, String newLastName, String newFirstName, String newBirthdate) throws RuntimeException {
        LocalDate date;
        try {
            date = LocalDate.parse(newBirthdate);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Try: yyyy-MM-dd.\n");
        }


        User updatedUser = new User(
                newFirstName,
                newLastName,
                userToUpdate.getEmail(),
                date
        );
        updatedUser.setId(userToUpdate.getId());

        userRepo.update(updatedUser);
    }
}

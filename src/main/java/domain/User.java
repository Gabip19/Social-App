package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID> {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private final List<UUID> friendIDs;

    public User(String firstName, String lastName, String email, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;

        this.setId(UUID.randomUUID());
        friendIDs = new ArrayList<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<UUID> getFriendIDs() {
        return friendIDs;
    }

    @Override
    public String toString() {
        return "first-name:   " + firstName +
                ", last-name:   " + lastName +
                ", email:   " + email +
                ", birthdate:   " + birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(friendIDs, user.friendIDs) &&
                Objects.equals(email, user.email) &&
                Objects.equals(getId(), ((User) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, getId());
    }
}

package domain;

import java.time.LocalDate;
import java.util.*;

public class User extends Entity<UUID> {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private final List<UUID> friendIDs;
    private final String hexProfileColor;

    public User(String firstName, String lastName, String email, LocalDate birthdate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        this.hexProfileColor = getProfileColor();

        this.setId(UUID.randomUUID());
        friendIDs = new ArrayList<>();
    }

    public User(UUID id, String firstName, String lastName, String email, LocalDate birthdate, String hexProfileColor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        this.hexProfileColor = hexProfileColor;

        this.setId(id);
        friendIDs = new ArrayList<>();
    }

    private String getProfileColor() {
        Random obj = new Random();
        int randNum = obj.nextInt(0xffffff + 1);
        return String.format("#%06x", randNum);
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

    public String getHexProfileColor() {
        return hexProfileColor;
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

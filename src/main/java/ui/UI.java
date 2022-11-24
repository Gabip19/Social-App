package ui;

import config.ApplicationContext;
import domain.User;
import repository.database.DatabaseTables;
import domain.validators.FriendshipValidator;
import domain.validators.UserValidator;
import repository.database.FriendshipDatabaseRepo;
import repository.database.UserDatabaseRepo;
import service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {

    private static final UI instance = new UI();
    private final Service srv;
    private final Scanner scanner = new Scanner(System.in);

    private UI() {
        srv = new Service(
//            new UserFileRepository(new UserValidator(), "src/main/java/repository/file/resources/users.txt"),
            new UserDatabaseRepo(
                    DatabaseTables.users.toString(),
                    ApplicationContext.DATABASE_URL,
                    ApplicationContext.DB_USERNAME,
                    ApplicationContext.DB_PASSWORD,
                    new UserValidator()
            ),
//            new FriendshipFileRepository(new FriendshipValidator(), "src/main/java/repository/file/resources/friendships.txt")
            new FriendshipDatabaseRepo(
                    DatabaseTables.friendships.toString(),
                    ApplicationContext.DATABASE_URL,
                    ApplicationContext.DB_USERNAME,
                    ApplicationContext.DB_PASSWORD,
                    new FriendshipValidator()
            )
        );
    }

    public static UI getInstance() {
        return instance;
    }

    public void run() {
        boolean running = true;
        int option;

        while (running) {
            try {
                option = getMenuOption();
                if (option != 0) {
                    executeOption(option);
                }
                else running = false;
            }
            catch (NumberFormatException e) {
                System.out.println("\nOption must be an integer.\n");
            }
            catch (Exception e) {
                System.out.println("\n" + e.getMessage());
                //e.printStackTrace();
            }
        }
    }

    private void executeOption(int option) {
        switch (option) {
            case 1 -> addUser();
            case 2 -> removeUser();
            case 3 -> printList(srv.getUsers());
            case 4 -> addFriend();
            case 5 -> removeFriend();
            case 6 -> printFriendships();
            case 7 -> numOfCommunities();
            case 8 -> mostActiveCommunity();
            case 9 -> updateUser();
            default -> System.out.println("\nInvalid option.\n");
        }
    }

    private int getMenuOption() {
        printMenu();
        System.out.print("\tYour option: ");
        String line = scanner.nextLine();
        return Integer.parseInt(line);
    }

    private void printMenu() {
        System.out.println("\n------------===< MENU >===------------\n");
        System.out.println("\t1. Add user.");
        System.out.println("\t2. Remove user.");
        System.out.println("\t3. Print all users.");
        System.out.println("\t4. Add friend.");
        System.out.println("\t5. Remove friend.");
        System.out.println("\t6. Print all friendships.");
        System.out.println("\t7. Print number of communities.");
        System.out.println("\t8. Print the most active community.");
        System.out.println("\t9. Update user.");
        System.out.println("\t0. EXIT");
        System.out.println("\n------------===< ---- >===------------");
    }

    private <T> void printList(List<T> list) {
        if (list.isEmpty()) {
//            System.out.println("List is empty.");
            throw new RuntimeException("List is empty.");
        }
        System.out.println();
        list.forEach( (T x) -> System.out.println(list.indexOf(x) + ". " + x));
        System.out.println("\n");
    }

    private int readIndex(String s) {
        System.out.print(s);
        String indexStr = scanner.nextLine();
        return Integer.parseInt(indexStr);
    }

    private ArrayList<User> getUsersBySubString() {
        System.out.print("Introduce substring: ");
        String substring = scanner.nextLine();
        return srv.getUsersWithName(substring);
    }

    private void addUser() {
        System.out.println("\n------------===< ADD >===------------\n");
        System.out.print("Introduce last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Introduce first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Introduce email: ");
        String email = scanner.nextLine();

        System.out.print("Introduce birthdate: ");
        String birthdate = scanner.nextLine();

        srv.addUser(lastName, firstName, email, birthdate);
        System.out.println("\nUser added successfully.\n");
    }

    private void removeUser() {
        System.out.println("\n------------===< REMOVE >===------------\n");
        System.out.println("Search user to be removed");

        ArrayList<User> matchingUsers = getUsersBySubString();

        printList(matchingUsers);

        if (!matchingUsers.isEmpty()) {
            int index = readIndex("Index of user to be removed: ");

            if (0 <= index && index < matchingUsers.size()) {
                srv.removeUser(matchingUsers.get(index));

                System.out.println("\nUser removed successfully.\n");
            }
            else throw new IndexOutOfBoundsException("Invalid index.\n");
        }
    }

    private void addFriend() {
        System.out.println("\n------------===< ADD FRIEND >===------------\n");
        System.out.println("Search for current user");

        ArrayList<User> matchingUsers = getUsersBySubString();
        printList(matchingUsers);

        if (!matchingUsers.isEmpty()) {

            int index = readIndex("Index of current user: ");
            User currentUser = matchingUsers.get(index);

            System.out.println("\nSearch for friends");
            matchingUsers = getUsersBySubString();
            printList(matchingUsers);

            if (!matchingUsers.isEmpty()) {
                index = readIndex("Index of user you want to add as friend: ");
                User friendUser = matchingUsers.get(index);

                srv.addFriendship(currentUser, friendUser);

                System.out.println(
                        "\n" + currentUser.getFirstName() +
                        " is now friend with "
                        + friendUser.getFirstName() + ".\n"
                );
                //printList(srv.getFriendsForUser(currentUser));
            }
        }

    }

    private void removeFriend() {
        System.out.println("\n------------===< REMOVE FRIEND >===------------\n");
        System.out.println("Search for current user");

        ArrayList<User> matchingUsers = getUsersBySubString();
        printList(matchingUsers);

        if (!matchingUsers.isEmpty()) {
            int index = readIndex("Index of current user: ");
            User currentUser = matchingUsers.get(index);

            ArrayList<User> friends = srv.getFriendsForUser(currentUser);
            printList(friends);
            index = readIndex("Index of friend to be removed: ");
            User friendToRemove = friends.get(index);

            srv.removeFriendship(currentUser, friendToRemove);

            System.out.println("\nFriend removed successfully.\n");
        }
    }

    private void printFriendships() {
        var a = srv.getFriendships().stream().map( x ->
                srv.findOneUser(x.getUser1ID()).getLastName()
                        + " is friend with " +
                        srv.findOneUser(x.getUser2ID()).getLastName()
                        + " since " +
                        x.getFriendsFrom() + "."
        ).toList();

        printList(a);
    }

    private void numOfCommunities() {
        System.out.println("\nThe number of communities is: " + srv.getNumOfCommunities() + "\n");
    }

    private void mostActiveCommunity() {
        System.out.println("\n------------===< MOST ACTIVE COMMUNITY >===------------\n");
        ArrayList<User> commUsers = srv.mostActiveCommunity();

        if (!commUsers.isEmpty()) {
            System.out.println("The most active community is formed by the following users:");
            printList(srv.mostActiveCommunity());
        }
        else System.out.println("\nNo existing community was found.\n");
    }

    private void updateUser() {
        System.out.println("\n------------===< UPDATE USER >===------------\n");
        System.out.println("Search for user:");

        ArrayList<User> matchingUsers = getUsersBySubString();
        printList(matchingUsers);

        if (!matchingUsers.isEmpty()) {

            int index = readIndex("Index of current user: ");
            User userToUpdate = matchingUsers.get(index);

            System.out.print("Introduce the new last name: ");
            String newLastName = scanner.nextLine();
            System.out.print("Introduce the new first name: ");
            String newFirstName = scanner.nextLine();
            System.out.print("Introduce the new birthdate: ");
            String newBirthdate = scanner.nextLine();

            srv.updateUser(userToUpdate, newLastName, newFirstName, newBirthdate);

            System.out.println("User updated successfully.\n");
        }
    }
}
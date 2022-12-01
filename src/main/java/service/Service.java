package service;

import domain.Friendship;
import domain.User;
import domain.validators.exceptions.FriendshipException;
import utils.Graph;

import java.util.*;

public class Service {
    private final UserService userSrv;
    private final FriendshipService friendSrv;

    public Service(UserService userSrv, FriendshipService friendSrv) {
        this.userSrv = userSrv;
        this.friendSrv = friendSrv;
    }

    /**
     * Returns the list of existing users
     * @return list of Users
     */
    public ArrayList<User> getUsers() {
        return userSrv.getUsers();
    }

    /**
     * Returns the list of existing friendships
     * @return list of Friendships
     */
    public List<Friendship> getFriendships() {
        return friendSrv.getFriendships();
    }

    /**
     * Returns the user with the given ID
     * @param userID the ID of the user to search for
     * @return the user with the given ID
     */
    public User findOneUser(UUID userID) {
        return userSrv.findOneUser(userID);
    }

    /**
     * Creates a new user then adds the user to the corresponding repository.
     * Automatically capitalizes the given names before using them
     *
     * @param lastName the last name of the user
     * @param firstName the first name of the user
     * @param birthdate birthdate of the user
     * @throws RuntimeException <p>if an invalid date is provided</p>
     *                          <p>if the email is already used by a user</p>
     */
    public void addUser(String lastName, String firstName, String email, String birthdate) throws RuntimeException {
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

        userSrv.addUser(lastName, firstName, email, birthdate);
    }

    /**
     * Finds the users whose full name contains a given string
     * (Case insensitive)
     * @param string the string that must be contained
     * @return a list with all the users respecting the rule
     */
    public ArrayList<User> getUsersWithName(String string) {
        return userSrv.getUsersWithName(string);
    }

    /**
     * Determines the users which are in a friendship with the given user
     * @param user the user whose friends will be determined
     * @return a list of all friends of USER
     */
    public ArrayList<User> getFriendsForUser(User user) {
        friendSrv.loadFriends(user);

        return new ArrayList<>(
                user.getFriendIDs().stream().map(userSrv::findOneUser).toList()
        );
    }

    /**
     * Deletes a given user from the corresponding repository.
     * All friendships containing the given user are also deleted.
     * @param user the user to be deleted
     */
    public void removeUser(User user) {
        userSrv.removeUser(user);

        removeFriendsForUser(user.getId());
    }

    /**
     * Deletes from the Friendship Repository all friendships containing a given user
     * @param id the ID of the user
     */
    private void removeFriendsForUser(UUID id) {
        ArrayList<UUID> fsIDsToRemove = new ArrayList<>();
        friendSrv.getFriendships().forEach(
                x -> {
                    if (x.containsID(id))
                        fsIDsToRemove.add(x.getId());
                }
        );

        fsIDsToRemove.forEach(friendSrv::delete);
    }

    /**
     * Creates a friendship between two given users then saves it.
     * <p>The users' friends lists are also updated.</p>
     * @param currentUser the user that sent the friend request
     * @param friendUser the user to be added as friend
     * @throws FriendshipException if the users are already friends
     */
    public void addFriendship(User currentUser, User friendUser) throws FriendshipException {
        final Friendship auxFriendS = new Friendship(currentUser.getId(), friendUser.getId());

        friendSrv.loadFriends(currentUser);
        friendSrv.loadFriends(friendUser);

        friendSrv.addFriendship(auxFriendS);

        currentUser.getFriendIDs().add(friendUser.getId());
        friendUser.getFriendIDs().add(currentUser.getId());
    }

    /**
     * Removes the friendship between two given users.
     * <p>The users' friends list are also updated.</p>
     * @param currentUser the user that wants to remove a friend
     * @param friendToRemove the friend that will be removed
     * @throws FriendshipException if the given users are not friends
     */
    public void removeFriendship(User currentUser, User friendToRemove) throws FriendshipException {
        final Friendship auxFriendS = new Friendship(currentUser.getId(), friendToRemove.getId());
        try {
            friendSrv.removeFriendship(auxFriendS);
            currentUser.getFriendIDs().remove(friendToRemove.getId());
            friendToRemove.getFriendIDs().remove(currentUser.getId());
        } catch (FriendshipException e) {
            throw new FriendshipException(
                    friendToRemove.getLastName() +
                            " is not a friend of " +
                            currentUser.getLastName() + ".\n"
            );
        }
    }

    /**
     * Creates an undirected graph where nodes represent users' IDs and
     * edges the friendship relation between the two users at the ends of the edge
     * @return the created graph
     */
    private Graph<UUID> createFriendsGraph() {
        Graph<UUID> graph = new Graph<>();

        friendSrv.getFriendships().forEach(
                x -> graph.addUndirectedEdge(x.getUser1ID(), x.getUser2ID())
        );
        return graph;
    }

    /**
     * Determines the number of communities in the network.
     * <p>(Community - connected users via a friendship relation)</p>
     * @return the number of distinct communities
     */
    public int getNumOfCommunities() {
        Graph<UUID> graph = createFriendsGraph();
        //graph.getConnectedComponents().forEach(System.out::println);
        return graph.getConnectedCompsNum();
    }

    /**
     * Determines the most active community in the network
     * <p>The most active community is the one which has the longest path
     * in the corresponding friends graph</p>
     * @return a list with the users forming the most active community
     */
    public ArrayList<User> mostActiveCommunity() {
        Graph<UUID> graph = createFriendsGraph();
        return new ArrayList<>(graph.getComponentWithMaxLen().stream().map(userSrv::findOneUser).toList());
    }

    /**
     * Updates a user
     * @param userToUpdate the user that is going to be updated
     * @param newLastName the new last name for the user
     * @param newFirstName the new first name for the user
     * @param newBirthdate the new birthdate for the user
     * @throws RuntimeException if an invalid date is provided
     */
    public void updateUser(User userToUpdate, String newLastName, String newFirstName, String newBirthdate) throws RuntimeException {
        newLastName = newLastName.substring(0, 1).toUpperCase() + newLastName.substring(1);
        newFirstName = newFirstName.substring(0, 1).toUpperCase() + newFirstName.substring(1);

        userSrv.updateUser(userToUpdate, newLastName, newFirstName, newBirthdate);
    }

    /////////////////////////////////
//    public void populateLists() {
//        User user1 = new User("Florin", "Rahela", "florin.rahela@gmail.com", LocalDate.parse("1992-10-25"));
//        userRepo.save(user1);
//        User user2 = new User("Florian", "Simona", "florian.simona@gmail.com", LocalDate.parse("1993-03-28"));
//        userRepo.save(user2);
//        User user3 = new User("Oana", "Mihaela", "oana_mihaela@gmail.com", LocalDate.parse("1999-08-02"));
//        userRepo.save(user3);
//        User user4 = new User("Aurora", "Constanta", "constanta-aurora01@gmail.com", LocalDate.parse("2004-05-26"));
//        userRepo.save(user4);
//        User user5 = new User("Alex", "Costache", "alex.costache@gmail.com", LocalDate.parse("2007-09-10"));
//        userRepo.save(user5);
//        User user6 = new User("Mircea", "Daria", "mircea.daria@gmail.com", LocalDate.parse("1998-10-21"));
//        userRepo.save(user6);
//        User user7 = new User("Beniamin", "Eugenia", "beni_eug@gmail.com", LocalDate.parse("2003-10-06"));
//        userRepo.save(user7);
//        User user8 = new User("Silvia", "Florina", "silvia.florina@gmail.com", LocalDate.parse("2006-08-28"));
//        userRepo.save(user8);
//        User user9 = new User("Sofia", "Anamaria", "sofia_anamaria@gmail.com", LocalDate.parse("2007-08-24"));
//        userRepo.save(user9);
//        User user10 = new User("Ozana", "Iuliana", "ozana.iuliana@yahoo.com", LocalDate.parse("2008-08-07"));
//        userRepo.save(user10);
//        User user11 = new User("Mihail", "Popescu", "mihail_pop@yahoo.com", LocalDate.parse("2003-12-31"));
//        userRepo.save(user11);
//
//        addFriendship(user1, user4);
//        addFriendship(user1, user7);
//        addFriendship(user4, user7);
//        addFriendship(user4, user6);
//        addFriendship(user6, user7);
//
//        addFriendship(user2, user3);
//
//        addFriendship(user5, user9);
//        addFriendship(user8, user9);
//        addFriendship(user9, user10);
//    }
    /////////////////////////////////
}

package service;

import domain.Friendship;
import domain.HashedPasswordDTO;
import domain.User;
import domain.validators.exceptions.FriendshipException;
import domain.validators.exceptions.SignInException;
import utils.Graph;
import utils.PasswordHasher;

import java.util.*;

public class Network {
    private final UserService userSrv;
    private final FriendshipService friendSrv;
    private User currentUser;

    public Network(UserService userSrv, FriendshipService friendSrv) {
        this.userSrv = userSrv;
        this.friendSrv = friendSrv;
        currentUser = null;
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

    private void setCurrentUser(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void signIn(String email, String password) {
        User user = userSrv.getUserWithEmail(email);
        if (user == null) {
            throw new SignInException("Incorrect email provided.");
        }

        if (checkPassword(password, userSrv.getLoginInfo(user.getId()))) {
            setCurrentUser(user);
        }
        else throw new SignInException("Incorrect password provided.");
    }

    public void signOut() {
        setCurrentUser(null);
    }

    private boolean checkPassword(String password, HashedPasswordDTO loginInfo) {
        String hashedPassword = PasswordHasher.getHashedPassword(password, loginInfo.getSalt());
        String passwordToMatch = loginInfo.getHashedPassword();
        return hashedPassword.equals(passwordToMatch);
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
    public void addUser(String lastName, String firstName, String email, String birthdate, String password) {
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

        HashedPasswordDTO hashedPassword = PasswordHasher.newHashForPassword(password);

        User newUser = userSrv.addUser(lastName, firstName, email, birthdate, hashedPassword);

        setCurrentUser(newUser);
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
     * @param friendToAdd the user to be added as friend
     * @throws FriendshipException if the users are already friends
     */
    public void sendFriendRequest(User friendToAdd) {
        final Friendship auxFriendS = new Friendship(currentUser.getId(), friendToAdd.getId());

        friendSrv.loadFriends(currentUser);
        friendSrv.loadFriends(friendToAdd);

        friendSrv.addFriendship(auxFriendS);

        currentUser.getFriendIDs().add(friendToAdd.getId());
        friendToAdd.getFriendIDs().add(currentUser.getId());
    }

    /**
     * Removes the friendship between two given users.
     * <p>The users' friends list are also updated.</p>
     * @param friendToRemove the friend that will be removed
     * @throws FriendshipException if the given users are not friends
     */
    public void removeFriend(User friendToRemove) throws FriendshipException {
        final Friendship auxFriendS = new Friendship(currentUser.getId(), friendToRemove.getId());
        try {
            friendSrv.removeFriendship(auxFriendS);
            currentUser.getFriendIDs().remove(friendToRemove.getId());
            friendToRemove.getFriendIDs().remove(currentUser.getId());
        } catch (FriendshipException e) {
            throw new FriendshipException("You are not friend with " + friendToRemove.getFirstName() + "\n");
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
     * @param newLastName the new last name for the user
     * @param newFirstName the new first name for the user
     * @param newBirthdate the new birthdate for the user
     * @throws RuntimeException if an invalid date is provided
     */
    public void updateUser(String newLastName, String newFirstName, String newBirthdate) {
        newLastName = newLastName.substring(0, 1).toUpperCase() + newLastName.substring(1);
        newFirstName = newFirstName.substring(0, 1).toUpperCase() + newFirstName.substring(1);

        userSrv.updateUser(currentUser, newLastName, newFirstName, newBirthdate);
    }
}

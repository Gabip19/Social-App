package service;

import domain.*;
import domain.validators.exceptions.FriendshipException;
import domain.validators.exceptions.SignInException;
import domain.validators.exceptions.ValidationException;
import utils.Graph;
import utils.PasswordHasher;

import java.time.LocalDateTime;
import java.util.*;

// TODO: 12/13/22 haveOngoingFriendship
public class Network {
    private final UserService userSrv;
    private final FriendshipService friendSrv;

    private final TextMessageService messageSrv;
    private User currentUser;

    public Network(UserService userSrv, FriendshipService friendSrv, TextMessageService messageSrv) {
        this.userSrv = userSrv;
        this.friendSrv = friendSrv;
        this.messageSrv = messageSrv;
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

    public List<Friendship> getUserFriendships(User user) {
        return getFriendships().stream()
                .filter(x -> x.containsID(user.getId()))
                .toList();
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
        if (lastName.length() == 0 || firstName.length() == 0)
            throw new ValidationException("Empty name field(s).");

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
        UUID userId = user.getId();
        ArrayList<User> friends = new ArrayList<>();
        friendSrv.getFriendships().forEach(
                (Friendship x) -> {
                    if (x.getFriendshipStatus().equals(FriendshipStatus.ACCEPTED)) {
                        if (x.getSenderID().equals(userId))
                            friends.add(userSrv.findOneUser(x.getReceiverID()));
                        else if (x.getReceiverID().equals(userId))
                            friends.add(userSrv.findOneUser(x.getSenderID()));
                    }
                }
        );
        return friends;
    }

    public List<Friendship> getFriendRequestsForUser(User user) {
        return friendSrv.getFriendships()
                .stream()
                .filter(x ->
                        x.getReceiverID().equals(user.getId())
//                       && x.getFriendshipStatus().equals(FriendshipStatus.PENDING)
                )
                .sorted((f1, f2) -> {
                    if (f1.getFriendshipStatus().equals(FriendshipStatus.PENDING)) {
                        if (f2.getFriendshipStatus().equals(FriendshipStatus.PENDING))
                            return f2.getFriendshipDate().compareTo(f1.getFriendshipDate());
                        return -1;
                    }
                    return f2.getFriendshipDate().compareTo(f1.getFriendshipDate());
                }).toList();
    }

    public List<Friendship> getFriendRequestsFromUser(User user) {
        return friendSrv.getFriendships()
                .stream()
                .filter(x ->
                        x.getSenderID().equals(user.getId()) &&
                        x.getFriendshipStatus().equals(FriendshipStatus.PENDING))
                .toList();
    }

    /**
     * Deletes a given user from the corresponding repository.
     * All friendships containing the given user are also deleted.
     * @param user the user to be deleted
     */
    public void removeUser(User user) {
        if (user.equals(currentUser))
            signOut();

        userSrv.removeUser(user);

        removeFriendshipsForUser(user.getId());
    }

    /**
     * Deletes from the Friendship Repository all friendships containing a given user
     * @param id the ID of the user
     */
    private void removeFriendshipsForUser(UUID id) {
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
    public Friendship sendFriendRequest(User friendToAdd) {
        final Friendship auxFriendS = new Friendship(
                currentUser.getId(),
                friendToAdd.getId(),
                FriendshipStatus.PENDING
        );

        friendSrv.addFriendship(auxFriendS);
        return auxFriendS;
    }

    public void acceptFriendRequest(Friendship friendRequest) {
        friendSrv.updateFriendship(
                friendRequest,
                LocalDateTime.now(),
                FriendshipStatus.ACCEPTED
        );
    }

    public void rejectFriendRequest(Friendship friendRequest) {
        friendSrv.updateFriendship(
                friendRequest,
                LocalDateTime.now(),
                FriendshipStatus.REJECTED
        );
    }

    public void cancelFriendRequest(Friendship friendshipToCancel) {
        friendSrv.removeFriendship(friendshipToCancel);
    }

    /**
     * Removes the friendship between two given users.
     * <p>The users' friends list are also updated.</p>
     * @param friendToRemove the friend that will be removed
     * @throws FriendshipException if the given users are not friends
     */
    public void removeFriend(User friendToRemove) throws FriendshipException {
        final Friendship auxFriendS = new Friendship(
                currentUser.getId(),
                friendToRemove.getId(),
                FriendshipStatus.ACCEPTED
        );

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

        friendSrv.getFriendships()
                .stream()
                .filter(x -> x.getFriendshipStatus().equals(FriendshipStatus.ACCEPTED))
                .forEach(x -> graph.addUndirectedEdge(x.getSenderID(), x.getReceiverID()));
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

    public boolean usersAreFriends(User currentUser, User user) {
        for (Friendship friendship : friendSrv.getFriendships()) {
            if (friendship.containsID(currentUser.getId()) && friendship.containsID(user.getId()))
                return true;
        }
        return false;
    }

    public ArrayList<TextMessage> getMessagesBetweenUsers(User secondUser) {
        return messageSrv.getMessagesBetweenUsers(currentUser, secondUser);
    }

    public void sendMessage(User receiver, String text) {
        messageSrv.addMessage(currentUser.getId(), receiver.getId(), text, LocalDateTime.now());
    }
}

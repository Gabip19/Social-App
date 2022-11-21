package domain.validators;

import domain.Friendship;
import domain.validators.exceptions.FriendshipException;

/**
 * Friendship entity validator
 */
public class FriendshipValidator implements Validator<Friendship>{
    /**
     * Validates a friendship
     * <p>A friendship is valid if:</p>
     * <p>- the IDs are different</p>
     * <p>- the IDs are not null</p>
     * @param friendship user that will be validated
     * @throws FriendshipException if the friendship does not meet the requirements
     */
    @Override
    public void validate(Friendship friendship) throws FriendshipException {
        if (friendship.getUser1ID() == null || friendship.getUser2ID() == null) {
            throw new FriendshipException("IDs can not be null.\n");
        }
        if (friendship.getUser1ID().equals(friendship.getUser2ID())) {
            throw new FriendshipException("A user can not befriend himself.\n");
        }
    }
}

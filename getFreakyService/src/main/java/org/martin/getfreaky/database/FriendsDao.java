/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.getfreaky.database;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.dataObjects.FacebookUser;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.FriendResponse;

/**
 *
 * @author martin
 *
 * Provides database access to user friends.
 *
 */
@Stateless
public class FriendsDao {

    @PersistenceContext(unitName = "getfreaky")
    private EntityManager em;

    /**
     *
     * @param userId User's id
     * @return Set of the user's friends
     */
    public Set<User> getFriends(String userId) {
        User user = em.find(User.class, userId);
        Set<User> result = new HashSet<>();
        if (user != null) {
            result.addAll(user.getFriends());
            result.addAll(user.getFriendsOf());
        }
        return result;
    }

    /**
     *
     * @param userId User's id
     * @param friendEmail Friend's email
     * @return The result of the friend addition
     */
    public FriendResponse addFriendByEmail(String userId, String friendEmail) {
        try {
            User user = em.find(User.class, userId);
            User friend = em.createQuery("SELECT u from User u where u.email = :email", User.class)
                    .setParameter("email", friendEmail)
                    .getSingleResult();

            return addFriend(user, friend);
        } catch (NoResultException e) {
            return new FriendResponse(FriendResponse.Message.COULD_NOT_FIND_USER);
        }
    }

    /**
     *
     * @param userId User's id who added the other
     * @param facebookId Facebook id of the invited user
     * @return The result of the friend addition
     */
    public FriendResponse addFriendByFacebook(String userId, String facebookId) {
        try {
            User user = em.find(User.class, userId);
            User friend = em.createQuery("SELECT u from User u where u.facebookId = :facebookId", User.class)
                    .setParameter("facebookId", facebookId)
                    .getSingleResult();

            return addFriend(user, friend);
        } catch (NoResultException e) {
            return new FriendResponse(FriendResponse.Message.COULD_NOT_FIND_USER);
        }
    }

    /**
     *
     * @param userId User's id who added the other
     * @param googleId Google id of the invited user
     * @return The result of the friend addition
     */
    public FriendResponse addFriendByGoogle(String userId, String googleId) {
        try {
            User user = em.find(User.class, userId);
            User friend = em.createQuery("SELECT u from User u where u.googleId = :googleId", User.class)
                    .setParameter("googleId", googleId)
                    .getSingleResult();

            return addFriend(user, friend);
        } catch (NoResultException e) {
            return new FriendResponse(FriendResponse.Message.COULD_NOT_FIND_USER);
        }
    }

    /**
     *
     * @param user User who invited the other to be friends
     * @param friend The invited user
     * @return The result of the friend addition
     */
    public FriendResponse addFriend(User user, User friend) {
        List<User> friends = user.getFriends();
        List<User> friendsOf = user.getFriendsOf();
        if (!user.equals(friend)) {
            if (!friends.contains(friend) && !friendsOf.contains(friend)) {
                friends.add(friend);
                em.refresh(friend);
                return new FriendResponse(FriendResponse.Message.FRIEND_ADDED);
            } else {
                return new FriendResponse(FriendResponse.Message.ALREADY_FRIENDS);
            }
        } else {
            return new FriendResponse(FriendResponse.Message.COULD_NOT_ADD_FRIEND);
        }
    }

    /**
     * @param userId User's id who added the others
     * @param friends List of facebook friends
     * @return The result of the friend additions
     */
    public FriendResponse addFacebookFriends(String userId, List<FacebookUser> friends) {
        FriendResponse response = new FriendResponse(FriendResponse.Message.COULD_NOT_ADD_FRIEND);
        friends.forEach(friend -> {
            FriendResponse fr = addFriendByFacebook(userId, friend.getId());
            if (fr.getMessage().equals(FriendResponse.Message.FRIEND_ADDED)) {
                response.setMessage(fr.getMessage());
            }
        });
        return response;
    }

}

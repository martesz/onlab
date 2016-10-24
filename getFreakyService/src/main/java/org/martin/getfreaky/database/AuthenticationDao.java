package org.martin.getfreaky.database;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.martin.getfreaky.dataObjects.MergeData;
import org.martin.getfreaky.dataObjects.User;
import org.martin.getfreaky.network.LoginResponse;
import org.martin.getfreaky.network.MergeResponse;
import org.martin.getfreaky.utils.FacebookLogin;
import org.martin.getfreaky.utils.GoogleSignIn;
import org.martin.getfreaky.utils.JWTService;
import org.martin.getfreaky.utils.Password;

/**
 *
 * @author martin
 *
 * Provides user registration and login services on the database level.
 */
@Stateless
public class AuthenticationDao {

    @PersistenceContext(unitName = "getfreaky")
    private EntityManager em;

    @EJB
    JWTService jWTService;
    
    /**
     * 
     * @param user The user to be logged in, or registered
     * @return The result of the login or registration
     */
    public LoginResponse signInOrRegisterUser(User user) {
        try {
            User existingUser = (User) em.createQuery("SELECT u from User u where u.email = :email")
                    .setParameter("email", user.getEmail())
                    .getSingleResult();

            if (Password.equals(user.getPassword(), existingUser.getPassword())) {
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN,
                        existingUser.getId(), existingUser, jWTService.issueToken(existingUser.getId()));
            } else {
                return new LoginResponse(LoginResponse.ResponseMessage.WRONG_PASSWORD);
            }
        } catch (NoResultException nre) {
            String userId = user.generateUniqueId();
            user.setPassword(Password.getHash(user.getPassword()));
            em.persist(user);
            return new LoginResponse(LoginResponse.ResponseMessage.USER_REGISTERED,
                    userId, user, jWTService.issueToken(user.getId()));
        }
    }
    
    /**
     * 
     * @param user The user to be logged in, or registered
     * @return The result of the login or registration
     */
    public LoginResponse signInOrRegisterGoogle(User user) {
        if (user != null) {
            try {
                User existingUser = (User) em.createQuery("SELECT u from User u where u.googleId = :googleId")
                        .setParameter("googleId", user.getGoogleId())
                        .getSingleResult();
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN,
                        existingUser.getId(), existingUser, jWTService.issueToken(existingUser.getId()));
            } catch (NoResultException nre) {
                user.generateUniqueId();
                em.persist(user);
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN,
                        user.getId(), user, jWTService.issueToken(user.getId()));
            }
        } else {
            return new LoginResponse(LoginResponse.ResponseMessage.WRONG_GOOGLE_ID_TOKEN);
        }
    }

    /**
     * 
     * @param user The user to be logged in, or registered
     * @return The result of the login or registration
     */
    public LoginResponse signInOrRegisterFacebook(User user) {
        if (user != null) {
            try {
                User existingUser = (User) em.createQuery("SELECT u from User u where u.facebookId = :facebookId")
                        .setParameter("facebookId", user.getFacebookId())
                        .getSingleResult();
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN,
                        existingUser.getId(), existingUser, jWTService.issueToken(existingUser.getId()));
            } catch (NoResultException nre) {
                user.generateUniqueId();
                em.persist(user);
                return new LoginResponse(LoginResponse.ResponseMessage.USER_SIGNED_IN,
                        user.getId(), user, jWTService.issueToken(user.getId()));
            }
        } else {
            return new LoginResponse(LoginResponse.ResponseMessage.WRONG_FACEBOOK_ACCESS_TOKEN);
        }
    }
    
    /**
     * 
     * @param mergeData The data to be merged into the user's account
     * @return The result of the merge
     */
     public MergeResponse mergeAccounts(MergeData mergeData) {
        User user = em.find(User.class, mergeData.getUserId());
        if (user != null) {
            if (mergeData.getEmail() != null) {
                if (associateEmail(user, mergeData.getEmail(), mergeData.getPassword())) {
                    return new MergeResponse(MergeResponse.Message.EMAIL_ASSOCIATED);
                } else {
                    return new MergeResponse(MergeResponse.Message.MERGE_NOT_SUCCESSFUL);
                }
            } else if (mergeData.getGoogleIdToken() != null) {
                if (associateGoogle(user, mergeData.getGoogleIdToken())) {
                    return new MergeResponse(MergeResponse.Message.GOOGLE_ACCOUNT_ASSOCIATED);
                } else {
                    return new MergeResponse(MergeResponse.Message.ALREADY_ASSOCIATED_WITH_OTHER_ACCOUNT);
                }
            } else if (mergeData.getFacebookAccessToken() != null) {
                if (associateFacebook(user, mergeData.getFacebookAccessToken())) {
                    return new MergeResponse(MergeResponse.Message.FACEBOOK_ACCOUNT_ASSOCIATED);
                } else {
                    return new MergeResponse(MergeResponse.Message.ALREADY_ASSOCIATED_WITH_OTHER_ACCOUNT);
                }
            }
        }
        return new MergeResponse(MergeResponse.Message.MERGE_NOT_SUCCESSFUL);
    }

     /**
      * 
      * @param user The user that need the new email
      * @param email The email to be set
      * @param password The password to be set
      * @return The result of the association
      */
    private boolean associateEmail(User user, String email, String password) {
        try {
            User existingUser = (User) em.createQuery("SELECT u from User u where u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();

            if (Password.equals(password, existingUser.getPassword())
                    && existingUser.getGoogleId() == null
                    && existingUser.getFacebookId() == null
                    && !existingUser.getId().equals(user.getId())) {
                mergeUsers(user, existingUser);
                user.setEmail(email);
                user.setPassword(Password.getHash(password));
                return true;
            } else {
                return false;
            }
        } catch (NoResultException nre) {
            user.setEmail(email);
            user.setPassword(Password.getHash(password));
            return true;
        }
    }

    /**
     * 
     * @param user The user that gets the google id token
     * @param googleIdToken
     * @return The result of the association
     */
    private boolean associateGoogle(User user, String googleIdToken) {
        User googleUser = GoogleSignIn.authenticateAndroid(googleIdToken);
        try {
            User existingUser = (User) em.createQuery("SELECT u from User u where u.googleId = :googleId")
                    .setParameter("googleId", googleUser.getGoogleId())
                    .getSingleResult();
            if (existingUser.getEmail() == null && existingUser.getFacebookId() == null
                    && !existingUser.getId().equals(user.getId())) {
                mergeUsers(user, existingUser);
                user.setGoogleId(googleUser.getId());
                return true;
            } else {
                return false;
            }
        } catch (NoResultException nre) {
            user.setGoogleId(googleUser.getGoogleId());
            return true;
        }
    }

    /**
     * 
     * @param user The user that gets the facebook access token
     * @param facebookAccessToken
     * @return The result of the association
     */
    private boolean associateFacebook(User user, String facebookAccessToken) {
        User fUser = FacebookLogin.login(facebookAccessToken);
        try {
            User existingUser = (User) em.createQuery("SELECT u from User u where u.facebookId = :facebookId")
                    .setParameter("facebookId", fUser.getFacebookId())
                    .getSingleResult();
            if (existingUser.getEmail() == null && existingUser.getGoogleId() == null
                    && !existingUser.getId().equals(user.getId())) {
                mergeUsers(user, existingUser);
                user.setFacebookId(fUser.getFacebookId());
                return true;
            } else {
                return false;
            }
        } catch (NoResultException nre) {
            user.setFacebookId(fUser.getFacebookId());
            return true;
        }
    }

    private void mergeUsers(User a, User b) {
        a.getWorkouts().addAll(b.getWorkouts());
        em.remove(b);
    }

}

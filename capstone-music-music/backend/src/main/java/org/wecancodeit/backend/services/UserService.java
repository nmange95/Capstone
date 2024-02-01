package org.wecancodeit.backend.services;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.models.UserSearchCriteria;
import org.wecancodeit.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a List of all users
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(UserSearchCriteria criteria) {
        return userRepository.findUsersByCriteria(criteria);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the user if found
     */
    public Optional<User> findUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return an Optional containing the user if found
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user
     * @return an Optional containing the user if found
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a new user or updates an existing user in the repository.
     *
     * @param user the user to save or update
     * @return the saved or updated user
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the repository.
     *
     * @param user the user with updated information
     * @return the updated user
     */
    public User updateUser(@NonNull User user) {
        // Assumes the password is already set correctly (either unmodified or null if
        // not updating the password)
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    public void deleteUser(@NonNull Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Retrieves the user data for the sender by username.
     *
     * @param senderUsername the username of the sender
     * @return the sender user data
     */
    public User getSenderUserData(String senderUsername) {
        return userRepository.findByUsername(senderUsername).orElse(null);
    }

    /**
     * Retrieves the user data for the receiver by username.
     *
     * @param receiverUsername the username of the receiver
     * @return the receiver user data
     */
    public User getReceiverUserData(String receiverUsername) {
        return userRepository.findByUsername(receiverUsername).orElse(null);
    }

    public Set<User> getUserFriends(@NonNull Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return user.getFriends();
    }

    @Transactional
    public boolean setUserFriends(@NonNull User user1, @NonNull User user2) {
        if (!userRepository.areAlreadyFriends(user1.getId(), user2.getId()) &&
                !userRepository.areAlreadyFriends(user2.getId(), user1.getId())) {
            user1.addFriend(user2);
            userRepository.save(user1);
            return true;
        }
        return false;
    }
}
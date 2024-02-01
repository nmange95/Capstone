package org.wecancodeit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wecancodeit.backend.enums.MusicTagsEnum;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.models.UserSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username
     * @return an Optional possibly containing the found user
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email
     * @return an Optional possibly containing the found user
     */
    Optional<User> findByEmail(String email);

    /**
     * finds users by similar User tags
     */
    default Set<User> findBySimilarTags(String username, Set<MusicTagsEnum> userTags) {
        return findByMusicTagsInAndUsernameNot(userTags, username);
    }

    /*
     * Finds users with similar music tags.
     */
    Set<User> findByMusicTagsInAndUsernameNot(Set<MusicTagsEnum> musicTags, String username);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.friends f WHERE u.id = :userId AND f.id = :friendId")
    boolean areAlreadyFriends(Long userId, Long friendId);

    @Query("SELECT u FROM User u WHERE " +
            "(COALESCE(:#{#criteria.email}, '') = '' OR u.email = :#{#criteria.email}) AND " +
            "(COALESCE(:#{#criteria.username}, '') = '' OR LOWER(u.username) LIKE LOWER('%' || :#{#criteria.username} || '%')) AND "
            +
            "(COALESCE(:#{#criteria.instrument}, '') = '' OR LOWER(u.instrument) LIKE LOWER('%' || :#{#criteria.instrument} || '%'))")
    List<User> findUsersByCriteria(@Param("criteria") UserSearchCriteria criteria);

}
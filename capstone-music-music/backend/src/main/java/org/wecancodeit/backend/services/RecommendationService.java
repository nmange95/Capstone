package org.wecancodeit.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wecancodeit.backend.enums.MusicTagsEnum;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.repositories.UserRepository;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getRecommendations(String username) {
        Optional<User> currentUser = userRepository.findByUsername(username);
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            Set<MusicTagsEnum> userTags = user.getMusicTags();

            // This is just returning people with similar tags if more in the future add here
            Set<User> recommendedUsers = userRepository.findBySimilarTags(username, userTags);

            //Convert from Set<String> into List<User>
            List<User> recommendedUsersList = new ArrayList<>(recommendedUsers);

            return recommendedUsersList;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
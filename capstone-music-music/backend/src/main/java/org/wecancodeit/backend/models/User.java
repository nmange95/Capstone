package org.wecancodeit.backend.models;

import jakarta.persistence.*;

import java.util.HashSet;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.wecancodeit.backend.enums.ExperienceLevelEnum;
import org.wecancodeit.backend.enums.MusicTagsEnum;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a user in the OtterCollab platform.
 * This class holds user details relevant for music collaboration.
 */
@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String instrument;
    private String genre;

    @Enumerated(EnumType.STRING)
    private ExperienceLevelEnum experienceLevel;

    private String imageURL;

    @ElementCollection
    private Set<MusicTagsEnum> musicTags = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    private List<PairRequest> pendingPairRequests;

    @ManyToMany
    @JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @JsonIgnore
    private Set<User> friends = new HashSet<>();

    /**
     * Default constructor for JPA.
     */
    public User() {
    }

    /**
     * Constructs a new User with specified details.
     *
     * @param username
     * @param password
     * @param email
     * @param instrument
     * @param genre
     * @param experienceLevel
     * @param imageURL
     * @param musicTag
     */
    public User(String username, String password, String email, String instrument, String genre,
            ExperienceLevelEnum experienceLevel,
            String imageURL) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.instrument = instrument;
        this.genre = genre;
        this.experienceLevel = experienceLevel;
        this.imageURL = imageURL;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User user) {
        if (!friends.contains(user)) {
            friends.add(user);
            if (!user.getFriends().contains(this)) {
                user.addFriend(this);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getInstrument() {
        return instrument;
    }

    public String getGenre() {
        return genre;
    }

    public ExperienceLevelEnum getExperienceLevel() {
        return experienceLevel;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Set<MusicTagsEnum> getMusicTags() {
        return musicTags;
    }

    public List<PairRequest> getPendingPairRequests() {
        return pendingPairRequests;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setExperienceLevel(ExperienceLevelEnum experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setMusicTags(Set<MusicTagsEnum> musicTags) {
        this.musicTags = musicTags;
    }

    public void addMusicTag(MusicTagsEnum musicTag) {
        this.musicTags.add(musicTag);
    }

    public void removeMusicTag(MusicTagsEnum musicTag) {
        this.musicTags.remove(musicTag);
    }

    public void setPendingPairRequests(List<PairRequest> pendingPairRequests) {
        this.pendingPairRequests = pendingPairRequests;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return experienceLevel == user.experienceLevel &&
                Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(instrument, user.instrument) &&
                Objects.equals(genre, user.genre) &&
                Objects.equals(imageURL, user.imageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, instrument, genre, experienceLevel, imageURL);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", instrument='" + instrument + '\'' +
                ", genre='" + genre + '\'' +
                ", experienceLevel=" + experienceLevel +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}

package org.wecancodeit.backend.models;

public class UserSearchCriteria {
    private String userSearching;
    private String email;
    private String username;
    private String instrument;

    public UserSearchCriteria(String userSearching, String email, String username, String instrument) {
        this.userSearching = userSearching;
        this.email = email;
        this.username = username;
        this.instrument = instrument;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getUserSearching() {
        return userSearching;
    }

    public void setUserSearching(String userSearching) {
        this.userSearching = userSearching;
    }

}
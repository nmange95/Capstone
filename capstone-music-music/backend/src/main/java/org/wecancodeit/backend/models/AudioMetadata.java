package org.wecancodeit.backend.models;

import jakarta.persistence.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

/**
 * Entity representing metadata for an audio file.
 */
@Entity
public class AudioMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String artist;
    private String genre;
    private Double duration; // in seconds
    private Date uploadDate;
    private String filePath; // path to the actual audio file

    /**
     * Default constructor for JPA.
     */
    public AudioMetadata() {
    }

    /**
     * Constructs a new AudioMetaData.
     *
     * @param title
     * @param artist
     * @param genre      genre
     * @param duration   duration in seconds
     * @param uploadDate date the audio was uploaded
     * @param filePath   file path to the audio file
     */
    public AudioMetadata(String title, String artist, String genre, Double duration, Date uploadDate, String filePath) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.uploadDate = uploadDate;
        this.filePath = filePath;
    }

    // Getters
    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public Double getDuration() {
        return duration;
    }
    
    public Date getUploadDate() {
        return uploadDate;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
        public User getUser() {
            return user;
        }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "AudioMetaData{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", uploadDate=" + uploadDate +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AudioMetadata that = (AudioMetadata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(genre, that.genre) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(uploadDate, that.uploadDate) &&
                Objects.equals(filePath, that.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, artist, genre, duration, uploadDate, filePath);
    }

    public Path getFileName() {
        if (filePath != null) {
            return Paths.get(filePath).getFileName();
        } else {
            throw new IllegalStateException("File path is null");
        }
    }
}
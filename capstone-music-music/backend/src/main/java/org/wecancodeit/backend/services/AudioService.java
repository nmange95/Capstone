package org.wecancodeit.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wecancodeit.backend.models.AudioMetadata;
import org.wecancodeit.backend.models.User;
import org.wecancodeit.backend.repositories.AudioMetadataRepository;
import org.wecancodeit.backend.repositories.UserRepository;

// imports for Checking Duration of uploaded audio files
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import javazoom.jl.decoder.Bitstream;
//MP3 File Processing
import javazoom.jl.decoder.JavaLayerException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AudioService {

    private final AudioMetadataRepository audioMetaDataRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AudioService.class);

    public AudioService(AudioMetadataRepository audioMetaDataRepository, UserRepository userRepository) {
        this.audioMetaDataRepository = audioMetaDataRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all audio metadata entries.
     *
     * @return a list of all audio metadata entries
     */
    public List<AudioMetadata> findAll() {
        return audioMetaDataRepository.findAll();
    }

    /**
     * Finds audio metadata by its ID.
     *
     * @param id the ID of the audio metadata
     * @return an Optional containing the audio metadata if found
     */
    public Optional<AudioMetadata> findById(@NonNull Long id) {
        return audioMetaDataRepository.findById(id);
    }

    /**
     * Retrieves all audio metadata entries for a given user.
     *
     * @param userId the ID of the user
     * @return a list of audio metadata entries for the user
     */
    public List<AudioMetadata> findAllByUserId(Long userId) {
        return audioMetaDataRepository.findByUserId(userId);
    }

    /**
     * Saves or updates audio metadata.
     *
     * @param audioMetaData the audio metadata to save or update
     * @return the saved or updated audio metadata
     */
    public AudioMetadata updateMetadata(@NonNull AudioMetadata audioMetaData) {
        return audioMetaDataRepository.save(audioMetaData);
    }

    /**
     * Handles the uploading of an audio file and creation of its metadata.
     *
     * @param file   the audio file to upload
     * @param title  the title of the audio piece
     * @param artist the artist of the audio piece
     * @param genre  the genre of the audio piece
     * @return the saved audio metadata
     */
    @Transactional
    public Optional<AudioMetadata> uploadAudio(MultipartFile file, String title, String artist, String genre,
            Long userId)
            throws IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User owner = userOptional.get();

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("audio/")) {
                throw new IllegalArgumentException("File must be an audio file");
            }
            // no videos allowed
            if (isVideoContentType(contentType)) {
                throw new IllegalArgumentException("Video uploads are not allowed");
            }

            String fileName = storeFile(file);

            // Finds the file being uploaded and calculates the audio files duration
            String filePath = Paths.get("/wcci/OtterCollab/media").resolve(fileName).toString();
            Double duration = getAudioFileDuration(filePath);
            processAudioFile(fileName, filePath);
            AudioMetadata metaData = new AudioMetadata(title, artist, genre, duration, new Date(), fileName);
            metaData.setUser(owner);
            return Optional.of(audioMetaDataRepository.save(metaData));
        } else {
            // Log a warning or handle the case where the user is not found
            logger.warn("User not found for userId: {}", userId);
            return Optional.empty();
        }
    }

    private boolean isVideoContentType(String contentType) {
        return contentType.startsWith("video/");
    }

    // Process the audio file based on its format
    private void processAudioFile(String fileName, String filePath) {
        String fileExtension = getFileExtension(fileName);
        if ("mp3".equalsIgnoreCase(fileExtension)) {
            // Process MP3 file
            processMp3File(filePath);
            logger.info("Processing mp3 file: {}", fileName);
        } else if ("wav".equalsIgnoreCase(fileExtension)) {
            // Process WAV file
            // processWavFile(filePath);
            logger.info("Processing WAV file: {}", fileName);
        } else if ("ogg".equalsIgnoreCase(fileExtension)) {
            // Process OGG file
            // processOggFile(filePath);
            logger.info("Processing OGG file: {}", fileName);
        } else {
            // Unsupported file format
            // handleUnsupportedFormat(fileExtension);
            logger.warn("Unsupported audio file format: {}", fileExtension);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    // MP3 File Processing
    private void processMp3File(String filePath) {
        try {
            // Create a Bitstream object from the FileInputStream of the MP3 file
            Bitstream bitstream = new Bitstream(new FileInputStream(filePath));

            // Read the header of the MP3 file to get information about the audio
            bitstream.readFrame();

            // Initialize frameCount to 0
            int frameCount = 0;

            // Check if there are frames in the MP3 file before attempting to read frame
            // count
            if (bitstream.readFrame() != null) {
                // Read the frame count from the MP3 file using the max_number_of_frames method
                frameCount = bitstream.readFrame().max_number_of_frames(frameCount);
            }

            // Assign the frame count as the duration (this might not be accurate, depends
            // on the use case)
            int durationInSeconds = frameCount;

            // Log the duration information
            logger.info("MP3 file duration: {} seconds", durationInSeconds);
        } catch (JavaLayerException | IOException e) {
            // Handle exceptions that might occur during MP3 file processing
            logger.error("Error processing MP3 file", e);
        }
    }

    /**
     * Deletes audio metadata and its corresponding file by the metadata's ID.
     *
     * @param id the ID of the audio metadata to delete
     */
    @Transactional
    public void deleteById(@NonNull Long id) {
        Optional<AudioMetadata> metaData = audioMetaDataRepository.findById(id);
        metaData.ifPresent(m -> {
            deleteFile(m.getFilePath());
            audioMetaDataRepository.deleteById(id);
        });
    }

    /**
     * 
     * @param filePath to the audio file
     * @return the duration of the audio file in seconds
     */
    private Double getAudioFileDuration(String filePath) {
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioInputStream);
            long microseconds = (long) fileFormat.properties().get("duration");
            return microseconds / 1_000_000.0; // Converts Microseconds to seconds
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = Optional.ofNullable(originalFilename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(originalFilename.lastIndexOf(".") + 1))
                    .orElse("");
        }

        String newFilename = UUID.randomUUID().toString() + (fileExtension.isEmpty() ? "" : "." + fileExtension);

        Path baseDir = Paths.get(System.getProperty("user.dir"), "wcci", "OtterCollab", "media");
        Path targetLocation = baseDir.resolve(newFilename);

        Files.createDirectories(baseDir);

        Files.copy(file.getInputStream(), targetLocation);

        return targetLocation.toString();
    }

    private void deleteFile(String filePath) {
        try {
            Path file = Paths.get(filePath);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // Handle the exception, possibly log it
            e.printStackTrace();
        }
    }
}

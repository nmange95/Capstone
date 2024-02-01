package org.wecancodeit.backend.controllers;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wecancodeit.backend.models.AudioMetadata;
import org.wecancodeit.backend.services.AudioService;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    /**
     * Endpoint for uploading an audio file and its metadata.
     *
     * @param file   the audio file to upload
     * @param title
     * @param artist
     * @param genre
     * @param userId the id of the user owning the audio file
     * @return the saved audio metadata
     */
    @PostMapping("/upload")
    public ResponseEntity<AudioMetadata> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("genre") String genre,
            @RequestParam("userId") @NonNull Long userId) {
        try {
            Optional<AudioMetadata> metadataOptional = audioService.uploadAudio(file, title, artist, genre, userId);
            if (metadataOptional.isPresent()) {
                AudioMetadata metadata = metadataOptional.get();
                return ResponseEntity.ok(metadata);
            } else {
                // Handle the case where the user is not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to retrieve all audio metadata.
     *
     * @return a list of all audio metadata
     */
    @GetMapping
    public List<AudioMetadata> getAllAudioMetadata() {
        return audioService.findAll();
    }

    /**
     * GET endpoint to retrieve all audio metadata for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of audio metadata for the user
     */
    @GetMapping("/user/{userId}")
    public List<AudioMetadata> getAllAudioByUser(@PathVariable Long userId) {
        return audioService.findAllByUserId(userId);
    }

    /**
     * Endpoint to delete audio file and data by ID.
     *
     * @param id the ID of the audio to delete
     * @return a response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudio(@PathVariable @NonNull Long id) {
        audioService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<Resource> streamAudioFile(@PathVariable @NonNull Long id, HttpServletRequest request) {
        try {
            AudioMetadata metaData = audioService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Audio not found"));
            Path filePath = Paths.get(metaData.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the file!");
            }

            long resourceLength = resource.contentLength();
            String rangeString = request.getHeader(HttpHeaders.RANGE);
            long start = 0, end = resourceLength - 1;

            if (rangeString != null && rangeString.startsWith("bytes=")) {
                String[] ranges = rangeString.substring("bytes=".length()).split("-");
                start = Long.parseLong(ranges[0]);
                end = ranges.length > 1 ? Long.parseLong(ranges[1]) : end;
                if (start >= resourceLength || end >= resourceLength) {
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + resourceLength);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(end - start + 1));

            InputStreamResource inputStreamResource = new InputStreamResource(resource.getInputStream());
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .body(inputStreamResource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}

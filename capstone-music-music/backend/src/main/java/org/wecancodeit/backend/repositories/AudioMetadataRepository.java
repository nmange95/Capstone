package org.wecancodeit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wecancodeit.backend.models.AudioMetadata;

import java.util.List;

/**
 * Repository interface for AudioMetaData entity.
 */
@Repository
public interface AudioMetadataRepository extends JpaRepository<AudioMetadata, Long> {

    /**
     * @return a list of audio metadata entries
     */
    List<AudioMetadata> findByArtist(String artist);

    /**
     * @return a list of audio metadata entries
     */
    List<AudioMetadata> findByGenre(String genre);

    /**
     * @return a list of audio metadata entries
     */
    List<AudioMetadata> findByUserId(Long userId);
}

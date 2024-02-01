// NotificationRepository.java

package org.wecancodeit.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wecancodeit.backend.models.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByOrderByTimestampDesc();
    List<Notification> findByUserId(Long userId);
}

// NotificationService.java

package org.wecancodeit.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.wecancodeit.backend.models.Notification;
import org.wecancodeit.backend.repositories.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // service method to get all Notification
    public List<Notification> getAllNotification() {
        return notificationRepository.findByOrderByTimestampDesc();
    }

    public Notification getNotificationById(@NonNull Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public Notification saveNotification(@NonNull Notification notification) {
        return notificationRepository.save(notification);
    }

    public void deleteNotification(@NonNull Long id) {
        notificationRepository.deleteById(id);
    }

    public NotificationRepository getNotificationRepository() {
        return notificationRepository;
    }

    public void setNotificationRepository(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> updateAllNotificationsForUser(Long userId, List<Notification> notifications) {
        List<Notification> existingNotifications = notificationRepository.findByUserId(userId);

        if (existingNotifications.isEmpty()) {
            return null;
        }

        for (Notification updatedNotification : notifications) {
            Notification existingNotification = existingNotifications.stream()
                    .filter(n -> n.getId() == updatedNotification.getId())
                    .findFirst()
                    .orElse(null);

            if (existingNotification != null) {
                existingNotification.setMessage(updatedNotification.getMessage());
                existingNotification.setSeen(updatedNotification.isSeen());
            }
        }

        return notificationRepository.saveAll(existingNotifications);
    }
}

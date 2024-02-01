// NotificationController.java

package org.wecancodeit.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.wecancodeit.backend.models.Notification;
import org.wecancodeit.backend.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotification();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@NonNull @PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@NonNull @RequestBody Notification notification) {
        Notification savedNotification = notificationService.saveNotification(notification);
        return ResponseEntity.ok(savedNotification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(
            @NonNull @PathVariable Long id, @NonNull @RequestBody Notification updatedNotification) {
        Notification existingNotification = notificationService.getNotificationById(id);
        if (existingNotification != null) {
            Notification savedNotification = notificationService.saveNotification(updatedNotification);
            return ResponseEntity.ok(savedNotification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> updateAllNotificationsForUser(
            @NonNull @PathVariable Long userId,
            @NonNull @RequestBody List<Notification> notifications) {

        List<Notification> updatedNotifications = notificationService.updateAllNotificationsForUser(userId,
                notifications);
        if (updatedNotifications != null) {
            return ResponseEntity.ok(updatedNotifications);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@NonNull @PathVariable Long id) {
        Notification existingNotification = notificationService.getNotificationById(id);
        if (existingNotification != null) {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.hojunara.web.controller;

import com.hojunara.web.dto.response.NotificationDto;
import com.hojunara.web.entity.Notification;
import com.hojunara.web.entity.User;
import com.hojunara.web.service.NotificationService;
import com.hojunara.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling user notifications.
 * <p>
 * Provides endpoints for getting notification counts, fetching recent notifications,
 * and mark notifications as read.
 * All endpoints are prefixed with <code>/api/notification</code>.
 * </p>
 *
 * @author Taejun Seo
 */
@RestController
@RequestMapping("api/notification")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves the number of unread notifications for a user (max 20).
     *
     * @param userId the ID of the user
     * @return the count of unread notifications
     */
    @GetMapping("get/count")
    public ResponseEntity<Long> getNotificationCount(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        List<Notification> notifications = user.getNotifications().stream().limit(20).collect(Collectors.toList());
        long unreadCount = notifications.stream()
                .filter(notification -> Boolean.FALSE.equals(notification.getIsRead()))
                .count();

        return ResponseEntity.ok(unreadCount);
    }

    /**
     * Retrieves the 20 most recent notifications for a user.
     *
     * @param userId the ID of the user
     * @return a list of {@link NotificationDto} objects
     */
    @GetMapping("get/recent/notifications")
    public ResponseEntity<List<NotificationDto>> getRecentNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.get20RecentNotificationsByUserId(userId);
        List<NotificationDto> notificationDtoList = notifications.stream().map(Notification::convertToNotificationDto).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDtoList);
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification to mark as read
     * @return {@code true} if the update was successful
     */
    @PutMapping("update/notification/as/read")
    public ResponseEntity<Boolean> setNotificationAsRead(@RequestParam Long notificationId) {
        Boolean isUpdated = notificationService.updateNotificationAsRead(notificationId);
        return ResponseEntity.ok(isUpdated);
    }
}
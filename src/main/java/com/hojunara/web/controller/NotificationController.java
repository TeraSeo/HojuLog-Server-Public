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

    @GetMapping("get/count")
    public ResponseEntity<Long> getNotificationCount(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        List<Notification> notifications = user.getNotifications().stream().limit(20).collect(Collectors.toList());
        long unreadCount = notifications.stream()
                .filter(notification -> Boolean.FALSE.equals(notification.getIsRead()))
                .count();

        return ResponseEntity.ok(unreadCount);
    }

    @GetMapping("get/recent/notifications")
    public ResponseEntity<List<NotificationDto>> getRecentNotifications(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        List<Notification> notifications = user.getNotifications().stream().limit(20).collect(Collectors.toList());
        List<NotificationDto> notificationDtoList = notifications.stream().map(Notification::convertToNotificationDto).collect(Collectors.toList());
        return ResponseEntity.ok(notificationDtoList);
    }

    @PutMapping("update/notification/as/read")
    public ResponseEntity<Boolean> setNotificationAsRead(@RequestParam Long notificationId) {
        Boolean isUpdated = notificationService.updateNotificationAsRead(notificationId);
        return ResponseEntity.ok(isUpdated);
    }
}
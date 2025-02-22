package com.hojunara.web.service;

import com.hojunara.web.entity.Notification;
import com.hojunara.web.entity.User;

import java.util.List;

public interface NotificationService {

    List<Notification> getWholeNotificationsByUserId(Long id);

    Notification getNotificationById(Long id);

    void createNotification(String title, String message, User user);

    void deleteNotificationById(Long id);

    Boolean updateNotificationAsRead(Long notificationId);
}

package com.promo.web.service;

import com.promo.web.entity.Notification;
import com.promo.web.entity.User;

import java.util.List;

public interface NotificationService {

    List<Notification> getWholeNotificationsByUserId(Long id);

    Notification getNotificationById(Long id);

    void createNotification(String type, String message, User user);

    void deleteNotificationById(Long id);
}

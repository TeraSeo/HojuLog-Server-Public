package com.promo.web.service;

import com.promo.web.entity.Notification;
import com.promo.web.entity.NotificationType;
import com.promo.web.entity.User;
import com.promo.web.exception.NotificationNotFoundException;
import com.promo.web.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public List<Notification> getWholeNotificationsByUserId(Long id) {
        User user = userService.getUserById(id);

        try {
            List<Notification> notifications = user.getNotifications();
            log.info("Successfully got whole notifications with user id: {}", id);
            return notifications;
        } catch (Exception e) {
            log.error("Failed to get whole notifications with user id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Notification getNotificationById(Long id) {
        try {
            Optional<Notification> notification = notificationRepository.findById(id);
            if (notification.isPresent()) {
                log.info("Successfully got notification with id: {}", id);
                return notification.get();
            }
            throw new NotificationNotFoundException("Notification not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to get notification with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void createNotification(String type, String message, User user) {
        try {
            Notification notification = Notification.builder().type(NotificationType.valueOf(type)).message(message).user(user).isRead(false).createdAt(new Timestamp(System.currentTimeMillis())).build();

            user.getNotifications().add(notification);
            notificationRepository.save(notification);
            log.info("Successfully created notification with user id: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to create notification with user id: {}", user.getId(), e);
            throw e;
        }
    }

    @Override
    public void deleteNotificationById(Long id) {
        try {
            notificationRepository.deleteById(id);
            log.info("Successfully created notification with id : {}", id);
        } catch (Exception e) {
            log.error("Failed to delete notification with id: {}", id, e);
            throw e;
        }
    }
}

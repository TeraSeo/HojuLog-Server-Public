package com.promo.web.service;

import com.promo.web.entity.Notification;
import com.promo.web.entity.Role;
import com.promo.web.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationImplTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Test
    void createNotification() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        userService.createUser(user1);

        notificationService.createNotification("LIKE", "notification", user1);

        List<Notification> wholeNotificationsByUserId = notificationService.getWholeNotificationsByUserId(user1.getId());
        assertEquals(1, wholeNotificationsByUserId.size(), "There should be only one notification for this user");

        Notification notification = wholeNotificationsByUserId.get(0);
        assertEquals(notification.getUser().getId(), user1.getId(), "The notification should be associated with the correct user");
    }

}
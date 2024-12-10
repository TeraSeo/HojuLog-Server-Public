package com.promo.web.service;

import com.promo.web.dto.request.UserDto;
import com.promo.web.entity.Notification;
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
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        notificationService.createNotification("LIKE", "notification", createdUser);

        List<Notification> wholeNotificationsByUserId = notificationService.getWholeNotificationsByUserId(createdUser.getId());
        assertEquals(1, wholeNotificationsByUserId.size(), "There should be only one notification for this user");

        Notification notification = wholeNotificationsByUserId.get(0);
        assertEquals(notification.getUser().getId(), createdUser.getId(), "The notification should be associated with the correct user");
    }

}
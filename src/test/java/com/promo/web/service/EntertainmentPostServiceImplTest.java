package com.promo.web.service;

import com.promo.web.dto.EntertainmentPostDto;
import com.promo.web.dto.UserDto;
import com.promo.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EntertainmentPostServiceImplTest {

    @Autowired
    private EntertainmentPostService entertainmentPostService;

    @Autowired
    private UserService userService;

    @Test
    void createEntertainmentPost() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        EntertainmentPostDto entertainmentPostDto = EntertainmentPostDto.builder()
                .title("Title")
                .subTitle("Sub")
                .description("Description")
                .category(Category.Technology)
                .subCategory(SubCategory.AI_APPLICATION)
                .visibility("Private")
                .isOwnWork(true)
                .isPortrait(true)
                .webUrl("https://play.google.com")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        entertainmentPostService.createPost(createdUser.getId(), entertainmentPostDto);

        List<EntertainmentPost> wholePosts = entertainmentPostService.getWholePosts();

        assertEquals(wholePosts.isEmpty(), false);
        assertEquals(wholePosts.size(), 1);

        EntertainmentPost entertainmentPost = wholePosts.get(0);

        assertEquals(entertainmentPost.getTitle(), "Title");
        assertEquals(entertainmentPost.getIsOwnWork(), true);
        assertEquals(entertainmentPost.getTags().size(), 2);
    }

}
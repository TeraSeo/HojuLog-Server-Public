package com.promo.web.service;

import com.promo.web.dto.request.TechnologyPostDto;
import com.promo.web.dto.request.UserDto;
import com.promo.web.entity.Category;
import com.promo.web.entity.Post;
import com.promo.web.entity.SubCategory;
import com.promo.web.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostBookmarkServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TechnologyPostService technologyPostService;

    @Autowired
    private PostBookmarkService postBookmarkService;

    @Test
    void createDuplicatedBookmark() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        MockMultipartFile logoFile = new MockMultipartFile("file1", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile1 = new MockMultipartFile("file2", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );

        MockMultipartFile[] images = new MockMultipartFile[]{multipartFile1};

        TechnologyPostDto technologyPostDto = TechnologyPostDto.builder()
                .title("TechnologyTitle")
                .subTitle("Sub")
                .description("Description")
                .category(Category.Technology)
                .subCategory(SubCategory.AI_APPLICATION)
                .visibility("Private")
                .isOwnWork(true)
                .isPortrait(true)
                .playStoreUrl("https://play.google.com/store/apps/details?id=com.world.lotto")
                .appStoreUrl("https://apps.apple.com/kr/app/world-lotto/id6505033228?l=en-GB")
                .webUrl("https://play.google.com/store/apps/details?id=com.world.lotto")
                .youtubeUrl("https://youtube/videos")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        Post createdPost = technologyPostService.createPost(createdUser.getEmail(), technologyPostDto, logoFile, images);
        postBookmarkService.createBookmark(createdPost, createdUser);

        assertEquals(createdPost.getBookmarks().size(), 1);
        assertEquals(createdUser.getPostBookmarks().size(), 1);

        postBookmarkService.deleteBookmarkById(createdUser.getId(), createdPost.getId());
        assertEquals(createdPost.getBookmarks().size(), 0);
        assertEquals(createdUser.getPostBookmarks().size(), 0);
    }

}
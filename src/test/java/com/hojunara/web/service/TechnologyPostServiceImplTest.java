package com.hojunara.web.service;

import com.hojunara.web.dto.request.TechnologyPostDto;
import com.hojunara.web.dto.request.UserDto;
import com.hojunara.web.entity.*;
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
class TechnologyPostServiceImplTest {

    @Autowired
    private TechnologyPostService technologyPostService;

    @Autowired
    private UserService userService;

    @Test
    void createTechnologyPost() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        MockMultipartFile logoFile = new MockMultipartFile("file1", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile1 = new MockMultipartFile("file2", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile2 = new MockMultipartFile("file3", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile3 = new MockMultipartFile("file4", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile4 = new MockMultipartFile("file5", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile5 = new MockMultipartFile("file6", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );

        MockMultipartFile[] images = new MockMultipartFile[]{multipartFile1, multipartFile2, multipartFile3, multipartFile4, multipartFile5};

        TechnologyPostDto technologyPostDto = TechnologyPostDto.builder()
                .title("Title")
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
                .youtubeUrl("https://youtube/video")
                .tags(List.of("Tag1", "Tag2"))
                .build();

        long startTime = System.nanoTime();

        technologyPostService.createPost(createdUser.getEmail(), technologyPostDto, logoFile, images);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;

        System.out.println("Time taken to create technology post: " + durationInMillis + " ms");

        List<TechnologyPost> wholePosts = technologyPostService.getWholePosts();

        assertEquals(wholePosts.isEmpty(), false);
        assertEquals(wholePosts.size(), 1);

        TechnologyPost technologyPost = wholePosts.get(0);

        assertEquals(technologyPost.getTitle(), "Title");
        assertEquals(technologyPost.getIsOwnWork(), true);
        assertEquals(technologyPost.getTags().size(), 2);
        assertEquals(technologyPost.getImages().size(), 5);
        assertEquals(technologyPost.getVideos().size(), 1);
    }
}
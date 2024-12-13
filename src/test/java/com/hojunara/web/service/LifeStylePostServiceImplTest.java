//package com.hojunara.web.service;
//
//import com.hojunara.web.dto.request.LifeStylePostDto;
//import com.hojunara.web.dto.request.UserDto;
//import com.hojunara.web.entity.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class LifeStylePostServiceImplTest {
//
//    @Autowired
//    private LifeStylePostService lifeStylePostService;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    void createLifeStylePost() {
//        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
//        userService.createUser(user1);
//        User createdUser = userService.getUserByEmail(user1.getEmail());
//
//        MockMultipartFile logoFile = new MockMultipartFile("file1", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
//        MockMultipartFile multipartFile1 = new MockMultipartFile("file2", "test1.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
//
//        MockMultipartFile[] images = new MockMultipartFile[]{multipartFile1};
//
//        LifeStylePostDto lifeStylePostDto = LifeStylePostDto.builder()
//                .title("Title")
//                .subTitle("Sub")
//                .description("Description")
//                .category(Category.Technology)
//                .subCategory(SubCategory.AI_APPLICATION)
//                .visibility("Private")
//                .isOwnWork(true)
//                .isPortrait(true)
//                .webUrl("https://play.google.com")
//                .youtubeUrl("https://youtube/video")
//                .tags(List.of("Tag1", "Tag2"))
//                .build();
//
//        lifeStylePostService.createPost(createdUser.getEmail(), lifeStylePostDto, logoFile, images);
//
//        List<LifeStylePost> wholePosts = lifeStylePostService.getWholePosts();
//
//        assertEquals(wholePosts.isEmpty(), false);
//        assertEquals(wholePosts.size(), 1);
//
//        LifeStylePost lifeStylePost = wholePosts.get(0);
//
//        assertEquals(lifeStylePost.getTitle(), "Title");
//        assertEquals(lifeStylePost.getIsOwnWork(), true);
//        assertEquals(lifeStylePost.getTags().size(), 2);
//    }
//
//}
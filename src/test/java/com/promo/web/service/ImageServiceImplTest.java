//package com.promo.web.service;
//
//import com.promo.web.dto.request.UserDto;
//import com.promo.web.entity.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class ImageServiceImplTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    void createImage() {
//        UserDto user1 = UserDto.builder().email("user2@gmail.com").username("user1").password("1234").build();
//        userService.createUser(user1);
//
//        Post post1 = Post.builder().title("title").content("content").category(Category.Technology).visibility(Visibility.Public).isOwnWork(false).ownerInfo("info").build();
//
//        User createdUser = userService.getUserByEmail(user1.getEmail());
//        postService.createPost(createdUser.getId(), post1);
//
//        imageService.createImage("url", post1);
//
//        Post createdPost = postService.getPostById(post1.getId());
//        assertEquals(createdPost.getImages().size(), 1);
//    }
//}
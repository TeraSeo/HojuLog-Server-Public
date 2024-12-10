//package com.promo.web.service;
//
//import com.promo.web.dto.request.UserDto;
//import com.promo.web.entity.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class LikeServiceImplTest {
//
//    @Autowired
//    private LikeService likeService;
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private UserService userService;
//
//    @Test
//    void addLike() {
//        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
//        userService.createUser(user1);
//        User createdUser = userService.getUserByEmail(user1.getEmail());
//
//        Post post1 = Post.builder().title("title").content("content").category(Category.Technology).visibility(Visibility.Public).build();
//        postService.createPost(createdUser.getId(), post1);
//
//        likeService.createLike(post1, createdUser);
//
//        List<PostLike> wholeLikes = likeService.getWholeLikesByPostId(post1.getId());
//        assertEquals(1, wholeLikes.size(), "There should be only one like for this post");
//
//        PostLike postLike = wholeLikes.get(0);
//        assertEquals(post1.getId(), postLike.getPost().getId(), "The like should be associated with the correct post");
//        assertEquals(createdUser.getId(), postLike.getUser().getId(), "The like should be associated with the correct user");
//    }
//}
package com.promo.web.service;

import com.promo.web.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeServiceImplTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void addLike() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();
        postService.createPost(user1.getId(), post1);

        likeService.createLike(post1, user1);

        List<PostLike> wholeLikes = likeService.getWholeLikesByPostId(post1.getId());
        assertEquals(1, wholeLikes.size(), "There should be only one like for this post");

        PostLike postLike = wholeLikes.get(0);
        assertEquals(post1.getId(), postLike.getPost().getId(), "The like should be associated with the correct post");
        assertEquals(user1.getId(), postLike.getUser().getId(), "The like should be associated with the correct user");
    }
}
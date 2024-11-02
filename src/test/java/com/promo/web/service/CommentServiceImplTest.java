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
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void addComment() {
        User user1 = User.builder().email("user1@gmail.com").username("user1").password("1234").role(Role.USER).build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();
        postService.createPost(user1.getId(), post1);

        commentService.createComment(post1, user1, "comment");

        List<Comment> wholeComments = commentService.getWholeCommentsByPostId(post1.getId());
        assertEquals(1, wholeComments.size(), "There should be only one comment for this post");

        Comment comment = wholeComments.get(0);
        assertEquals(post1.getId(), comment.getPost().getId(), "The comment should be associated with the correct post");
        assertEquals(user1.getId(), comment.getUser().getId(), "The comment should be associated with the correct user");
    }
}
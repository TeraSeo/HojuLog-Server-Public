package com.promo.web.service;

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
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Test
    void addComment() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);
        User createdUser = userService.getUserByEmail(user1.getEmail());

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();
        postService.createPost(createdUser.getId(), post1);

        commentService.createComment(post1, createdUser, "comment");

        List<Comment> wholeComments = commentService.getWholeCommentsByPostId(post1.getId());
        assertEquals(1, wholeComments.size(), "There should be only one comment for this post");

        Comment comment = wholeComments.get(0);
        assertEquals(post1.getId(), comment.getPost().getId(), "The comment should be associated with the correct post");
        assertEquals(createdUser.getId(), comment.getUser().getId(), "The comment should be associated with the correct user");
    }
}
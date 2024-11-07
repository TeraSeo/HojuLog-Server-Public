package com.promo.web.service;

import com.promo.web.dto.UserDto;
import com.promo.web.entity.*;
import com.promo.web.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private AdditionalUrlService additionalUrlService;

    @Test
    void getWholePosts() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();
        Post post2 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();

        User createdUser = userService.getUserByEmail(user1.getEmail());

        postService.createPost(createdUser.getId(), post1);
        postService.createPost(createdUser.getId(), post2);

        List<Post> wholePosts = postService.getWholePosts();
        assertNotNull(wholePosts);
        assertTrue(wholePosts.size() == 2);
    }

    @Test
    void createPost() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();

        User createdUser = userService.getUserByEmail(user1.getEmail());
        postService.createPost(createdUser.getId(), post1);

        Post createdPost = postService.getPostById(post1.getId());
        assertEquals("title", createdPost.getTitle());
        assertEquals("content", createdPost.getContent());
        assertEquals("category", createdPost.getCategory());
        assertEquals("imageUrl", createdPost.getImageUrl());
        assertEquals(Visibility.Public, createdPost.getVisibility());
        assertEquals(createdUser.getId(), createdPost.getUser().getId());

        List<AdditionalUrl> wholeAdditionalUrlsByPostId = additionalUrlService.getWholeAdditionalUrlsByPostId(createdPost.getId());
        assertEquals(0, wholeAdditionalUrlsByPostId.size());
    }

    @Test
    void createPostWithTagsNAdditionalUrls() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).likes(List.of()).comments(List.of()).build();

        User createdUser = userService.getUserByEmail(user1.getEmail());
        postService.createPostWithTagsNAdditionalUrls(createdUser, post1, Set.of("Tag1", "Tags2"), List.of("Additional Url1", "Additional Url2"));

        Post createdPost = postService.getPostById(post1.getId());
        assertEquals("title", createdPost.getTitle());
        assertEquals("content", createdPost.getContent());
        assertEquals("category", createdPost.getCategory());
        assertEquals("imageUrl", createdPost.getImageUrl());
        assertEquals(Visibility.Public, createdPost.getVisibility());
        assertEquals(createdUser.getId(), createdPost.getUser().getId());
        assertEquals(2, createdPost.getTags().size());
        assertEquals(2, createdPost.getAdditionalUrls().size());

        Optional<Tag> createdTag = tagService.getTagByName("Tag1");
        createdTag.ifPresent(tag -> {
            assertEquals(1, tag.getPosts().size());
        });

        AdditionalUrl createdAdditionalUrl = additionalUrlService.getAdditionalUrlByUrl("Additional Url1");
        assertEquals(createdPost.getId(), createdAdditionalUrl.getPost().getId());
    }

    @Test
    void updatePost() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();

        User createdUser = userService.getUserByEmail(user1.getEmail());
        postService.createPostWithTagsNAdditionalUrls(createdUser, post1, Set.of("tag1", "tag2"), List.of("url1, url2"));

        Post createdPost = postService.getPostById(post1.getId());

        List<AdditionalUrl> wholeAdditionalUrlsByPostId = additionalUrlService.getWholeAdditionalUrlsByPostId(createdPost.getId());
        assertEquals(createdPost.getAdditionalUrls().size(), wholeAdditionalUrlsByPostId.size());

        assertEquals(2, createdPost.getTags().size());

        Post post2 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Private).build();
        postService.updatePost(post1.getId(), post2, Set.of(), List.of());

        Post updatedPost = postService.getPostById(post1.getId());
        assertEquals(Visibility.Private, updatedPost.getVisibility());

        // Check whether the excluded urls are deleted well
        wholeAdditionalUrlsByPostId = additionalUrlService.getWholeAdditionalUrlsByPostId(updatedPost.getId());
        assertEquals(0, wholeAdditionalUrlsByPostId.size());

        // Check whether the excluded tags are deleted well
        List<Tag> wholeTags = tagService.getWholeTags();
        assertEquals(0, wholeTags.size());
    }

    @Test
    void deletePostById() {
        UserDto user1 = UserDto.builder().email("user1@gmail.com").username("user1").password("1234").build();
        userService.createUser(user1);

        Post post1 = Post.builder().title("title").content("content").category("category").imageUrl("imageUrl").visibility(Visibility.Public).build();

        User createdUser = userService.getUserByEmail(user1.getEmail());
        postService.createPost(createdUser.getId(), post1);

        Post createdPost = postService.getPostById(post1.getId());
        postService.deletePostById(createdPost.getId());

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            postService.getPostById(createdPost.getId());
        });
        assertEquals("Post not found with id: " + createdPost.getId(), exception.getMessage());
    }
}
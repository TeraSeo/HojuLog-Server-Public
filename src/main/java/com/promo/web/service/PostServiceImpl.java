package com.promo.web.service;

import com.promo.web.entity.*;
import com.promo.web.exception.PostNotFoundException;
import com.promo.web.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService, TagService tagService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    @Override
    public List<Post> getWholePosts() {
        try {
            List<Post> posts = postRepository.findAll();
            log.info("Successfully got Whole Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Posts", e);
            throw e;
        }
    }

    @Override
    public Post getPostById(Long id) {
        try {
            Optional<Post> p = postRepository.findById(id);
            if (p.isPresent()) {
                log.info("Successfully found post with id: {}", id);
                return p.get();
            }
            throw new PostNotFoundException("Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find post with id: {}", id, e);
            throw e;
        }
    }

//    @Override
//    public void createPost(Long userId, Post post) {
//        User user = userService.getUserById(userId);
//        try {
//            post.setUser(user);
//            postRepository.save(post);
//            log.info("Successfully created post");
//        } catch (Exception e) {
//            log.error("Failed to create post", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public void createPostWithTagsNAdditionalUrls(User user, Post post, Set<String> tagNames, List<String> urls) {
//        try {
//            Set<Tag> tags = tagNames.stream()
//                    .map(tagName -> {
//                        Tag tag = tagService.getTagByName(tagName)
//                                .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));
//
//                        tag.getPosts().add(post);
//                        return tag;
//                    })
//                    .collect(Collectors.toSet());
//
//            post.setUser(user);
//            post.setTags(tags);
//            postRepository.save(post);
//            log.info("Successfully created post with tags and additional urls");
//        } catch (Exception e) {
//            log.error("Failed to create post with tags and additional urls", e);
//            throw e;
//        }
//    }
//
//    @Override
//    public void updatePost(Long id, Post post, Set<String> newTagNames, List<String> newUrls, List<Image> images) {
//        Post existingPost = getPostById(id);
//
//        try {
//            existingPost.setTitle(post.getTitle());
//            existingPost.setDescription(post.getDescription());
//            existingPost.setCategory(post.getCategory());
//            existingPost.setImages(images);
//            existingPost.setVisibility(post.getVisibility());
//
//            Set<Tag> updatedTags = newTagNames.stream()
//                    .map(tagName -> tagService.getTagByName(tagName)
//                            .orElseGet(() ->
//                                    tagService.createTag(new Tag(null, tagName, Set.of()))
//                            ))
//                    .collect(Collectors.toSet());
//
//            existingPost.setTags(updatedTags);
//
//            postRepository.save(existingPost);
//
//            // Delete excluded tags
//            tagService.deleteTagsWithoutPosts();
//
//            log.info("Successfully updated post with id: {}", id);
//        } catch (Exception e) {
//            log.error("Failed to update post with id: {}", id, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public void deletePostById(Long id) {
//        try {
//            postRepository.deleteById(id);
//            log.info("Successfully deleted post with id: {}", id);
//        } catch (Exception e) {
//            log.error("Failed to delete post with id: {}", id, e);
//            throw e;
//        }
//    }
}

package com.promo.web.service;

import com.promo.web.entity.AdditionalUrl;
import com.promo.web.entity.Post;
import com.promo.web.entity.Tag;
import com.promo.web.entity.User;
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
    private final AdditionalUrlService additionalUrlService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService, TagService tagService, AdditionalUrlService additionalUrlService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.additionalUrlService = additionalUrlService;
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

    @Override
    public void createPost(Long userId, Post post) {
        User user = userService.getUserById(userId);
        try {
            post.setUser(user);
            postRepository.save(post);
            log.info("Successfully created post");
        } catch (Exception e) {
            log.error("Failed to create post", e);
            throw e;
        }
    }

    @Override
    public void createPostWithTagsNAdditionalUrls(User user, Post post, Set<String> tagNames, List<String> urls) {
        try {
            Set<Tag> tags = tagNames.stream()
                    .map(tagName -> {
                        Tag tag = tagService.getTagByName(tagName)
                                .orElseGet(() -> tagService.createTag(new Tag(null, tagName, new HashSet<>())));

                        tag.getPosts().add(post);
                        return tag;
                    })
                    .collect(Collectors.toSet());

            List<AdditionalUrl> additionalUrls = urls.stream()
                    .map(url -> AdditionalUrl.builder()
                            .url(url)
                            .post(post)
                            .build())
                    .collect(Collectors.toList());

            post.setUser(user);
            post.setTags(tags);
            post.setAdditionalUrls(additionalUrls);
            postRepository.save(post);
            log.info("Successfully created post with tags and additional urls");
        } catch (Exception e) {
            log.error("Failed to create post with tags and additional urls", e);
            throw e;
        }
    }

    @Override
    public void updatePost(Long id, Post post, Set<String> newTagNames, List<String> newUrls) {
        Post existingPost = getPostById(id);

        try {
            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());
            existingPost.setCategory(post.getCategory());
            existingPost.setImageUrl(post.getImageUrl());
            existingPost.setVisibility(post.getVisibility());

            Set<Tag> updatedTags = newTagNames.stream()
                    .map(tagName -> tagService.getTagByName(tagName)
                            .orElseGet(() ->
                                    tagService.createTag(new Tag(null, tagName, Set.of()))
                            ))
                    .collect(Collectors.toSet());

            List<AdditionalUrl> existingUrls = existingPost.getAdditionalUrls();
            Set<String> newUrlSet = new HashSet<>(newUrls);

            // Delete not included urls from existing urls
            existingUrls.removeIf(existingUrl -> {
                boolean shouldRemove = !newUrlSet.contains(existingUrl.getUrl());
                if (shouldRemove) {
                    additionalUrlService.deleteAdditionalUrlById(existingUrl.getId());
                    log.info("Deleting AdditionalUrl with id: {}", existingUrl.getId());
                    return true;
                }
                return false;
            });

            // Add new urls
            List<AdditionalUrl> newAdditionalUrls = newUrls.stream()
                    .filter(url -> existingUrls.stream().noneMatch(existingUrl -> existingUrl.getUrl().equals(url)))
                    .map(url -> AdditionalUrl.builder()
                            .url(url)
                            .post(existingPost)
                            .build())
                    .collect(Collectors.toList());


            existingPost.setTags(updatedTags);
            existingPost.getAdditionalUrls().addAll(newAdditionalUrls);

            postRepository.save(existingPost);

            // Delete excluded tags
            tagService.deleteTagsWithoutPosts();

            log.info("Successfully updated post with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to update post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deletePostById(Long id) {
        try {
            postRepository.deleteById(id);
            log.info("Successfully deleted post with id: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete post with id: {}", id, e);
            throw e;
        }
    }
}

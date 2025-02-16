//package com.hojunara.web.service;
//
//import com.hojunara.web.entity.Post;
//import com.hojunara.web.entity.PostBookmark;
//import com.hojunara.web.entity.User;
//import com.hojunara.web.exception.PostBookmarkNotFoundByUserNPostException;
//import com.hojunara.web.exception.PostBookmarkNotFoundException;
//import com.hojunara.web.repository.PostBookmarkRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//@Transactional(rollbackFor = Exception.class)
//public class PostBookmarkServiceImpl implements PostBookmarkService {
//
//    private final PostBookmarkRepository postBookmarkRepository;
//    private final PostService postService;
//
//    @Autowired
//    public PostBookmarkServiceImpl(PostBookmarkRepository postBookmarkRepository, PostService postService) {
//        this.postBookmarkRepository = postBookmarkRepository;
//        this.postService = postService;
//    }
//
//    @Override
//    public List<PostBookmark> getWholeBookmarksByPostId(Long id) {
//        Post post = postService.getPostById(id);
//
//        try {
//            List<PostBookmark> postBookmarks = post.getBookmarks();
//            log.info("Successfully got whole post bookmarks with post id: {}", id);
//            return postBookmarks;
//        } catch (Exception e) {
//            log.error("Failed to get whole post bookmarks with post id: {}", id, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public PostBookmark getPostBookmarkById(Long id) {
//        try {
//            Optional<PostBookmark> postBookmark = postBookmarkRepository.findById(id);
//            if (postBookmark.isPresent()) {
//                log.info("Successfully got post bookmark with id: {}", id);
//                return postBookmark.get();
//            }
//            throw new PostBookmarkNotFoundException("PostLike not found with id: " + id);
//        } catch (Exception e) {
//            log.error("Failed to get post bookmark with id: {}", id, e);
//            throw e;
//        }
//    }
//
//    @Override
//    public Boolean createBookmark(Post post, User user) {
//        try {
//            if (checkIsPostBookmarkedByUser(user.getId(), post.getId())) {
//                return true;
//            }
//
//            PostBookmark postBookmark = PostBookmark.builder().post(post).user(user).createdAt(new Timestamp(System.currentTimeMillis())).build();
//            post.getBookmarks().add(postBookmark);
//            user.getPostBookmarks().add(postBookmark);
//            postBookmarkRepository.save(postBookmark);
//
//            log.info("Successfully created post bookmark with post id: {}, user id: {}", post.getId(), user.getId());
//            return true;
//        } catch (Exception e) {
//            log.error("Failed to create post bookmark with post id: {}, user id: {}", post.getId(), user.getId(), e);
//            return false;
//        }
//    }
//
//    @Override
//    public Boolean deleteBookmarkById(Long postId, Long userId) {
//        try {
//            Optional<PostBookmark> p = postBookmarkRepository.findByUserIdAndPostId(userId, postId);
//            if (p.isPresent()) {
//                PostBookmark postBookmark = p.get();
//
//                Post post = postBookmark.getPost();
//                User user = postBookmark.getUser();
//
//                if (post != null) post.getBookmarks().remove(postBookmark);
//                if (user != null) user.getPostBookmarks().remove(postBookmark);
//
//                postBookmarkRepository.deleteById(postBookmark.getId());
//                log.info("Successfully deleted post bookmark with id: {}", postBookmark.getId());
//
//                return true;
//            }
//
//            throw new PostBookmarkNotFoundByUserNPostException("PostBookmark not found by user id: " + userId + " and post id: " + postId);
//        } catch (Exception e) {
//            log.error("Failed to delete post bookmark with post id: {}  user id: {}", postId, userId, e);
//            return false;
//        }
//    }
//
//    @Override
//    public Boolean checkIsPostBookmarkedByUser(Long userId, Long postId) {
//        return postBookmarkRepository.existsByUserIdAndPostId(userId, postId);
//    }
//}

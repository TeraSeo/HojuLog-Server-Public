package com.hojunara.web.service;

import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.dto.request.UpdateStudyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.StudyPostNotFoundException;
import com.hojunara.web.repository.StudyPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class StudyPostServiceImpl implements StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final UserService userService;
    private final BlogContentService blogContentService;
    private final KeywordService keywordService;

    @Autowired
    public StudyPostServiceImpl(StudyPostRepository studyPostRepository, UserService userService, BlogContentService blogContentService, KeywordService keywordService) {
        this.studyPostRepository = studyPostRepository;
        this.userService = userService;
        this.blogContentService = blogContentService;
        this.keywordService = keywordService;
    }

    @Override
    public List<StudyPost> getWholePosts() {
        try {
            List<StudyPost> posts = studyPostRepository.findAll();
            log.info("Successfully got Whole Study Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Study Posts", e);
            throw e;
        }
    }

    @Override
    public Page<StudyPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<StudyPost> posts = studyPostRepository.findAllWithPinnedFirst(pageable);
            log.info("Successfully got pageable Study Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Study Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<StudyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<StudyPost> posts = studyPostRepository.findAllBySubCategoryOrderByUpdatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Study Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Study Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<StudyPost> getRecent5Posts() {
        try {
            List<StudyPost> posts = studyPostRepository.findTop5ByOrderByUpdatedAtDesc();
            log.info("Successfully got Recent 5 Study Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Study Posts", e);
            throw e;
        }
    }

    @Override
    public StudyPost getPostById(Long id) {
        try {
            Optional<StudyPost> s = studyPostRepository.findById(id);
            if (s.isPresent()) {
                log.info("Successfully found study post with id: {}", id);
                return s.get();
            }
            throw new StudyPostNotFoundException("Study Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find study post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(StudyPostDto studyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(studyPostDto.getUserId());
        try {
            StudyPost studyPost = StudyPost.builder()
                    .title(studyPostDto.getTitle())
                    .category(Category.유학)
                    .postType(PostType.BLOG)
                    .subCategory(studyPostDto.getSubCategory())
                    .school(studyPostDto.getSchool())
                    .isPublic(studyPostDto.getIsPublic())
                    .isCommentAllowed(studyPostDto.getIsCommentAllowed())
                    .build();

            studyPost.setUser(user);
            StudyPost createdPost = studyPostRepository.save(studyPost);

            // save post images data
            if (studyPostDto.getBlogContents().size() > 0) {
                List<BlogContent> blogContents = BlogContent.convertMapToBlogContent(studyPostDto.getBlogContents());
                blogContentService.saveBlogContentList(blogContents, images, user.getEmail(), createdPost);
            }

            // save keywords
            studyPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created study post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create study post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdateStudyPostDto updateStudyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(updateStudyPostDto.getUserId());
        StudyPost studyPost = getPostById(updateStudyPostDto.getPostId());
        try {
            boolean isBlogUpdated = false;

            if (!studyPost.getTitle().equals(updateStudyPostDto.getTitle())) {
                studyPost.setTitle(updateStudyPostDto.getTitle());
            }
            if (!Objects.equals(studyPost.getSchool(), updateStudyPostDto.getSchool())) { // prevent null value comparison
                studyPost.setSchool(updateStudyPostDto.getSchool());
            }
            if (!studyPost.getIsPublic().equals(updateStudyPostDto.getIsPublic())) {
                studyPost.setIsPublic(updateStudyPostDto.getIsPublic());
            }
            if (!studyPost.getIsCommentAllowed().equals(updateStudyPostDto.getIsCommentAllowed())) {
                studyPost.setIsCommentAllowed(updateStudyPostDto.getIsCommentAllowed());
            }

            // 블로그 contents 업데이트
            List<Map<String, String>> originalBlogContentMap = BlogContent.convertBlogContentToMap(studyPost.getBlogContents());
            List<Map<String, String>> updatedBlogContentMap = updateStudyPostDto.getBlogContents();
            if (!originalBlogContentMap.equals(updatedBlogContentMap)) {
                List<Map<String, String>> addedContentList = new ArrayList<>();
                List<Long> addedContentOrderList = new ArrayList<>();

                // 새로 더해진 contents 구분 후 순서 부여
                for (int i = 0; i < updatedBlogContentMap.size(); i++) {
                    Map<String, String> updatedMap = updatedBlogContentMap.get(i);
                    if (!originalBlogContentMap.contains(updatedMap)) {
                        addedContentList.add(updatedMap);
                        addedContentOrderList.add((long) i);
                    }
                    else {
                        originalBlogContentMap.remove(updatedMap);
                    }
                }

                List<Map<String, String>> removedContentList = new ArrayList<>(originalBlogContentMap);

                if (!addedContentList.isEmpty()) {
                    List<BlogContent> addedBlogContents = BlogContent.convertMapToBlogContent(addedContentList);
                    blogContentService.updateBlogContentList(addedBlogContents, addedContentOrderList, images, user.getEmail(), studyPost);
                }

                // 삭제된 contents 삭제
                if (!removedContentList.isEmpty()) {
                    // 중복되는 contents 전체 삭제 방지
                    Map<Map<String, String>, Integer> removalCounts = new HashMap<>();
                    for (Map<String, String> removedMap : removedContentList) {
                        removalCounts.put(removedMap, removalCounts.getOrDefault(removedMap, 0) + 1);
                    }

                    Iterator<BlogContent> iterator = studyPost.getBlogContents().iterator();
                    while (iterator.hasNext()) {
                        BlogContent blogContent = iterator.next();
                        Map<String, String> blogContentMap = BlogContent.convertBlogContentToMap(List.of(blogContent)).get(0);

                        if (removalCounts.containsKey(blogContentMap) && removalCounts.get(blogContentMap) > 0) {
                            iterator.remove();
                            removalCounts.put(blogContentMap, removalCounts.get(blogContentMap) - 1);
                        }
                    }
                }

                isBlogUpdated = true;
            }

            // 키워드 업데이트
            List<String> updatedKeywords = updateStudyPostDto.getSelectedKeywords();
            keywordService.updateKeyword(studyPost, updatedKeywords);

            studyPost.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Australia/Sydney"))));
            studyPostRepository.save(studyPost);
            studyPostRepository.flush(); // 업데이트 내용 반영

            // contents 순서 알맞게 변경
            if (isBlogUpdated) {
                StudyPost updatedStudyPost = getPostById(updateStudyPostDto.getPostId());

                List<BlogContent> orderedBlogContents = new ArrayList<>();
                List<BlogContent> updatedBlogContents = new ArrayList<>(updatedStudyPost.getBlogContents());

                for (int i = 0; i < updatedBlogContentMap.size(); i++) {
                    Map<String, String> blogMap = updatedBlogContentMap.get(i);

                    Optional<BlogContent> matchingContent = updatedBlogContents.stream()
                            .filter(blogContent -> {
                                String blogType = blogMap.get("type");
                                if (blogType == null) return false;

                                if (blogContent instanceof ImageContent) {
                                    return "image".equals(blogType) &&
                                            ((ImageContent) blogContent).getImageUrl().equals(blogMap.get("imageUrl"));
                                }
                                else if (blogContent instanceof DescriptionContent) {
                                    if (!"description".equals(blogType)) return false;

                                    Map<String, String> blogContentToMap = BlogContent.convertBlogContentToMap(List.of(blogContent)).get(0);
                                    return blogContentToMap.get("content").equals(blogMap.get("content")) &&
                                            blogContentToMap.get("fontSize").equals(blogMap.get("fontSize")) &&
                                            blogContentToMap.get("fontWeight").equals(blogMap.get("fontWeight"));
                                }
                                return false;
                            })
                            .findFirst();

                    if (matchingContent.isEmpty()) {
                        orderedBlogContents.add(null);
                    }
                    else {
                        matchingContent.ifPresent(content -> {
                            orderedBlogContents.add(content);
                            updatedBlogContents.remove(content);
                        });
                    }
                }

                for (int i = 0; i < orderedBlogContents.size(); i++) {
                    BlogContent content = orderedBlogContents.get(i);
                    if (content != null) {
                        content.setOrderIndex((long) i);

                        blogContentService.updateBlogContent(content);
                    }
                }

                return updatedStudyPost;
            }

            log.info("Successfully updated study post");

            return studyPost;
        } catch (Exception e) {
            log.error("Failed to update study post", e);
            return null;
        }
    }

    @Override
    public List<StudyPost> searchStudyPost(String title, String subCategory, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");

        List<StudyPost> studyPostList;
        if (isTitleEmpty && isSubCategoryEmpty) studyPostList = searchByCategory();
        else if (isSubCategoryEmpty) studyPostList = searchByTitle(title);
        else if (isTitleEmpty) studyPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else studyPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));

        if (keywords != null && !keywords.isEmpty()) {
            studyPostList = studyPostList.stream()
                    .map(post -> {
                        List<String> postKeywords = post.getKeywords().stream()
                                .map(Keyword::getKeyWord)
                                .toList();

                        long matchCount = keywords.stream()
                                .filter(postKeywords::contains)
                                .count();

                        return new AbstractMap.SimpleEntry<>(post, matchCount);
                    })
                    .filter(entry -> {
                        long matchCount = entry.getValue();
                        int totalSearchedKeywords = keywords.size();
                        long inCorrectKeywordsCount = totalSearchedKeywords - matchCount;

                        if (totalSearchedKeywords >= 7) {
                            return inCorrectKeywordsCount <= 3;
                        }
                        if (totalSearchedKeywords >= 5) {
                            return inCorrectKeywordsCount <= 2;
                        } else if (totalSearchedKeywords >= 2) {
                            return inCorrectKeywordsCount <= 1;
                        }
                        return inCorrectKeywordsCount <= 0;
                    })
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Sort by matchCount descending
                    .map(Map.Entry::getKey) // Extract the original post
                    .toList();
        }

        return studyPostList;
    }

    @Override
    public List<StudyPost> searchByCategory() {
        return studyPostRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public List<StudyPost> searchByTitle(String title) {
        return studyPostRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    }

    @Override
    public List<StudyPost> searchBySubCategory(SubCategory subCategory) {
        return studyPostRepository.findBySubCategoryOrderByUpdatedAtDesc(subCategory);
    }

    @Override
    public List<StudyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return studyPostRepository.findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(title, subCategory);
    }

    @Override
    public Page<StudyPost> convertPostsAsPage(List<StudyPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<StudyPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}
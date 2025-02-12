package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.StudyPostDto;
import com.hojunara.web.dto.request.UpdateStudyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.StudyPostNotFoundException;
import com.hojunara.web.repository.StudyPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
            Page<StudyPost> posts = studyPostRepository.findAllByOrderByCreatedAtDesc(pageable);
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
            Page<StudyPost> posts = studyPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
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
            List<StudyPost> posts = studyPostRepository.findTop5ByOrderByCreatedAtDesc();
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
        try {
            StudyPost studyPost = getPostById(updateStudyPostDto.getPostId());

            boolean isUpdated = false;
            boolean isBlogUpdated = false;

            if (!studyPost.getTitle().equals(updateStudyPostDto.getTitle())) {
                studyPost.setTitle(updateStudyPostDto.getTitle());
                isUpdated = true;
            }
            if (!Objects.equals(studyPost.getSchool(), updateStudyPostDto.getSchool())) { // prevent null value comparison
                studyPost.setSchool(updateStudyPostDto.getSchool());
                isUpdated = true;
            }
            if (!studyPost.getIsPublic().equals(updateStudyPostDto.getIsPublic())) {
                studyPost.setIsPublic(updateStudyPostDto.getIsPublic());
                isUpdated = true;
            }
            if (!studyPost.getIsCommentAllowed().equals(updateStudyPostDto.getIsCommentAllowed())) {
                studyPost.setIsCommentAllowed(updateStudyPostDto.getIsCommentAllowed());
                isUpdated = true;
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
            List<String> originalKeywords = studyPost.getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
            List<String> updatedKeywords = updateStudyPostDto.getSelectedKeywords();
            if (!originalKeywords.equals(updatedKeywords)) {
                List<String> addedKeywords = updatedKeywords.stream()
                        .filter(keyword -> !originalKeywords.contains(keyword))
                        .collect(Collectors.toList());

                List<String> removedKeywords = originalKeywords.stream()
                        .filter(keyword -> !updatedKeywords.contains(keyword))
                        .collect(Collectors.toList());

                if (!addedKeywords.isEmpty()) {
                    addedKeywords.forEach(keyword -> {
                        keywordService.createKeyword(keyword, studyPost);
                    });
                }

                if (!removedKeywords.isEmpty()) {
                    studyPost.getKeywords().removeIf(keyword -> removedKeywords.contains(keyword.getKeyWord()));
                }

                isUpdated = true;
            }

            if (isUpdated || isBlogUpdated) {
                studyPostRepository.save(studyPost);
                studyPostRepository.flush(); // 업데이트 내용 반영
            }

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
}
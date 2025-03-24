package com.hojunara.web.service;

import com.hojunara.web.dto.request.SocietyPostDto;
import com.hojunara.web.dto.request.UpdateSocietyPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.SocietyPostNotFoundException;
import com.hojunara.web.repository.SocietyPostRepository;
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
public class SocietyPostServiceImpl implements SocietyPostService {

    private final SocietyPostRepository societyPostRepository;
    private final UserService userService;
    private final KeywordService keywordService;
    private final BlogContentService blogContentService;

    @Autowired
    public SocietyPostServiceImpl(SocietyPostRepository societyPostRepository, UserService userService, KeywordService keywordService, BlogContentService blogContentService) {
        this.societyPostRepository = societyPostRepository;
        this.userService = userService;
        this.keywordService = keywordService;
        this.blogContentService = blogContentService;
    }

    @Override
    public List<SocietyPost> getWholePosts() {
        try {
            List<SocietyPost> posts = societyPostRepository.findAll();
            log.info("Successfully got Whole Society Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Society Posts", e);
            throw e;
        }
    }

    @Override
    public Page<SocietyPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<SocietyPost> posts = societyPostRepository.findAllWithPinnedFirst(pageable);
            log.info("Successfully got pageable Society Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Society Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<SocietyPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<SocietyPost> posts = societyPostRepository.findAllBySubCategoryOrderByUpdatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Society Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Society Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<SocietyPost> getRecent5Posts() {
        try {
            List<SocietyPost> posts = societyPostRepository.findTop5ByOrderByUpdatedAtDesc();
            log.info("Successfully got Recent 5 Society Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Society Posts", e);
            throw e;
        }
    }

    @Override
    public SocietyPost getPostById(Long id) {
        try {
            Optional<SocietyPost> s = societyPostRepository.findById(id);
            if (s.isPresent()) {
                log.info("Successfully found society post with id: {}", id);
                return s.get();
            }
            throw new SocietyPostNotFoundException("Society Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find society post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(SocietyPostDto societyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(societyPostDto.getUserId());
        try {
            SocietyPost societyPost = SocietyPost.builder()
                    .title(societyPostDto.getTitle())
                    .category(Category.생활)
                    .subCategory(societyPostDto.getSubCategory())
                    .postType(PostType.BLOG)
                    .isPublic(societyPostDto.getIsPublic())
                    .isCommentAllowed(societyPostDto.getIsCommentAllowed())
                    .viewCounts(0L)
                    .build();

            societyPost.setUser(user);
            SocietyPost createdPost = societyPostRepository.save(societyPost);

            // save post images data
            if (societyPostDto.getBlogContents().size() > 0) {
                List<BlogContent> blogContents = BlogContent.convertMapToBlogContent(societyPostDto.getBlogContents());
                blogContentService.saveBlogContentList(blogContents, images, user.getEmail(), createdPost);
            }

            // save keywords
            societyPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created society post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create society post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdateSocietyPostDto updateSocietyPostDto, MultipartFile[] images) {
        User user = userService.getUserById(updateSocietyPostDto.getUserId());
        try {
            SocietyPost societyPost = getPostById(updateSocietyPostDto.getPostId());
            boolean isBlogUpdated = false;

            if (!societyPost.getTitle().equals(updateSocietyPostDto.getTitle())) {
                societyPost.setTitle(updateSocietyPostDto.getTitle());
            }
            if (!societyPost.getIsPublic().equals(updateSocietyPostDto.getIsPublic())) {
                societyPost.setIsPublic(updateSocietyPostDto.getIsPublic());
            }
            if (!societyPost.getIsCommentAllowed().equals(updateSocietyPostDto.getIsCommentAllowed())) {
                societyPost.setIsCommentAllowed(updateSocietyPostDto.getIsCommentAllowed());
            }

            // 블로그 contents 업데이트
            List<Map<String, String>> originalBlogContentMap = BlogContent.convertBlogContentToMap(societyPost.getBlogContents());
            List<Map<String, String>> updatedBlogContentMap = updateSocietyPostDto.getBlogContents();
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
                    blogContentService.updateBlogContentList(addedBlogContents, addedContentOrderList, images, user.getEmail(), societyPost);
                }

                // 삭제된 contents 삭제
                if (!removedContentList.isEmpty()) {
                    // 중복되는 contents 전체 삭제 방지
                    Map<Map<String, String>, Integer> removalCounts = new HashMap<>();
                    for (Map<String, String> removedMap : removedContentList) {
                        removalCounts.put(removedMap, removalCounts.getOrDefault(removedMap, 0) + 1);
                    }

                    Iterator<BlogContent> iterator = societyPost.getBlogContents().iterator();
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
            List<String> updatedKeywords = updateSocietyPostDto.getSelectedKeywords();
            keywordService.updateKeyword(societyPost, updatedKeywords);

            final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
            societyPost.setUpdatedAt(Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant()));
            societyPostRepository.save(societyPost);
            societyPostRepository.flush(); // 업데이트 내용 반영

            // contents 순서 알맞게 변경
            if (isBlogUpdated) {
                SocietyPost updatedSocietyPost = getPostById(updateSocietyPostDto.getPostId());

                List<BlogContent> orderedBlogContents = new ArrayList<>();
                List<BlogContent> updatedBlogContents = new ArrayList<>(updatedSocietyPost.getBlogContents());

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
                                            blogContentToMap.get("fontWeight").equals(blogMap.get("fontWeight")) &&
                                            blogContentToMap.get("fontFamily").equals(blogMap.get("fontFamily"));
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

                return updatedSocietyPost;
            }

            log.info("Successfully updated society post");

            return societyPost;
        } catch (Exception e) {
            log.error("Failed to update society post", e);
            return null;
        }
    }

    @Override
    public List<SocietyPost> searchSocietyPost(String title, String subCategory, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");

        List<SocietyPost> societyPostList;
        if (isTitleEmpty && isSubCategoryEmpty) societyPostList = searchByCategory();
        else if (isSubCategoryEmpty) societyPostList = searchByTitle(title);
        else if (isTitleEmpty) societyPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else societyPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));

        if (keywords != null && !keywords.isEmpty()) {
            societyPostList = societyPostList.stream()
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

        return societyPostList;
    }

    @Override
    public List<SocietyPost> searchByCategory() {
        return societyPostRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public List<SocietyPost> searchByTitle(String title) {
        return societyPostRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    }

    @Override
    public List<SocietyPost> searchBySubCategory(SubCategory subCategory) {
        return societyPostRepository.findBySubCategoryOrderByUpdatedAtDesc(subCategory);
    }

    @Override
    public List<SocietyPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return societyPostRepository.findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(title, subCategory);
    }

    @Override
    public Page<SocietyPost> convertPostsAsPage(List<SocietyPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<SocietyPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}

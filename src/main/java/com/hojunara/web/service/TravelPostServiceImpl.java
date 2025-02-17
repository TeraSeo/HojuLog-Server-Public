package com.hojunara.web.service;

import com.hojunara.web.dto.request.TravelPostDto;
import com.hojunara.web.dto.request.UpdateTravelPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.TravelPostNotFoundException;
import com.hojunara.web.repository.TravelPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TravelPostServiceImpl implements TravelPostService {

    private final TravelPostRepository travelPostRepository;
    private final UserService userService;
    private final KeywordService keywordService;
    private final BlogContentService blogContentService;

    @Autowired
    public TravelPostServiceImpl(TravelPostRepository travelPostRepository, UserService userService, KeywordService keywordService, BlogContentService blogContentService) {
        this.travelPostRepository = travelPostRepository;
        this.userService = userService;
        this.keywordService = keywordService;
        this.blogContentService = blogContentService;
    }

    @Override
    public List<TravelPost> getWholePosts() {
        try {
            List<TravelPost> posts = travelPostRepository.findAll();
            log.info("Successfully got Whole Travel Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Whole Travel Posts", e);
            throw e;
        }
    }

    @Override
    public Page<TravelPost> getCreatedAtDescPostsByPage(Pageable pageable) {
        try {
            Page<TravelPost> posts = travelPostRepository.findAllByOrderByCreatedAtDesc(pageable);
            log.info("Successfully got pageable Travel Posts order by createdAt Desc");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Travel Posts order by createdAt Desc", e);
            throw e;
        }
    }

    @Override
    public Page<TravelPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable) {
        try {
            Page<TravelPost> posts = travelPostRepository.findAllBySubCategoryOrderByCreatedAtDesc(subCategory, pageable);
            log.info("Successfully got pageable Travel Posts order by createdAt Desc and subcategory: {}", subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable Travel Posts order by createdAt Desc and subcategory: {}", subCategory, e);
            throw e;
        }
    }

    @Override
    public List<TravelPost> getRecent5Posts() {
        try {
            List<TravelPost> posts = travelPostRepository.findTop5ByOrderByCreatedAtDesc();
            log.info("Successfully got Recent 5 Travel Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 Travel Posts", e);
            throw e;
        }
    }

    @Override
    public TravelPost getPostById(Long id) {
        try {
            Optional<TravelPost> t = travelPostRepository.findById(id);
            if (t.isPresent()) {
                log.info("Successfully found travel post with id: {}", id);
                return t.get();
            }
            throw new TravelPostNotFoundException("Travel Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find travel post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public Post createPost(TravelPostDto travelPostDto, MultipartFile[] images) {
        User user = userService.getUserById(travelPostDto.getUserId());
        try {
            TravelPost travelPost = TravelPost.builder()
                    .title(travelPostDto.getTitle())
                    .category(Category.여행)
                    .subCategory(travelPostDto.getSubCategory())
                    .postType(PostType.BLOG)
                    .country(travelPostDto.getCountry())
                    .location(travelPostDto.getLocation())
                    .isPublic(travelPostDto.getIsPublic())
                    .isCommentAllowed(travelPostDto.getIsCommentAllowed())
                    .build();

            travelPost.setUser(user);
            TravelPost createdPost = travelPostRepository.save(travelPost);

            // save post images data
            if (travelPostDto.getBlogContents().size() > 0) {
                List<BlogContent> blogContents = BlogContent.convertMapToBlogContent(travelPostDto.getBlogContents());
                blogContentService.saveBlogContentList(blogContents, images, user.getEmail(), createdPost);
            }

            // save keywords
            travelPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            log.info("Successfully created travel post");

            return createdPost;
        } catch (Exception e) {
            log.error("Failed to create travel post", e);
            return null;
        }
    }

    @Override
    public Post updatePost(UpdateTravelPostDto updateTravelPostDto, MultipartFile[] images) {
        User user = userService.getUserById(updateTravelPostDto.getUserId());
        try {
            TravelPost travelPost = getPostById(updateTravelPostDto.getPostId());

            boolean isUpdated = false;
            boolean isBlogUpdated = false;

            if (!travelPost.getTitle().equals(updateTravelPostDto.getTitle())) {
                travelPost.setTitle(updateTravelPostDto.getTitle());
                isUpdated = true;
            }
            if (!travelPost.getCountry().equals(updateTravelPostDto.getCountry())) {
                travelPost.setCountry(updateTravelPostDto.getCountry());
                isUpdated = true;
            }
            if (!travelPost.getLocation().equals(updateTravelPostDto.getLocation())) {
                travelPost.setLocation(updateTravelPostDto.getLocation());
                isUpdated = true;
            }
            if (!travelPost.getIsPublic().equals(updateTravelPostDto.getIsPublic())) {
                travelPost.setIsPublic(updateTravelPostDto.getIsPublic());
                isUpdated = true;
            }
            if (!travelPost.getIsCommentAllowed().equals(updateTravelPostDto.getIsCommentAllowed())) {
                travelPost.setIsCommentAllowed(updateTravelPostDto.getIsCommentAllowed());
                isUpdated = true;
            }

            // 블로그 contents 업데이트
            List<Map<String, String>> originalBlogContentMap = BlogContent.convertBlogContentToMap(travelPost.getBlogContents());
            List<Map<String, String>> updatedBlogContentMap = updateTravelPostDto.getBlogContents();
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
                    blogContentService.updateBlogContentList(addedBlogContents, addedContentOrderList, images, user.getEmail(), travelPost);
                }

                // 삭제된 contents 삭제
                if (!removedContentList.isEmpty()) {
                    // 중복되는 contents 전체 삭제 방지
                    Map<Map<String, String>, Integer> removalCounts = new HashMap<>();
                    for (Map<String, String> removedMap : removedContentList) {
                        removalCounts.put(removedMap, removalCounts.getOrDefault(removedMap, 0) + 1);
                    }

                    Iterator<BlogContent> iterator = travelPost.getBlogContents().iterator();
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
            List<String> originalKeywords = travelPost.getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
            List<String> updatedKeywords = updateTravelPostDto.getSelectedKeywords();
            if (!originalKeywords.equals(updatedKeywords)) {
                List<String> addedKeywords = updatedKeywords.stream()
                        .filter(keyword -> !originalKeywords.contains(keyword))
                        .collect(Collectors.toList());

                List<String> removedKeywords = originalKeywords.stream()
                        .filter(keyword -> !updatedKeywords.contains(keyword))
                        .collect(Collectors.toList());

                if (!addedKeywords.isEmpty()) {
                    addedKeywords.forEach(keyword -> {
                        keywordService.createKeyword(keyword, travelPost);
                    });
                }

                if (!removedKeywords.isEmpty()) {
                    travelPost.getKeywords().removeIf(keyword -> removedKeywords.contains(keyword.getKeyWord()));
                }

                isUpdated = true;
            }

            if (isUpdated || isBlogUpdated) {
                travelPostRepository.save(travelPost);
                travelPostRepository.flush(); // 업데이트 내용 반영
            }

            // contents 순서 알맞게 변경
            if (isBlogUpdated) {
                TravelPost updatedTravelPost = getPostById(updateTravelPostDto.getPostId());

                List<BlogContent> orderedBlogContents = new ArrayList<>();
                List<BlogContent> updatedBlogContents = new ArrayList<>(updatedTravelPost.getBlogContents());

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

                return updatedTravelPost;
            }

            log.info("Successfully updated travel post");

            return travelPost;
        } catch (Exception e) {
            log.error("Failed to update travel post", e);
            return null;
        }
    }

    @Override
    public List<TravelPost> searchTravelPost(String title, String subCategory, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");

        List<TravelPost> travelPostList;
        if (isTitleEmpty && isSubCategoryEmpty) travelPostList = searchByCategory();
        else if (isSubCategoryEmpty) travelPostList = searchByTitle(title);
        else if (isTitleEmpty) travelPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else travelPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));

        if (keywords != null && !keywords.isEmpty()) {
            travelPostList = travelPostList.stream()
                    .filter(post -> {
                        List<String> postKeywords = post.getKeywords().stream()
                                .map(Keyword::getKeyWord)
                                .toList();

                        // 맞는 키워드 개수 체크
                        long matchCount = keywords.stream()
                                .filter(postKeywords::contains)
                                .count();

                        // 최소 필요한 키워드 개수 정하기
                        int totalSearchedKeywords = keywords.size();
                        long inCorrectKeywordsCount = totalSearchedKeywords - matchCount;
                        if (totalSearchedKeywords >= 7) {
                            return inCorrectKeywordsCount <= 3;
                        }
                        if (totalSearchedKeywords >= 5) {
                            return inCorrectKeywordsCount <= 2;
                        }
                        else if (totalSearchedKeywords >= 2) {
                            return inCorrectKeywordsCount <= 1;
                        }
                        return inCorrectKeywordsCount <= 0;
                    })
                    .toList();
        }

        return travelPostList;
    }

    @Override
    public List<TravelPost> searchByCategory() {
        return travelPostRepository.findAll();
    }

    @Override
    public List<TravelPost> searchByTitle(String title) {
        return travelPostRepository.findByTitleContainingOrderByCreatedAtDesc(title);
    }

    @Override
    public List<TravelPost> searchBySubCategory(SubCategory subCategory) {
        return travelPostRepository.findBySubCategoryOrderByCreatedAtDesc(subCategory);
    }

    @Override
    public List<TravelPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return travelPostRepository.findByTitleContainingAndSubCategoryOrderByCreatedAtDesc(title, subCategory);
    }

    @Override
    public Page<TravelPost> convertPostsAsPage(List<TravelPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<TravelPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}

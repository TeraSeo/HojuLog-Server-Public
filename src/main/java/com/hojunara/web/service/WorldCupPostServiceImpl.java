package com.hojunara.web.service;

import com.hojunara.web.aws.s3.AwsFileService;
import com.hojunara.web.dto.request.UpdateWorldCupPostDto;
import com.hojunara.web.dto.request.WorldCupPostDto;
import com.hojunara.web.entity.*;
import com.hojunara.web.exception.WorldCupPostNotFoundException;
import com.hojunara.web.repository.WorldCupPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class WorldCupPostServiceImpl implements WorldCupPostService {

    private final WorldCupPostRepository worldCupPostRepository;
    private final CandidateService candidateService;
    private final UserService userService;
    private final KeywordService keywordService;
    private final AwsFileService awsFileService;

    @Autowired
    public WorldCupPostServiceImpl(WorldCupPostRepository worldCupPostRepository, CandidateService candidateService, UserService userService, KeywordService keywordService, AwsFileService awsFileService) {
        this.worldCupPostRepository = worldCupPostRepository;
        this.candidateService = candidateService;
        this.userService = userService;
        this.keywordService = keywordService;
        this.awsFileService = awsFileService;
    }

    @Override
    public WorldCupPost getPostById(Long id) {
        try {
            Optional<WorldCupPost> w = worldCupPostRepository.findById(id);
            if (w.isPresent()) {
                log.info("Successfully found world cup post with id: {}", id);
                return w.get();
            }
            throw new WorldCupPostNotFoundException("World Cup Post not found with id: " + id);
        } catch (Exception e) {
            log.error("Failed to find world cup post with id: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<WorldCupPost> getRecent5Posts() {
        try {
            List<WorldCupPost> posts = worldCupPostRepository.findTop5ByOrderByUpdatedAtDesc();
            log.info("Successfully got Recent 5 World Cup Posts");
            return posts;
        } catch (Exception e) {
            log.error("Failed to get Recent 5 World Cup Posts", e);
            throw e;
        }
    }

    @Override
    public Page<WorldCupPost> getCreatedAtDescPostsByPage(Pageable pageable, String option) {
        try {
            Page<WorldCupPost> posts;
            if (option.equals("최신순")) {
                posts = worldCupPostRepository.findAllWithPinnedFirst(pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = worldCupPostRepository.findAllOrderByLikesWithPinnedFirst(pageable);
            }
            else {
                posts = worldCupPostRepository.findAllOrderByViewCountsWithPinnedFirst(pageable);
            }
            log.info("Successfully got pageable World Cup Posts order by option: {}", option);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable World Cup Posts order by option: {}", option, e);
            throw e;
        }
    }

    @Override
    public Page<WorldCupPost> getCreatedAtDescPostsByPageNSubCategory(SubCategory subCategory, Pageable pageable, String option) {
        try {
            Page<WorldCupPost> posts;
            if (option.equals("최신순")) {
                posts = worldCupPostRepository.findAllBySubCategoryOrderByUpdatedAtDesc(subCategory, pageable);
            }
            else if (option.equals("좋아요순")) {
                posts = worldCupPostRepository.findAllBySubCategoryOrderByLikesDesc(subCategory, pageable);
            }
            else {
                posts = worldCupPostRepository.findAllBySubCategoryOrderByViewCountsDesc(subCategory, pageable);
            }
            log.info("Successfully got pageable World cup Posts order by option: {} and subCategory: {}", option, subCategory);
            return posts;
        } catch (Exception e) {
            log.error("Failed to get pageable World cup Posts order by option: {} and subCategory: {}", option, subCategory, e);
            throw e;
        }
    }

    @Override
    public WorldCupPost createWorldCupPost(WorldCupPostDto worldCupPostDto, MultipartFile[] images, MultipartFile coverImage) {
        User user = userService.getUserById(worldCupPostDto.getUserId());
        try {
            if (user.getLog() < 10) return null;

            String email = user.getEmail();

            WorldCupPost worldCupPost = WorldCupPost.builder()
                    .title(worldCupPostDto.getWorldCupTitle())
                    .category(Category.이상형월드컵)
                    .postType(PostType.WORLD_CUP)
                    .subCategory(SubCategory.valueOf(worldCupPostDto.getSubCategory()))
                    .isCommentAllowed(worldCupPostDto.getIsCommentAllowed())
                    .viewCounts(0L)
                    .user(user)
                    .build();

            worldCupPost = worldCupPostRepository.saveAndFlush(worldCupPost);

            if (coverImage != null) {
                String coverImageUrl = awsFileService.uploadPostFile(coverImage, email);
                worldCupPost.setCoverImageUrl(coverImageUrl);
            }

            WorldCupPost createdPost = worldCupPostRepository.save(worldCupPost);
            candidateService.createCandidate(worldCupPostDto.getCandidateTitleList(), worldCupPostDto.getImageUrlList(), images, createdPost, email);

            // save keywords
            worldCupPostDto.getSelectedKeywords().stream().forEach(
                    keyword -> keywordService.createKeyword(keyword, createdPost)
            );

            userService.updateUserLog(user, user.getLog() - 10);

            log.info("Successfully created world cup post");
            return worldCupPost;
        } catch (Exception e) {
            log.error("Failed to create world cup post", e);
            throw e;
        }
    }

    @Override
    public WorldCupPost updatePost(UpdateWorldCupPostDto updateWorldCupPostDto, MultipartFile[] images, MultipartFile coverImage) {
        User user = userService.getUserById(updateWorldCupPostDto.getUserId());
        WorldCupPost worldCupPost = getPostById(updateWorldCupPostDto.getPostId());
        String userEmail = user.getEmail();

        if (!worldCupPost.getTitle().equals(updateWorldCupPostDto.getWorldCupTitle())) {
            worldCupPost.setTitle(updateWorldCupPostDto.getWorldCupTitle());
        }
        if (!worldCupPost.getIsCommentAllowed().equals(updateWorldCupPostDto.getIsCommentAllowed())) {
            worldCupPost.setIsCommentAllowed(updateWorldCupPostDto.getIsCommentAllowed());
        }

        List<String> updatedKeywords = updateWorldCupPostDto.getSelectedKeywords();
        keywordService.updateKeyword(worldCupPost, updatedKeywords);

        if (coverImage != null) {
            awsFileService.removeProfileFile(userEmail, worldCupPost.getCoverImageUrl());
            String coverImageUrl = awsFileService.uploadPostFile(coverImage, userEmail);
            worldCupPost.setCoverImageUrl(coverImageUrl);
        }

        final ZoneId SYDNEY_ZONE = ZoneId.of("Australia/Sydney");
        worldCupPost.setUpdatedAt(Timestamp.from(java.time.ZonedDateTime.now(SYDNEY_ZONE).toInstant()));
        worldCupPostRepository.save(worldCupPost);
        worldCupPostRepository.flush();

        candidateService.updateCandidate(updateWorldCupPostDto.getCandidateTitleList(), updateWorldCupPostDto.getImageUrlList(), images, worldCupPost, userEmail);

        return worldCupPost;
    }

    @Override
    public List<WorldCupPost> searchWorldCupPost(String title, String subCategory, List<String> keywords) {
        boolean isTitleEmpty = title == null || title.equals("");
        boolean isSubCategoryEmpty = subCategory == null || subCategory.equals("");

        List<WorldCupPost> worldCupPostList;
        if (isTitleEmpty && isSubCategoryEmpty) worldCupPostList = searchByCategory();
        else if (isSubCategoryEmpty) worldCupPostList = searchByTitle(title);
        else if (isTitleEmpty) worldCupPostList = searchBySubCategory(SubCategory.valueOf(subCategory));
        else worldCupPostList = searchByTitleAndSubCategory(title, SubCategory.valueOf(subCategory));

        if (keywords != null && !keywords.isEmpty()) {
            worldCupPostList = worldCupPostList.stream()
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

        return worldCupPostList;
    }

    @Override
    public List<WorldCupPost> searchByCategory() {
        return worldCupPostRepository.findAllByOrderByUpdatedAtDesc();
    }

    @Override
    public List<WorldCupPost> searchByTitle(String title) {
        return worldCupPostRepository.findByTitleContainingOrderByUpdatedAtDesc(title);
    }

    @Override
    public List<WorldCupPost> searchBySubCategory(SubCategory subCategory) {
        return worldCupPostRepository.findBySubCategoryOrderByUpdatedAtDesc(subCategory);
    }

    @Override
    public List<WorldCupPost> searchByTitleAndSubCategory(String title, SubCategory subCategory) {
        return worldCupPostRepository.findByTitleContainingAndSubCategoryOrderByUpdatedAtDesc(title, subCategory);
    }

    @Override
    public Page<WorldCupPost> convertPostsAsPage(List<WorldCupPost> posts, Pageable pageable) {
        try {
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), posts.size());

            List<WorldCupPost> paginatedPosts = posts.subList(start, end);

            return new PageImpl<>(paginatedPosts, pageable, posts.size());
        } catch (Exception e) {
            log.error("Failed to convert Post as Page");
            throw e;
        }
    }
}
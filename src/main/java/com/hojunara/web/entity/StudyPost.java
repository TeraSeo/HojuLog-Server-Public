package com.hojunara.web.entity;

import com.hojunara.web.dto.request.UpdateStudyPostDto;
import com.hojunara.web.dto.response.DetailedStudyPostDto;
import com.hojunara.web.dto.response.NormalStudyPostDto;
import com.hojunara.web.dto.response.SummarizedStudyPostDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("STUDY")
public class StudyPost extends BlogPost {

    private String school;

    public SummarizedStudyPostDto convertPostToSummarizedStudyPostDto() {
        return SummarizedStudyPostDto.builder().postId(getId()).title(getTitle()).createdAt(getUpdatedAt()).isPublic(getIsPublic()).build();
    }

    public NormalStudyPostDto convertPostToNormalStudyPostDto() {
        List<BlogContent> blogContents = getBlogContents();
        String description = blogContents.stream()
                .filter(blogContent -> blogContent.getType().equals("description"))
                .map(blogContent -> (DescriptionContent) blogContent)
                .map(DescriptionContent::getContent)
                .findFirst()
                .orElse("");

        return NormalStudyPostDto.builder().postId(getId()).title(getTitle()).description(description).viewCounts((long) getViewedUsers().size()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).createdAt(getUpdatedAt()).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).pinnedAdExpiry(getPinnedAdExpiry()).build();
    }

    public UpdateStudyPostDto convertToUpdateStudyPostDto() {
        List<Map<String, String>> blogContents = BlogContent.convertBlogContentToMap(getBlogContents());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        return UpdateStudyPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).school(school).blogContents(blogContents).selectedKeywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }

    public DetailedStudyPostDto convertPostToDetailedStudyPostDto(String userId) {
        List<Map<String, String>> blogContentMap = BlogContent.convertBlogContentToMap(getBlogContents());

        List<String> keywords = getKeywords().stream()
                .map(Keyword::getKeyWord)
                .collect(Collectors.toList());

        Boolean isUserLiked = false;
        if (userId != null && userId != "") {
            Long parsedId = Long.valueOf(userId);
            isUserLiked = getLikes().stream()
                    .map(PostLike::getUser)
                    .map(User::getId)
                    .anyMatch(id -> id.equals(parsedId));
        }

        return DetailedStudyPostDto.builder().postId(getId()).title(getTitle()).userId(getUser().getId()).subCategory(getSubCategory()).school(school).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getUpdatedAt()).viewCounts((long) getViewedUsers().size()).blogContents(blogContentMap).keywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

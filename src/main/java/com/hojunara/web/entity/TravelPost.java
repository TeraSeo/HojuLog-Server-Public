package com.hojunara.web.entity;

import com.hojunara.web.dto.request.UpdateTravelPostDto;
import com.hojunara.web.dto.response.DetailedTravelPostDto;
import com.hojunara.web.dto.response.NormalTravelPostDto;
import com.hojunara.web.dto.response.SummarizedTravelPostDto;
import jakarta.persistence.Column;
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
@DiscriminatorValue("TRAVEL")
public class TravelPost extends BlogPost {
    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String location;

    public SummarizedTravelPostDto convertPostToSummarizedTravelPostDto() {
        return SummarizedTravelPostDto.builder().postId(getId()).title(getTitle()).location(location).createdAt(getUpdatedAt()).isPublic(getIsPublic()).build();
    }

    public NormalTravelPostDto convertPostToNormalTravelPostDto() {
        List<BlogContent> blogContents = getBlogContents();
        String description = blogContents.stream()
                .filter(blogContent -> blogContent.getType().equals("description"))
                .map(blogContent -> (DescriptionContent) blogContent)
                .map(DescriptionContent::getContent)
                .findFirst()
                .orElse("");

        return NormalTravelPostDto.builder().postId(getId()).title(getTitle()).description(description).country(country).viewCounts((long) getViewedUsers().size()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).location(location).createdAt(getUpdatedAt()).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).pinnedAdExpiry(getPinnedAdExpiry()).build();
    }

    public UpdateTravelPostDto convertToUpdateTravelPostDto() {
        List<Map<String, String>> blogContents = BlogContent.convertBlogContentToMap(getBlogContents());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        return UpdateTravelPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).country(country).location(location).blogContents(blogContents).selectedKeywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }

    public DetailedTravelPostDto convertPostToDetailedTravelPostDto(String userId) {
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

        return DetailedTravelPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).subCategory(getSubCategory()).location(location).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getUpdatedAt()).viewCounts((long) getViewedUsers().size()).blogContents(blogContentMap).keywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

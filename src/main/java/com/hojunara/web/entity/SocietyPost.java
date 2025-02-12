package com.hojunara.web.entity;

import com.hojunara.web.dto.request.UpdateSocietyPostDto;
import com.hojunara.web.dto.response.DetailedSocietyPostDto;
import com.hojunara.web.dto.response.NormalSocietyPostDto;
import com.hojunara.web.dto.response.SummarizedSocietyPostDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("SOCIETY")
public class SocietyPost extends BlogPost {

    public SummarizedSocietyPostDto convertPostToSummarizedSocietyPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedSocietyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).averageRate(randomAverageRate).createdAt(getCreatedAt()).isPublic(getIsPublic()).build();
    }

    public NormalSocietyPostDto convertPostToNormalSocietyPostDto() {
        List<BlogContent> blogContents = getBlogContents();
        String description = blogContents.stream()
                .filter(blogContent -> blogContent.getType().equals("description"))
                .map(blogContent -> (DescriptionContent) blogContent)
                .map(DescriptionContent::getContent)
                .findFirst()
                .orElse("");

        return NormalSocietyPostDto.builder().postId(getId()).title(getTitle()).description(description).viewCounts((long) getViewedUsers().size()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).createdAt(getCreatedAt()).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }

    public UpdateSocietyPostDto convertToUpdateSocietyPostDto() {
        List<Map<String, String>> blogContents = BlogContent.convertBlogContentToMap(getBlogContents());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        return UpdateSocietyPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).blogContents(blogContents).selectedKeywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }

    public DetailedSocietyPostDto convertPostToDetailedSocietyPostDto(String userId) {
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

        return DetailedSocietyPostDto.builder().postId(getId()).title(getTitle()).userId(getUser().getId()).subCategory(getSubCategory()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).viewCounts((long) getViewedUsers().size()).blogContents(blogContentMap).keywords(keywords).isPublic(getIsPublic()).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

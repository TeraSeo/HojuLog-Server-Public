package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedStudyPostDto;
import com.hojunara.web.dto.response.NormalStudyPostDto;
import com.hojunara.web.dto.response.SummarizedStudyPostDto;
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

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("STUDY")
public class StudyPost extends BlogPost {

    private String school;

    private String major;

    @Column(nullable = false)
    private double rate;

    public SummarizedStudyPostDto convertPostToSummarizedStudyPostDto() {
        return SummarizedStudyPostDto.builder().postId(getId()).title(getTitle()).rate(rate).createdAt(getCreatedAt()).build();
    }

    public NormalStudyPostDto convertPostToNormalStudyPostDto() {
        List<BlogContent> blogContents = getBlogContents();
        String description = blogContents.stream()
                .filter(blogContent -> blogContent.getType().equals("description"))
                .map(blogContent -> (DescriptionContent) blogContent)
                .map(DescriptionContent::getContent)
                .findFirst()
                .orElse("");

        return NormalStudyPostDto.builder().postId(getId()).title(getTitle()).description(description).rate(rate).viewCounts((long) getViewedUsers().size()).createdAt(getCreatedAt()).build();
    }

    public DetailedStudyPostDto convertPostToDetailedStudyPostDto(String userId) {
        List<Map<String, String>> blogContentMap = BlogContent.convertBlogContentToMap(getBlogContents());

        Boolean isUserLiked = false;
        if (userId != null) {
            isUserLiked = getLikes().stream()
                    .map(PostLike::getUser)
                    .map(User::getId)
                    .anyMatch(id -> id.equals(userId));
        }

        return DetailedStudyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).subCategory(getSubCategory()).school(school).major(major).rate(rate).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).viewCounts((long) getViewedUsers().size()).blogContents(blogContentMap).build();
    }
}

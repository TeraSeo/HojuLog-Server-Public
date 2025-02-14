package com.hojunara.web.entity;

import com.hojunara.web.dto.request.*;
import com.hojunara.web.dto.response.DetailedJobPostDto;
import com.hojunara.web.dto.response.NormalJobPostDto;
import com.hojunara.web.dto.response.SummarizedJobPostDto;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("JOB")
public class JobPost extends NormalPost {

    @Column(nullable = false)
    private JobType jobType;

    private String location;

    public SummarizedJobPostDto convertPostToSummarizedJobPostDto() {
        return SummarizedJobPostDto.builder().postId(getId()).title(getTitle()).viewCounts((long) getViewedUsers().size()).subCategory(getSubCategory()).createdAt(getCreatedAt()).build();
    }

    public NormalJobPostDto convertPostToNormalJobPostDto() {
        return NormalJobPostDto.builder().postId(getId()).title(getTitle()).location(getLocation()).suburb(getSuburb()).viewCounts((long) getViewedUsers().size()).jobType(getJobType()).subCategory(getSubCategory()).createdAt(getCreatedAt()).build();
    }

    public UpdateJobPostDto convertToUpdateJobPostDto() {
        List<String> imageUrls = getImages().stream().map(Image::getUrl).collect(Collectors.toList());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        UpdateJobMainInfoPostDto updateJobMainInfoPostDto = UpdateJobMainInfoPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).description(getDescription()).contact(getContact()).email(getEmail()).suburb(getSuburb()).jobType(getJobType()).location(getLocation()).selectedKeywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
        UpdateJobMediaInfoPostDto updateJobMediaInfoPostDto = UpdateJobMediaInfoPostDto.builder().existingImages(imageUrls).build();

        return UpdateJobPostDto.builder().updateJobMainInfoPostDto(updateJobMainInfoPostDto).updateJobMediaInfoPostDto(updateJobMediaInfoPostDto).build();
    }

    public DetailedJobPostDto convertPostToDetailedJobPostDto(String userId) {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

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

        return DetailedJobPostDto.builder().postId(getId()).title(getTitle()).description(getDescription()).subCategory(getSubCategory()).userId(getUser().getId()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).jobType(jobType).location(location).viewCounts((long) getViewedUsers().size()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

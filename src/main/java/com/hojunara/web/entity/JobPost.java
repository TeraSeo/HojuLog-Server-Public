package com.hojunara.web.entity;

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
        return SummarizedJobPostDto.builder().postId(getId()).title(getTitle()).viewCounts(getViewCounts()).subCategory(getSubCategory()).createdAt(getCreatedAt()).build();
    }

    public NormalJobPostDto convertPostToNormalJobPostDto() {
        return NormalJobPostDto.builder().postId(getId()).title(getTitle()).location(getLocation()).suburb(getSuburb()).viewCounts(getViewCounts()).jobType(getJobType()).subCategory(getSubCategory()).createdAt(getCreatedAt()).build();
    }

    public DetailedJobPostDto convertPostToDetailedJobPostDto(Long userId) {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        Boolean isUserLiked = getLikes().stream()
                .map(PostLike::getUser)
                .map(User::getId)
                .anyMatch(id -> id.equals(userId));

        return DetailedJobPostDto.builder().postId(getId()).title(getTitle()).description(getDescription()).subCategory(getSubCategory()).username(getUser().getUsername()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).jobType(jobType).location(location).viewCounts(getViewCounts()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).build();
    }
}

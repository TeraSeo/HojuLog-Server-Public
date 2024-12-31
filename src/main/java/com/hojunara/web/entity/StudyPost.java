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
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@DiscriminatorValue("STUDY")
public class StudyPost extends Post {

    private String school;

    private String major;

    @Column(nullable = false)
    private double rate;

    public SummarizedStudyPostDto convertPostToSummarizedStudyPostDto() {
        return SummarizedStudyPostDto.builder().postId(getId()).title(getTitle()).rate(rate).createdAt(getCreatedAt()).build();
    }

    public NormalStudyPostDto convertPostToNormalStudyPostDto() {
        return NormalStudyPostDto.builder().postId(getId()).title(getTitle()).description(getDescription()).rate(rate).suburb(getSuburb()).viewCounts(getViewCounts()).createdAt(getCreatedAt()).build();
    }

    public DetailedStudyPostDto convertPostToDetailedStudyPostDto() {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return DetailedStudyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).school(school).major(major).rate(rate).createdAt(getCreatedAt()).viewCounts(getViewCounts()).build();
    }
}

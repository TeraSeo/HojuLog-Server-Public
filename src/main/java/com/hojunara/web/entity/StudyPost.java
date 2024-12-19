package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedStudyPostDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.concurrent.ThreadLocalRandom;

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

    public SummarizedStudyPostDto convertPostToSummarizedStudyPostDto() {
        return SummarizedStudyPostDto.builder().title(getTitle()).createdAt(getCreatedAt()).build();
    }
}

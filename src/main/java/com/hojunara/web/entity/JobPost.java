package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedJobPostDto;
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
@DiscriminatorValue("JOB")
public class JobPost extends Post {
    private JobType jobType;

    private String location;

    public SummarizedJobPostDto convertPostToSummarizedJobPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedJobPostDto.builder().title(getTitle()).averageRate(randomAverageRate).createdAt(getCreatedAt()).build();
    }
}

package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedSocietyPostDto;
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
@DiscriminatorValue("SOCIETY")
public class SocietyPost extends Post {
    public SummarizedSocietyPostDto convertPostToSummarizedSocietyPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedSocietyPostDto.builder().title(getTitle()).username(getUser().getUsername()).averageRate(randomAverageRate).createdAt(getCreatedAt()).build();
    }
}

package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedTravelPostDto;
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
@DiscriminatorValue("TRAVEL")
public class TravelPost extends Post {
    private String location;

    public SummarizedTravelPostDto convertPostToSummarizedTravelPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedTravelPostDto.builder().title(getTitle()).averageRate(randomAverageRate).location(location).createdAt(getCreatedAt()).build();
    }
}

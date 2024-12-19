package com.hojunara.web.entity;

import com.hojunara.web.dto.response.SummarizedPropertyPostDto;
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
@DiscriminatorValue("PROPERTY")
public class PropertyPost extends Post {
    private Period period;

    private Long price;

    private String location;

    private String availableTime;

    public SummarizedPropertyPostDto convertPostToSummarizedPropertyPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedPropertyPostDto.builder().title(getTitle()).averageRate(randomAverageRate).imageUrl(imageUrl).location(getLocation()).createdAt(getCreatedAt()).build();
    }
}

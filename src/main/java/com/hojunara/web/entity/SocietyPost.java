package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedSocietyPostDto;
import com.hojunara.web.dto.response.DetailedTransactionPostDto;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
        return SummarizedSocietyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).averageRate(randomAverageRate).createdAt(getCreatedAt()).build();
    }

    public NormalSocietyPostDto convertPostToNormalSocietyPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return NormalSocietyPostDto.builder().postId(getId()).title(getTitle()).averageRate(randomAverageRate).suburb(getSuburb()).viewCounts(getViewCounts()).createdAt(getCreatedAt()).build();
    }

    public DetailedSocietyPostDto convertPostToDetailedSocietyPostDto() {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return DetailedSocietyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).createdAt(getCreatedAt()).viewCounts(getViewCounts()).build();
    }
}

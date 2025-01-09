package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedSocietyPostDto;
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
public class SocietyPost extends NormalPost {
    public SummarizedSocietyPostDto convertPostToSummarizedSocietyPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return SummarizedSocietyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).averageRate(randomAverageRate).createdAt(getCreatedAt()).build();
    }

    public NormalSocietyPostDto convertPostToNormalSocietyPostDto() {
        double randomAverageRate = Math.round(ThreadLocalRandom.current().nextDouble(4.0, 5.01) * 10.0) / 10.0;
        return NormalSocietyPostDto.builder().postId(getId()).title(getTitle()).averageRate(randomAverageRate).suburb(getSuburb()).viewCounts((long) getViewedUsers().size()).createdAt(getCreatedAt()).build();
    }

    public DetailedSocietyPostDto convertPostToDetailedSocietyPostDto(String userId) {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        Boolean isUserLiked = false;
        if (userId != null) {
            isUserLiked = getLikes().stream()
                    .map(PostLike::getUser)
                    .map(User::getId)
                    .anyMatch(id -> id.equals(userId));
        }

        return DetailedSocietyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).viewCounts((long) getViewedUsers().size()).build();
    }
}

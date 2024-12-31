package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedTravelPostDto;
import com.hojunara.web.dto.response.NormalTravelPostDto;
import com.hojunara.web.dto.response.SummarizedTravelPostDto;
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
@DiscriminatorValue("TRAVEL")
public class TravelPost extends Post {

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double rate;

    public SummarizedTravelPostDto convertPostToSummarizedTravelPostDto() {
        return SummarizedTravelPostDto.builder().postId(getId()).title(getTitle()).rate(rate).location(location).createdAt(getCreatedAt()).build();
    }

    public NormalTravelPostDto convertPostToNormalTravelPostDto() {
        return NormalTravelPostDto.builder().postId(getId()).title(getTitle()).description(getDescription()).rate(rate).suburb(getSuburb()).viewCounts(getViewCounts()).location(location).createdAt(getCreatedAt()).build();
    }

    public DetailedTravelPostDto convertPostToDetailedTravelPostDto() {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return DetailedTravelPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).location(location).rate(rate).createdAt(getCreatedAt()).viewCounts(getViewCounts()).build();
    }
}

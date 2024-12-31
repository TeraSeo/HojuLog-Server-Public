package com.hojunara.web.entity;

import com.hojunara.web.dto.response.DetailedPropertyPostDto;
import com.hojunara.web.dto.response.NormalPropertyPostDto;
import com.hojunara.web.dto.response.SummarizedPropertyPostDto;
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
@DiscriminatorValue("PROPERTY")
public class PropertyPost extends Post {
    @Column(nullable = false)
    private Period period;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String availableTime;

    @Column(nullable = false)
    private String roomCount;

    @Column(nullable = false)
    private BathroomType bathroomType;

    @Column(nullable = false)
    private Boolean isParkable;

    @Column(nullable = false)
    private Boolean isBillIncluded;

    public SummarizedPropertyPostDto convertPostToSummarizedPropertyPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        return SummarizedPropertyPostDto.builder().postId(getId()).title(getTitle()).imageUrl(imageUrl).location(getLocation()).createdAt(getCreatedAt()).subCategory(getSubCategory()).period(period).price(price).suburb(getSuburb()).build();
    }

    public NormalPropertyPostDto convertPostToNormalPropertyPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        return NormalPropertyPostDto.builder().postId(getId()).title(getTitle()).imageUrl(imageUrl).location(getLocation()).suburb(getSuburb()).viewCounts(getViewCounts()).price(getPrice()).period(period).subCategory(getSubCategory()).roomCount(roomCount).bathroomType(bathroomType).isParkable(isParkable).createdAt(getCreatedAt()).isBillIncluded(isBillIncluded).build();
    }

    public DetailedPropertyPostDto convertPostToDetailedPropertyPostDto() {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        return DetailedPropertyPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).period(period).price(price).location(location).availableTime(availableTime).roomCount(roomCount).bathroomType(bathroomType).isParkable(isParkable).isBillIncluded(isBillIncluded).createdAt(getCreatedAt()).viewCounts(getViewCounts()).build();
    }
}

package com.hojunara.web.entity;

import com.hojunara.web.dto.request.UpdatePropertyMainInfoPostDto;
import com.hojunara.web.dto.request.UpdatePropertyMediaInfoPostDto;
import com.hojunara.web.dto.request.UpdatePropertyPostDto;
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
public class PropertyPost extends NormalPost {
    private Period period;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String availableTime;

    @Column(nullable = false)
    private String roomCount;

    private BathroomType bathroomType;

    @Column(nullable = false)
    private Boolean isParkable;

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
        return NormalPropertyPostDto.builder().postId(getId()).title(getTitle()).imageUrl(imageUrl).location(getLocation()).suburb(getSuburb()).viewCounts((long) getViewedUsers().size()).price(getPrice()).period(period).subCategory(getSubCategory()).roomCount(roomCount).bathroomType(bathroomType).isParkable(isParkable).createdAt(getCreatedAt()).isBillIncluded(isBillIncluded).build();
    }

    public UpdatePropertyPostDto convertToUpdatePropertyPostDto() {
        List<String> imageUrls = getImages().stream().map(Image::getUrl).collect(Collectors.toList());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        UpdatePropertyMainInfoPostDto updatePropertyMainInfoPostDto = UpdatePropertyMainInfoPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).description(getDescription()).contact(getContact()).email(getEmail()).suburb(getSuburb()).period(period).price(price).location(location).availableTime(availableTime).roomCount(roomCount).bathroomType(bathroomType).isParkable(isParkable).isBillIncluded(isBillIncluded).selectedKeywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
        UpdatePropertyMediaInfoPostDto updatePropertyMediaInfoPostDto = UpdatePropertyMediaInfoPostDto.builder().existingImages(imageUrls).build();

        return UpdatePropertyPostDto.builder().updatePropertyMainInfoPostDto(updatePropertyMainInfoPostDto).updatePropertyMediaInfoPostDto(updatePropertyMediaInfoPostDto).build();
    }

    public DetailedPropertyPostDto convertPostToDetailedPropertyPostDto(String userId) {
        List<String> imageUrls = getImages().stream()
                .map(Image::getUrl)
                .collect(Collectors.toList());

        List<String> keywords = getKeywords().stream()
                .map(Keyword::getKeyWord)
                .collect(Collectors.toList());

        Boolean isUserLiked = false;
        if (userId != null && userId != "") {
            Long parsedId = Long.valueOf(userId);
            isUserLiked = getLikes().stream()
                    .map(PostLike::getUser)
                    .map(User::getId)
                    .anyMatch(id -> id.equals(parsedId));
        }

        return DetailedPropertyPostDto.builder().postId(getId()).title(getTitle()).userId(getUser().getId()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).period(period).price(price).location(location).availableTime(availableTime).roomCount(roomCount).bathroomType(bathroomType).isParkable(isParkable).isBillIncluded(isBillIncluded).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getCreatedAt()).viewCounts((long) getViewedUsers().size()).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

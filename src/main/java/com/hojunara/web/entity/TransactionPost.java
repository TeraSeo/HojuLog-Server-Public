package com.hojunara.web.entity;

import com.hojunara.web.dto.request.*;
import com.hojunara.web.dto.response.DetailedTransactionPostDto;
import com.hojunara.web.dto.response.NormalTransactionPostDto;
import com.hojunara.web.dto.response.SummarizedTransactionPostDto;
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
@DiscriminatorValue("TRANSACTION")
public class TransactionPost extends NormalPost {

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private PriceType priceType;

    private Long price;

    public SummarizedTransactionPostDto convertPostToSummarizedTransactionPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        return SummarizedTransactionPostDto.builder().postId(getId()).title(getTitle()).username(getUser().getUsername()).imageUrl(imageUrl).price(price).priceType(priceType).createdAt(getUpdatedAt()).build();
    }

    public NormalTransactionPostDto convertPostToNormalTransactionPostDto() {
        String imageUrl = getImages().stream()
                .map(Image::getUrl)
                .findFirst()
                .orElse(null);
        return NormalTransactionPostDto.builder().postId(getId()).title(getTitle()).imageUrl(imageUrl).suburb(getSuburb()).viewCounts(getViewCounts()).price(getPrice()).transactionType(getTransactionType()).priceType(getPriceType()).commentCounts((long) getComments().size()).createdAt(getUpdatedAt()).pinnedAdExpiry(getPinnedAdExpiry()).build();
    }

    public UpdateTransactionPostDto convertToUpdateTransactionPostDto() {
        List<String> imageUrls = getImages().stream().map(Image::getUrl).collect(Collectors.toList());
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());

        UpdateTransactionMainInfoPostDto updateTransactionMainInfoPostDto = UpdateTransactionMainInfoPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).description(getDescription()).contact(getContact()).email(getEmail()).suburb(getSuburb()).transactionType(transactionType).priceType(priceType).price(price).selectedKeywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
        UpdateTransactionMediaInfoPostDto updateTransactionMediaInfoPostDto = UpdateTransactionMediaInfoPostDto.builder().existingImages(imageUrls).build();

        return UpdateTransactionPostDto.builder().updateTransactionMainInfoPostDto(updateTransactionMainInfoPostDto).updateTransactionMediaInfoPostDto(updateTransactionMediaInfoPostDto).build();
    }

    public DetailedTransactionPostDto convertPostToDetailedTransactionPostDto(String userId) {
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

        return DetailedTransactionPostDto.builder().postId(getId()).title(getTitle()).userId(getUser().getId()).description(getDescription()).subCategory(getSubCategory()).contact(getContact()).email(getEmail()).imageUrls(imageUrls).transactionType(transactionType).priceType(priceType).price(price).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getUpdatedAt()).viewCounts(getViewCounts()).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

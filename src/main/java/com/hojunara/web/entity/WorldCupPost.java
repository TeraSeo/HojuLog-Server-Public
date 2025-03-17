package com.hojunara.web.entity;

import com.hojunara.web.dto.response.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "WorldCupPost")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class WorldCupPost extends PinnablePost {
    @OneToMany(mappedBy = "worldCupPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC") // Keep the order consistent
    @Builder.Default
    private List<Candidate> candidates = new ArrayList<>();

    @Column(length = 1000)
    private String coverImageUrl;

    public SummarizedWorldCupPostDto convertPostToSummarizedWorldCupPostDto() {
        return SummarizedWorldCupPostDto.builder().postId(getId()).title(getTitle()).subCategory(getSubCategory()).imageUrl(coverImageUrl).createdAt(getUpdatedAt()).build();
    }

    public NormalWorldCupPostDto convertPostToNormalCupPostDto() {
        List<String> keywords = getKeywords().stream().map(Keyword::getKeyWord).collect(Collectors.toList());
        return NormalWorldCupPostDto.builder().postId(getId()).title(getTitle()).subCategory(getSubCategory()).imageUrl(coverImageUrl).createdAt(getUpdatedAt()).viewCounts((long) getViewedUsers().size()).pinnedAdExpiry(getPinnedAdExpiry()).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).build();
    }

    public UpdateDetailedWorldCupPostDto convertPostToUpdatePostDto() {
        List<CandidateDto> candidateDtoList = candidates.stream().map(candidate -> candidate.convertToCandidateDto()).collect(Collectors.toList());

        List<String> keywords = getKeywords().stream()
                .map(Keyword::getKeyWord)
                .collect(Collectors.toList());

        return UpdateDetailedWorldCupPostDto.builder().postId(getId()).title(getTitle()).coverImageUrl(coverImageUrl).candidateDtoList(candidateDtoList).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
    }


    public DetailedWorldCupPostDto convertPostToDetailedWorldCupPostDto(String userId) {
        List<CandidateDto> candidateDtoList = candidates.stream().map(candidate -> candidate.convertToCandidateDto()).collect(Collectors.toList());

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

        return DetailedWorldCupPostDto.builder().postId(getId()).userId(getUser().getId()).title(getTitle()).subCategory(getSubCategory()).candidateDtoList(candidateDtoList).likeCounts((long) getLikes().size()).commentCounts((long) getComments().size()).isUserLiked(isUserLiked).createdAt(getUpdatedAt()).viewCounts((long) getViewedUsers().size()).keywords(keywords).isCommentAllowed(getIsCommentAllowed()).build();
    }
}

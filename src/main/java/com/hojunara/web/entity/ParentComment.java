package com.hojunara.web.entity;

import com.hojunara.web.dto.response.ResponseCommentDto;
import com.hojunara.web.dto.response.SummarizedCommentDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parent_comment")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ParentComment extends Comment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResponseComment> responseComments = new ArrayList<>();

    @Override
    public SummarizedCommentDto convertToSummarizedCommentDto(Long userId) {
        List<Long> responseCommentIds = responseComments.stream().map(ResponseComment::getId).toList();
        List<Long> likedUserIds = getLikes().stream().map(CommentLike::getUser).map(User::getId).toList();
        Boolean isCurrentUserLiked = likedUserIds.contains(userId);
        return SummarizedCommentDto.builder()
                .commentId(getId())
                .content(getContent())
                .isCurrentUserLiked(isCurrentUserLiked)
                .wholeLikedUserLength((long) getLikes().size())
                .summarizedUserDto(getUser().convertToSummarisedUserDto())
                .responseCommentIds(responseCommentIds)
                .createdAt(getCreatedAt())
                .build();
    }

    @Override
    public ResponseCommentDto convertToResponseCommentDto(Long userId) {
        return null;
    }
}

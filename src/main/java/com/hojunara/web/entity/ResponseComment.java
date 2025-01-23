package com.hojunara.web.entity;

import com.hojunara.web.dto.response.ResponseCommentDto;
import com.hojunara.web.dto.response.SummarizedCommentDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "response_comment")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ResponseComment extends Comment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", nullable = false)
    private ParentComment parentComment;

    @Override
    public SummarizedCommentDto convertToSummarizedCommentDto(String userId) {
        return null;
    }

    @Override
    public ResponseCommentDto convertToResponseCommentDto(String userId) {
        List<Long> likedUserIds = getLikes().stream().map(CommentLike::getUser).map(User::getId).toList();
        Boolean isCurrentUserLiked = false;
        if (userId != null && userId != "") {
            long parsedId = Long.parseLong(userId);
            isCurrentUserLiked = likedUserIds.contains(parsedId);
        }
        return ResponseCommentDto.builder()
                .commentId(getId())
                .content(getContent())
                .isCurrentUserLiked(isCurrentUserLiked)
                .wholeLikedUserLength((long) getLikes().size())
                .summarizedUserDto(getUser().convertToSummarisedUserDto())
                .createdAt(getCreatedAt())
                .build();
    }
}

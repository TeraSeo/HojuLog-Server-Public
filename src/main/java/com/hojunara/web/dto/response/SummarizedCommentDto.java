package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedCommentDto {

    private Long commentId;

    private String content;

    private SummarizedUserDto summarizedUserDto;

    private Long wholeLikedUserLength;

    private Boolean isCurrentUserLiked;

    private List<Long> responseCommentIds;

    private Timestamp createdAt;
}

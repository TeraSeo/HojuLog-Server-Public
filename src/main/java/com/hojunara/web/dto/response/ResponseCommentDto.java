package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseCommentDto {

    private Long commentId;

    private String content;

    private SummarizedUserDto summarizedUserDto;

    private Long wholeLikedUserLength;

    private Boolean isCurrentUserLiked;

    private Timestamp createdAt;
}

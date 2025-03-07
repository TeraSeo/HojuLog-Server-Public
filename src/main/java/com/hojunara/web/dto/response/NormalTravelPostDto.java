package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalTravelPostDto {
    private Long postId;

    private String title;

    private String description;

    private String country;

    private Timestamp createdAt;

    private Long viewCounts;

    private String location;

    private Boolean isPublic;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isCommentAllowed;

    private Timestamp pinnedAdExpiry;
}

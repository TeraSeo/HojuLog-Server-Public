package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Suburb;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalStudyPostDto {
    private Long postId;

    private String title;

    private String description;

    private Timestamp createdAt;

    private Long viewCounts;

    private Boolean isPublic;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isCommentAllowed;

    private Timestamp pinnedAdExpiry;
}

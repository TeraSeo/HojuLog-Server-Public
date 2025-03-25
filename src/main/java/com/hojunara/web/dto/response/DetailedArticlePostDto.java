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
public class DetailedArticlePostDto {
    private Long postId;

    private Long userId;

    private String title;

    private String description;

    private List<String> imageUrls;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;

    private Boolean isCommentAllowed;
}

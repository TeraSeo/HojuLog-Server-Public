package com.hojunara.web.dto.response;

import com.hojunara.web.entity.SubCategory;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedSocietyPostDto {
    private Long postId;

    private Long userId;

    private String title;

    private SubCategory subCategory;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;

    private List<Map<String, String>> blogContents;

    private List<String> keywords;

    private Boolean isPublic;

    private Boolean isCommentAllowed;
}

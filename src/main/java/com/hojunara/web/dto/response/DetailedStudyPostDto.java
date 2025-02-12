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
public class DetailedStudyPostDto {
    private Long postId;

    private Long userId;

    private String title;

    private SubCategory subCategory;

    private String school;

    private Timestamp createdAt;

    private Long viewCounts;

    private List<Map<String, String>> blogContents;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;

    private List<String> keywords;

    private Boolean isPublic;

    private Boolean isCommentAllowed;
}

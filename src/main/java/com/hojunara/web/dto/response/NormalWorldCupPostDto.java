package com.hojunara.web.dto.response;

import com.hojunara.web.entity.SubCategory;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalWorldCupPostDto {
    private Long postId;

    private String title;

    private String imageUrl;

    private Timestamp createdAt;

    private Long viewCounts;

    private SubCategory subCategory;

    private Timestamp pinnedAdExpiry;

    private List<String> keywords;

    private Boolean isCommentAllowed;

    private Long likeCounts;

    private Long commentCounts;

}
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
public class DetailedWorldCupPostDto {
    private Long postId;

    private Long userId;

    private String title;

    private SubCategory subCategory;

    private List<CandidateDto> candidateDtoList;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;

    private List<String> keywords;

    private Boolean isCommentAllowed;
}

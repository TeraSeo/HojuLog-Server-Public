package com.hojunara.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateDetailedWorldCupPostDto {
    private Long postId;

    private String title;

    private String coverImageUrl;

    private List<CandidateDto> candidateDtoList;

    private List<String> keywords;

    private Boolean isCommentAllowed;
}

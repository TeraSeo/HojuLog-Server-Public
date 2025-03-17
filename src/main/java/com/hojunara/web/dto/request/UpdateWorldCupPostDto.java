package com.hojunara.web.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateWorldCupPostDto {
    private Long postId;

    private Long userId;

    private String worldCupTitle;

    private List<String> candidateTitleList;

    private List<String> imageUrlList;

    private String coverImageUrl;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}

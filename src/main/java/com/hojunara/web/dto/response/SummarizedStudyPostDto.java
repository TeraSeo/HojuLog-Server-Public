package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedStudyPostDto {
    private Long postId;

    private String title;

    private Timestamp createdAt;

    private Boolean isPublic;
}

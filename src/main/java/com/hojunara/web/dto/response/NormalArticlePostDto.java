package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalArticlePostDto {
    private Long postId;

    private String title;

    private Timestamp createdAt;

    private Long viewCounts;

    private String username;
}

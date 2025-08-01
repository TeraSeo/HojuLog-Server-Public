package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedSocietyPostDto {
    private Long postId;

    private String title;

    private String description;

    private String username;

    private Timestamp createdAt;

    private Boolean isPublic;
}

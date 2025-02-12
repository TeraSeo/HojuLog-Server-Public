package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedTravelPostDto {
    private Long postId;

    private String title;

    private String location;

    private Timestamp createdAt;

    private Boolean isPublic;
}

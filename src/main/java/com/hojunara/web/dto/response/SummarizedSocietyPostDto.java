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

    private String username;

    private double averageRate;

    private Timestamp createdAt;

}

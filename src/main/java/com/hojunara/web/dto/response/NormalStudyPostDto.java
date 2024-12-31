package com.hojunara.web.dto.response;

import com.hojunara.web.entity.Suburb;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalStudyPostDto {
    private Long postId;

    private String title;

    private String description;

    private Timestamp createdAt;

    private Suburb suburb;

    private Long viewCounts;

    private double rate;
}

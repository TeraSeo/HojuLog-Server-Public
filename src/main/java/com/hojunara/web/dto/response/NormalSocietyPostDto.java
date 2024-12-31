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
public class NormalSocietyPostDto {
    private Long postId;

    private String title;

    private double averageRate;

    private Timestamp createdAt;

    private Suburb suburb;

    private Long viewCounts;
}

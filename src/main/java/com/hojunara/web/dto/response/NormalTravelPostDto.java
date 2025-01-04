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
public class NormalTravelPostDto {
    private Long postId;

    private String title;

    private String description;

    private double rate;

    private Timestamp createdAt;

    private Long viewCounts;

    private String location;
}

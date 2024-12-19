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
    private String title;

    private double averageRate;

    private String location;

    private Timestamp createdAt;

}

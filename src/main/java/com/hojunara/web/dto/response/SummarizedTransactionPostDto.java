package com.hojunara.web.dto.response;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedTransactionPostDto {
    private String title;

    private double averageRate;

    private String imageUrl;

    private Timestamp createdAt;
}

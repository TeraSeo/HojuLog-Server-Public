package com.hojunara.web.dto.response;

import com.hojunara.web.entity.PriceType;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedTransactionPostDto {
    private Long postId;

    private String title;

    private String username;

    private String imageUrl;

    private Timestamp createdAt;

    private Long price;

    private PriceType priceType;
}

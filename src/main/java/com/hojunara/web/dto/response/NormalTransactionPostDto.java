package com.hojunara.web.dto.response;

import com.hojunara.web.entity.PriceType;
import com.hojunara.web.entity.Suburb;
import com.hojunara.web.entity.TransactionType;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class NormalTransactionPostDto {
    private Long postId;

    private String title;

    private String imageUrl;

    private Timestamp createdAt;

    private Suburb suburb;

    private Long price;

    private TransactionType transactionType;

    private PriceType priceType;

    private Long viewCounts;

    private Long commentCounts;

    private Timestamp pinnedAdExpiry;
}

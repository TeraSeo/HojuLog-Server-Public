package com.hojunara.web.dto.response;

import com.hojunara.web.entity.PriceType;
import com.hojunara.web.entity.SubCategory;
import com.hojunara.web.entity.TransactionType;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedTransactionPostDto {
    private Long postId;

    private String username;

    private String title;

    private String description;

    private SubCategory subCategory;

    private String contact;

    private String email;

    private List<String> imageUrls;

    private TransactionType transactionType;

    private PriceType priceType;

    private Long price;

    private Timestamp createdAt;

    private Long viewCounts;

    private Long likeCounts;

    private Long commentCounts;

    private Boolean isUserLiked;
}

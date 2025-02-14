package com.hojunara.web.dto.request;

import com.hojunara.web.entity.JobType;
import com.hojunara.web.entity.PriceType;
import com.hojunara.web.entity.Suburb;
import com.hojunara.web.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateTransactionMainInfoPostDto {
    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    @NotNull
    private String title;

    private String description;

    private String contact;
    private String email;

    private Suburb suburb;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private PriceType priceType;

    private Long price;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}
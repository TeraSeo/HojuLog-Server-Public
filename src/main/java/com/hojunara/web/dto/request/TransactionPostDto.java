package com.hojunara.web.dto.request;

import com.hojunara.web.entity.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TransactionPostDto {
    @NotNull
    private Long userId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Category category;

    @NotNull
    private SubCategory subCategory;

    private String contact;

    @Email
    private String email;

    private TransactionType transactionType;

    private PriceType priceType;

    private Long price;

    private Suburb suburb;

    private List<String> selectedKeywords;

    private Boolean isCommentAllowed;
}

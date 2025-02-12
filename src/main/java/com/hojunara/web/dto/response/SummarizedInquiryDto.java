package com.hojunara.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedInquiryDto {
    private Long inquiryId;

    private String title;

    private String description;

    private Boolean isSolved;
}

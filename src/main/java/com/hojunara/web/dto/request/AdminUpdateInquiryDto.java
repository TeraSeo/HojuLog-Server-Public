package com.hojunara.web.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUpdateInquiryDto {
    private Long inquiryId;
    private String response;
}

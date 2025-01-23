package com.hojunara.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InquiryDto {
    @NotNull
    private Long userId;

    @NotNull
    private String title;

    @NotNull
    private String description;
}

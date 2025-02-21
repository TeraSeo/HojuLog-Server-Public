package com.hojunara.web.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetailedInquiryDto {
    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String response;

    private List<String> imageUrls;
}

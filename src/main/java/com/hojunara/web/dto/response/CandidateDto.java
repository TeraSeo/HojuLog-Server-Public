package com.hojunara.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CandidateDto {
    private Long id;

    private String title;

    private String imageUrl;

    private Long victoryCount;
}

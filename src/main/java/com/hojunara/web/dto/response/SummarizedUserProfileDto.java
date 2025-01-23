package com.hojunara.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedUserProfileDto {
    private Long id;

    private String username;

    private String profilePicture;
}

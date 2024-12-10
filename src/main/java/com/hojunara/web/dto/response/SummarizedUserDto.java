package com.hojunara.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SummarizedUserDto {
    private Long id;

    private String username;

    private String email;

    private String profilePicture;

    private String description;
}

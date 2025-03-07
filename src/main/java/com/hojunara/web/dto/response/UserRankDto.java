package com.hojunara.web.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserRankDto {
    private Long userId;

    private String username;

    private Long likeCountThisWeek;

    private String profileUrl;
}

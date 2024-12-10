package com.hojunara.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostBookmarkRequestDto {
    private Long postId;

    private Long userId;

}

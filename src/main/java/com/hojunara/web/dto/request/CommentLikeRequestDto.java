package com.hojunara.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentLikeRequestDto {
    private Long commentId;

    private Long userId;
}

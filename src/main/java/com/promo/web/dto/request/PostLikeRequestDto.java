package com.promo.web.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostLikeRequestDto {

    private Long postId;

    private Long userId;

}

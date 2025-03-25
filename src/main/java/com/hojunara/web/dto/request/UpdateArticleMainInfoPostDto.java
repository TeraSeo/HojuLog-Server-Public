package com.hojunara.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateArticleMainInfoPostDto {
    @NotNull
    private Long userId;

    private Long postId;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private Boolean isCommentAllowed;
}

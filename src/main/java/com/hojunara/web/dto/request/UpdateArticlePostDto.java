package com.hojunara.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateArticlePostDto {
    private UpdateArticleMainInfoPostDto updateArticleMainInfoPostDto;
    private UpdateArticleMediaInfoPostDto updateArticleMediaInfoPostDto;
}

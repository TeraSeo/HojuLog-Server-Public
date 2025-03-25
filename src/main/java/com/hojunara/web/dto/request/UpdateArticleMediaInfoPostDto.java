package com.hojunara.web.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateArticleMediaInfoPostDto {
    private List<String> existingImages;
}

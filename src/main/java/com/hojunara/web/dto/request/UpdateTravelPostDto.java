package com.hojunara.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateTravelPostDto {
    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    @NotNull
    private String title;

    private String country;

    private String location;

    private List<Map<String, String>> blogContents;

    private List<String> selectedKeywords;

    private Boolean isPublic;

    private Boolean isCommentAllowed;
}